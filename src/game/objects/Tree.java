package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Tree extends GameObject{
	
	private BufferedImage tex;
	private String biome;

	public Tree(int x, int y, ID id, String biome) {
		super(x, y, id);
		this.biome = biome;
		
		switch(biome) {
			case "forest":
				tex = Textures.tree;
				break;
			default:
				tex = Textures.tree;
				break;
		}
		
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(tex, x-16, y-32, null);
		g.setColor(Color.green);
		g.drawRect(x, y, 16, 16);
	}

}
