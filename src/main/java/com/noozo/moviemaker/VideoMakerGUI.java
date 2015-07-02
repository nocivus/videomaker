package com.noozo.moviemaker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * void
 * 02/07/15.
 */
public class VideoMakerGUI extends JFrame {

    private JPanel mainPanel;
    private JScrollPane imagesScroll;
    private JList imageList;
    private ImageListModel imageListModel;
    private JPanel sidebarPanel;
    private JPanel actionsPanel;
    private JPanel sidebarActionsPanel;
    private JPanel imageDetailsPanel;
    private JButton addImagesButton;
    private String lastFolder = ".";
    private JPanel imageDetailsFields;
    private ImagePanel imagePanel;
    private JPanel titleTextPanel;
    private JLabel titleTextLabel;
    private JTextField titleTextField;
    private JButton exitButton;
    private JButton generateButton;
    private ColorPickerPanel titleColorPanel;

    @SuppressWarnings("unchecked")
    public VideoMakerGUI() throws IOException {

        super("Video Maker");
        setSize(1024, 768);
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

        getImageListModel().add(new ImageData("templates/black.jpg"));
        getImageListModel().add(new ImageData("test/1.jpg"));
        getImageListModel().add(new ImageData("test/2.jpg"));
        getImageListModel().add(new ImageData("test/3.jpg"));
        getImageListModel().add(new ImageData("templates/black.jpg"));

        updateDetails();
    }

    private void confirmExit() {
        if (Utils.confirm(VideoMakerGUI.this, "Tem a certeza que deseja sair?")) {
            System.exit(0);
        }
    }

    private JPanel createBorderPanel(JPanel panel, JComponent north, JComponent south, JComponent east, JComponent west, JComponent center) {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            if (north != null) {
                panel.add(north, BorderLayout.NORTH);
            }
            if (south != null) {
                panel.add(south, BorderLayout.SOUTH);
            }
            if (east != null) {
                panel.add(east, BorderLayout.EAST);
            }
            if (west != null) {
                panel.add(west, BorderLayout.WEST);
            }
            if (center != null) {
                panel.add(center, BorderLayout.CENTER);
            }
            panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return panel;
    }

    private JPanel createPanel(JPanel panel, JComponent...comps) {
        if (panel == null) {
            panel = new JPanel();
            for (JComponent comp : comps) {
                panel.add(comp);
            }
            panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return panel;
    }

    private JLabel createLabel(JLabel label, String text) {
        if (label == null) {
            label = new JLabel(text);
        }
        return label;
    }

    private JTextField createTextField(JTextField field, int width, KeyAdapter keyAdapter) {
        if (field == null) {
            field = new JTextField();
            field.setSize(width, field.getHeight());
            field.setPreferredSize(new Dimension(width, field.getPreferredSize().height));
            field.setEnabled(false);
            if (keyAdapter != null) {
                field.addKeyListener(keyAdapter);
            }
        }
        return field;
    }

    private JButton createButton(JButton button, String text, ActionListener l) {
        if (button == null) {
            button = new JButton(text);
            button.addActionListener(l);
        }
        return button;
    }

    private JPanel getMainPanel() {

        mainPanel = createBorderPanel(mainPanel, null, getActionsPanel(), null, getSidebarPanel(), getImageDetailsPanel());
        return mainPanel;
    }

    private JPanel getActionsPanel() {
        actionsPanel = createPanel(actionsPanel, getGenerateButton(), getExitButton());
        return actionsPanel;
    }

    private JButton getGenerateButton() {
        generateButton = createButton(generateButton, "Criar video", e -> {

        });
        return generateButton;
    }

    private JButton getExitButton() {
        exitButton = createButton(exitButton, "Sair", e -> {
            confirmExit();
        });
        return exitButton;
    }

    private JPanel getSidebarPanel() {
        sidebarPanel = createBorderPanel(sidebarPanel, null, getSidebarActionsPanel(), null, null, getImagesScroll());
        return sidebarPanel;
    }

    private JPanel getSidebarActionsPanel() {
        sidebarActionsPanel = createPanel(sidebarActionsPanel, getAddImagesButton());
        return sidebarActionsPanel;
    }

    private JScrollPane getImagesScroll() {
        if (imagesScroll == null) {
            imagesScroll = new JScrollPane(getImageList());
        }
        return imagesScroll;
    }

    @SuppressWarnings("unchecked")
    private JList getImageList() {
        if (imageList == null) {
            imageList = new JList();
            imageList.setModel(getImageListModel());
            imageList.setCellRenderer(new ImageDataListCellRenderer());
            imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            imageList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    if (!e.getValueIsAdjusting()) {
                        updateDetails();
                    }
                }
            });
        }
        return imageList;
    }

    private ImageListModel getImageListModel() {
        if (imageListModel == null) {
            imageListModel = new ImageListModel();
        }
        return imageListModel;
    }

    private JPanel getImageDetailsPanel() {
        imageDetailsPanel = createBorderPanel(imageDetailsPanel, null, getImageDetailsFields(), null, null, getImagePanel());
        return imageDetailsPanel;
    }

    private JPanel getImageDetailsFields() {
        imageDetailsFields = createPanel(imageDetailsFields);
        imageDetailsFields.setLayout(new BoxLayout(imageDetailsFields, BoxLayout.Y_AXIS));
        imageDetailsFields.add(getTitleTextPanel());
        return imageDetailsFields;
    }

    private JPanel getTitleTextPanel() {
        titleTextPanel = createPanel(titleTextPanel, getTitleTextLabel(), getTitleTextField(), getTitleColorPanel());
        return titleTextPanel;
    }

    private JLabel getTitleTextLabel() {
        titleTextLabel = createLabel(titleTextLabel, "Título");
        return titleTextLabel;
    }

    private JTextField getTitleTextField() {
        titleTextField = createTextField(titleTextField, 200, new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                super.keyReleased(e);
                updateImage(getSelectedImage());
            }
        });
        return titleTextField;
    }

    private ColorPickerPanel getTitleColorPanel() {
        if (titleColorPanel == null) {
            titleColorPanel = new ColorPickerPanel(20, 20, new ColorPickerPanel.ColorChangeListener() {

                @Override
                public void onColorChanged() {

                    updateImage(getSelectedImage());
                }
            });
        }
        return titleColorPanel;
    }

    private ImagePanel getImagePanel() {
        if (imagePanel == null) {
            imagePanel = new ImagePanel();
            imagePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        }
        return imagePanel;
    }

    private JButton getAddImagesButton() {

        addImagesButton = createButton(addImagesButton, "Adicionar Imagens", e -> {

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
        return addImagesButton;
    }

    private ImageData getSelectedImage() {
        return getImageListModel().getElementAt(imageList.getSelectedIndex());
    }

    private void updateDetails() {

        ImageData data = getSelectedImage();

        // Activar ou desactivar os campos
        getImagePanel().setEnabled(data != null);
        getTitleTextField().setEnabled(data != null);
        getTitleColorPanel().setEnabled(data != null);

        if (data != null) {

            // Definir campos
            getTitleTextField().setText(data.title);
            getTitleColorPanel().setColor(data.titleColor);
            updateImage(data);

        } else {

            // Limpar campos
            getTitleTextField().setText("");
            getTitleColorPanel().clear();
            getImagePanel().clear();
        }
    }
    private void updateImage(ImageData data) {

        if (data == null) {
            return;
        }

        data.title = getTitleTextField().getText();
        data.titleColor = getTitleColorPanel().getColor();
        getImagePanel().setImage(Utils.overlayText(data.image, data.title, data.titleColor));
    }
}
