import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
 
public class VideoEditor implements ListSelectionListener, ActionListener, MouseListener, ChangeListener {
     
    static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage leftImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    static BufferedImage rightImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

    private int frameCounter = 0, leftListTracker = 0, rightListTracker = -1;
    JFrame frame = new JFrame("HyperVideo Linking Tool");
    JPanel panel = new JPanel();
    private static JPanel leftVideo = new JPanel();
    JPanel rightVideo = new JPanel();
    JPanel buttons = new JPanel();
    JButton create = new JButton("Create Link");
    JButton cancel = new JButton("Cancel Link");
    JButton connect = new JButton("Connect");
    JButton save = new JButton("Save");
    JButton delete = new JButton("Delete");
    JButton leftNext = new JButton("< Next Frame");
    JButton leftPre = new JButton("< Previous Frame");
    JButton rightNext = new JButton("Next Frame >");
    JButton rightPre = new JButton("Previous Frame >");
    JList<String> leftVideoList, rightVideoList;
    JComboBox<String> hyperLinkList;
    JSlider leftSlider, rightSlider;
    JTextField leftProgressTime, rightProgressTime;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem player = new JMenuItem("Open Video Player");
    JLayeredPane leftLayer = new JLayeredPane();
    DefaultComboBoxModel<String> comboModel;
     
    HyperVideo[] videos;
    LayerPanel[][] layers = new LayerPanel[9000][100];
    int[] layerCounter = new int[9000];
    
    VideoEditor(HyperVideo[] videos){
        this.videos = videos; 
        IMAGE_WIDTH = videos[leftListTracker].getWidth();
        IMAGE_HEIGHT = videos[leftListTracker].getHeight();
        GUI();
        readImg(leftImg, videos[leftListTracker].getFrame(0).getPath());
    }
     
    public void updateImage(int index){
        if(index == 0) {
            leftLayer.removeAll();
            readImg(leftImg, videos[leftListTracker].getFrame(frameCounter).getPath());
        	int counter = layerCounter[frameCounter];
        	int j = 0;
            if(layers[frameCounter][0] != null) {
            	for(int i = counter; i > 0 ; i--) {
			        leftLayer.add(layers[frameCounter][i - 1], j);
			        j++;
		        }	
            }
            leftLayer.add(leftVideo, j);		
        }
        else if(index == 1) {
            readImg(rightImg, videos[rightListTracker].getFrame(frameCounter).getPath());
            rightVideo.revalidate();
            rightVideo.repaint();
        }
    }
      
    public static void readImg(BufferedImage img, String fileName) {
        VideoReader.importImage(img, fileName);
    }
     
