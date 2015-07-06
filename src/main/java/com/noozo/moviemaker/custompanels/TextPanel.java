package com.noozo.moviemaker.custompanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

import com.noozo.moviemaker.VideoMaker;
import com.noozo.moviemaker.data.DataChangeListener;
import com.noozo.moviemaker.data.Text;

public class TextPanel extends GenericDataPanel<Text> {
	public TextPanel() {
	}
	
	private JLabel titleTextLabel;
    private JTextField titleTextField;
    private ColorPickerPanel titleColorPanel;
    private JSpinner titleXPosition;
    private JLabel lblX;
    private JSeparator separator;
    private JSeparator separator_1;
    private JLabel lblY;
    private JSpinner titleYPosition;
    private JSpinner fontSize;
    private JSeparator separator_2;
    private JLabel lblTamanho;
	private DataChangeListener listener;
	private JPanel mainPanel;
	private JButton btnCentrar;
	
	@Override
	protected JComponent getChildComponents() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.add(getTitleTextLabel());
			mainPanel.add(getTitleTextField());
			mainPanel.add(getSeparator());
			mainPanel.add(getTitleColorPanel());
			mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			mainPanel.add(getSeparator_1());
			mainPanel.add(getLblX());
			mainPanel.add(getTitleXPosition());
			mainPanel.add(getLblY());
			mainPanel.add(getTitleYPosition());
			mainPanel.add(getSeparator_2());
			mainPanel.add(getBtnCentrar());
			mainPanel.add(getLblTamanho());
			mainPanel.add(getFontSize());
		}
		return mainPanel;
	}
	
	public void setDataChangeListener(DataChangeListener listener) {
		
		this.listener = listener;
	}
	
	@Override
	protected Text createNewObject() {
		return new Text();
	}

	@Override
	protected void fieldsToObject(Text object) {
		object.text = getTitleTextField().getText();
		object.color = getTitleColorPanel().getColor();
		object.position = new Point((int)getTitleXPosition().getValue(), (int)getTitleYPosition().getValue());
		object.fontSize = (int)getFontSize().getValue();
	}
	
	@Override
	public void objectToFields(Text data) {
		
		getTitleTextField().setText(data.text);
        getTitleColorPanel().setColor(data.color);
        getTitleXPosition().setValue(data.position.x);
        getTitleYPosition().setValue(data.position.y);
        getFontSize().setValue(data.fontSize);
	}
	
	@Override
	public void clear() {
		
		getTitleTextField().setText("");
        getTitleColorPanel().clear();
        getTitleXPosition().setValue(0);
        getTitleYPosition().setValue(0);
        getFontSize().setValue(20);
	}
	
	private void update() {
		
		listener.onDataChanged();
	}

	private JLabel getTitleTextLabel() {
    	if (titleTextLabel == null) {
    		titleTextLabel = new JLabel("Texto");
    	}
        return titleTextLabel;
    }

    private JTextField getTitleTextField() {
    	if (titleTextField == null) {
    		titleTextField = new JTextField();
    		titleTextField.setSize(300, titleTextField.getHeight());
    		titleTextField.setPreferredSize(new Dimension(300, titleTextField.getPreferredSize().height));
    		titleTextField.addKeyListener(new KeyAdapter() {

    			@Override
	                public void keyReleased(KeyEvent e) {
	
	                    super.keyReleased(e);
	                    update();
	                }
	        });
        }
        return titleTextField;
    }

    private ColorPickerPanel getTitleColorPanel() {
        if (titleColorPanel == null) {
            titleColorPanel = new ColorPickerPanel(20, 20, new ColorPickerPanel.ColorChangeListener() {

                @Override
                public void onColorChanged() {
                	update();
                }
            });
        }
        return titleColorPanel;
    }
	
	private JSpinner getTitleXPosition() {
		if (titleXPosition == null) {
			titleXPosition = new JSpinner(new SpinnerNumberModel(0, 0, VideoMaker.VIDEO_WIDTH, 20));
			titleXPosition.addChangeListener(e -> update());
		}
		return titleXPosition;
	}
	private JLabel getLblX() {
		if (lblX == null) {
			lblX = new JLabel("X");
		}
		return lblX;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	private JSeparator getSeparator_1() {
		if (separator_1 == null) {
			separator_1 = new JSeparator();
		}
		return separator_1;
	}
	private JLabel getLblY() {
		if (lblY == null) {
			lblY = new JLabel("Y");
		}
		return lblY;
	}
	private JSpinner getTitleYPosition() {
		if (titleYPosition == null) {
			titleYPosition = new JSpinner(new SpinnerNumberModel(0, 0, VideoMaker.VIDEO_HEIGHT, 20));
			titleYPosition.addChangeListener(e -> update());
		}
		return titleYPosition;
	}
	private JSpinner getFontSize() {
		if (fontSize == null) {
			fontSize = new JSpinner(new SpinnerNumberModel(20, 12, 100, 1));
			fontSize.addChangeListener(e -> update());
		}
		return fontSize;
	}
	private JSeparator getSeparator_2() {
		if (separator_2 == null) {
			separator_2 = new JSeparator();
		}
		return separator_2;
	}
	private JLabel getLblTamanho() {
		if (lblTamanho == null) {
			lblTamanho = new JLabel("Tamanho");
		}
		return lblTamanho;
	}

	private JButton getBtnCentrar() {
		if (btnCentrar == null) {
			btnCentrar = new JButton("><");
			btnCentrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font font = new Font("Verdana", Font.BOLD, (int) getFontSize().getValue());
					BufferedImage img = new BufferedImage(VideoMaker.VIDEO_WIDTH, VideoMaker.VIDEO_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
					FontMetrics fm = img.createGraphics().getFontMetrics();
					getTitleXPosition().setValue((int)((float)VideoMaker.VIDEO_WIDTH/2f-(float)fm.stringWidth(getTitleTextField().getText())/2f));
					getTitleYPosition().setValue((int)((float)VideoMaker.VIDEO_HEIGHT/2f-(float)font.getSize()/2f));
					update();
				}
			});
			btnCentrar.setVisible(false);
		}
		return btnCentrar;
	}
}
