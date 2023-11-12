import java.awt.*;
import java.awt.image.BufferedImage;

public class Boot {
    private Image bootImage;
    private int x = 0, y = 0;

    public Boot(int initWidth, int initHeight) {
        bootImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("res/boot.png"));
        scaleBoot(initWidth, initHeight);
    }

    public void scaleBoot(int width, int height) {
        bootImage = bootImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public Image getBoot() {
        return bootImage;
    }

    public int getWidth() {
        return bootImage.getWidth(null);
    }

    public int getHeight() {
        return bootImage.getHeight(null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    public BufferedImage getBufferedImage() {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(bootImage, 0, 0, null);
        g.dispose();
        return bi;
    }
}
