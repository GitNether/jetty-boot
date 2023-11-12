import java.awt.*;

public class Tube {
    private int x = 0, y = 0, width = 0, height = 0;

    public Tube(int initWidth, int initHeight) {
        width = initWidth;
        height = initHeight;
    }

    public Rectangle getTube() {
        return (new Rectangle(x, y, width, height));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
}
