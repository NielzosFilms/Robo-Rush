package game.assets.items;

import game.assets.objects.Crate;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.system.main.Game;
import game.textures.Textures;

import java.awt.*;

public class ItemCrate extends Item {
	public ItemCrate(int amount, ITEM_ID itemType) {
		super(amount, itemType);
		this.setPlaceable(true);
		this.tex = Textures.tileSetHouseBlocks.get(6);
		this.itemGround = new ItemGround(0, 0, 1, ID.Item, this);
	}

	public boolean place(int x, int y) {
		if(!Game.handler.objectExistsAtCoords(new Point(x, y), 1, 16, 16)) {
			Game.handler.addObject(new Crate(x, y, 1, ID.Crate));
			return true;
		}
//		Game.handler.addObject(new Crate(x, y, 1, ID.Crate));
//		return true;
		return false;
	}
}
