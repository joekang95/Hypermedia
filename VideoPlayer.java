import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VideoPlayer implements ListSelectionListener, ActionListener {

	static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
	static String items[] = {"USCOne", "USCTwo", "LondonOne", "LondonTwo", "NYOne", "NYTwo", "AIFilmOne", "AIFilmTwo"};
	static String videoLocation[] = {"./USC/", "./USC/", "./London/", "./London/", "./NewYorkCity/", "./NewYorkCity/", "./AIFilm/", "./AIFilm/"};
	
	private boolean playing = false;
    private String filename = "";
    private String header = "./USC/";
    private Timer timer;
    private int counter = 1;

	PlayWaveFile sound;
    JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JButton play = new JButton("Play");
	JButton pause = new JButton("Pause");
	JButton stop = new JButton("Stop");
	JList<String> videoList;
	JProgressBar progressBar;
	JTextField progressTime;
	
	VideoPlayer(){
		DefaultListModel<String> listModel;
	    listModel = new DefaultListModel<String>();
	    for(String s : items) {
	    	listModel.addElement(s);
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
	    
	    progressTime = new JTextField("0:00/5:00");
	    progressTime.setEditable(false);
	    progressTime.setBorder(null);
	    progressTime.setOpaque(false);
	    progressTime.setHorizontalAlignment(JTextField.CENTER);
	    progressTime.setBackground(new Color(0,0,0,0));
		
		panel.setLayout(new GridBagLayout());
		
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
	    c.insets = new Insets(10,6,0,6);  //padding
	    c.gridheight = 6;
		panel.add(listScrollPane, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
	    c.gridwidth = 4;
	    c.gridheight = 4;
	    c.insets = new Insets(10,6,10,6);  // padding
		panel2.add(new JLabel(new ImageIcon (img)));
		panel.add(panel2, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
	    c.gridwidth = 4;
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
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		filename = header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + String.format("%04d", counter) + ".rgb";
		readImg(filename);
	}
	
	public void headerSelect() {
		int index = videoList.getSelectedIndex();
		header = videoLocation[index];
	}
	
	public void updateImage(){
		nextFrame();
		readImg(filename);
		panel2.revalidate();
		panel2.repaint();
	}
	
	public void nextFrame() {
		counter ++;
		filename = header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + String.format("%04d", counter) + ".rgb";
	}
	 
	public static void readImg(String fileName) {
		try{
		    File file = new File(fileName);
		    InputStream is = new FileInputStream(file);
	
		    long len = file.length();
		    byte[] bytes = new byte[(int)len];
		    
		    int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0){
	            offset += numRead;
	        }
	    	int id = 0;
	    	for(int y = 0 ; y < IMAGE_HEIGHT ; y++){
				for(int x = 0 ; x < IMAGE_WIDTH ; x++){
					
					byte r = bytes[id];
					byte g = bytes[id + IMAGE_HEIGHT * IMAGE_WIDTH];
					byte b = bytes[id + IMAGE_HEIGHT * IMAGE_WIDTH * 2];

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	                img.setRGB(x, y, pix);
	                id++;
				}
	    	}
			is.close();
			
	   	}catch(FileNotFoundException e) {
	   		e.printStackTrace();
	   	}catch(IOException e) {
	   		e.printStackTrace();
	   	}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting() == false) {
			if(playing) {
				timer.stop();
				sound.stopMusic();
			}
			counter = 1;
			progressTime.setText("0:00/5:00");
            progressBar.setValue(0);
			panel2.removeAll();
			headerSelect();
			filename = header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + String.format("%04d", counter) + ".rgb";
			readImg(filename);
			panel2.add(new JLabel(new ImageIcon (img)));
			panel2.revalidate();
			panel2.repaint();	
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Play") {
			timer = new Timer(1000/30, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	updateImage();
	            	if(counter > 1 && counter % 30 == 0){
	            		int tmp = counter / 30;
	            		int min = tmp / 60, ten = (tmp % 60) / 10, sec = (tmp % 60) % 10;
	            		progressTime.setText(min + ":" + ten + sec + "/5:00");
	                    progressBar.setValue(tmp);
	            	}
	            	if(counter == 9000){
	            		timer.stop();
	            		sound.stopMusic();
	            	}
	            }
	        });
			timer.start();	
			if(counter == 1) {
				sound = new PlayWaveFile(header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + ".wav");
			}
			sound.start();
			playing = true;
		}
		if(e.getActionCommand() == "Pause") {
			timer.stop();	
			playing = false;
			sound.pauseMusic();
		}
		if(e.getActionCommand() == "Stop") {
			timer.stop();
			counter = 1;
			playing = false;
			sound.stopMusic();
		}
	}

}
