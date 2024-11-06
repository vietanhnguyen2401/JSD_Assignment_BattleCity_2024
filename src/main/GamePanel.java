package main;

import entity.Base;
import entity.Player;
import item.SuperItem;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 8; // 8 x 8 tile size
    final int scale = 2;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 28;
    public final int maxScreenRow = 28;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;


    // FPS
    final int FPS = 60;
    final long ONE_SECOND_IN_NANOSECOND = 1000000000;
    // System
    TileManager TManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    UI ui = new UI(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Sound sound = new Sound();

    // Entity
    Player player1 = new Player(this, keyHandler, 132, 400, 1);

    Base base = new Base(this);

    // Game state
    public int gameState;
    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public final int PAUSE_STATE = 2;
    public final int GAME_OVER_STATE = 3;

public int enemyCount = 10;

    public SuperItem[] item = new SuperItem[10];
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        }

    public void setupGame() {
        aSetter.setItem();
        //        gameState = PLAY_STATE;
        gameState = GAME_OVER_STATE;
        playMusic(0);

    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = ONE_SECOND_IN_NANOSECOND/FPS;  // 0.01666666 seconds
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

            if (timer >= ONE_SECOND_IN_NANOSECOND) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                 timer = 0;

            }
        }
    }

    public void update(){
        if (gameState == PLAY_STATE){
            player1.update();
        }
        if (gameState == PAUSE_STATE){

        }
//        if (gameState == GAME_OVER_STATE){
//            base.
//        }

    }

    public void paintComponent( Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // TITLE SCREEN
        if(gameState == TITLE_STATE){
            ui.draw(g2);
        } else {
            // this will draw tiles and player
            TManager.draw(g2, player1);
            base.draw(g2);
            for(int i  = 0; i < item.length; i++){
                if(item[i] != null) {
                    item[i].draw(g2, this);
                }
            }
            ui.draw(g2);


        }
        g2.dispose();

    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
//        sound.loop();
    }
}
