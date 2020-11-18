package game.system.menu.menuAssets;

import game.system.inputs.MouseInput;
import game.system.main.Helpers;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SliderInput {
	private boolean holding = false;
	private SliderKnob knob;
	private MouseInput mouse;

	private int x, y, width;

	public SliderInput(int x, int y, int width, MouseInput mouse) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.mouse = mouse;
		this.knob = new SliderKnob(x, y, this);
	}

	public void tick() {
		if(holding) {
			knob.setX(mouse.mouse_x);
			clampKnob();
			System.out.println("slider %: " + this.getValue());
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, 2);
		knob.render(g);
	}

	public void mousePressed(MouseEvent e) {
		holding = true;
	}

	public void mouseReleased(MouseEvent e) {
		holding = false;
	}

	public Rectangle getBounds() {
		int knobW = knob.getWidth();
		int knobH = knob.getHeight();
		return new Rectangle(x, y - knobH / 2, width, 2 + knobH / 2);
	}

	private void clampKnob() {
		Rectangle knobBounds = knob.getBounds();
		knob.setX(Helpers.clampInt(knobBounds.x, x, x + width));
	}

	public int getValue() {
		Rectangle knobBounds = knob.getBounds();
		int knobX = (int) knobBounds.getCenterX();
		knobX = knobX - x;
		int max = width;
		return 100 / max * knobX;
	}
}
