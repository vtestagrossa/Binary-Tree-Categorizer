/*
 * Vincent Testagrossa
 * Project 3: Binary Tree
 * 26JUN2022
 * 
 * Requirements: InvalidTreeSyntax
 * 
 * Public Methods:
 * 
 * count(): int - Returns a node count for the tree.
 * height(): int - Returns the max level of the tree, starting with 0 as the first level.
 * isBalanced(): Boolean - Returns true if the difference between each subtree's leaves is no more than 1 level.
 * isProper(): Boolean - Returns true if each node in the tree has either 0 or 2 children.
 * isFull(): Boolean - Returns true if the max number of nodes exists for the height of the tree.
 * inOrder(): String - Returns a parenthesized inorder formatted string of the tree. 
 * 
 * A binary tree with a generic, static inner node class. Constructor takes a parenthesized preorder string
 * (ex: (A(2(3)(4))(B(C)(D)))) ) and builds a binary tree from that input, or throws InvalidTreeSyntax if the
 * string is too short, or does not properly encase the nodes in parentheses. The constructor uses a helper method
 * to find the corresponding closing parenthesis for each of the opening parentheses and returns an index for use in
 * the recursive approach in the constructor, so that root.left and root.right can be passed to the constructor and
 * ultimately return each subtree to its own root. Each public method calls a recursive private method to accomplish
 * its task. 
 */

package Project3;

import java.util.Stack;

