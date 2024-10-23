package main;

import java.awt.*;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font gameFont, arial_80B;
    public int commandNumber = 0; // 0: one player, 1: two players, 2: game exit, 3: back to main menu
    public UI(GamePanel gp){
        this.gp = gp;
        gameFont = new Font("Font 7x7 Regular", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(gameFont);
        g2.setColor(Color.WHITE);

        // TITLE STATE
        if (gp.gameState == gp.TITLE_STATE){
            drawTitleScreen();
        }

        if (gp.gameState == gp.PLAY_STATE) {

        }

        // PAUSE STATE
        if (gp.gameState == gp.PAUSE_STATE){
            drawPauseScreen();
        }
        if (gp.gameState == gp.GAME_OVER_STATE){
            drawGameOverScreen();
        }
    }

    public void drawPauseScreen(){
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,170 ));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        String text = "GAME OVER";
        g2.setColor(Color.WHITE);


        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 3;
        g2.drawString(text, x, y);


        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));

        // retry option
        String retry = "RETRY";

        // back to main menu option
        String quit = "QUIT";

        x = getXForCenteredText(retry);
        y = gp.tileSize*15;
        if (commandNumber == 0){
            g2.drawString(">",  x - x/5, y);
        }
        g2.drawString(retry, x, y);

        x = getXForCenteredText(quit);
        y = gp.tileSize*17;
        if (commandNumber == 3){
            g2.drawString(">",  x - x/5, y);
        }
        g2.drawString(quit, x, y);
    }

    public int getXForCenteredText (String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public void drawTitleScreen(){
        // TITLE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        String text = "BATTLE CITY";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*5;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);


        // MENU OPTIONS
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));

        String onePlayer = "1 PLAYER";
        String twoPlayer = "2 PLAYERS";
        String quit = "QUIT";

        x = getXForCenteredText(onePlayer);
        y = gp.tileSize*15;
        if (commandNumber == 0){
            g2.drawString(">",  x - x/5, y);
        }
        g2.drawString(onePlayer, x, y);
        y = gp.tileSize*17;
        if (commandNumber == 1){
            g2.drawString(">",  x - x/5, y);
        }
        g2.drawString(twoPlayer, x, y);
        x = getXForCenteredText(quit);
        y = gp.tileSize*19;
        if (commandNumber == 2){
            g2.drawString(">",  x - x/5, y);
        }
        g2.drawString(quit, x, y);


    }
}