    private void GUI() {
         
        DefaultListModel<String> listModel;
        listModel = new DefaultListModel<String>();
        for(HyperVideo v : videos) {
            listModel.addElement(v.getName());
        }
        
        leftVideoList = new JList<String>(listModel);
        leftVideoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftVideoList.setSelectedIndex(0);
        leftVideoList.addListSelectionListener(this);
        JScrollPane leftListScrollPane = new JScrollPane(leftVideoList);
        leftListScrollPane.setPreferredSize(new Dimension(130, 280));
         
        rightVideoList = new JList<String>(listModel);
        rightVideoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rightVideoList.setSelectedIndex(-1);
        rightVideoList.addListSelectionListener(this);
        JScrollPane rightListScrollPane = new JScrollPane(rightVideoList);
        rightListScrollPane.setPreferredSize(new Dimension(130, 280));

        comboModel = new DefaultComboBoxModel<String>();
        hyperLinkList = new JComboBox<String>(comboModel);
        hyperLinkList.setEditable(false);
        hyperLinkList.setEnabled(false);
         
        create.addActionListener(this);
        connect.addActionListener(this);
        save.addActionListener(this);
        cancel.addActionListener(this);
        delete.addActionListener(this);
         
        leftSlider = new JSlider(0, 9000);
        leftSlider.setMajorTickSpacing(1000);
        leftSlider.setMinorTickSpacing(500);
        leftSlider.setValue(0);
        leftSlider.setPaintTicks(true);
        leftSlider.setPaintLabels(true);
        leftSlider.addMouseListener(this);
        leftSlider.addChangeListener(this);
        
        rightSlider = new JSlider(0, 9000);
        rightSlider.setMajorTickSpacing(1000);
        rightSlider.setMinorTickSpacing(500);
        rightSlider.setValue(0);
        rightSlider.setEnabled(false);
        rightSlider.setPaintTicks(true);
        rightSlider.setPaintLabels(true);
        rightSlider.addMouseListener(this);
        rightSlider.addChangeListener(this);
         
        leftProgressTime = new JTextField("------");
        leftProgressTime.setEditable(false);
        leftProgressTime.setBorder(null);
        leftProgressTime.setOpaque(false);
        leftProgressTime.setHorizontalAlignment(JTextField.CENTER);
        leftProgressTime.setBackground(new Color(0,0,0,0));
         
        rightProgressTime = new JTextField("------");
        rightProgressTime.setEditable(false);
        rightProgressTime.setBorder(null);
        rightProgressTime.setOpaque(false);
        rightProgressTime.setHorizontalAlignment(JTextField.CENTER);
        rightProgressTime.setBackground(new Color(0,0,0,0));
         
        player.addActionListener(this);
        menu.add(player);
        menuBar.add(menu);
         
        panel.setLayout(new GridBagLayout());GridBagConstraints c = new GridBagConstraints();
        buttons.setLayout(new GridBagLayout());GridBagConstraints b = new GridBagConstraints();
        leftLayer.setLayout(null);
        leftLayer.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10,8,0,0);  //padding
        c.gridheight = 3;
        panel.add(leftListScrollPane, c);
         
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.insets = new Insets(10,6,8,0);  // padding
        leftVideo.add(new JLabel(new ImageIcon (leftImg)));
        leftVideo.setOpaque(false);
        leftVideo.setSize(leftLayer.getPreferredSize());
        leftLayer.add(leftVideo, 100);		
        panel.add(leftLayer, c);
         
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.CENTER;
        b.gridx = 0;
        b.gridy = 0;
        b.weightx = 0.4;
        b.insets = new Insets(0,6,15,6);  // padding
        buttons.add(create, b);
 
        b.gridx = 0;
        b.gridy = 1;
        buttons.add(cancel, b);
 
        b.gridx = 0;
        b.gridy = 2;
        buttons.add(connect, b);
 
        b.gridx = 0;
        b.gridy = 3;
        buttons.add(hyperLinkList, b);
 
        b.gridx = 0;
        b.gridy = 4;
        buttons.add(save, b);
 
        b.gridx = 0;
        b.gridy = 5;
        buttons.add(delete, b);
 
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        panel.add(buttons, c);
 
        c.gridx = 5;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.insets = new Insets(10,6,8,0);  // padding
        rightVideo.add(new JLabel(new ImageIcon (rightImg)));
        panel.add(rightVideo, c);
 
        c.gridx = 8;
        c.gridy = 0;
        c.insets = new Insets(10,6,0,8);  //padding
        c.gridwidth = 1;
        c.gridheight = 3;
        panel.add(rightListScrollPane, c);
 
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.insets = new Insets(0,-3,10,-10);  //padding
        panel.add(leftSlider, c);
 
        c.gridx = 5;
        c.gridy = 3;
        c.insets = new Insets(0,-3,10,-10);  //padding
        panel.add(rightSlider, c);
 
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        //c.insets = new Insets(0,6,10,8);  //padding
        panel.add(leftProgressTime, c);
 
        c.gridx = 8;
        c.gridy = 3;
        //c.insets = new Insets(0,8,10,6);  //padding
        panel.add(rightProgressTime, c);
        
