package game.assets.entities.player;

import game.assets.HealthBar_Player;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.helpers.Timer;
import game.system.inputs.KeyInput;
import game.system.systems.gameObject.GameObject;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Character_Robot extends Player {
	private Timer enerySubtractTimer = new Timer(60 * 4);
	private int maxEnergy = 100, energy = maxEnergy;

    public Character_Robot(int x, int y, int z_index) {
        super(x, y, z_index);
    }

    @Override
    protected void initAnimations() {
    	hand = new Texture(TEXTURE_LIST.player_list, 4, 0);
    	dash_idle = new Texture(TEXTURE_LIST.player_list, 4, 4);
        idle = new Animation(6,
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 1, 0),
				new Texture(TEXTURE_LIST.player_list, 2, 0),
				new Texture(TEXTURE_LIST.player_list, 3, 0));

		blink = new Animation(12,
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 0, 1),
				new Texture(TEXTURE_LIST.player_list, 1, 1),
				new Texture(TEXTURE_LIST.player_list, 0, 1),
				new Texture(TEXTURE_LIST.player_list, 1, 1),
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 0, 0));

		run = new Animation(6,
				new Texture(TEXTURE_LIST.player_list, 0, 2),
				new Texture(TEXTURE_LIST.player_list, 1, 2),
				new Texture(TEXTURE_LIST.player_list, 2, 2),
				new Texture(TEXTURE_LIST.player_list, 3, 2),
				new Texture(TEXTURE_LIST.player_list, 4, 2),
				new Texture(TEXTURE_LIST.player_list, 5, 2));

		hurt = new Animation(6,
				//new Texture(TEXTURE_LIST.player_list, 0, 3),
				new Texture(TEXTURE_LIST.player_list, 1, 3),
				new Texture(TEXTURE_LIST.player_list, 2, 3),
				new Texture(TEXTURE_LIST.player_list, 3, 3),
				new Texture(TEXTURE_LIST.player_list, 2, 3),
				new Texture(TEXTURE_LIST.player_list, 3, 3),
				new Texture(TEXTURE_LIST.player_list, 0, 3));

		dash_start = new Animation(3,
				new Texture(TEXTURE_LIST.player_list, 1, 4),
				new Texture(TEXTURE_LIST.player_list, 2, 4),
				new Texture(TEXTURE_LIST.player_list, 3, 4));

		dash_end = new Animation(6,
				//new Texture(TEXTURE_LIST.player_list, 5, 4),
				new Texture(TEXTURE_LIST.player_list, 6, 4),
				new Texture(TEXTURE_LIST.player_list, 7, 4),
				new Texture(TEXTURE_LIST.player_list, 1, 4));
    }

    @Override
    protected void tickAbstract() {
    	if(enerySubtractTimer.timerOver()) {
    		enerySubtractTimer.resetTimer();
			energy = Helpers.clampInt(energy - 1, 0, maxEnergy);
		}
		enerySubtractTimer.tick();
    }

    @Override
    protected void renderAbstract(Graphics g) {

    }

    public int getEnergy() {
    	return energy;
	}

	public float getEnergyPercent() {
    	return 1 / (float)maxEnergy * (float)energy;
	}

	public void setEnergy(int energy) {
    	this.energy = Helpers.clampInt(energy, 0, maxEnergy);;
	}

	public void addEnergy(int energy) {
    	this.energy = Helpers.clampInt(this.energy + energy, 0, maxEnergy);;
	}

	public boolean isEnergyMax() {
    	return this.energy == this.maxEnergy;
	}
}
