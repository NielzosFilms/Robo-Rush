package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.entities.Player;
import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Tree extends GameObject{
	
	private BufferedImage tex;
	private String biome;
	private Player player;
	private Textures textures;

	public Tree(int x, int y, int z_index, ID id, String biome, Player player, Textures textures) {
		super(x, y, z_index, id);
		this.biome = biome;
		this.player = player;
		this.textures = textures;
		
		switch(biome) {
			case "forest":
				tex = textures.tree;
				break;
			default:
				tex = textures.tree;
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
		//g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y+3, 16, 16-6);
	}
	
	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, 16, 16);
	}

}
