package jsd.project.tank90.main;

import jsd.project.tank90.item.SuperItem;
import jsd.project.tank90.entity.*;
import jsd.project.tank90.tile.Drawer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The GamePanel class is used for running the game.
 */
public class GamePanel extends JPanel implements Runnable {
    final int ORIGINAL_TILE_SIZE = 8; // 8 x 8 jsd.project.tank90.tile size
    final int SCALE = 2; // Scaling factor

    public final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public final int maxScreenCol = 28;
    public final int maxScreenRow = 28;
    public final int screenWidth = TILE_SIZE * maxScreenCol;
    public final int screenHeight = TILE_SIZE * maxScreenRow;
    public UI ui = new UI(this);

    // FPS
    final int FPS = 60;
    final long ONE_SECOND_IN_NANOSECOND = 1000000000;

    // System Managers
    public Drawer drawer = new Drawer(this);
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

    // Game state
    public int gameState;
    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public final int PAUSE_STATE = 2;
    public final int GAME_OVER_STATE = 3;

    public int totalPoint = 0;
    public int currentLevel = 1; // from 1 to 5
    public SuperItem[] item = new SuperItem[10];

    // NPC spawn settings
    private final int SPAWN_INTERVAL = 15 * 60; // 15 seconds at 60 FPS
    private int spawnTimer = 0;
    public int enemyCount = 10;
    private final int MAX_ENEMIES = npc.length;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setTimedItems();
        gameState = TITLE_STATE;
        playMusic(0);
        aSetter.setNPC();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = ONE_SECOND_IN_NANOSECOND / FPS; // 0.01666 seconds per frame
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= ONE_SECOND_IN_NANOSECOND) {
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
                int spawnX = 398, spawnY = 50;
                boolean validPosition = true;

                for (int x = spawnX; x < spawnX + TILE_SIZE; x += TILE_SIZE / 2) {
                    for (int y = spawnY; y < spawnY + TILE_SIZE; y += TILE_SIZE / 2) {
                        if (cChecker.checkTileCollision(x, y) || cChecker.isPositionOccupiedByEntity(x, y)) {
                            validPosition = false;
                            break;
                        }
                    }
                    if (!validPosition) break;
                }

                if (validPosition) {
                    npc[i] = new Enemy(this);
                    npc[i].x = spawnX;
                    npc[i].y = spawnY;
                    enemiesToSpawn--;
                }
            }
        }
    }

    public void retry() {
        player.setDefaultValues();
        totalPoint = 0;
        currentLevel = 1;

        for (int i = 0; i < npc.length; i++) {
            npc[i] = null;
        }

        enemyCount = 10;
        aSetter.setNPC();
        aSetter.setTimedItems();
        drawer.loadMap();
    }

    public void nextLevel() {
        player.setDefaultValues();

        for (int i = 0; i < npc.length; i++) {
            npc[i] = null;
        }

        enemyCount = 10;
        player.starCount = 0;
        playMusic(3);

        if (currentLevel < 5) {
            currentLevel++;
        } else {
            gameState = GAME_OVER_STATE;
            playMusic(6);
        }

        aSetter.setNPC();
        aSetter.setTimedItems();
        drawer.loadMap();
    }

    public void update() {
        if (gameState == GAME_OVER_STATE) return;

        if (enemyCount <= 0) {
            playMusic(3);
            nextLevel();
        }

        if (player.lives == 0) {
            gameState = GAME_OVER_STATE;
            System.out.println("Game Over!");
            playMusic(4);
            return;
        }

        if (gameState == PLAY_STATE) {
            player.update();

            spawnTimer++;
            if (spawnTimer >= SPAWN_INTERVAL) {
                spawnEnemies();
                spawnTimer = 0;
            }

            updateBullets(player.bullets);
            updateNPCs();
            updateExplosions();
        }
    }

    private void updateBullets(List<Bullet> bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (!bullet.alive) {
                bullets.remove(i);
                i--;
            }
        }
    }

    private void updateNPCs() {
        for (int i = 0; i < npc.length; i++) {
            Enemy enemy = npc[i];
            if (enemy != null && enemy.alive) {
                enemy.update();
            } else if (enemy != null) {
                npc[i] = null;
                enemyCount--;
            }
        }

        for (Enemy enemy : npc) {
            if (enemy != null && enemy.alive) {
                updateBullets(enemy.bullets);
            }
        }
    }

    private void updateExplosions() {
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            explosion.update();
            if (!explosion.isAlive()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == TITLE_STATE) {
            ui.draw(g2);
        } else {
            drawer.draw(g2, player, npc);
            base.draw(g2);

            for (SuperItem value : item) {
                if (value != null) {
                    value.draw(g2, this);
                }
            }

            for (Bullet bullet : player.bullets) {
                bullet.draw(g2);
            }

            for (Explosion explosion : explosions) {
                explosion.draw(g2);
            }

            ui.draw(g2);
        }
        g2.dispose();
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
    }
}
