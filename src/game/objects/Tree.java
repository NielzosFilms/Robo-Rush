package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.entities.Player;
import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Tree extends GameObject{
	
	private BufferedImage tex;
	private String biome;
	private Player player;

	public Tree(int x, int y, int z_index, ID id, String biome, Player player) {
		super(x, y, z_index, id);
		this.biome = biome;
		this.player = player;
		
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
		int player_cenX = player.getX()+8;
		int player_cenY = player.getY()+(16+8)/2;
		int tree_cenY = y+8;
		
		if(player_cenY > tree_cenY) {
			this.setZIndex(1);
		}else {
			this.setZIndex(3);
		}
	}

	public void render(Graphics g) {
		g.drawImage(tex, x-16, y-32, null);
		g.setColor(Color.green);
		g.drawRect(x, y, 16, 16);
	}

}
