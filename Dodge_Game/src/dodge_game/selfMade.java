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
//image imports
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
//blur game over img
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
//game over blur effect
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class selfMade extends JPanel implements ActionListener, KeyListener{

    static final int width = 600;
    static final int length = 600;
    
    int playerXPosition = width/2;
    int playerYPosition = length - 130;
    
    //declare falling heart image
    Image fallingHeart;
    //declare player image
    Image playerImage;
    //declare bg image
    Image gameBackground;
    
    int playerWidth = 45;
    int playerHeight = 65;
    
    Timer timer;
    boolean movingLeft = false;
    boolean movingRight = false;
    int playerSpeed = 7;
    
    ArrayList<Rectangle> blocks;
    Random random;
    int blockSize = 40;
    int tickCount = 0;
    
    int score = 0;
    
    boolean running = true;
    
    selfMade(){
        this.setPreferredSize(new Dimension(width,length));
        setFocusable(true);
        this.addKeyListener(this);
        random = new Random();
        blocks = new ArrayList<>();
        timer = new Timer(20, this);
        
        //image loader for falling hearts
        try {
            // Using the exact path you provided
            fallingHeart = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\fallling_hearts.png"));
        } catch (Exception e) {
            System.out.println("Image not found! Check the path.");
        }
        
        //image loader for player
        try {
            playerImage = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\player.png"));
        } catch (Exception e) {
            System.out.println("Player image not found! Check the path.");
        }
        
        //image loader for bg
        try {
            gameBackground = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\game_bg.jpg"));
        } catch (Exception e) {
            System.out.println("Background image not found! Check the path.");
        }
        
        timer.start();
    }
    
    public void restartGame(){
        if(!running){
            playerXPosition = width/2;
            blocks.clear();
            score = 0;
            tickCount = 0;
            running = true;
            timer.start();
        }
    }
    
    public Image blurImage(BufferedImage image) {
        // 1. Create a 3x3 blur matrix (this is the math that does the blurring)
        float weight = 1.0f / 9.0f;
        float[] matrix = {
            weight, weight, weight,
            weight, weight, weight,
            weight, weight, weight
        };

        // 2. Create the filter
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel);

        // 3. Apply the blur and return the new image
        BufferedImage blurredImage = new BufferedImage(
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );

        return op.filter(image, blurredImage);
    }
     
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // 1. Draw Background
        g.drawImage(gameBackground, 0, 0, width, length, this);

        // 2. Draw Falling Hearts
        for(Rectangle block : blocks){
            g.drawImage(fallingHeart, block.x, block.y, block.width, block.height, this);            
        }  

        // 3. Draw Player
        g.drawImage(playerImage, playerXPosition, playerYPosition, playerWidth, playerHeight, this);

        // 4. Draw Score
        g.setColor(Color.LIGHT_GRAY); 
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 450, 80);

        // 5. If the game is over, apply the blur and draw the text
        if (!running) {

            // Create a BufferedImage to capture the current screen
            BufferedImage screenshot = new BufferedImage(width, length, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = screenshot.createGraphics();

            // Draw the current screen onto the screenshot
            g2d.drawImage(gameBackground, 0, 0, null);
            for(Rectangle block : blocks){
                g2d.drawImage(fallingHeart, block.x, block.y, block.width, block.height, null);            
            }
            g2d.drawImage(playerImage, playerXPosition, playerYPosition, playerWidth, playerHeight, null);
            g2d.dispose();

            // Blur the screenshot
            Image blurredScreen = blurImage(screenshot);

            // Draw the blurred screenshot OVER the sharp screen
            g.drawImage(blurredScreen, 0, 0, width, length, this);

            // Draw Game Over Text on top of the blur
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running){
            if(movingLeft && playerXPosition > 35){
                playerXPosition -= playerSpeed;
            }
            if(movingRight && playerXPosition < width - playerWidth - 35){
                playerXPosition += playerSpeed;
            }
            tickCount++;
            if(tickCount % 25 == 0){
                int minX = 35;
                int maxX = width - blockSize - 35;
                int randomX = minX + random.nextInt(maxX - minX);
                blocks.add(new Rectangle(randomX, 0, blockSize, blockSize));
            }
            
            Rectangle playerCollision = new Rectangle(playerXPosition, playerYPosition, playerWidth, playerHeight);

            for(int i = 0; i < blocks.size(); i++){
                Rectangle getBlock = blocks.get(i);
                getBlock.y += 5;

                if(getBlock.intersects(playerCollision)){
                    running = false;
                }
                
                if(getBlock.y > length){
                    blocks.remove(i);
                    i--;
                    score += 10;
                }

            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
            movingLeft = true;
        }
        
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            movingRight = true;
        }
        
        if(!running && key == KeyEvent.VK_ENTER){
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
            movingLeft = false;
        }
        
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            movingRight = false;
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        selfMade window = new selfMade();
        frame.add(window);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        window.requestFocusInWindow();
    }
    
}
