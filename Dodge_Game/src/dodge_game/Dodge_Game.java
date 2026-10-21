package Dodge_Game;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Dodge_Game extends JPanel implements ActionListener, KeyListener {

    // Window dimensions
    static final int WIDTH  = 600;
    static final int HEIGHT = 600;

    // The Player
    int playerX = WIDTH / 2; // the default place of the player in width
    int playerY = HEIGHT - 100; // the default place of the player in height
    int playerWidth = 40;
    int playerHeight = 40;
    
    // Player movement (Left/Right)
    boolean movingLeft = false;
    boolean movingRight = false;
    int playerSpeed = 7;

    // Obstacles
    ArrayList<Rectangle> blocks;
    Random random;
    int blockSize = 40;
    
    // Timer & Game state
    Timer timer;
    int score = 0;
    int tickCount = 0;
    boolean running = true;

    public Dodge_Game() {
        random = new Random();
        blocks = new ArrayList<>();
        
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true); // pay attention to the keyboard
        this.addKeyListener(this); // receives the keyboard input and sends it to your code
        
        timer = new Timer(20, this); //20ms is equivalent to a single frame
        timer.start();
    }

    // This runs 50 times a second
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            // 1. Move Player
            if (movingLeft && playerX > 0) {
                playerX -= playerSpeed;
            }
            if (movingRight && playerX < WIDTH - playerWidth) {
                playerX += playerSpeed;
            }

            // 2. Spawn Blocks (Every 25 ticks, spawn a new block)
            tickCount++;
            if (tickCount % 25 == 0) {
                int randomX = random.nextInt(WIDTH - blockSize);
                blocks.add(new Rectangle(randomX, 0, blockSize, blockSize));
            }

            // the player in positiona and size initialized in one
            Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
            
            for (int i = 0; i < blocks.size(); i++) {
                Rectangle block = blocks.get(i);
                block.y += 5; // Block falls down at 5 pixels per tick

                // Collision check!
                if (block.intersects(playerRect)) {
                    running = false; // Game Over
                }

                // Remove block if it goes off screen
                if (block.y > HEIGHT) {
                    blocks.remove(i);
                    i--; // Step back since the list shrank
                    score += 10; // Survived a block!
                }
            }
        }
        repaint(); // Redraw screen
    }

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

    // --- Keyboard Handling ---
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = true;
        }
        
        // Restart game if dead and press Enter
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

    @Override public void keyTyped(KeyEvent e) {}

    // --- Restart Logic ---
    public void restartGame() {
        playerX = WIDTH / 2;
        blocks.clear();
        score = 0;
        tickCount = 0;
        running = true;
        timer.start();
    }

    // --- Main Method (Run this to play!) ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dodge the Blocks");
        Dodge_Game game = new Dodge_Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        game.requestFocusInWindow();
    }
}
