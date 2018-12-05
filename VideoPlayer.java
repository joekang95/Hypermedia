import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
 
public class VideoPlayer implements ListSelectionListener, ActionListener, MouseListener, MouseMotionListener {
 
    static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage img = null;
    static String items[];
     
    private boolean playing = false, resume = false;
    private Timer timer;
    private int frameCounter = 0, listTracker = 0;
 
    PlayWaveFile sound;
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel videoPanel = new JPanel();
    LayerPresent coverLayer = new LayerPresent(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, true);
    JButton play = new JButton("Play");
    JButton pause = new JButton("Pause");
    JButton stop = new JButton("Stop");
    JList<String> videoList;
    JProgressBar progressBar;
    JTextField progressTime;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem editor = new JMenuItem("Open Editor");
    JLayeredPane videoLayer = new JLayeredPane();
    HyperVideo[] videos;
     
    VideoPlayer(HyperVideo[] videos){
        this.videos = videos; 
        IMAGE_WIDTH = videos[listTracker].getWidth();
        IMAGE_HEIGHT = videos[listTracker].getHeight();
        img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        readImg(videos[listTracker].getFramePath(frameCounter)); 
        GUI();
    }
    
    public void removeVideoLayer(){
    	Component[] compons = videoLayer.getComponents();
    	//System.out.printf("Layer num: H: %d\n", compons.length);
    	for(int i = 1; i<compons.length; i++){
    		videoLayer.remove(videoLayer.getPosition(compons[i]));
    	}
    }
    
    public void updateImage(){
    	System.out.println("KKK");
        frameCounter++;
        readImg(videos[listTracker].getFramePath(frameCounter));
	    ArrayList<HyperLink> links = videos[listTracker].getFrame(frameCounter).getLinks();
	    coverLayer.setClickArea(links);
	    
	    removeVideoLayer();
	    int layer = 1;	
	    for(HyperLink l : links){
	    	videoLayer.add(new LayerPresent(l.getX(), l.getY(), IMAGE_WIDTH, IMAGE_HEIGHT, l.getWidth(), l.getHeight()), layer);
			layer++;
	    }	
		videoLayer.add(videoPanel, layer);
    }
      
    public static void readImg(String fileName) {
    	VideoReader.importImage(img, fileName);
    }
     
