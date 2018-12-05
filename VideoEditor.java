import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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

import org.json.JSONException;
 
public class VideoEditor implements ListSelectionListener, ActionListener, MouseListener, ChangeListener, ItemListener {
     
    static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage leftImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    static BufferedImage rightImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

    private int frameCounter = 0, leftListTracker = 0, rightListTracker = -1;
    private boolean creating = false, frameChange = false;
    JFrame frame = new JFrame("HyperVideo Linking Tool");
    JPanel panel = new JPanel();
    JPanel leftVideo = new JPanel();
    JPanel rightVideo = new JPanel();
    JPanel buttons = new JPanel();
    JButton create = new JButton("Create Link");
    JButton cancel = new JButton("Cancel Link");
    JButton connect = new JButton("Connect Frame");
    JButton add = new JButton("Add Link");
    JButton delete = new JButton("Delete Link");
    JButton save = new JButton("Sava Changes");
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
    LayerPanel newLayer = null;
    LayerPanel[] layerPanels;
    ArrayList<HyperLink> links;
    
    VideoEditor(HyperVideo[] videos){
        this.videos = videos; 
        IMAGE_WIDTH = videos[leftListTracker].getWidth();
        IMAGE_HEIGHT = videos[leftListTracker].getHeight();
        GUI();
        readImg(leftImg, videos[leftListTracker].getFramePath(0));
		int layer = 0;
		int selectedIndex = hyperLinkList.getSelectedIndex();
		links = videos[leftListTracker].getFrame(frameCounter).getLinks();
		layerPanels = new LayerPanel[links.size()];
		for(HyperLink l : links){
			boolean top = (layer == selectedIndex) ? true : false;
			layerPanels[layer] = new LayerPanel(l.getX(), l.getY(), IMAGE_WIDTH, IMAGE_HEIGHT, l.getWidth(), l.getHeight(), l.getName(), top);
			leftLayer.add(layerPanels[layer], layer);
			layer++;
    	}
        leftLayer.add(leftVideo, layer);
    }
         
