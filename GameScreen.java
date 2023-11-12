import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    private static final long serialVersionUID = 1L;
    private int width, height;
    private Tube bottomTube1, bottomTube2;
    private Tube topTube1, topTube2;
    private Boot boot;
    private String message = "";
    private int score;
    private int scoreWidth = 0;
    private int messageWidth = 0;
    private Font font = new Font("Calibri", Font.BOLD, 50);

    public GameScreen(int initWidth, int initHeight) {
        width = initWidth;
        height = initHeight;
    }

    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

        if(bottomTube1 != null && bottomTube2 != null && topTube1 != null && topTube2 != null) {
            graphic.setColor(new Color(10, 125, 4));
            graphic.fillRect(bottomTube1.getX(), bottomTube1.getY(), bottomTube1.getWidth(), bottomTube1.getHeight());
            graphic.fillRect(bottomTube2.getX(), bottomTube2.getY(), bottomTube2.getWidth(), bottomTube2.getHeight());
            graphic.fillRect(topTube1.getX(), topTube1.getY(), topTube1.getWidth(), topTube1.getHeight());
            graphic.fillRect(topTube2.getX(), topTube2.getY(), topTube2.getWidth(), topTube2.getHeight());
        }

        if(boot != null) {
            graphic.drawImage(boot.getBoot(), boot.getX(), boot.getY(), null);
        }

        graphic.setColor(Color.BLACK);
        graphic.setFont(font);
        FontMetrics metric = graphic.getFontMetrics(font);
        scoreWidth = metric.stringWidth(String.format("%d", score));
        graphic.drawString(String.format("%d", score), width/2 - scoreWidth/2, 50);

        messageWidth = metric.stringWidth(message);
        graphic.drawString(message, width/2 - messageWidth/2, height/3);
    }

    public void setBottomTube(Tube tube1, Tube tube2) {
        bottomTube1 = tube1;
        bottomTube2 = tube2;
    }

    public void setTopTube(Tube tube1, Tube tube2) {
        topTube1 = tube1;
        topTube2 = tube2;
    }

    public void setBoot(Boot boot) {
        this.boot = boot;
    }

    public void sendText(String message) {
        this.message = message;
    }

    public void incrementScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
