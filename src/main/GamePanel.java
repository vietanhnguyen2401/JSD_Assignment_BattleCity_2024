package main;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import item.SuperItem;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 8; // 8 x 8 tile size
    final int scale = 2;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 26;
    public final int maxScreenRow = 26;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // FPS
    int FPS = 60;
    TileManager TManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Player player = new Player(this, keyHandler);
    //npc
    public Enemy npc[] = new Enemy[10];

    public SuperItem item[] = new SuperItem[10];
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setItem();
        aSetter.setNPC();
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;  // 0.01666666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1){
                double nextDrawTime = System.nanoTime() + drawInterval;
//            // 1.UPDATE: update information such as character position
                update();
                // 2.DRAW: draw the screen with the updated information
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                 timer = 0;

            }
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000;
//
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//                Thread.sleep((long)remainingTime);
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    public void update(){
        player.update();
        for (int i = 0; i < npc.length; i++) {
            if(npc[i] != null){
                npc[i].update();
            }
        }
    }

    public void paintComponent( Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // this will draw tiles and player
        TManager.draw(g2, player);
        for(int i  = 0; i < item.length; i++){
            if(item[i] != null) {
                item[i].draw(g2, this);
            }
        }
        // NPC
        for(int i  = 0; i < npc.length; i++){
            if(npc[i] != null) {
                npc[i].draw(g2);
            }
        }
        g2.dispose();

    }
}
