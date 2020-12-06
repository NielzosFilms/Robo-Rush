package game.assets.objects.crate;

import game.assets.items.Item;
import game.assets.items.Item_Ground;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Item_Crate extends Item {
	public Item_Crate(int amount) {
		super(amount, ITEM_ID.Crate);
		this.setPlaceable(true);
		this.tex = new Texture(TEXTURE_LIST.house_list, 6, 0);
		this.itemGround = new Item_Ground(0, 0, 1, ID.Item, this);
	}

	public boolean place(int x, int y) {
		if(!Game.world.getHandler().objectExistsAtCoords(new Point(x, y), 1)) {
			Game.world.getHandler().addObject(new Crate(x, y, 1, ID.Crate));
			return true;
		}
//		Game.handler.addObject(new Crate(x, y, 1, ID.Crate));
//		return true;
		return false;
	}
}