    public void updateImage(int index){
        if(index == 0) {
            leftLayer.removeAll();
            readImg(leftImg, videos[leftListTracker].getFramePath(frameCounter));
			
			// Save temporary changes if frame doesn't change
			int size = videos[leftListTracker].getFrame(frameCounter).getLinksSize();
			int offset = (size == layerPanels.length) ? 0 : (size - layerPanels.length);
		
			if(layerPanels.length != 0 && !frameChange) {
				for(int i = 0 ; i < size - offset; i++) {
					LayerPanel p = layerPanels[i];
					links.get(i).setX((int)p.x1);
					links.get(i).setY((int)p.y1);
					links.get(i).setWidth((int)p.sizeX);
					links.get(i).setHeight((int)p.sizeY);
				}
			}
			
			// Change box only if frame changed
			links = videos[leftListTracker].getFrame(frameCounter).getLinks();	
			if(frameChange) {
	            if(hyperLinkList.getItemCount() != 0) {
	            	comboModel.removeAllElements();
	            };
	            for(HyperLink l : links) {
	            	comboModel.addElement(l.getName());
	            }
			}
			
			// Add layers
			int layer = 0;		
			int selectedIndex = hyperLinkList.getSelectedIndex();
			layerPanels = new LayerPanel[links.size()];
			for(HyperLink l : links){
				boolean top = (layer == hyperLinkList.getSelectedIndex()) ? true : false;
				layerPanels[layer] = new LayerPanel(l.getX(), l.getY(), IMAGE_WIDTH, IMAGE_HEIGHT, l.getWidth(), l.getHeight(), l.getName(), top);
				leftLayer.add(layerPanels[layer], layer);
				layer++;
        	}
            leftLayer.add(leftVideo, layer);
            
            // Move select layer to front
            if(selectedIndex != -1) {
            	leftLayer.moveToFront(leftLayer.getComponent(selectedIndex));
            }
            
            // Reset boolean of frameChange
            frameChange = false;
        }
        else if(index == 1) {
            readImg(rightImg, videos[rightListTracker].getFramePath(frameCounter));
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

    	links = videos[leftListTracker].getFrame(frameCounter).getLinks();

        comboModel = new DefaultComboBoxModel<String>();
    	for(HyperLink link : links){
    		comboModel.addElement(link.getName());
    	}
        hyperLinkList = new JComboBox<String>(comboModel);
        hyperLinkList.setEditable(false);
        hyperLinkList.addItemListener(this);
        if(hyperLinkList.getItemCount() == 0){
        	hyperLinkList.setEnabled(false);
        }
        create.addActionListener(this);
        connect.addActionListener(this);
        add.addActionListener(this);
        cancel.addActionListener(this);
        delete.addActionListener(this);
        save.addActionListener(this);
         
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
        leftLayer.add(leftVideo, 0);		
        panel.add(leftLayer, c);
         
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.CENTER;
        b.gridx = 0;
        b.gridy = 0;
        b.weightx = 0.4;
        b.insets = new Insets(8,6,5,6);  // padding
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
        buttons.add(add, b);
 
        b.gridx = 0;
        b.gridy = 5;
        buttons.add(delete, b);
 
        b.gridx = 0;
        b.gridy = 6;
        buttons.add(save, b);
 
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
                creating = false; 
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
        	if(!creating){
	        	hyperLinkList.setEditable(true);
        		hyperLinkList.getEditor().setItem("");
		        if(hyperLinkList.getItemCount() == 0){
		        	hyperLinkList.setEnabled(true);
		        }
	        	leftLayer.removeAll();
			    int layer = 0;
			    newLayer = new LayerPanel(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
			    leftLayer.add(newLayer, layer);
			    for(HyperLink l : links){
			    	layer++;
	        		leftLayer.add(new LayerPanel(l.getX(), l.getY(), IMAGE_WIDTH, IMAGE_HEIGHT, l.getWidth(), l.getHeight(), l.getName(), false), layer);
	        	}	
			    leftLayer.add(leftVideo, layer + 1);	
			    creating = true;
        	}
        }
        if(e.getSource() == cancel) {
        	if(creating){
        		leftLayer.remove(0);
        		leftLayer.revalidate();
        		leftLayer.repaint();
	        	hyperLinkList.setEditable(false);
        		creating = false;
        		updateImage(0);
        	}
        }
        if(e.getSource() == connect) {
        	int rightFrame = rightSlider.getValue();
        }
        if(e.getSource() == add) {
        	String input = hyperLinkList.getEditor().getItem().toString();
        	if(creating) {
	        	if(comboModel.getIndexOf(input) == -1){
	        		if(input.matches("(\\w|\\d)+")){
		        		HyperLink newLink = new HyperLink((int)newLayer.x1, (int)newLayer.y1, (int)newLayer.sizeX, (int)newLayer.sizeY, input);
		        		//links.add(newLink);
		        		videos[leftListTracker].getFrame(frameCounter).addLink(newLink);
		        		hyperLinkList.addItem(input);
		        		hyperLinkList.setEditable(false);
	        		}
	        		else{
		        		hyperLinkList.getEditor().setItem("");
	            		JOptionPane.showMessageDialog(frame, "Name Can Only Include Alphabets and Numbers");
	        		}
	        	}
	        	else{
	        		hyperLinkList.getEditor().setItem("");
	        		JOptionPane.showMessageDialog(frame, "Name Already Exists!");
	        	}
	    		creating = false;
        	}
        }
        if(e.getSource() == delete) {
//    		HyperLink newLink = links.getLink(hyperLinkList.getSelectedItem());
//    		links.remove(newLink);
    		hyperLinkList.getEditor().setItem("");
    		buttons.revalidate();
    		buttons.repaint();
        }   
        if(e.getSource() == save) {
    		try {
    			updateImage(0);
				videos[leftListTracker].saveMetaData();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
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
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == leftSlider) {
            creating = false;
            hyperLinkList.setSelectedIndex(-1);
			int sliderVal = leftSlider.getValue();
            if(sliderVal < 9000) {
                leftProgressTime.setText("Frame " + String.format("%04d", sliderVal + 1));
                if(frameCounter != sliderVal) {
                	frameChange = true;
                }
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
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == 1 && !creating && !frameChange) {	
			updateImage(0);
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

}