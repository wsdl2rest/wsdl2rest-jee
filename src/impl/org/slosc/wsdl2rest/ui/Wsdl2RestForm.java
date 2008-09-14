package org.slosc.wsdl2rest.ui;

/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.slosc.wsdl2rest.service.ClassDefinition;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.service.MethodInfo;
import org.slosc.wsdl2rest.service.Param;
import org.slosc.wsdl2rest.util.WSDLFileFilter;
import org.slosc.wsdl2rest.Wsdl2Rest;

public class Wsdl2RestForm extends JPanel
                      implements TreeSelectionListener, ActionListener {

    private JTree serviceMethods;
    private JTable methodDetails;

    protected List<ClassDefinition> svcClasses = null;
    protected ClassDefinition clazzDef = null;
    private static String lineStyle = "Horizontal";
    protected JTextField fileName;
    private JFileChooser fc;
    private JButton openButton;
    private Wsdl2Rest wsdl2rest;
    private DefaultMutableTreeNode topServiceTree;

    private JComboBox resources;
	private JComboBox httpMethod;
	private JComboBox mimeType;


    public Wsdl2RestForm() {

        super(new GridLayout(1,0));
        
        fileName = new JTextField(20);
        fileName.addActionListener(this);
        JLabel textFieldLabel = new JLabel("WSDL File Name: ");
        textFieldLabel.setLabelFor(fileName);

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

//        c.anchor = GridBagConstraints.EAST;
//        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
//        c.fill = GridBagConstraints.NONE;      //reset to default
//        c.weightx = 0.0;                       //reset to default
        c.gridx = 0;
        c.gridy = 0;

        textControlsPane.add(textFieldLabel, c);
//        //c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        textControlsPane.add(fileName, c);

        //Create a file chooser
        fc = new JFileChooser();
        FileFilter filter = new WSDLFileFilter();
        fc.setFileFilter(filter);
        openButton = new JButton("Browse");
        openButton.addActionListener(this);

        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.anchor = GridBagConstraints.WEST;
//        c.weightx = 2.0;
        c.gridx = 2;
        c.gridy = 0;
        textControlsPane.add(openButton, c);

        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("WSDL"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));

        topServiceTree =  new DefaultMutableTreeNode("Webservice Methods");
        //Create a tree that allows one selection at a time.
        serviceMethods = new JTree(topServiceTree);
        serviceMethods.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        serviceMethods.addTreeSelectionListener(this);
        //TODO
        serviceMethods.putClientProperty("JTree.lineStyle", lineStyle);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(serviceMethods);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setLeftComponent(treeView);
        mainSplitPane.setRightComponent(addEditPane());
        mainSplitPane.setDividerLocation(300);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        splitPane.setTopComponent(textControlsPane);
        splitPane.setBottomComponent(mainSplitPane);

        Dimension minimumSize = new Dimension(300, 300);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); //XXX: ignored in some releases
                                           //of Swing. bug 4101306
        //workaround for bug 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100));

        splitPane.setPreferredSize(new Dimension(700, 400));

        //Add the split pane to this panel.
        add(splitPane);
    }

    private Component addEditPane() {
        JPanel mainEditPane = new JPanel(new GridLayout(3,2));
        JPanel editPane = new JPanel(new GridLayout(3,2));
        JLabel lbl1 = new JLabel("Resources: ");
        lbl1.setLabelFor(resources);
        JLabel lbl2 = new JLabel("HTTP Method: ");
        lbl2.setLabelFor(httpMethod);
        JLabel lbl3 = new JLabel("MIME type: ");
        lbl3.setLabelFor(mimeType);
        resources = new JComboBox();
        httpMethod = new JComboBox();
        mimeType = new JComboBox();

        editPane.add(lbl1);
        editPane.add(resources);
        editPane.add(lbl2);
        editPane.add(httpMethod);
        editPane.add(lbl3);
        editPane.add(mimeType);
//        editPane.setMaximumSize(new Dimension(400, 100));
//        editPane.setPreferredSize(new Dimension(400, 100));
        editPane.setBorder(new TitledBorder("REST definitions"));
        mainEditPane.add(editPane);
        return mainEditPane;
    }

    public static void createAndShowGUI() {
//        try {
//            UIManager.setLookAndFeel(
//                UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.err.println("Couldn't use system look and feel.");
//        }

        //Create and set up the window.
        JFrame frame = new JFrame("Wsdl2Rest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Wsdl2RestForm newContentPane = new Wsdl2RestForm();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void generateServiceTree() {
       if(fileName.getText() == null && fileName.getText().length() == 0) return;
        
       wsdl2rest = new Wsdl2Rest();
       wsdl2rest.process(fileName.getText(), "", "");
        svcClasses = wsdl2rest.getSvcClasses();

       for(ClassDefinition classDef : svcClasses){
            this.clazzDef = classDef;
            String packageName = clazzDef.getPackageName();

            if(clazzDef.getClassName() != null){

                DefaultMutableTreeNode svcClass = new DefaultMutableTreeNode(packageName + "."+clazzDef.getClassName());
                topServiceTree.add(svcClass);
                //serviceMethods.scrollPathToVisible(new TreePath(svcClass.getPath()));
                
                writeMethods(clazzDef.getMethods(), svcClass);
            }
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods, DefaultMutableTreeNode svcClass){
        if(methods == null) return;
        boolean visible = false;
        for(MethodInfo mInf:methods){
            DefaultMutableTreeNode method = new DefaultMutableTreeNode(mInf.getMethodName());
            svcClass.add(method);
//            if(!visible){
//                visible = true;
//                serviceMethods.scrollPathToVisible(new TreePath(method.getParent()));
//            }
            
//            ImageIcon leafIcon = new ImageIcon("images/middle.gif");
//            if (leafIcon != null) {
//                DefaultTreeCellRenderer renderer =
//                new DefaultTreeCellRenderer();
//                renderer.setLeafIcon(leafIcon);
//                tree.setCellRenderer(renderer);
//            }

            

            String retType = mInf.getReturnType();
//            writer.print("\tpublic "+(retType!=null?retType:"void")+" ");
//            writer.print(mInf.getMethodName()+"(");
            writeParams(mInf.getParams());
            String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
//            writer.println(")"+excep+";");
        }
    }

    protected void writeMethod(MethodInfo mInf){
        if(mInf == null) return;
        String retType = mInf.getReturnType();
//        writer.print("\tpublic "+(retType!=null?retType:"void")+" ");
//        writer.print(mInf.getMethodName()+"(");
        writeParams(mInf.getParams());
        String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
//        writer.println(")"+excep+";\n");
    }

    protected void writeParams(List<Param> params){
        if(params != null) {
            int i=0; int size = params.size();
            for(Param p : params){
//                String comma = (++i != size)?", ":"";
//                writer.print(p.getParamType()+" "+p.getParamName()+comma);
            }
        }
    }

    public void valueChanged(TreeSelectionEvent e) {

    }

    class WSDLFileFilter  extends FileFilter {
        public WSDLFileFilter() {
        }

        public boolean accept(File pathname) {
            return pathname.isDirectory() || pathname.isFile() && pathname.getName().endsWith(".wsdl");
        }

        public String getDescription() {
            return "WSDL Files";
        }
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {

            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                fileName.setText(file.getAbsolutePath());
                generateServiceTree();
            } else{
                JOptionPane.showMessageDialog(this, "Please select a WSDL file..."); // ,"warning",  JOptionPane.WARNING_MESSAGE);
            }

            //else if (e.getSource() == ) {

        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
