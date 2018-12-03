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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
 
public class VideoEditor implements ListSelectionListener, ActionListener, MouseListener {
     
    static int IMAGE_WIDTH = 352;
    static int IMAGE_HEIGHT = 288;
    static BufferedImage leftImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    static BufferedImage rightImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
     
    private int frameCounter = 0, leftListTracker = 0, rightListTracker = -1;
    JFrame frame = new JFrame("HyperVideo Linking Tool");
    JPanel panel = new JPanel();
    JPanel leftVideo = new JPanel();
    JPanel rightVideo = new JPanel();
    JPanel buttons = new JPanel();
    JButton create = new JButton("Create Link");
    JButton connect = new JButton("Connect");
    JButton save = new JButton("Save");
    JButton leftNext = new JButton("< Next Frame");
    JButton leftPre = new JButton("< Previous Frame");
    JButton rightNext = new JButton("Next Frame >");
    JButton rightPre = new JButton("Previous Frame >");
    JList<String> leftVideoList, rightVideoList;
    JProgressBar leftProgressBar, rightProgressBar;
    JTextField leftProgressTime, rightProgressTime;
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem player = new JMenuItem("Open Video Player");
     
    HyperVideo[] videos;
     
    VideoEditor(HyperVideo[] v){
        videos = v;
        GUI();
        readImg(leftImg, videos[leftListTracker].getFrame(0));
    }
     
    public void updateImage(int index){
        if(index == 0) {
            readImg(leftImg, videos[leftListTracker].getFrame(frameCounter));
            leftVideo.revalidate();
            leftVideo.repaint();
        }
        else if(index == 1) {
            readImg(rightImg, videos[rightListTracker].getFrame(frameCounter));
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
         
        create.addActionListener(this);
        connect.addActionListener(this);
        save.addActionListener(this);
         
        leftProgressBar = new JProgressBar(0, 9000);
        leftProgressBar.setValue(0);
        leftProgressBar.setStringPainted(false);
        leftProgressBar.setVisible(true);
        leftProgressBar.addMouseListener(this);
         
        rightProgressBar = new JProgressBar(0, 9000);
        rightProgressBar.setValue(0);
        rightProgressBar.setStringPainted(false);
        rightProgressBar.setVisible(true);
        rightProgressBar.addMouseListener(this);
         
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
        panel.add(leftVideo, c);
         
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.CENTER;
        b.gridx = 0;
        b.gridy = 0;
        b.insets = new Insets(0,6,8,6);  // padding
        buttons.add(leftNext, b);
 
        b.gridx = 0;
        b.gridy = 1;
        buttons.add(leftPre, b);
 
        b.gridx = 0;
        b.gridy = 2;
        buttons.add(create, b);
 
        b.gridx = 0;
        b.gridy = 3;
        buttons.add(connect, b);
 
        b.gridx = 0;
        b.gridy = 4;
        buttons.add(save, b);
 
        b.gridx = 0;
        b.gridy = 5;
        buttons.add(rightNext, b);
 
        b.gridx = 0;
        b.gridy = 6;
        buttons.add(rightPre, b);
 
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
        c.insets = new Insets(0,8,10,0);  //padding
        panel.add(leftProgressBar, c);
 
        c.gridx = 5;
        c.gridy = 3;
        c.insets = new Insets(0,8,10,0);  //padding
        panel.add(rightProgressBar, c);
 
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
                leftProgressBar.setValue(1);
                leftProgressTime.setText("Frame 0001");
                leftListTracker = leftVideoList.getSelectedIndex();
                updateImage(0); 
            }
            if(e.getSource() == rightVideoList) {
                rightProgressBar.setValue(1);
                rightProgressTime.setText("Frame 0001");
                rightListTracker = rightVideoList.getSelectedIndex();
                updateImage(1);
            }
             
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == create) {
        }
        if(e.getSource() == connect) {
        }
        if(e.getSource() == save) {
        }
         
    }
 
    @Override
    public void mouseClicked(MouseEvent e) {
        //Retrieves the mouse position relative to the component origin.
        int mouseX = e.getX();
        if(e.getSource() == leftProgressBar) {
            //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
            int progressBarVal = (int)Math.round(((double)mouseX / (double)leftProgressBar.getWidth()) * leftProgressBar.getMaximum());
            leftProgressBar.setValue(progressBarVal);
            leftProgressTime.setText("Frame " + String.format("%04d", progressBarVal));
            frameCounter = progressBarVal;  
            updateImage(0);
        }
        else if(e.getSource() == rightProgressBar) {
            if(rightListTracker != -1) {
                int progressBarVal = (int)Math.round(((double)mouseX / (double)rightProgressBar.getWidth()) * rightProgressBar.getMaximum());
                rightProgressBar.setValue(progressBarVal);
                rightProgressTime.setText("Frame " + String.format("%04d", progressBarVal));
                frameCounter = progressBarVal;  
                updateImage(1);
            }
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