public class BinaryTree {
    static Node<String> root = null;
    public BinaryTree(String input) throws InvalidTreeSyntax{
        /*
         * Required:
         * Parenthesized string in prefix format.
         * 
         * Takes a string and builds a Binary Tree out of it. If the format
         * is incorrect, throws InvalidTreeSyntax.
         */
        try{
            if (input.length() <= 2){
                String message = "Invalid Tree Syntax: Input supplied was too short.";
                throw new InvalidTreeSyntax(message);
            }
            //Format is incorrect because the string doesn't begin with '(', end with ')', or it's empty.
            else if (findIndex(input, 0, input.length() - 1) == -2){
                String message = "Invalid Tree Syntax: Missing closing or extra open parenthesis.";
                throw new InvalidTreeSyntax(message);
            }
            else if (findIndex(input, 0, input.length() - 1) != input.length() -1){
                String message = "Invalid Tree Syntax: Missing open or extra closing parenthesis.";
                throw new InvalidTreeSyntax(message);
            }
            //format passes the first check
            else{             
                //strip leading and trailing parentheses and build the binary tree   
                root = stringToTree(input, 1, input.length() - 2);
            }
        }
        //another format test has failed, such as a missing or extra open/close paren, or an invalid character.
        catch (InvalidTreeSyntax ex){
            throw new InvalidTreeSyntax(ex.getMessage());
        }
        catch (StringIndexOutOfBoundsException ex){            
            String message = "Invalid Tree Syntax: Input string was empty";
            throw new InvalidTreeSyntax(message);
        }


    }
    private int findIndex(String input, int si, int ei){
        /*
         * This method was adapted from https://tutorialspoint.dev/data-structure/binary-tree-data-structure/construct-binary-tree-string-bracket-representation
         * as a helper method for stringToTree to find the location of the next ')' for either left or right branches to call stringToTree recursively with
         * the correct start and end indexes.
         * 
         * 
         */

        //Start index is greater than end index. 
        if (si > ei){
            return -1;
        }
        if (input.charAt(ei) != ')'){
            return -2;
        }
        //stack to hold the parentheses
        Stack<String> brackStack = new Stack<String>();

        //loop through each character of the string, starting with si and ending with ei.
        for (int i = si; i <= ei; i++){
            //if the charAt i is '(', push it
            if (input.charAt(i) == '('){
                brackStack.push(String.valueOf(input.charAt(i)));
            }
            //if the charAt i is ')'
            else if (input.charAt(i) == ')'){
                if (brackStack.peek().equals("(")){
                    brackStack.pop();

                    //if the stack is empty, return the index.
                    if (brackStack.empty()){
                        return i;
                    }
                }
            }
        }
        //it wasn't found. return -2
        return -2;
    }
    private Node<String> stringToTree(String input, int si, int ei) throws InvalidTreeSyntax{
        /* 
         * stringToTree helper method:
         * 
         * Requirements: String input, int si (Start index), int ei (end index).
         * 
         * Recursive method for converting a parethesized preorder string to a binary tree. Uses the helper method 
         * findIndex to determine when to call stringToTree recursively for left and right subtrees.
         * 
         * It's required to strip of trailing and leading parentheses to use this method because the tree's root
         * needs to be created before the left and right subtrees can be built recursively. Makes for a useful
         * format test in the constructor; check for first item in the string being a '(' and the last being
         * a ')' before passing the string to this method. If both aren't true, throw InvalidTreeSyntax.
         * 
         * This method was adapted from https://tutorialspoint.dev/data-structure/binary-tree-data-structure/construct-binary-tree-string-bracket-representation.
         * I had originally intended to use an iterative method because I was struggling with some of the recursion without using higher scope variables to keep
         * track of the index. On looking for techniques to process the initial parentheses, I found a lot of different versions of the code that didn't include
         * the ones surrounding the root node. 
         */
        if (si > ei){
            return null;
        }

        int index = -1;
        String operands = "^[a-zA-Z0-9]*$"; //Valid node "operands" regex.
        Node<String> root = null;
        //Test for bad values first and throw an exception.
        if(!String.valueOf(input.charAt(si)).matches(operands)){ //Checks for a valid operand to build a new root for the tree/subtree
            //An invalid character was found, so throw InvalidTreeSyntax.
            String message = "Invalid Tree Syntax: Ensure all parentheses are closed, and only alphanumeric characters\n" +
                            "are used as nodes in the tree.";
            throw new InvalidTreeSyntax(message);
        }
        root = new Node<String>(String.valueOf(input.charAt(si)));
        //if the next character is '(', find the index of the next ')'
        if (si + 1 <= ei && input.charAt(si + 1) == '('){
            index = findIndex(input, si + 1, ei);
        }
        //if the next character is an operand, there's an extra operand
        else if (si + 1 <= ei && String.valueOf(input.charAt(si + 1)).matches(operands)){
            String message = "Invalid Tree Syntax: Only one alphanumeric character may be used for each node of the tree.";
            throw new InvalidTreeSyntax(message);
            
        }
        //index of the next ')' was found
        if (index > -1){
            /*
             * Index is going to be the index of the complementary closing parenthesis.
             * 
             * On the left subtree, the next operand is going to be 2 more than the current, 
             * if a closing paren was found, which will be the next start index, given it is 
             * an operand (hence the check). The end index will be 1 less than the previous
             * index because it ends at the previous complementary closing paren.
             * 
             * On the right, the next operand will be 2 more than the previous index, because the
             * next operand is going to be preceded by another '('. The end index will be the previous
             * end index.
             * 
             */
            
            //build the left subtree recursively starting 2 characters to the right of the left subtree's previous
            //valid operand. (Next two characters would be ')' '(', if it's not null).
            root.left = stringToTree(input, si + 2, index - 1);
            //build the right subtree recursively starting 2 characters to the right from the end of the left subtree's
            //last valid operand (next two characters would be ')' and '(').
            root.right = stringToTree(input, index + 2, ei - 1);

        }
        //A closing parenthesis was not found to match an open parenthesis. 
        else if (index == -2){
            String message = "Invalid Tree Syntax: Missing closing parentheses, or invalid format.";
            throw new InvalidTreeSyntax(message);
        }
        return root;
    }
    static class Node<T>{
        /*
         * Represents a single node of the Binary Tree. Has 2 links to reference
         * left and right child nodes, and a generic data parameter.
         */
        Node<T> left, right;
        T data;
        public Node(T data){
            left = null;
            right = null;
            this.data = data;
        }
    }
    private String recInorder(Node<String> root){
        /*
         * Recursive inorder string method:
         * Parameters: Node<String> root - The root of the node that is to be traversed.
         * 
         * Takes a Node<String> and traverses Inorder (left, root, node), surrounding the data from each
         * branch in parentheses recursively until an end node has been reached.
         */
        String rtnLeft = "", rtnRight = "", rtnRoot = root.data;
        if(root.left != null && root.right != null){
            rtnRoot = "(" + recInorder(root.left) + " " + root.data + " " +  recInorder(root.right) + ")";
        }
        //if there's only data on the left, this is needed for proper spacing and parenthesis encasing.
        if (root.left != null && root.right == null){
            rtnRoot = " " + root.data + " )";
            rtnLeft = "(" + recInorder(root.left);
        }
        //should be impossible since the tree is processed left to right and not able to have a right node without a left.
        //Remains for later implementations if needed.
        /* if (root.left == null && root.right != null){
            rtnRoot = "( " + root.data + " ";
            rtnRight = recInorder(root.right) + ")";
        } */
        //Reached a leaf.
        if (root.left == null && root.right == null){
            rtnRoot = "( " + root.data + " )";
        }
        return rtnLeft + rtnRoot + rtnRight;
    }
    private int recHeight(Node<String> root){
        /*
         * Recursive Height method:
         * Parameters: Node<String> root
         * 
         * Takes the root of the tree and does a depth first search of
         * each branch, adding one every time a branch isn't null, or 0
         * when it is null. Returns the max of the two branches + 1.
         */
        if(root == null || root.left == null && root.right == null)
        {
            return 0;
        }
        return 1 + Math.max(recHeight(root.left), recHeight(root.right));
    }
    private boolean recIsBalanced(Node<String> root){
        /*
         * Recursive isBalanced method:
         * Parameters: Node<String> root
         * 
         * Takes the height of each subtree and compares them. If the difference is more than 1 for any comparison, 
         * returns false. Because of the way recHeight handles null values for each node, the internal doubles storing
         * the values needed to be set to 0 for each null case. For non-null values, 1 was added to the height to 
         * compensate for the 0-based height check to avoid duplicating code with a depth check. Finally, the 
         * absolute value of the difference between the left and right is calculated, which is then checked as
         * diff <= 1. If that check is passed, then the recursive tests for the left and right subtrees are logically
         * ANDed as the return value to ensure each subtree is balanced as well. 
         * 
         */
        double leftHeight = 0, rightHeight = 0;
        if (root == null){
            return true;
        }
        if(root.left == null){
            leftHeight = 0;
        }
        else{            
            leftHeight = recHeight(root.left) + 1;
        }
        if (root.right == null){
            rightHeight = 0;
        }
        else{
            rightHeight = recHeight(root.right) + 1;
        }
        double diff = Math.abs(leftHeight - rightHeight);
        if (diff <= 1){
            return  recIsBalanced(root.left) && recIsBalanced(root.right);
        }
        return false;
    }
    private boolean recIsFull(Node<String> root){
        /*
         * Recursive isFull method:
         * Parameters: Node<String> root
         * 
         * Uses the recHeight method to recursively calculate the height of the binary tree. Because the method returns a 0-based
         * height, 1 is added to the result for the calculation for maximum count: max = 2^n - 1, where n is the number of levels in the
         * tree. If recCount == 2^n -1, then the binary tree is full.
         */
        if(Math.pow(2, recHeight(root) + 1) - 1 == recCount(root)){
            return true;
        }
        return false;
    }
    private boolean recIsProper(Node<String> root){
        /*
         * Recursive isProper method:
         * Parameters: Node<String> root
         * 
         * Recursively checks the left and right subtrees if the children are either both null or neither are null.
         * Logically ANDs the results together to return the value.
         */
        if(root == null){
            return true;
        }
        if (root.left == null && root.right == null || root.left != null && root.right != null){
            return recIsProper(root.left) && recIsProper(root.right);
        }
        return false;
    }
    private int recCount(Node<String> root){
        /*
         * Recursive count method:
         * Parameters: Node<String> root
         * 
         * Mostly just a helper function to get the node count to compare with the height for the isFull() method.
         * Made available through a public getter as well.
         */
        if (root == null){
            return 0;
        }
        return 1 + recCount(root.left) + recCount(root.right);
    }
    public int count(){
        return recCount(root);
    }
    public int height(){
        return recHeight(root);
    }
    public boolean isBalanced(){
        return recIsBalanced(root);
    }
    public boolean isFull(){
        return recIsFull(root);
    }
    public boolean isProper(){
        return recIsProper(root);
    }
    public String inOrder(){
        return recInorder(root);
    }
}