    private void GUI() {
         
        DefaultListModel<String> listModel;
        listModel = new DefaultListModel<String>();
        for(HyperVideo v : videos) {
            listModel.addElement(v.getName());
        }
        videoList = new JList<String>(listModel);
        videoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        videoList.setSelectedIndex(0);
        videoList.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(videoList);
        listScrollPane.setPreferredSize(new Dimension(130, 325));
         
        play.addActionListener(this);
        pause.addActionListener(this);
        stop.addActionListener(this);
         
        progressBar = new JProgressBar(0, 300);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setVisible(true);
        progressBar.addMouseListener(this);
        progressBar.addMouseMotionListener(this);
         
        progressTime = new JTextField("0:00/5:00");
        progressTime.setEditable(false);
        progressTime.setBorder(null);
        progressTime.setOpaque(false);
        progressTime.setHorizontalAlignment(JTextField.CENTER);
        progressTime.setBackground(new Color(0,0,0,0));
         
        editor.addActionListener(this);
        menu.add(editor);
        menuBar.add(menu);
         
        panel.setLayout(new GridBagLayout());
        videoLayer.setLayout(null);
        videoLayer.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
         
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.insets = new Insets(10,6,0,0);  // padding
        panel.add(play, c);
 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.4;
        c.insets = new Insets(10,6,0,0);  // padding
        panel.add(pause, c);
 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5;
        c.insets = new Insets(10,6,0,6);  //padding
        panel.add(stop, c);
 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(10,6,10,6);  //padding
        c.gridheight = 5;
        panel.add(listScrollPane, c);
         
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.gridheight = 4;
        c.insets = new Insets(0,20,10,6);  // padding
        videoPanel.add(new JLabel(new ImageIcon (img)));
        videoPanel.setOpaque(false);
        videoPanel.setSize(videoLayer.getPreferredSize());
        
        
        videoLayer.add(coverLayer, 0);
        videoLayer.add(videoPanel, 1);
        panel.add(videoLayer, c);
         
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.insets = new Insets(-10,10,5,6);  // padding
        panel.add(progressBar, c);
         
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 5;
        c.gridwidth = 1;
        c.insets = new Insets(-10,10,5,6);  // padding
        panel.add(progressTime, c);
         
        frame.getContentPane().add(panel);
        frame.pack();       
        frame.setJMenuBar(menuBar);
        frame.setSize(540, 430);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    
    }
 
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting() == false) {
            if(playing) {
                timer.stop();
                sound.stopMusic();
            }
            frameCounter = 0;
            progressTime.setText("0:00/5:00");
            progressBar.setValue(0);
            videoPanel.removeAll();
            listTracker = videoList.getSelectedIndex();
            readImg(videos[listTracker].getFramePath(frameCounter));
            videoPanel.add(new JLabel(new ImageIcon (img)));
            videoPanel.revalidate();
            videoPanel.repaint();   
            playing = false;
            updateImage();
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == play) {
            if(!playing){
                timer = new Timer(1000/30, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(frameCounter == 8999){
                            timer.stop();
                            //sound.stopMusic();
                            progressTime.setText("0:00/5:00");
                            progressBar.setValue(0);
                            readImg(videos[listTracker].getFramePath(0));
                            videoPanel.revalidate();
                            videoPanel.repaint();
                            playing = false;
                            resume = false;
                            frameCounter = 0;
                        }
                        else {
	                        updateImage();
	                        if(frameCounter > 1 && frameCounter % 30 == 0){
	                            int tmp = frameCounter / 30;
	                            int min = tmp / 60, ten = (tmp % 60) / 10, sec = (tmp % 60) % 10;
	                            progressTime.setText(min + ":" + ten + sec + "/5:00");
	                            progressBar.setValue(tmp);
	                            play.getModel().setPressed(false);
	                        }
                        }
                        if(coverLayer.clickDetected){
	                        HyperLink obtainClickLink = coverLayer.click;
	                        int destFrame = obtainClickLink.getToVideoFrameIndex();
	                        String destVideo = obtainClickLink.getToVideoName();
	                        int index = 0;
	                        for(HyperVideo v : videos){
	                        	if(v.getName().equals(destVideo)){
	                        		break;
	                        	}
	                        	index++;
	                        }
	                        videoList.setSelectedIndex(index);
	                        progressBar.setValue(destFrame / 30);
	                        frameCounter = destFrame;
	                        resume = true;
	                        updateImage();
	                        coverLayer.clickDetected = false;
                        }
                    }
                }); 
                
                timer.start(); 
                sound = new PlayWaveFile(videos[listTracker].getAudioPath(), resume, frameCounter); 
                sound.start();
                playing = true;
            }
        }
        if(e.getSource() == pause) {
            timer.stop();   
            playing = false;
            sound.pauseMusic(frameCounter);
            resume = true;
        }
        if(e.getSource() == stop) {
            timer.stop();
            frameCounter = 0;
            playing = false;
            resume = false;
            sound.stopMusic();
            readImg(videos[listTracker].getFramePath(0));
            updateImage();
            progressBar.setValue(0);
            progressTime.setText("0:00/5:00");
        }
        if(e.getSource() == editor) {
        	stop.doClick();
            frame.dispose();
            new VideoEditor(videos);
        }
    }
 
    @Override
    public void mouseClicked(MouseEvent e) {
        //Retrieves the mouse position relative to the component origin.
        int mouseX = e.getX();
        //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
        int progressBarVal = (int)Math.round(((double)mouseX / (double)progressBar.getWidth()) * progressBar.getMaximum());
        progressBar.setValue(progressBarVal);
        int min = progressBarVal / 60, ten = (progressBarVal % 60) / 10, sec = (progressBarVal % 60) % 10;
        progressTime.setText(min + ":" + ten + sec + "/5:00");
        frameCounter = progressBarVal * 30; 
        updateImage();
        resume = true;
        if(playing) {
        	sound.stopMusic();
//        	System.out.println(videos[listTracker].getAudioPath());
            sound = new PlayWaveFile(videos[listTracker].getAudioPath(), resume, frameCounter);
            sound.start();
        }
    }
 
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
         
    }

	
    @Override
	public void mouseDragged(MouseEvent e) {
		//Retrieves the mouse position relative to the component origin.
        int mouseX = e.getX();
        //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
        int progressBarVal = (int)Math.round(((double)mouseX / (double)progressBar.getWidth()) * progressBar.getMaximum());
        boolean outOfRange = false;
        if(progressBarVal > progressBar.getMaximum()){
        	progressBarVal = progressBar.getMaximum();
        	outOfRange = true;
        }
        if(progressBarVal < progressBar.getMinimum()){
        	progressBarVal = progressBar.getMinimum();
        	outOfRange = true;
        }
        progressBar.setValue(progressBarVal);
        int min = progressBarVal / 60, ten = (progressBarVal % 60) / 10, sec = (progressBarVal % 60) % 10;
        progressTime.setText(min + ":" + ten + sec + "/5:00");
        if(!outOfRange){
	        frameCounter = progressBarVal * 30; 
	        updateImage();
	        resume = true;
	        if(playing) {
	        	sound.stopMusic();
	            sound = new PlayWaveFile(videos[listTracker].getAudioPath(), resume, frameCounter);
	            sound.start();
	        }
        }
	}

	
    @Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
 
}