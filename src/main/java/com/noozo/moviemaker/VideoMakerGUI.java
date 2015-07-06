package com.noozo.moviemaker;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.noozo.moviemaker.custompanels.GenericDataPanel.Mode;
import com.noozo.moviemaker.custompanels.ImageDataPanel;
import com.noozo.moviemaker.data.CustomListModel;
import com.noozo.moviemaker.data.ImageData;
import com.noozo.moviemaker.data.ImageDataListCellRenderer;

/**
 * void
 * 02/07/15.
 */
public class VideoMakerGUI extends JFrame {

	VideoMaker maker = new VideoMaker();
    private JPanel mainPanel;
    private JScrollPane imagesScroll;
    private JList<ImageData> imageList;
    private CustomListModel<ImageData> imageListModel;
    private JPanel sidebarPanel;
    private JPanel actionsPanel;
    private JPanel sidebarActionsPanel;
    private ImageDataPanel imageDetailsPanel;
    private JButton addImagesButton;
    private String lastFolder = ".";
    private JButton exitButton;
    private JButton generateButton;
    private ImageData selected;
    private JButton moveUpButton;
    private JButton moveDownButton;

    public VideoMakerGUI() throws Exception {

        setTitle("Video Maker");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                super.windowClosing(e);
                confirmExit();
            }
        });
        setContentPane(getMainPanel());
        Utils.centerWindow(this);

        ImageData firstSlide = new ImageData(VideoMaker.BLACK);
        firstSlide.title.fontSize = 30;
        firstSlide.title.position = new Point(280, 360);
        firstSlide.title.text = "Moradia T3 - 195.000 Euro";
        firstSlide.title2.fontSize = 20;
        firstSlide.title2.position = new Point(420, 420);
        firstSlide.title2.text = "Quinta do Conde";
        
        ImageData lastSlide = new ImageData(VideoMaker.BLACK);
        lastSlide.title.fontSize = 30;
        lastSlide.title.position = new Point(280, 360);
        lastSlide.title.text = "Isabel Dias - 939 608 228";
        lastSlide.title2.fontSize = 20;
        lastSlide.title2.position = new Point(400, 420);
        lastSlide.title2.text = "Tippy Family Century 21";        
        
        getImageListModel().add(firstSlide);        
        getImageListModel().add(lastSlide);
    }

    private void confirmExit() {
        if (Utils.confirm(VideoMakerGUI.this, "Tem a certeza que deseja sair?")) {
            System.exit(0);
        }
    }
    
    private void loadImageData(ImageData data) {
    	
    	getImageDetailsPanel().setVisible(data != null);
    	
    	// Force repaint, so we get proper dimensions
        this.validate();
        this.repaint();
    	
    	if (data != null) {
    		getImageDetailsPanel().setObject(data, Mode.EDIT);
    	}
    }
    
    private ImageData getSelectedImage() {
        return getImageList().getSelectedValue();
    }
    
    private JPanel getMainPanel() {
    	if (mainPanel == null) {
    		mainPanel = new JPanel();
    		mainPanel.setLayout(new BorderLayout());
    		mainPanel.add(getActionsPanel(), BorderLayout.SOUTH);
    		mainPanel.add(getSidebarPanel(), BorderLayout.WEST);
    		mainPanel.add(getImageDetailsPanel(), BorderLayout.CENTER);
    		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return mainPanel;
    }

    private JPanel getActionsPanel() {
        if (actionsPanel == null) {
        	actionsPanel = new JPanel();
        	actionsPanel.add(getGenerateButton());
        	actionsPanel.add(getExitButton());
        	actionsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return actionsPanel;
    }

    private JButton getGenerateButton() {
    	if (generateButton == null) {
    		generateButton = new JButton("Criar video");
    		generateButton.addActionListener(e -> {
    			
    			// Update the current selection data
    			getImageDetailsPanel().callFieldsToObject();
    			
    			// Ask for file location and name
    			File movieFile = Utils.askForTarget("mp4", this);
    			
    			// Ask for seconds per image
    			int secondsPerImage = Utils.integerInput("Segundos por imagem?", 5);
    			
    			// Generate movie
    			try {
    			
    				maker.process(getImageListModel().getData());
    				maker.create(movieFile.getAbsolutePath(), secondsPerImage);
    				
    				// Ok when done
    				Utils.showInfo("Video criado com sucesso em: " + movieFile.getAbsolutePath());
    			
    			} catch (Exception exc) {
    			
    				Utils.showError("Erro ao criar o video: " + exc.getMessage());
    				exc.printStackTrace();
    			}
    		});
    	}
        return generateButton;
    }

    private JButton getExitButton() {
    	if (exitButton == null) {
    		exitButton = new JButton("Sair");
    		exitButton.addActionListener(e -> {
    			confirmExit();
    		});
    	}
    	return exitButton;
    }

    private JPanel getSidebarPanel() {
        if (sidebarPanel == null) {
        	sidebarPanel = new JPanel();
        	sidebarPanel.setLayout(new BorderLayout());
        	sidebarPanel.add(getSidebarActionsPanel(), BorderLayout.SOUTH);
        	sidebarPanel.add(getImagesScroll(), BorderLayout.CENTER);
        	sidebarPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return sidebarPanel;
    }

    private JPanel getSidebarActionsPanel() {
        if (sidebarActionsPanel == null) {
        	sidebarActionsPanel = new JPanel();
        	sidebarActionsPanel.add(getAddImagesButton());
        	sidebarActionsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        	sidebarActionsPanel.add(getMoveUpButton());
        	sidebarActionsPanel.add(getMoveDownButton());
        }
        return sidebarActionsPanel;
    }

    private JScrollPane getImagesScroll() {
        if (imagesScroll == null) {
            imagesScroll = new JScrollPane(getImageList());
        }
        return imagesScroll;
    }
    
    private JList<ImageData> getImageList() {
        if (imageList == null) {
            imageList = new JList<>();
            imageList.setModel(getImageListModel());
            imageList.setCellRenderer(new ImageDataListCellRenderer());
            imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            imageList.addListSelectionListener(new ListSelectionListener() {

				@Override
                public void valueChanged(ListSelectionEvent e) {

					if (!e.getValueIsAdjusting()) {
						
						// Update the values of the current one
						if (selected != null) {
							//System.out.println("Saving information for the current selection");
							getImageDetailsPanel().callFieldsToObject();
						}
	
						// Load the data from the newly selected
						selected = getSelectedImage();
						//System.out.println("Current selection is now " + selected);
						//System.out.println("Loading information from the current selection");
	                    loadImageData(selected);
                    }                    
                }
            });
        }
        return imageList;
    }

    private CustomListModel<ImageData> getImageListModel() {
        if (imageListModel == null) {
            imageListModel = new CustomListModel<>();
        }
        return imageListModel;
    }

    private ImageDataPanel getImageDetailsPanel() {
        if (imageDetailsPanel == null) {
        	imageDetailsPanel = new ImageDataPanel();
        	imageDetailsPanel.setVisible(false);
        	imageDetailsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return imageDetailsPanel;
    }

    private JButton getAddImagesButton() {
    	if (addImagesButton == null) {
    		addImagesButton = new JButton("Adicionar Imagens");
    		addImagesButton.addActionListener(e -> {

	            JFileChooser f = new JFileChooser(lastFolder);
	            f.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            f.setMultiSelectionEnabled(true);
	            f.setFileFilter(new FileFilter() {
	
	                @Override
	                public boolean accept(File f) {
	                    return f.getName().toLowerCase().endsWith("jpg");
	                }
	
	                @Override
	                public String getDescription() {
	                    return "JPG files";
	                }
	            });
	            f.showOpenDialog(null);
	
	            if (f.getSelectedFiles() != null) {
	
	                for (File file : f.getSelectedFiles()) {
	
	                    try {
	
	                        getImageListModel().add(new ImageData(file.getAbsolutePath()));
	
	                    } catch (IOException e1) {
	
	                        Utils.showError("Não foi possível adicionar a imagem: " + file.getAbsolutePath());
	                        e1.printStackTrace();
	                    }
	                    lastFolder = file.getParent();
	                }
	            }
	        });
    	}
        return addImagesButton;
    }
	private JButton getMoveUpButton() {
		if (moveUpButton == null) {
			moveUpButton = new JButton("^");
			moveUpButton.addActionListener(e -> {
			
				int index = getImageList().getSelectedIndex();
				getImageListModel().moveUp(selected);
				getImageList().setSelectedIndex(index-1);
			});
		}
		return moveUpButton;
	}
	private JButton getMoveDownButton() {
		if (moveDownButton == null) {
			moveDownButton = new JButton("v");
			moveDownButton.addActionListener(e -> { 
				int index = getImageList().getSelectedIndex();
				getImageListModel().moveDown(selected);
				getImageList().setSelectedIndex(index+1);
			});
		}
		return moveDownButton;
	}
}
