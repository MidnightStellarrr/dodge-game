package dodge_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
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
    int blockSize = 40;
    
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //e contains the data about what just happened on the keyboard
        //.getKeyCode() a method that retrieve the numerical ID of the key
        //every key on your keyboard has a unique number assigned to it
        int key = e.getKeyCode();
        
        //KeyEvent is a class in Java 
        //VK_LEFT and VK_A is a varible in KeyEvent class that has the same value as key
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = true;
        }
        
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = true;
        }
        
        if (!running && key == KeyEvent.VK_ENTER) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = false;
        }
        
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            //Move Player to the left
            if (movingLeft && playerX > 0) {
                playerX -= playerSpeed;
            }
            
            //Move Player to the right
            if (movingRight && playerX < WIDTH - playerWidth) {
                playerX += playerSpeed;
            }
            
            tickCount++;
            //if there's no remainder that is where the blocks will fall
            if (tickCount % 25 == 0) {
                int randomX = random.nextInt(WIDTH - blockSize);
                //randomX the position of the new block
                //0 the y position which is at the very top
                //1st blockSize is the width, the 2nd is the height
                //the creation of the block and placed it to arraylist
                blocks.add(new Rectangle(randomX, 0, blockSize, blockSize));
            }
            
            //a replica of the player to check if it hits the falling blocks
            Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
            
            for (int i = 0; i < blocks.size(); i++) {
                //gets the block from the arraylist
                Rectangle block = blocks.get(i);
                //moves the falling blocks in 5 pixels
                //the .y is prebuilt in rectangle class
                block.y += 5;

                //.intersects checks if two rectangles are overlapping
                if (block.intersects(playerRect)) {
                    //game over
                    running = false; 
                }

                //the i goes from 0 to 1 then back to 0
                //this creates the loop of falling blocks
                if (block.y > HEIGHT) {
                    blocks.remove(i);
                    i--; 
                    score += 10; 
                }
            }
        }
        //the bridge between your game logic and your drawing code.
        //connects actionPerformed and paintComponent
        repaint();
    }
    
    //This is where the game actually draws everything on the screen.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (running) {
            // Draw Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 20, 30);

            // Draw Player (Blue rectangle)
            g.setColor(Color.CYAN);
            g.fillRect(playerX, playerY, playerWidth, playerHeight);

            // Draw Blocks (Red rectangles)
            g.setColor(Color.RED);
            for (Rectangle block : blocks) {
                g.fillRect(block.x, block.y, block.width, block.height);
            }
        } else {
            // Draw Game Over Screen
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 150, 280);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Final Score: " + score, 200, 330);
            
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press ENTER to restart", 190, 400);
        }
    }
    
    public void restartGame(){
        playerX = WIDTH / 2;
        blocks.clear();
        score = 0;
        tickCount = 0;
        running = true;
        timer.start(); 
    }
        
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dodge the Blocks");
        dodge_gamep2 game = new dodge_gamep2();
        //the panel
        frame.add(game);
        //sizes the window to perfectly fit the panel inside it.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        //the line that actually gives the keyboard focus to your game
        game.requestFocusInWindow(); 
    }
}
