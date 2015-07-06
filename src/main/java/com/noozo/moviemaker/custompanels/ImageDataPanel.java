package com.noozo.moviemaker.custompanels;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.noozo.moviemaker.Utils;
import com.noozo.moviemaker.data.DataChangeListener;
import com.noozo.moviemaker.data.ImageData;

public class ImageDataPanel extends GenericDataPanel<ImageData> implements DataChangeListener {

	private JPanel mainPanel;
	private JPanel imageDetailsFields;
    private ImagePanel imagePanel;
    private TextPanel titlePanel;
    private TextPanel title2Panel;
    
    public ImageDataPanel() {
    	
    	super(true);
	}

	@Override
	protected JComponent getChildComponents() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getImageDetailsFields(), BorderLayout.SOUTH);
			mainPanel.add(getImagePanel(), BorderLayout.CENTER);
		}
		return mainPanel;
	}

	@Override
	protected ImageData createNewObject() {
		return new ImageData();
	}

	@Override
	protected void objectToFields(ImageData object) {
		
		getTitlePanel().objectToFields(object.title);
		getTitle2Panel().objectToFields(object.title2);
	}

	@Override
	protected void fieldsToObject(ImageData object) {
		
		getTitlePanel().fieldsToObject(object.title);
		getTitle2Panel().fieldsToObject(object.title2);
	}
	
	@Override
	public void onDataChanged() {
		
		//System.out.println("Updating preview");
		ImageData preview = new ImageData();
		fieldsToObject(preview);
    	getImagePanel().setImage(Utils.overlayText(object.image, preview.title, preview.title2));
	}
	
	private JPanel getImageDetailsFields() {
        if (imageDetailsFields == null) {
        	imageDetailsFields = new JPanel();
        	imageDetailsFields.setLayout(new BoxLayout(imageDetailsFields, BoxLayout.Y_AXIS));
        	imageDetailsFields.add(getTitlePanel());
        	imageDetailsFields.add(getTitle2Panel());
        	imageDetailsFields.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return imageDetailsFields;
    }

    private TextPanel getTitlePanel() {
        if (titlePanel == null) {
        	titlePanel = new TextPanel();
        	titlePanel.setDataChangeListener(this);
        }
        return titlePanel;
    } 
    
    private TextPanel getTitle2Panel() {
        if (title2Panel == null) {
        	title2Panel = new TextPanel();
        	title2Panel.setDataChangeListener(this);
        }
        return title2Panel;
    } 

    private ImagePanel getImagePanel() {
        if (imagePanel == null) {
            imagePanel = new ImagePanel();
            imagePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return imagePanel;
    }
}
