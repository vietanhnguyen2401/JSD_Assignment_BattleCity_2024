package main;
import entity.*;

import item.SuperItem;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 8; // 8 x 8 tile size
    final int scale = 2;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 28;
    public final int maxScreenRow = 28;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public UI ui = new UI(this);

    // FPS
    int FPS = 60;
    public TileManager TManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;

    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Sound sound = new Sound();
    public List<Explosion> explosions = new ArrayList<>();

    // Entities
    public Player player = new Player(this, keyHandler);
    public Base base = new Base(this);
    public Enemy[] npc = new Enemy[10];
    public SuperItem[] item = new SuperItem[10];

    // Game state
    public int gameState;

    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public final int PAUSE_STATE = 2;
    public final int GAME_OVER_STATE = 3;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

    }

    public void setupGame() {
        aSetter.setItem();
        gameState = TITLE_STATE;
//        playMusic(0);
        aSetter.setNPC();
//        playMusic(0);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;  // 0.01666666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        if (player.lives <= 0) {
            gameState = GAME_OVER_STATE;
            System.out.println("Game Over!");
            return; // Stop further updates if game is over
        }

        if (gameState == PLAY_STATE) {
            player.update();

            // Update player bullets
            for (int i = 0; i < player.bullets.size(); i++) {
                Bullet bullet = player.bullets.get(i);
                bullet.update();
                if (!bullet.alive) {
                    player.bullets.remove(i);
                    i--; // Adjust index after removal
                }
            }

            // Update enemies and check for alive status
            for (int i = 0; i < npc.length; i++) {
                Enemy enemy = npc[i];
                if (enemy != null && enemy.alive) {
                    enemy.update();
                } else if (enemy != null && !enemy.alive) {
                    npc[i] = null; // Remove enemy when dead
                }
            }
            for (Enemy enemy : npc) {
                if (enemy != null && enemy.alive) {
                    for (int i = 0; i < enemy.bullets.size(); i++) {
                        Bullet bullet = enemy.bullets.get(i);
                        if (bullet.alive) {
                            bullet.update();
                        } else {
                            enemy.bullets.remove(i);
                            i--; // Adjust index after removal
                        }
                    }
                }
            }


            // Update explosions
            for (int i = 0; i < explosions.size(); i++) {
                Explosion explosion = explosions.get(i);
                explosion.update();
                if (!explosion.isAlive()) {
                    explosions.remove(i); // Remove completed explosions
                    i--; // Adjust index after removal
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == TITLE_STATE) {
            ui.draw(g2);
        } else {
            // Draw tiles and player
            TManager.draw(g2, player);
            base.draw(g2);

            for (SuperItem value : item) {
                if (value != null) {
                    value.draw(g2, this);
                }
            }

            for (Enemy enemy : npc) {
                if (enemy != null && enemy.alive) {
                    enemy.draw(g2);
                }
            }
            for (Enemy enemy : npc) {
                if (enemy != null && enemy.alive) {
                    for (Bullet bullet : enemy.bullets) {
                        if (bullet.alive) {
                            bullet.draw(g2); // Render bullet fired by the enemy
                        }
                    }
                }
            }

            // Draw bullets
            for (Bullet bullet : player.bullets) {
                bullet.draw(g2);
            }


            for (Explosion explosion : explosions) {
                explosion.draw(g2); }

            ui.draw(g2);
        }

        g2.dispose();

    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
//         sound.loop();
    }
}
