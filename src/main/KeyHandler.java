package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, shootPressed;
    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // TITLE STATE
        if (gp.gameState == gp.TITLE_STATE){
            if (code == KeyEvent.VK_W){
                if (gp.ui.commandNumber == 2)gp.ui.commandNumber=0;
            }
            if (code == KeyEvent.VK_S){
                if(gp.ui.commandNumber == 0) gp.ui.commandNumber=2;
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNumber == 0) gp.gameState = gp.PLAY_STATE;
                if(gp.ui.commandNumber == 1) gp.gameState = gp.PLAY_STATE;
                if(gp.ui.commandNumber == 2) System.exit(0);
            }
        }

        // PLAY STATE
        if (gp.gameState == gp.PLAY_STATE){
            // PLAYER CONTROLLER
            if (code == KeyEvent.VK_W) upPressed= true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_SPACE) shootPressed = true;
            if (code == KeyEvent.VK_P) gp.gameState = gp.PAUSE_STATE;

        } else if (gp.gameState == gp.PAUSE_STATE){
            if (code == KeyEvent.VK_P) gp.gameState = gp.PLAY_STATE;
        }
        // GAME OVER STATE
        if (gp.gameState == gp.GAME_OVER_STATE){
            if (code == KeyEvent.VK_W){
                if (gp.ui.commandNumber == 3) gp.ui.commandNumber = 0;
            }
            if (code == KeyEvent.VK_S){
                if(gp.ui.commandNumber == 0) gp.ui.commandNumber = 3;
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNumber == 0) {
                    gp.gameState = gp.PLAY_STATE;
                    gp.retry();
                };
                if(gp.ui.commandNumber == 3){
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (gp.gameState == gp.PLAY_STATE) {
            // PLAYER 1
            if (code == KeyEvent.VK_W) upPressed = false;
            else if (code == KeyEvent.VK_S) downPressed = false;
            else if (code == KeyEvent.VK_A) leftPressed = false;
            else if (code == KeyEvent.VK_D) rightPressed = false;
            else if (code == KeyEvent.VK_SPACE) shootPressed = false;
        }
    }
}
