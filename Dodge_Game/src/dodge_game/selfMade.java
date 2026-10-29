package dodge_game;

import java.awt.Color;
import java.awt.Dimension;
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

public class selfMade extends JPanel implements ActionListener, KeyListener{

    static final int width = 600;
    static final int length = 600;
    
    int playerXPosition = width/2;
    int playerYPosition = length - 100;
    
    //declare image
    Image fallingHeart;
    
    int playerWidth = 40;
    int playerHeight = 40;
    
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
        this.setBackground(Color.black);
        setFocusable(true);
        this.addKeyListener(this);
        random = new Random();
        blocks = new ArrayList<>();
        timer = new Timer(20, this);
        
        //image loader
        try {
            // Using the exact path you provided
            fallingHeart = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\fallling_hearts.png"));
        } catch (Exception e) {
            System.out.println("Image not found! Check the path.");
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
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(playerXPosition, playerYPosition, playerWidth, playerHeight);
    
        //block loader
        for(Rectangle block : blocks){
            g.drawImage(fallingHeart, block.x, block.y, block.width, block.height, this);            
        }    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running){
            if(movingLeft && playerXPosition > 0){
                playerXPosition -= playerSpeed;
            }
            if(movingRight && playerXPosition < width - playerWidth){
                playerXPosition += playerSpeed;
            }
            tickCount++;
            if(tickCount % 25 == 0){
                int randomX = random.nextInt(width - blockSize);
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
