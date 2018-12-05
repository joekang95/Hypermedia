import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class ObjectTagger {
	
	final static int COLOR_RED = new Color(255,0,0).getRGB();
    final static int COLOR_GREEN = new Color(0,255,0).getRGB();
    final static int COLOR_BLUE =new Color(0,0,255).getRGB();
	
	public static void autoTagging(HyperLink link, HyperVideo video, int frameIndex, int k, int thresold, int tolrFail) throws Exception {
		LinkedList<HyperLink> tolrFails = new LinkedList<HyperLink>();
		
		BufferedImage img = VideoReader.importImage(video.getFramePath(frameIndex), video.getWidth(), video.getHeight());
		BufferedImage linkImage = extractImage(img, link.getX(), link.getY(), link.getWidth(), link.getHeight());
		
		HyperLink obj = link.duplicate();	
		for(int i=frameIndex; i<video.getFrameSize(); i++) {
			BufferedImage curFrame = VideoReader.importImage(video.getFramePath(i), video.getWidth(), video.getHeight());
			obj = searchImage(curFrame, linkImage, obj, obj.getX(), obj.getY(), k);
		
			if(obj.getDiff() < thresold) {
				while(!tolrFails.isEmpty()) {
					int forward = tolrFails.size();
					HyperLink failObj = tolrFails.pop();
					video.getFrame(i - forward).addLink(failObj);
					video.updateAllLinks(failObj.getId(), i - forward);
				}
				video.getFrame(i).addLink(obj);
				video.updateAllLinks(link.getId(), i);
			}
			else if(tolrFails.size() <= tolrFail)
				tolrFails.push(obj);
			else
				break;
		}
	}
	
	public static void sceneDetect(HyperVideo video) throws Exception{
		int objX = 117, objY = 96;
		int objWidth = 117, objHeight = 96;
		int base = 10, k = 8;
			
		ArrayList<Double> diffList = new ArrayList<Double>();
		
		BufferedImage img1 = VideoReader.importImage(video.getFramePath(0), video.getWidth(), video.getHeight());
		HyperLink obj = new HyperLink(objX, objY, objWidth, objHeight);
		BufferedImage objImage = ObjectTagger.extractImage(img1, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
		
		BufferedImage pre = img1;
		BufferedImage preObjImage = objImage;
		HyperLink preObj = obj;
		for(int counter = 1; counter<video.getFrameSize(); counter=counter+base) {
			BufferedImage cur = VideoReader.importImage(video.getFramePath(counter), video.getWidth(), video.getHeight());
			HyperLink curObj = ObjectTagger.searchImage(cur, preObjImage, preObj, preObj.getX(), preObj.getY(), k);
			diffList.add(curObj.getDiff());
			pre = cur;
			preObj = obj;
			preObjImage = ObjectTagger.extractImage(pre, preObj.getX(), preObj.getY(), preObj.getWidth(), preObj.getHeight());
		}
		
		ArrayList<Double> slope = new ArrayList<Double>();
		
		double preDiff = diffList.get(0);
		for(int i = 1; i<diffList.size(); i++) {
			double curDiff = diffList.get(i);
			double diff = curDiff - preDiff ;
			if(diff < 0)
				diff = 0;
			slope.add(diff);
			preDiff = curDiff;
		}
		
		double mean = mean(slope);
		double std = std(slope);
		
		//System.out.printf("MEAN: %f, STD: %f\n", mean, std);
		
		LinkedList<Integer> indexes = new LinkedList<Integer>();
		
		double thresold = mean + std * 2; 
		for(int i=0; i<slope.size(); i++) {
			if(slope.get(i) > thresold)
				indexes.add((i+2)* base + 1);
		}
		
		ArrayList<Integer> scenes = new ArrayList<Integer>();
		
		int index = indexes.pop();
		scenes.add(index);
		while(!indexes.isEmpty()) {
			index = indexes.pop();
			if(index - scenes.get(scenes.size()-1) >= 90)
				scenes.add(index);
		}
		if(scenes.get(scenes.size()-1)>=9000)
			scenes.remove(scenes.size()-1);
		video.setScenes(scenes);
	}
	
	public static HyperLink searchImage(BufferedImage img, BufferedImage objImage, HyperLink obj, int searchX, int searchY, int searchK) throws Exception {
		int leftSearchBound = Math.max(searchX - searchK, 0);
		int rightSearchBound = Math.min(searchX + searchK, img.getWidth());
		int upperSearchBound = Math.max(searchY - searchK, 0);
		int lowerSearchBound = Math.min(searchY + searchK, img.getHeight());
		double minDiff = Double.MAX_VALUE;
		
		HyperLink minDiffObj = obj.duplicate();
		for(int i = leftSearchBound; i <= rightSearchBound; i++) {
			for(int j = upperSearchBound; j <= lowerSearchBound; j++) {
				BufferedImage tmpObjImage = extractImage(img, i, j, objImage.getWidth(), objImage.getHeight());
				
				if(tmpObjImage!= null) {
					double diff = imageDiff(tmpObjImage, objImage);
					if(diff < minDiff) {
						minDiffObj.setX(i);
						minDiffObj.setY(j);
						minDiffObj.setDiff(diff);
						minDiff = diff;
					}
				}
			}
		}
		//System.out.printf("(%d, %d) W:%d, H%d - Min Diff = %f\n", minDiffObj.getX(), minDiffObj.getY(), minDiffObj.getWidth(), minDiffObj.getHeight(), minDiffObj.getDiff());
		return minDiffObj;
	}
	
	public static double imageDiff(BufferedImage img1, BufferedImage img2) throws Exception {
		if((img1.getWidth() != img2.getWidth())||(img1.getHeight()!=img2.getHeight()))
			throw new Exception("[imageDiff]: img1 and img2 size should be the same");
		double diff = 0;
		for(int i=0; i<img1.getWidth(); i++) {
			for(int j=0; j<img1.getHeight(); j++) {
				diff += pixelDistance(img1.getRGB(i, j), img2.getRGB(i, j));
			}
		}
		//return Math.sqrt(diff/(img1.getWidth()* img1.getHeight()));
		return diff/(img1.getWidth()* img1.getHeight());
	}
	
	public static double pixelDistance(int rgb1, int rgb2) {
		//Euclidean Distance
    	Color c1 = new Color(rgb1);
    	Color c2 = new Color(rgb2);
    	return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }
	
	public static BufferedImage extractImage(BufferedImage img, int x, int y, int width, int height) {	
		if(x + width > img.getWidth() || y + height > img.getHeight())
			return null;
		return img.getSubimage(x, y, width, height);
	}
	
	public static BufferedImage boundObject(BufferedImage img, int x, int y, int width, int height, int rgb) {
		int leftBound = x, rightBound = x + width - 1;
		int upperBound = y, lowerBound = y + height - 1;
		
		//upper bound
		if(upperBound >= 0) {
			for(int i = leftBound; i <= rightBound; i++) 
				img.setRGB(i, upperBound, rgb);
		}
		
		//lower bound
		if(lowerBound <= img.getHeight()) {
			for(int i = leftBound; i <= rightBound; i++)
				img.setRGB(i, lowerBound, rgb);
		}
		
		//left bound
		if(leftBound >=0) {
			for(int j = upperBound; j <= lowerBound; j++)
				img.setRGB(leftBound, j, rgb);
		}
		
		//right bound
		if(rightBound <= img.getWidth()) {
			for(int j = upperBound; j <= lowerBound; j++)
				img.setRGB(rightBound, j, rgb);
		}
		return img;
	}
	
	public static double std(ArrayList<Double> table) { 
    	double mean = mean(table);
        double temp = 0;
        for (int i = 0; i < table.size(); i++)
            temp += Math.pow(table.get(i) - mean, 2);
        
        double meanOfDiffs = temp / (table.size());
        return Math.sqrt(meanOfDiffs);
    }
	
	public static double sum(ArrayList<Double> table) {
    	double sum = 0;
    	for(double item : table)
    		sum += item;
    	return sum;
    }
    
    public static double mean(ArrayList<Double> table) {
    	return sum(table)/table.size();
    }
}
