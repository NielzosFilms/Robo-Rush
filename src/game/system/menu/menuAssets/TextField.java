package game.system.menu.menuAssets;

import game.textures.Fonts;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TextField {
	private static final String CURSOR = "|";

	private StringBuffer buffer;
	private String text;
	private int fontSize, maxLength;
	private Rectangle bounds;
	private Font font;

	private int cursorDelay, index;
	private boolean cursor = false;

	private boolean focus;

	public TextField(Rectangle bounds, int fontSize, int maxLength) {
		this.bounds = bounds;
		this.fontSize = fontSize;
		this.buffer = new StringBuffer();
		this.font = Fonts.gameria_fonts.get(fontSize);
		this.maxLength = maxLength;
		this.text = "textfield test text";
		buffer = new StringBuffer(text);
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

		} else {
			text = text + e.getKeyChar();
		}
	}

	private void addChar(char character) {
		if(buffer.length() >= maxLength) return;
		buffer.append(character);
		// index to end
		// scroll text to side
		// text = buffer.substring()
	}

	public void tick() {
		if(focus) {
			cursorDelay++;
			cursorDelay = cursorDelay % 30;
			if(cursorDelay == 0) {
				cursor = !cursor;
			}
			text = buffer.toString();
		}
	}

	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(Color.red);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(Color.black);
		g.setFont(font);
		if(focus) {
			g.drawString(text + (cursor ? CURSOR : ""), bounds.x, bounds.y);
		} else {
			g.drawString(text, bounds.x, bounds.y);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public boolean getFocus() {
		return this.focus;
	}
}
