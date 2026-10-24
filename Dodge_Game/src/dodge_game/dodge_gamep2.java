package dodge_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

//extends means inheriting from a parent class
//having access to all of its code and features
//implements means only specific methods in this class
public class dodge_gamep2 extends JPanel implements KeyListener, ActionListener{
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
    Random random;
    
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
    
    //first run by the system
    public dodge_gamep2(){
        //declarations
        random = new Random();
        blocks = new ArrayList<>();
        
        // Set window size
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        // Set color
        this.setBackground(Color.BLACK);
        
        //initiate that the system will have access to any keyboard actions
        //from JPanel
        this.setFocusable(true);
        
        //accepts any keyboard actions
        //from KeyListener
        //1st this is the thing that is actually connected to the physical keyboard.
        //2nd this is the Guard (the Listener)
        this.addKeyListener(this);
        
        // Build the clock
        //from ActionListener
        //When 20ms passes, send the tick to this object.
        timer = new Timer(20, this);
        
        // Start the clock
        timer.start();
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
