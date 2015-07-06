package com.noozo.moviemaker.custompanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.google.common.collect.Lists;
import com.noozo.moviemaker.Utils;

public abstract class GenericDataPanel<DATAOBJECT> extends JPanel {

    public enum Mode {

        CREATE, EDIT, SHOW
    }

    protected Mode       mode;
    protected DATAOBJECT object;
    private JLabel       errorsLabel;
    private JScrollPane  jScrollPane;
    private JPanel       childComponentsOuterPanel;

    public GenericDataPanel() {

        this(false);
    }

    public GenericDataPanel(boolean fill) {

        // Set the object as a new one, for creation
        setObject(createNewObject(), Mode.CREATE);
        onModeChangedToCreate();

        initialize(fill);
    }

    public GenericDataPanel(int labelWidth) {

        this();
    }

    public GenericDataPanel(DATAOBJECT object, Mode mode) {

        if (object == null) {

            setObject(createNewObject(), mode);
            onModeChangedToCreate();

        } else {

            // Update the fields based on the object
            setObject(object, mode);
        }

        initialize();
    }

    private void initialize(boolean fill) {

        setLayout(new BorderLayout());

        if (getChildComponents() instanceof JTabbedPane || fill) {

            add(getChildComponents(), BorderLayout.CENTER);

        } else {

            add(getJScrollPane(), BorderLayout.CENTER);
        }

        afterInitialized();
    }

    private void initialize() {

        initialize(false);
    }

