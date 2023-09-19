/* 
 * Vincent Testagrossa
 * Project 3: Binary Tree
 * 26JUN2022
 * 
 * Handled exception that is thrown when the format provided to BinaryTree does not match a 
 * parenthesized inorder format.
 * 
 */
package Project3;

public class InvalidTreeSyntax extends Exception {
    public InvalidTreeSyntax(String message){
        super(message);
    }
}
