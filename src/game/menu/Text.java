package game.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;

import game.main.Game;
import game.main.MouseInput;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Rectangle;

public class Text {
    private int x, y;
    private String text;
    private int size;

    private FontMetrics fm;
    private Graphics g;
    private Graphics2D g2d;
    private Font f;

    private MouseInput mouseInput;

    public Text(String text, int x, int y, int size, MouseInput mouseInput) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = size;
        this.mouseInput = mouseInput;
    }

    public void render(Graphics g, Graphics2D g2d) {
        this.f = new Font("SansSerif", Font.PLAIN, this.size);
        this.g = g;
        this.g2d = g2d;
        this.fm = g2d.getFontMetrics(f);
        g2d.setFont(f);
        g2d.drawString(text, x, y);

        // method to render bounds on hover

        if (mouseOver()) {
            g.setColor(Color.GREEN);
            g.drawRect(getBounds(1).x, getBounds(1).y, getBounds(1).width, getBounds(1).height);
        }
    }

    public Rectangle getBounds(int margin) {
        int h = (int) fm.getAscent() - 4;
        int w = (int) fm.getStringBounds(text, g2d).getWidth();
        int xx = x;
        int yy = y - h;
        return new Rectangle(xx - margin, yy - margin, w + margin * 2, h + margin * 2);
    }

    public boolean mouseOver() {
        return mouseInput.mouseOverLocalVar(getBounds(1).x, getBounds(1).y, getBounds(1).width, getBounds(1).height);
    }
}
