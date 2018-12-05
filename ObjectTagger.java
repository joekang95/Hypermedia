import java.awt.Color;
import java.awt.image.BufferedImage;

public class ObjectTagger {
	
	final static int COLOR_RED = new Color(255,0,0).getRGB();
    final static int COLOR_GREEN = new Color(0,255,0).getRGB();
    final static int COLOR_BLUE =new Color(0,0,255).getRGB();
	
	public static void autoTagging(HyperLink link, HyperVideo video, int frameIndex, int k, int thresold) throws Exception {
		BufferedImage img = VideoReader.importImage(video.getFramePath(frameIndex), video.getWidth(), video.getHeight());
		BufferedImage linkImage = extractImage(img, link.getX(), link.getY(), link.getWidth(), link.getHeight());
		
		HyperLink obj = link.duplicate();	
		for(int i = frameIndex+1; i < video.getFrameSize(); i++) {
			System.out.printf("Frame: %d, ",i);
			BufferedImage curFrame = VideoReader.importImage(video.getFramePath(i), video.getWidth(), video.getHeight());
			obj = searchImage(curFrame, linkImage, obj, obj.getX(), obj.getY(), k);
		
			if(obj.getDiff() < thresold) {
				video.getFrame(i).addLink(obj);
				video.updateAllLinks(link.getId(), i);
			}
			else
				break;
		}
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
		System.out.printf("(%d, %d) W:%d, H%d - Min Diff = %f\n", minDiffObj.getX(), minDiffObj.getY(), minDiffObj.getWidth(), minDiffObj.getHeight(), minDiffObj.getDiff());
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
}