        frame.getContentPane().add(panel);
        frame.pack();           
        frame.setJMenuBar(menuBar); 
        frame.setSize(1200, 430);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false);
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting() == false) {
            frameCounter = 0;
            if(e.getSource() == leftVideoList) {
            	leftSlider.setValue(0);
                leftProgressTime.setText("Frame 0001");
                leftListTracker = leftVideoList.getSelectedIndex();
                updateImage(0); 
            }
            if(e.getSource() == rightVideoList) {
            	rightSlider.setValue(0);
                rightProgressTime.setText("Frame 0001");
                rightListTracker = rightVideoList.getSelectedIndex();
                updateImage(1);
                rightSlider.setEnabled(true);
            }
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == create) {
        	int counter = layerCounter[frameCounter];
	        if(hyperLinkList.getItemCount() == 0){
	        	hyperLinkList.setEditable(true);
	        	hyperLinkList.setEnabled(true);
	        }
        	if(counter == 0) {
		        leftLayer.removeAll();
		        LayerPanel newLayer = new LayerPanel(IMAGE_WIDTH, IMAGE_HEIGHT);
		        leftLayer.add(newLayer, 0);	
		        leftLayer.add(leftVideo, 1);	
		        layers[frameCounter][counter] = newLayer;
		        layerCounter[frameCounter] = 1;
        	}
        	else if(counter > 0){
		        leftLayer.removeAll();
		        LayerPanel newLayer = new LayerPanel(IMAGE_WIDTH, IMAGE_HEIGHT);
		        leftLayer.add(newLayer, 0);
		        for(int i = 1 ; i <= counter ; i++) {
			        leftLayer.add(layers[frameCounter][i - 1], i);	
		        }	
		        leftLayer.add(leftVideo, counter + 1);	
		        layers[frameCounter][counter] = newLayer;
		        layerCounter[frameCounter]++;	
        	}
        }
        if(e.getSource() == cancel) {
        	int counter = layerCounter[frameCounter];
        	leftLayer.remove(leftLayer.getIndexOf(layers[frameCounter][counter - 1]));
        	leftLayer.revalidate();
        	leftLayer.repaint();	
        	layerCounter[frameCounter]--;
        }
        if(e.getSource() == connect) {
        }
        if(e.getSource() == save) {
        	String input = hyperLinkList.getEditor().getItem().toString();
        	if(comboModel.getIndexOf(input) == -1){
        		hyperLinkList.addItem(input);
        		hyperLinkList.getEditor().setItem("");
        	}
        	else{
        		JOptionPane.showMessageDialog(frame, "Name Already Exists!");
        	}
        	
        }
        if(e.getSource() == delete) {
        	hyperLinkList.removeItem(hyperLinkList.getSelectedItem());
    		hyperLinkList.getEditor().setItem("");
        }
         
    }
 
    @Override
    public void mouseClicked(MouseEvent e) {
        //Retrieves the mouse position relative to the component origin.
        int mouseX = e.getX();
        if(e.getSource() == leftSlider) {
            int sliderVal = (int)Math.round(((double)mouseX / (double)leftSlider.getWidth()) * leftSlider.getMaximum());
            leftSlider.setValue(sliderVal);
            leftProgressTime.setText("Frame " + String.format("%04d", sliderVal));
            frameCounter = sliderVal;  
            updateImage(0);
        }
        if(e.getSource() == rightSlider) {
            int sliderVal = (int)Math.round(((double)mouseX / (double)rightSlider.getWidth()) * rightSlider.getMaximum());
            rightSlider.setValue(sliderVal);
            rightProgressTime.setText("Frame " + String.format("%04d", sliderVal));
            frameCounter = sliderVal;  
            updateImage(1);
        }
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
         
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
         
    }
 
    @Override
    public void mouseReleased(MouseEvent e) {
         
    }

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == leftSlider) {
			int sliderVal = leftSlider.getValue();
            if(sliderVal < 9000) {
                leftProgressTime.setText("Frame " + String.format("%04d", sliderVal + 1));
            	frameCounter = sliderVal;  
            	updateImage(0); 
            }
            if(sliderVal == 9000) {
                leftProgressTime.setText("Frame " + String.format("%04d", sliderVal));
            	
            }
		}
		if(e.getSource() == rightSlider) {
			int sliderVal = rightSlider.getValue();
            if(sliderVal < 9000) {
                rightProgressTime.setText("Frame " + String.format("%04d", sliderVal + 1));
            	frameCounter = sliderVal;  
            	updateImage(1); 
            }
            if(sliderVal == 9000) {
            	rightProgressTime.setText("Frame " + String.format("%04d", sliderVal));
            	
            }
		}
		
	}
}