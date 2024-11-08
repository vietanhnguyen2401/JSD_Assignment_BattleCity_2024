package main;

import entity.Enemy;

import entity.Base;
import entity.Player;
import entity.Bullet;
import item.SuperItem;
import tile.TileManager;

import java.util.Random;
import javax.swing.*;
import java.awt.*;

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
//    Sound sound = new Sound();

    // Entities
    Player player = new Player(this, keyHandler);
    Base base = new Base(this);
    public Enemy[] npc = new Enemy[10];
    public SuperItem[] item = new SuperItem[10];

    // Game state
    public int gameState;

    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public final int PAUSE_STATE = 2;
    public final int GAME_OVER_STATE = 3;
    // Variables for NPC spawn timing
    private final int SPAWN_INTERVAL = 15 * 60; // 10 seconds * 60 FPS
    private int spawnTimer = 0;

    // Keep track of the number of spawned enemies
    private int enemyCount = 0;

    // Maximum enemies allowed on the screen at a time
    private final int MAX_ENEMIES = npc.length;

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
        playMusic(0);
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

    private void spawnEnemies() {
        int enemiesToSpawn = 2;
        Random random = new Random();

        for (int i = 0; i < MAX_ENEMIES && enemiesToSpawn > 0; i++) {
            if (npc[i] == null) { // Find an empty slot in the NPC array
                int spawnX, spawnY;
                boolean validPosition;
                int attempts = 0;

                do {
                    spawnX = (random.nextInt(4) + maxScreenCol - 4) * tileSize; // Cột từ 21 đến 27
                    spawnY = (random.nextInt(4) + 1) * tileSize;

                    // Check if the spawn position is unblocked and unoccupied
                    validPosition = true;
                    for (int x = spawnX; x < spawnX + tileSize; x += tileSize / 2) {
                        for (int y = spawnY; y < spawnY + tileSize; y += tileSize / 2) {
                            if (cChecker.checkTileCollision(x, y) ||
                                    cChecker.isPositionOccupiedByEntity(x, y)) {
                                validPosition = false;
                                break;
                            }
                        }
                        if (!validPosition) break;
                    }

                    attempts++;
                } while (!validPosition && attempts < 10);

                if (validPosition) {
                    npc[i] = new Enemy(this);
                    npc[i].x = spawnX;
                    npc[i].y = spawnY;
                    enemiesToSpawn--;
                }
            }
        }
    }

    public void update() {
        if (gameState == PLAY_STATE) {
            player.update();
            // Spawn enemies every 10 seconds if there's space
            spawnTimer++;
            if (spawnTimer >= SPAWN_INTERVAL) {
                spawnEnemies();
                spawnTimer = 0;
            }
            for (Enemy enemy : npc) {
                if (enemy != null) {
                    enemy.update();
                }
            }

            // Update bullets
            for (int i = 0; i < player.bullets.size(); i++) {
                Bullet bullet = player.bullets.get(i);
                if (bullet.alive) {
                    bullet.update();
                } else {
                    player.bullets.remove(i);
                    i--;
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
                if (enemy != null) {
                    enemy.draw(g2);
                }
            }

            // Draw bullets
            for (Bullet bullet : player.bullets) {
                bullet.draw(g2);
            }

            ui.draw(g2);
        }

        g2.dispose();

    }

    public void playMusic(int i) {
//        sound.setFile(i);
//        sound.play();
//         sound.loop();
    }
}
