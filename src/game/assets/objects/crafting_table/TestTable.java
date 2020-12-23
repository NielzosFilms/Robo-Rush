package game.assets.objects.crafting_table;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Interactable;
import game.system.systems.inventory.Crafting_Inventory;
import game.system.systems.inventory.crafting_enums.CraftingTableDefinitions;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class TestTable extends GameObject implements Interactable, Collision {
    private Crafting_Inventory inventory = new Crafting_Inventory(4, 4, CraftingTableDefinitions.test_table);
    public TestTable(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        inventory.setInitXY(200, 100);

        this.tex = new Texture(TEXTURE_LIST.wood_list, 0, 0);
    }

    @Override
    public void tick() {
        Rectangle obj_bounds = this.getSelectBounds();
        Rectangle player_bounds = Game.world.getPlayer().getBounds();
        double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
                * (obj_bounds.getCenterX() - player_bounds.getCenterX())
                + (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
        if (dis > 50) {
            Game.world.getInventorySystem().removeOpenInventory(inventory);
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
        inventory.open();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
