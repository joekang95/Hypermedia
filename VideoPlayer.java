import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VideoPlayer implements ListSelectionListener, ActionListener, MouseListener {

	static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
	static String items[] = {"USCOne", "USCTwo", "LondonOne", "LondonTwo", "NYOne", "NYTwo", "AIFilmOne", "AIFilmTwo"};
	static String videoLocation[] = {"./USC/", "./USC/", "./London/", "./London/", "./NewYorkCity/", "./NewYorkCity/", "./AIFilm/", "./AIFilm/"};
	
	/*File folder = new File(".\\Film");
    File[] listOfFiles = folder.listFiles();
    String[] listOfFolders = new String[listOfFiles.length];

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        //System.out.println("File " + listOfFiles[i].getName());
      } else if (listOfFiles[i].isDirectory()) {
        //System.out.println("Directory " + listOfFiles[i].getName());
        listOfFolders[i] = listOfFiles[i].getName();
      }
       
    }
    folder = new File(".\\Film.\\" + listOfFolders[0] + ".\\" + listOfFolders[0] + "One");
    File[] listOfFiles2 = folder.listFiles();
     
    for (int i = 0; i < listOfFiles2.length; i++) {
      if (listOfFiles2[i].isFile()) {
        System.out.println("File " + listOfFiles2[i].getName());
      } else if (listOfFiles2[i].isDirectory()) {
        System.out.println("Directory " + listOfFiles2[i].getName());
      }
       
    }*/
	
	private boolean playing = false, resume = false;
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
	    progressBar.addMouseListener(this);
	    
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
	            		progressTime.setText("0:00/5:00");
	                    progressBar.setValue(0);
	            	}
	            }
	        });
			timer.start();	
			sound = new PlayWaveFile(header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + ".wav", resume, counter);
			
			sound.start();
			playing = true;
		}
		if(e.getActionCommand() == "Pause") {
			timer.stop();	
			playing = false;
			sound.pauseMusic(counter);
			resume = true;
		}
		if(e.getActionCommand() == "Stop") {
			timer.stop();
			counter = 1;
			playing = false;
			resume = false;
			sound.stopMusic();
    		readImg(header + videoList.getSelectedValue() + "/" + videoList.getSelectedValue() + String.format("%04d", counter) + ".rgb");
    		panel2.revalidate();
    		panel2.repaint();
            progressBar.setValue(0);
    		progressTime.setText("0:00/5:00");
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
	     counter = progressBarVal * 30;	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
