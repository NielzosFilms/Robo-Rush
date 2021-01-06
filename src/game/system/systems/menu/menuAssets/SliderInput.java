package game.system.systems.menu.menuAssets;

import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.inputs.MouseInput;
import game.system.helpers.Helpers;
import game.system.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SliderInput {
	private boolean holding = false;
	private SliderKnob knob;
	private MouseInput mouse;

	private int change_timer;
	private float value;

	private int x, y, width;

	public SliderInput(int x, int y, int width, MouseInput mouse, float value) {
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

			Rectangle knobBounds = knob.getBounds();
			float knobX = Math.round(knobBounds.getCenterX());
			knobX = knobX - x;
			float max = width;
			float new_value = 1 / max * knobX;

			if(value != new_value) onCange();
			value = new_value;
			clampKnob();
		}
	}

	private void onCange() {
		if(change_timer >= 2) {
			AudioPlayer.playSound(AudioFiles.menu_move_bar, Game.settings.getSound_vol(), false, 0);
			change_timer = 0;
		}
		change_timer++;
	}

	public void render(Graphics g) {
		g.setColor(new Color(38, 43, 68));
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;

		int knobX = Math.round(width * value);

		knob.setX(x + knobX);
	}

	public void alignCenterX(int screenWidth) {
		this.x = screenWidth / 2 - width / 2;
		setValue(value);
	}

	public int getX() {
		return x;
	}
}