    private JScrollPane getJScrollPane() {

        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBorder(null);
            jScrollPane.setViewportView(getChildComponentsOuterPanel());
        }
        return jScrollPane;
    }

    protected void enableScrollbars(boolean enable) {

        if (enable) {

            getJScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            getJScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        } else {

            getJScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            getJScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
    }

    /**
     * This prevents children's Y_AXIS BoxLayout to look terrible
     *
     * @return a JPanel
     */
    private JPanel getChildComponentsOuterPanel() {

        if (childComponentsOuterPanel == null) {
            childComponentsOuterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            childComponentsOuterPanel.add(getChildComponents());
        }
        return childComponentsOuterPanel;
    }

    private JLabel getErrorsLabel() {

        if (errorsLabel == null) {

            errorsLabel = new JLabel();
            //errorsLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            errorsLabel.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(5, 5, 5, 5))); //$NON-NLS-1$
        }
        return errorsLabel;
    }

    public void showErrors(List<String> errors) {

        add(getErrorsLabel(), BorderLayout.SOUTH);
        getErrorsLabel().setText(errors.stream().collect(Collectors.joining())); //$NON-NLS-1$
    }

    public void clearErrors() {

        remove(getErrorsLabel());
        getErrorsLabel().setText(""); //$NON-NLS-1$
    }

    public void setMode(Mode mode) {

        setMode(mode, true);
    }

    public void setMode(Mode mode, boolean doActions) {

        //LogUtils.debug("Enabling? " + (mode != Mode.SHOW)); //$NON-NLS-1$
        this.mode = mode;
        if (doActions) {
            Utils.enabledDisableChildren(this, mode != Mode.SHOW);
            remove(getErrorsLabel());

            // The children might want to do something to its fields, based on the
            // mode
            switch (mode) {

                case CREATE:
                    onModeChangedToCreate();
                    break;
                case EDIT:
                    onModeChangedToEdit();
                    break;
                case SHOW:
                default:
                    onModeChangedToShow();
                    break;
            }
        }
    }

    public Mode getMode() {

        return mode;
    }

    /**
     * The panel will start using this object and fill its fields with the
     * object data
     */
    public void setObject(DATAOBJECT object, Mode mode) {

        this.object = object;

        setMode(mode, false);

        if (object != null) {

            objectToFields(this.object);

        } else {

            clear();
        }
        setMode(mode);

    }

    /**
     * Reloads the GUI representation of the object
     */
    public void callObjectToFields() {

        objectToFields(object);
    }

    /**
     * Reloads the object from the representation of the object
     */
    public void callFieldsToObject() {

        fieldsToObject(object);
    }

    /**
     * Returns the object without updating the fields first
     */
    public DATAOBJECT getObjectWithoutUpdating() {

        if (object == null) {

            object = createNewObject();
        }
        return object;
    }

    /**
     * The panel populates the object with the data in its fields and returns
     * the object
     */
    public DATAOBJECT getObject() {

        if (object == null) {

            object = createNewObject();
        }

        fieldsToObject(object);
        return object;
    }

    /**
     * Determines whether the panel is in Mode.EDIT mode
     *
     * @return true if so
     */
    public boolean isEdit() {

        //        LogUtils.debug("Mode: " + this.mode); //$NON-NLS-1$
        //        LogUtils.debug("Editing: " + (this.mode == Mode.EDIT)); //$NON-NLS-1$
        return this.mode == Mode.EDIT;
    }

    public boolean isCreate() {

        //        LogUtils.debug("Mode: " + this.mode); //$NON-NLS-1$
        //        LogUtils.debug("Create: " + (this.mode == Mode.CREATE)); //$NON-NLS-1$
        return this.mode == Mode.CREATE;
    }

    public boolean isShow() {

        //        LogUtils.debug("Mode: " + this.mode); //$NON-NLS-1$
        //        LogUtils.debug("Show: " + (this.mode == Mode.SHOW)); //$NON-NLS-1$
        return this.mode == Mode.SHOW;
    }

    /**
     * Clear the panel's fields *
     */
    public void clear() {

    }

    // ********************
    // ******************** These need to be extended by the children
    // ********************

    /**
     * The child needs to return its main panel *
     */
    protected abstract JComponent getChildComponents();

    /**
     * The child panel knows how to instantiate a new data object *
     */
    protected abstract DATAOBJECT createNewObject();

    /**
     * Tell the child panel to transfer object data to the panel fields *
     */
    protected abstract void objectToFields(DATAOBJECT object);

    /**
     * Tell the child panel to transfer the information in its fields to this
     * object
     */
    protected abstract void fieldsToObject(DATAOBJECT object);

    // ********************
    // ******************** These might be overriden by the children
    // ********************

    /**
     * Called by the {@link GenericDataPanel} when the mode changes to
     * Mode.CREATE
     */
    protected void onModeChangedToCreate() {

    }

    /**
     * Called by the {@link GenericDataPanel} when the mode changes to Mode.EDIT
     */
    protected void onModeChangedToEdit() {

    }

    /**
     * Called by the {@link GenericDataPanel} when the mode changes to Mode.SHOW
     */
    protected void onModeChangedToShow() {

    }

    /**
     * Called by the {@link GenericDataPanel} when panel initialization has
     * finished (after constructor)
     */
    protected void afterInitialized() {

    }

    /**
     * Called by the ADOActions after the panel has
     * initialized (and clear has been called), before the NewDataDialog
     * is created
     */
    public void afterInitializedOnCreation() {

    }

    /**
     * Called by the ADOActions after the panel has
     * initialized (and clear has been called), before the NewDataDialog
     * is created
     */
    public void afterInitializedOnUpdate() {

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setSize(800, 600);
        GenericDataPanel contentPane = new GenericDataPanel(true) {

            @Override
            protected JComponent getChildComponents() {

                JPanel p = new JPanel(new BorderLayout());
                p.setBorder(new LineBorder(Color.BLACK, 2));

                p.add(new JLabel("Faren"), BorderLayout.CENTER); //$NON-NLS-1$

                return p;
            }

            @Override
            protected Object createNewObject() {

                return null;
            }

            @Override
            protected void objectToFields(Object object) {

            }

            @Override
            protected void fieldsToObject(Object object) {

            }

        };
        contentPane.showErrors(Lists.newArrayList("Erro 1")); //$NON-NLS-1$
        f.setContentPane(contentPane);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
