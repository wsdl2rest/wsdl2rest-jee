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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
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

public class Wsdl2RestForm extends JPanel
                      implements TreeSelectionListener, ActionListener {

    private JTree serviceMethods;
    private JTable methodDetails;

    protected List<ClassDefinition> svcClasses;
    protected ClassDefinition clazzDef;
    private static String lineStyle = "Horizontal";
    protected JTextField fileName;
    private JFileChooser fc;
    private JButton openButton;


    public Wsdl2RestForm(List<ClassDefinition> svcClassesDefs) {

        super(new GridLayout(1,0));
        svcClasses = svcClassesDefs;
        
        fileName = new JTextField(20);
        fileName.addActionListener(this);
        JLabel textFieldLabel = new JLabel("WSDL File Name: ");
        textFieldLabel.setLabelFor(fileName);

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(textFieldLabel, c);
        //c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = GridBagConstraints.RELATIVE;    
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(fileName, c);

        //Create a file chooser
        fc = new JFileChooser();
        openButton = new JButton("Browse");
        openButton.addActionListener(this);

        c.gridwidth = GridBagConstraints.RELATIVE;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2.0;
        textControlsPane.add(openButton, c);

        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("WSDL"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));

        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Webservice Methods");
       //generateServiceTree(top);
        //Create a tree that allows one selection at a time.
        serviceMethods = new JTree(top);
        serviceMethods.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        serviceMethods.addTreeSelectionListener(this);
        //TODO
        serviceMethods.putClientProperty("JTree.lineStyle", lineStyle);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(serviceMethods);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        splitPane.setTopComponent(textControlsPane);
        splitPane.setBottomComponent(treeView);

        Dimension minimumSize = new Dimension(500, 300);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); //XXX: ignored in some releases
                                           //of Swing. bug 4101306
        //workaround for bug 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100));

        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);
    }

    public static void createAndShowGUI(List<ClassDefinition> svcClassesDefs) {
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
        Wsdl2RestForm newContentPane = new Wsdl2RestForm(svcClassesDefs);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void generateServiceTree(DefaultMutableTreeNode top) {
        for(ClassDefinition classDef : svcClasses){
            this.clazzDef = classDef;
            String packageName = clazzDef.getPackageName();

            if(clazzDef.getClassName() != null){

                DefaultMutableTreeNode svcClass = new DefaultMutableTreeNode(packageName + "."+clazzDef.getClassName());
                top.add(svcClass);
                
                writeMethods(clazzDef.getMethods(), svcClass);
            }
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods, DefaultMutableTreeNode svcClass){
        if(methods == null) return;
        for(MethodInfo mInf:methods){
            DefaultMutableTreeNode method = new DefaultMutableTreeNode(mInf.getMethodName());
            svcClass.add(method);

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
                String comma = (++i != size)?", ":"";
//                writer.print(p.getParamType()+" "+p.getParamName()+comma);
            }
        }
    }

    public void valueChanged(TreeSelectionEvent e) {

    }


    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                fileName.setText(file.getAbsolutePath());
            }
        //Handle save button action.
        //} else if (e.getSource() == ) {

        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Wsdl2RestForm.createAndShowGUI(null);
            }
        });

    }
}
