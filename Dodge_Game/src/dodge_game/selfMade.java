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
//bgm
import javazoom.jl.player.Player;
import java.io.FileInputStream;


public class selfMade extends JPanel implements ActionListener, KeyListener {

    static final int width = 600;
    static final int length = 600;

    int playerXPosition = width / 2;
    int playerYPosition = length - 130;

    //declare falling heart image
    Image fallingHeart;
    //declare player image
    Image playerImage;
    //declare bg image
    Image gameBackground;
    //declare game over screen image
    Image gameOverScreen;
    //declarescore bg image
    Image scoreImage;

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

    selfMade() {
        this.setPreferredSize(new Dimension(width, length));
        setFocusable(true);
        this.addKeyListener(this);
        random = new Random();
        blocks = new ArrayList<>();
        timer = new Timer(20, this);

        //image loader for falling hearts
        try {
            fallingHeart = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\fallling_hearts.png"));
        } catch (Exception e) {
            System.out.println("Heart image not found! Check the path.");
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
        
        //image loader for score bg
        try {
            scoreImage = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\score_bg.png"));
        } catch (Exception e) {
            System.out.println("Background image not found! Check the path.");
        }

        //image loader for game over screen
        try {
            gameOverScreen = ImageIO.read(new File("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\game_over_screen.png"));
        } catch (Exception e) {
            System.out.println("Game Over image not found! Check the path.");
        }
        
        // Add your exact path to the MP3 file here
        playMusic("C:\\Users\\Hannah\\Documents\\NetBeansProjects\\dodge-game\\Dodge_Game\\src\\dodge_game\\img\\bgm.mp3");

        timer.start();
    }
    
    public void playMusic(String filePath) {
        // We use a new Thread so the music doesn't freeze the game
        new Thread(() -> {
            try {
                while (true) {
                    FileInputStream fis = new FileInputStream(filePath);
                    Player player = new Player(fis);
                    player.play();
                }
            } catch (Exception e) {
                System.out.println("Music error: " + e.getMessage());
            }
        }).start();
    }

    public void restartGame() {
        if (!running) {
            playerXPosition = width / 2;
            blocks.clear();
            score = 0;
            tickCount = 0;
            running = true;
            timer.start();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {
            // 1. Draw Background
            g.drawImage(gameBackground, 0, 0, width, length, this);

            // 2. Draw Falling Hearts
            for (Rectangle block : blocks) {
                g.drawImage(fallingHeart, block.x, block.y, block.width, block.height, this);
            }

            // 3. Draw Player
            g.drawImage(playerImage, playerXPosition, playerYPosition, playerWidth, playerHeight, this);

            //draw score bg
            g.drawImage(scoreImage, 380, 50, 167, 80, this);
            
            // 4. Draw Score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 420, 100);

        } else {
            // Draw the normal background first
            g.drawImage(gameBackground, 0, 0, width, length, this);

            // Calculate the center position for the 200x200 image
            int overlayWidth = 400;
            int overlayHeight = 400;
            int xPosition = (width - overlayWidth) / 2;
            int yPosition = (length - overlayHeight) / 2;

            // Draw the Game Over screen in the center
            g.drawImage(gameOverScreen, xPosition, yPosition, overlayWidth, overlayHeight, this);

            // Draw the text over it
            g.setColor(Color.pink);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("GAME OVER", 200, 250);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Score: " + score, 255, 310);

            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("Press ENTER to restart", 225, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            if (movingLeft && playerXPosition > 35) {
                playerXPosition -= playerSpeed;
            }
            if (movingRight && playerXPosition < width - playerWidth - 35) {
                playerXPosition += playerSpeed;
            }
            tickCount++;
            if (tickCount % 25 == 0) {
                int minX = 35;
                int maxX = width - blockSize - 35;
                int randomX = minX + random.nextInt(maxX - minX);
                blocks.add(new Rectangle(randomX, 0, blockSize, blockSize));
            }

            Rectangle playerCollision = new Rectangle(playerXPosition, playerYPosition, playerWidth, playerHeight);

            for (int i = 0; i < blocks.size(); i++) {
                Rectangle getBlock = blocks.get(i);
                getBlock.y += 5;

                if (getBlock.intersects(playerCollision)) {
                    running = false;
                }

                if (getBlock.y > length) {
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
        // Leave empty
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

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

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        selfMade window = new selfMade();
        frame.add(window);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Dodge the Hearts");
        window.requestFocusInWindow();
    }

}
