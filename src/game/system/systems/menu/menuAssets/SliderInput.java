package game.system.systems.menu.menuAssets;

import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.inputs.MouseInput;
import game.system.helpers.Helpers;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SliderInput {
	private boolean holding = false;
	private SliderKnob knob;
	private MouseInput mouse;

	private int value, change_timer;

	private int x, y, width;

	public SliderInput(int x, int y, int width, MouseInput mouse, int value) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.mouse = mouse;
		this.knob = new SliderKnob(x, y, this);
		setValue(value);
	}

	public void tick() {
		if(holding) {
			knob.setX(mouse.mouse_x + 4);
			clampKnob();
			if(value != getValue()) onCange();
			value = getValue();
		}
	}

	private void onCange() {
		if(change_timer >= 2) {
			AudioPlayer.playSound(AudioFiles.menu_move_bar, 0.2f, false, 0);
			change_timer = 0;
		}
		change_timer++;
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
		int knobH = knob.getHeight();
		//margins correct?
		return new Rectangle(x-4, y - 4, width + 4, 2 + 4);
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

	public void setValue(int value) {
		knob.setX(x + (width / 100 * value));
	}

	public void alignCenterX(int screenWidth) {
		this.x = screenWidth / 2 - width / 2;
		knob.setX(x);
	}
}
