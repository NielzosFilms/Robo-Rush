package game.assets.objects.crate;

import game.assets.items.item.Item;
import game.assets.items.Item_Ground;
import game.assets.items.item.Placeable;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.system.main.Game;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Item_Crate extends Item implements Placeable {
	public Item_Crate(int amount) {
		super(amount, ITEM_ID.Crate);
		this.tex = new Texture(TEXTURE_LIST.wood_list, 1, 0);
		this.itemGround = new Item_Ground(0, 0, 10, ID.Item, this);
	}

	public boolean place(int x, int y) {
		if(!Game.gameController.getHandler().objectExistsAtCoords(new Point(x, y))) {
			Game.gameController.getHandler().addObject(new Crate(x, y, 10, ID.Crate));
			return true;
		}
		return false;
	}
}
