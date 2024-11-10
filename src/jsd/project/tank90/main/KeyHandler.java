package jsd.project.tank90.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, shootPressed, cheatPressed, normalPressed;
    private StringBuilder cheatCodeBuffer = new StringBuilder();

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed here
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.TITLE_STATE) {
            if (code == KeyEvent.VK_W) {
                if (gp.ui.commandNumber == 2) gp.ui.commandNumber = 0;
            }
            if (code == KeyEvent.VK_S) {
                if (gp.ui.commandNumber == 0) gp.ui.commandNumber = 2;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumber == 0) gp.gameState = gp.PLAY_STATE;
                if (gp.ui.commandNumber == 2) System.exit(0);
            }
        }

        // PLAY STATE
        if (gp.gameState == gp.PLAY_STATE) {
            // PLAYER CONTROLLER
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_SPACE) shootPressed = true;
            if (code == KeyEvent.VK_P) gp.gameState = gp.PAUSE_STATE;

            // Check for cheat code or normal mode sequence
            checkCheatOrNormalCode(e);
        } else if (gp.gameState == gp.PAUSE_STATE) {
            if (code == KeyEvent.VK_P) gp.gameState = gp.PLAY_STATE;
        }

        // GAME OVER STATE
        if (gp.gameState == gp.GAME_OVER_STATE) {
            if (code == KeyEvent.VK_W) {
                if (gp.ui.commandNumber == 3) gp.ui.commandNumber = 0;
            }
            if (code == KeyEvent.VK_S) {
                if (gp.ui.commandNumber == 0) gp.ui.commandNumber = 3;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumber == 0) {
                    gp.gameState = gp.PLAY_STATE;
                    gp.retry();
                }
                if (gp.ui.commandNumber == 3) {
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (gp.gameState == gp.PLAY_STATE) {
            if (code == KeyEvent.VK_W) upPressed = false;
            else if (code == KeyEvent.VK_S) downPressed = false;
            else if (code == KeyEvent.VK_A) leftPressed = false;
            else if (code == KeyEvent.VK_D) rightPressed = false;
            else if (code == KeyEvent.VK_SPACE) shootPressed = false;
        }
    }

    private void checkCheatOrNormalCode(KeyEvent e) {
        char keyChar = e.getKeyChar();
        // Append the character to the cheat code buffer, only keeping the last 6 characters for "normal" check
        cheatCodeBuffer.append(keyChar);
        if (cheatCodeBuffer.length() > 6) {
            cheatCodeBuffer.deleteCharAt(0);
        }

        // If "cheat" sequence is typed, activate the cheat
        if (cheatCodeBuffer.toString().endsWith("cheat")) {
            cheatPressed = true;  // Set the cheat flag
            normalPressed = false; // Ensure normal mode is off
            cheatCodeBuffer.setLength(0);  // Clear the buffer after activation
            System.out.println("Cheat activated! Full power-up applied to the player.");
        }

        // If "normal" sequence is typed, disable the cheat
        if (cheatCodeBuffer.toString().endsWith("normal")) {
            normalPressed = true;  // Set the normal mode flag
            cheatPressed = false;  // Disable cheat mode
            cheatCodeBuffer.setLength(0);  // Clear the buffer after deactivation
            System.out.println("Normal mode activated! Player abilities reset.");
        }
    }
}
