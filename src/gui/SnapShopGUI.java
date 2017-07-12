/*
 * TCSS 305 - Assignment 3: SnapShop
 * Spring 2017
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Creates the GUI to spec to operate on images with buttons.
 * @author Tyler Pitsch
 * @version 4/28/17
 */
public class SnapShopGUI extends JFrame {
    /**
     * Index for special open button to avoid "magic" numbers.
     */
    private static final int OPEN_BUTTON_INDEX = 7;
    /**
     * Open string to stop CheckStyles from telling me I have two of these.
     */
    private static final String OPEN = "Open...";
    /**
     * Auto generated serial version UID.
     */
    private static final long serialVersionUID = -2144739668936849562L;
    /**
     * File Chooser object for the open and save buttons.
     */
    private JFileChooser myFileChooser;
    /**
     * Image that gets the filters added to it before being added to JLabel.
     */
    private PixelImage myImage;
    /**
     * Panel for the Image.
     */
    private JPanel myImagePanel;
    /**
     * JLabel for the pixel image to be packaged into.
     */
    private JLabel myLabel;
    /**
     * Holds all the filters we are using so we can easily make buttons.
     */
    private List<Filter> myFilters;
    /**
     * Holds all the buttons after they are created so we can turn them off and on.
     */
    private List<JButton> myButtons;
    
    
    
    /**
     * Sets the title for the frame.
     */
    public SnapShopGUI() {
        super("SnapShop");
    }

    /**
     * Sets up the frame with all the buttons.
     */
    public void start() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        setLocationRelativeTo(null);
        
       //Make all the objects that we need to use.
        myButtons = new ArrayList<JButton>();
        myFilters = new ArrayList<Filter>();
        myFileChooser = new JFileChooser();
        myFileChooser.setCurrentDirectory(new File("./sample_images"));
        myLabel = new JLabel();
        
        //Fill the objects with these helpers
        addFilters();
        makeButtons();
        turnOffButtons();
        
        //Make all the containers and shove them into the desired areas
        final JPanel buttonContainer = new JPanel(new BorderLayout());
        final JPanel overallPane = new JPanel(new BorderLayout());
        myImagePanel = new JPanel();
        final JPanel uBP = setUpperButtonPane();
        final JPanel imgContainer = new JPanel(new BorderLayout());
        final JPanel lBP = setLowerButtonPane();
        
        imgContainer.add(myImagePanel, BorderLayout.WEST);
        buttonContainer.add(lBP, BorderLayout.SOUTH);
        buttonContainer.add(new JPanel(), BorderLayout.CENTER);
        buttonContainer.add(uBP, BorderLayout.NORTH);
        
        overallPane.add(buttonContainer, BorderLayout.WEST);
        overallPane.add(imgContainer, BorderLayout.CENTER);
        
        
        add(overallPane);
        
        pack();
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
    /**
     * Adds all the filters into a list that we can use as needed.
     */
    private void addFilters() {
        myFilters.add(new EdgeDetectFilter());
        myFilters.add(new EdgeHighlightFilter());
        myFilters.add(new FlipHorizontalFilter());
        myFilters.add(new FlipVerticalFilter());
        myFilters.add(new GrayscaleFilter());
        myFilters.add(new SharpenFilter());
        myFilters.add(new SoftenFilter());
    }
    /**
     * Creates all the buttons based on the filters that we have stored.
     * Must all the special non filter buttons at the end because they are
     * their own methods with anonymous inner classes.
     */
    private void makeButtons() {
        for (final Filter f : myFilters) {
            final JButton j = new JButton(f.getDescription());
            j.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent theEvent) {
                    f.filter(myImage);
                    myLabel.setIcon(new ImageIcon(myImage));
                }
            });
            myButtons.add(j);
        }
        myButtons.add(openButton());
        myButtons.add(saveAsButton());
        myButtons.add(closeImageButton());
    }
    /**
     * Turns on all the buttons when we open an image.
     */
    private void turnOnButtons() {
        for (int i = 0; i <= myButtons.size() - 1; i++) {
            myButtons.get(i).setEnabled(true);
        }
            
    }
    /**
     * Turns off all the buttons except for the open button.
     */
    private void turnOffButtons() {
        for (int i = 0; i <= myButtons.size() - 1; i++) {
            if (!(myButtons.get(i).getText().equals(OPEN))) {
                myButtons.get(i).setEnabled(false);
            }
        }
    }
    /**
     * Creates and adds the buttons to a panel so that we can easily add 
     * the button panel to the frame.
     * @return the filled upper panel to be added to the frame.
     */
    private JPanel setUpperButtonPane() {
        final JPanel buttonPane = new JPanel(new GridLayout(7, 0, 0, 5));
        for (int i = 0; i < myButtons.size(); i++) {
            buttonPane.add(myButtons.get(i));
        }
        return buttonPane;
    }
    /**
     * Creates and adds the buttons to a panel so that we can easily add 
     * the button panel to the frame. 
     * @return the Lower set of Buttons panel.
     */
    private JPanel setLowerButtonPane() {
        final JPanel buttonPane = new JPanel(new GridLayout(3, 0, 0, 5));
        for (int i = OPEN_BUTTON_INDEX; i <= myButtons.size() - 1; i++) {
            buttonPane.add(myButtons.get(i));
        }
        return buttonPane;
    }
    /**
     * Creates a custom open button because it doesn't use a filter 
     * it must be made separate.
     * @return the JButton to open the fileChooser when pressed.
     */
    private JButton openButton() {
        final JButton openButton = new JButton(OPEN);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myFileChooser.showOpenDialog(openButton);
                try {
                    if (myFileChooser.getSelectedFile() != null) {
                        myImage = PixelImage.load(myFileChooser.getSelectedFile());
                        myLabel.setIcon(new ImageIcon(myImage));
                        myImagePanel.add(myLabel);
                        turnOnButtons();
                        setMinimumSize(getPreferredSize());
                        pack();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                }              
            }
            
        });
        return openButton;
    }
    /**
     *  Creates a custom Save As button because it doesn't use a filter 
     * it must be made separate.
     * @return the JButton to cause the image to save.
     */
    private JButton saveAsButton() {
        final JButton save = new JButton("Save As...");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myFileChooser.showSaveDialog(saveAsButton());
                final File saveFile = myFileChooser.getSelectedFile();
                try {
                    myImage.save(saveFile);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                
            }
            
        });
        return save;
    }
    /**
     *  Creates a custom Close button because it doesn't use a filter 
     * it must be made separate from the filters.
     * @return the JButton to case the image to be closed.
     */
    private JButton closeImageButton() {
        final JButton close = new JButton("Close Image");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myLabel.setIcon(null);
                turnOffButtons();
                setMinimumSize(getPreferredSize());
                pack();
            }
            
        });
        return close;
    }
}