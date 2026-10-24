package dodge_game;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

public class dodge_gamep2 {
    //set the variables
    static final int WIDTH  = 600;   // The window is 600 wide
    static final int HEIGHT = 600;   // The window is 600 tall
    int playerX = WIDTH / 2;         // Player starts in the middle (left-right)
    int playerY = HEIGHT - 100;      // Player starts near the bottom (top-bottom)
    
    // Player is 40x40
    int playerWidth = 40;            
    int playerHeight = 40;   
    
    //storage for the falling blocks
    //this storage is flexible. can shrink down to 0 index and expand with no limit
    ArrayList<Rectangle> blocks; 
    
    //makes the blocks fall randomly
    //declare and initiate a variable
    Random random = new Random();
    
    //movement rules
    boolean movingLeft = false;    // Is the Left key in the keyboard being held?
    boolean movingRight = false;   // Is the Right key  in the keyboard being held?
    
    // How fast the player moves in pixels
    // a single move is equivalent to 7 pixels
    int playerSpeed = 7; 
    
    //keeps the player and blocks moving from one place to another
    //movement of blocks are continuous
    //movemenent of the player are dependent of pressed key 
    Timer timer;
    
    int score = 0; //the score
    
    //record each 20ms that has passed
    //tickCount record every 25 frames
    //a single 20 ms is equivalent to 1 frame
    //blocks falls every 25 frames
    //listening to player movements is 20ms meaning every single frame
    int tickCount = 0; 
    
    //this means that the game will keep on running until false meaning game over
    boolean running = true;
    
}
