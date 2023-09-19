/*
 * Vincent Testagrossa
 * Project 3: Binary Tree
 * 26JUN2022
 * 
 * Requirements: Dependent on ProgramWindow, BinaryTree and InvalidTreeSyntax.
 * 
 * Builds a ProjectWindow GUI consisting of 3 frames, arranged in a vertical box layout. The top frame has the
 * input label and the input text field arranged in a flowlayout. The center frame has each of the buttons arranged
 * in a flow layout. The bottom frame has the output label and text field. ProjectWindow has ActionEvent methods that
 * detect button presses for each of the buttons. A static BinaryTree is declared in the main class, which constructed
 * by the makeTreeBtn, which catches InvalidTreeSyntax exceptions. Each other button calls the public methods from the 
 * BinaryTree class after performing a check that the tree exists (!= null) and the result is output to the output text
 * area if there are no errors. Otherwise, an error message is shown in a JOptionPane.
 */
package Project3;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;


public class Main {
        static BinaryTree tree;
    public static void main(String[] args) {
        ProgramWindow guiWindow = new ProgramWindow();
        guiWindow.toFront();
       /* Used for debugging/testing
            String input = "(A(B(1)(2))(C(1(2))))";
            try{
                BinaryTree tree = new BinaryTree(input);
                System.out.println(tree.inOrder());
                System.out.println("Height: " + tree.height());
                System.out.println("Balanced: " + tree.isBalanced());
                System.out.println("Proper: " + tree.isProper());
                System.out.println("Full: " + tree.isFull());
                System.out.println("Count: " + tree.count());
            }
            catch (InvalidTreeSyntax ex){
                System.out.println(ex.getMessage());
            } 
        */
    }
    private static class ProgramWindow extends JFrame implements ActionListener {
        private JPanel topPanel, centerPanel, bottomPanel;
        private JButton makeTreeBtn, isBalancedBtn, isFullBtn, isProperBtn, heightBtn, nodesBtn, inorderBtn;
        private JLabel inputLabel, outputLabel;
        private JTextField inputTextField, outputTextField;
        private BoxLayout layout;
    
    
        ProgramWindow(){
            //instantiate the Boxlayout and set the axis to Y. Instantiate the panels to be added to the frame using the layout.
            layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
            topPanel = new JPanel();
            centerPanel = new JPanel();
            bottomPanel = new JPanel();
    
            //set the close operation to exit, set the title and set the layout for the frame.
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("Binary Tree Categorizer");
            this.setLayout(layout);

            //Create the Label and JTextField for the input Tree
            inputLabel = new JLabel("Enter Tree:");
            inputTextField = new JTextField(30);
            
            //set the top panel layout to be a flowLayout, then add the top panel to the frame and add the
            //input label and textfield to the panel
            topPanel.setLayout(new FlowLayout());
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            this.add(topPanel);
            topPanel.add(inputLabel);
            topPanel.add(inputTextField);
    
            //create the buttons
            makeTreeBtn = new JButton("Make Tree");
            makeTreeBtn.addActionListener(this);
            isBalancedBtn = new JButton("Is Balanced?");
            isBalancedBtn.addActionListener(this);
            isFullBtn = new JButton("Is Full?");
            isFullBtn.addActionListener(this);
            isProperBtn = new JButton("Is Proper?");
            isProperBtn.addActionListener(this);
            heightBtn = new JButton("Height");
            heightBtn.addActionListener(this);
            nodesBtn = new JButton("Nodes");
            nodesBtn.addActionListener(this);
            inorderBtn = new JButton("Inorder");
            inorderBtn.addActionListener(this);
            
            //set the center panel layout to be a flowlayout, then add the buttons
            centerPanel.setLayout(new FlowLayout());
            centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            this.add(centerPanel);
            centerPanel.add(makeTreeBtn);
            centerPanel.add(isBalancedBtn);
            centerPanel.add(isFullBtn);
            centerPanel.add(isProperBtn);
            centerPanel.add(heightBtn);
            centerPanel.add(nodesBtn);
            centerPanel.add(inorderBtn);

            //Create the Label and JTextField for the output
            outputLabel = new JLabel("Output:");
            outputTextField = new JTextField(30);
            outputTextField.setEditable(false);
            
            //set the bottom panel layout to be a flowlayout, then add the bottom panel to the frame
            //and add the output label and textfield to it.
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            this.add(bottomPanel);
            bottomPanel.add(outputLabel);
            bottomPanel.add(outputTextField);
    
            //Pack the frame to fit to the width of the panels/components. Disable resizing, and set the frame to visible.
            this.pack();
            this.setResizable(false);
            this.setVisible(true);
        }
        @Override 
        public void actionPerformed(ActionEvent e){
            /* 
             * Checks if there is a binary tree before using any function other than makeTree. If there isn't,
             * a JOptionPane window is raised using checkTree and the error. If checkTree returns true, then the 
             * function is allowed to continue. If makeTree is called, the InvalidTreeSyntax may be thrown and a 
             * JOptionPane is opened displaying the specific error. Otherwise, builds a tree from the supplied input.
            */
            if (e.getSource() == makeTreeBtn){
                try{
                    String input = inputTextField.getText(); //parse the input
                    tree = new BinaryTree(input); //build the tree
                    outputTextField.setText(input); //display the tree in the outputfield.
                }
                //InvalidTreeSyntax was caught.
                catch (InvalidTreeSyntax ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Syntax", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getSource() == isBalancedBtn){
                if (checkTree()){
                    //Tree exists.
                    String message = "";
                    if (tree.isBalanced()){
                        //Tree is balanced.
                        message = "The tree is balanced.";
                    }
                    else{
                        //Tree isn't balanced.
                        message = "The tree is not balanced.";
                    }
                    outputTextField.setText(message);
                }
            }
            else if (e.getSource() == isFullBtn){
                if (checkTree()){
                    //Tree exists
                    String message = "";
                    if (tree.isFull()){
                        //Tree is full.
                        message = "The tree is full.";
                    }
                    else{
                        //Tree is not full.
                        message = "The tree is not full.";
                    }
                    outputTextField.setText(message);
                }
            }
            else if (e.getSource() == isProperBtn){
                if (checkTree()){
                    //Tree exists.
                    String message = "";
                    if (tree.isProper()){
                        //Tree is proper.
                        message = "The tree is proper.";
                    }
                    else{
                        //Tree is not proper
                        message = "The tree is not proper.";
                    }
                    outputTextField.setText(message);
                }
            }
            else if (e.getSource() == heightBtn){
                if (checkTree()){
                    //The tree exists, so set the message to display the height, and then show it in a JOptionPane.
                    String message = "The height of the tree is " + tree.height() + ".";
                    outputTextField.setText(message);
                }
            }
            else if (e.getSource() == nodesBtn){
                if (checkTree()){
                    //The tree exists, so set the message to display the count, and then show it in a JOptionPane.
                    String message = "The tree has " + tree.count() + " nodes.";
                    outputTextField.setText(message);
                }
            }
            else if (e.getSource() == inorderBtn){
                if (checkTree()){
                    //The tree exists, so set the outputfield to show the parenthesized inorder format.
                    outputTextField.setText(tree.inOrder());
                }
            }
        }
        private boolean checkTree(){
            /*
             * Helper method to check if the tree exists. If it doesn't, launches a JOptionPane
             * dialog telling the user to enter a tree.
             */
            if (tree == null){
                String message = "You must input a tree and click 'Make Tree' before you can use the other functions.";
                JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
    }
}
