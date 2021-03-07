package game.assets.objects.crafting_table;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Interactable;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class TestTable extends GameObject implements Interactable, Bounds {
//    private Crafting_Inventory inventory;
    public TestTable(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
//        inventory = new Crafting_Inventory(3, 3, CraftingTableDefinitions.test_table);
//        inventory.setInitXY(200, 100);

        this.tex = new Texture(TEXTURE_LIST.wood_list, 0, 0);
    }

    @Override
    public void tick() {
        Rectangle obj_bounds = this.getSelectBounds();
        Rectangle player_bounds = Game.gameController.getPlayer().getBounds();
        double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
                * (obj_bounds.getCenterX() - player_bounds.getCenterX())
                + (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
        if (dis > 50) {
//            Game.gameController.getInventorySystem().removeOpenInventory(inventory);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(tex.getTexure(), x, y, width, height, null);
    }

    @Override
    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void interact() {
//        inventory.open();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }
}
