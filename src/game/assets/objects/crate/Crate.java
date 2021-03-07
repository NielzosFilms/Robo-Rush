package game.assets.objects.crate;

import java.awt.*;
import java.util.Random;

import game.assets.HealthBar;
import game.assets.items.*;
import game.assets.items.item.Item;
import game.audio.SoundEffect;
import game.enums.DIRECTIONS;
import game.enums.LOOT_TABLES;
import game.system.helpers.JsonLoader;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.*;
import game.system.systems.hitbox.HitboxContainer;
import game.system.systems.hud.Selection;
import game.system.main.Game;
import game.enums.ID;
import game.system.helpers.Settings;
import game.system.world.JsonStructureLoader;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import org.json.simple.JSONObject;

public class Crate extends GameObject implements Bounds, Pushable, Interactable, Hitable, Destroyable, Health {
    private final int REGEN_DELAY_AFTER_HIT = 60*10;
    private final int REGEN_DELAY = 30;
    private final int REGEN_AMOUNT = 1;
    private int regen_timer_after_hit = 0;
    private int regen_timer = 0;

//    private Inventory inv;
    private Random rand = new Random();
    private HealthBar healthBar;

    private Selection selection = new Selection();

    private boolean destroyedCalled = false;

    public Crate(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
//        inv = new Inventory(3, 2);
//        inv.setXY(300, 100);
//        inv.setInitXY(300, 100);

        healthBar = new HealthBar(0, 0, 0, 7, 1);

        this.tex = new Texture(TEXTURE_LIST.wood_list, 1, 0);
    }

    public Crate(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Crate);

//        inv = new Inventory(
//                Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "inv_x")),
//                Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "inv_y")));
//        String loot_table = StructureLoaderHelpers.getCustomProp(json, "loot_table");
//        if(loot_table != null && !loot_table.equals("")) {
//            try {
//                inv.fillRandom(LOOT_TABLES.valueOf(loot_table).getGeneratedItems());
//            } catch(Exception e) {
//                Logger.printError("Loot table: [" + loot_table + "] not found, at: [" + x * division + "," + y* division + "]");
//            }
//        }
//        inv.setXY(300, 100);
//        inv.setInitXY(300, 100);

        healthBar = new HealthBar(0, 0, 0, 7, 1);

        this.tex = new Texture(TEXTURE_LIST.wood_list, 1, 0);
    }

    public void tick() {
        if(regen_timer_after_hit > 0) {
            regen_timer_after_hit--;
        } else {
            if(regen_timer > 0) {
                regen_timer--;
            } else {
                healthBar.addHealth(REGEN_AMOUNT);
                regen_timer = REGEN_DELAY;
            }
        }
        healthBar.setXY(x - 4, y - 8);

        Rectangle obj_bounds = this.getSelectBounds();
        Rectangle player_bounds = Game.gameController.getPlayer().getBounds();
        double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
                * (obj_bounds.getCenterX() - player_bounds.getCenterX())
                + (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
        if (dis > 50) {
//            Game.gameController.getInventorySystem().removeOpenInventory(inv);
        }

        if(healthBar.dead()) {
            Game.gameController.getHandler().removeHudObject(healthBar);
            Game.gameController.getHandler().findAndRemoveObject(this);
        }
    }

    public void render(Graphics g) {
        g.drawImage(this.tex.getTexure(), x, y, width, height, null);
//        if(Game.gameController.getInventorySystem().openInventoriesContains(this.inv)) {
//            selection.renderSelection(g, getSelectBounds(), 2);
//        }
    }

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

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
        
    }

    public void interact() {
//        inv.open();
    }

    public void destroyed() {
        //Game.world.getHud().removeHealthBar(healthBar);
//        Game.gameController.getInventorySystem().removeOpenInventory(inv);
//        for(InventorySlotDef slot : inv.getSlots()) {
//            if(slot.hasItem()) {
//                game.assets.items.item.Item inv_item = slot.getItem();
//                Item_Ground gnd_item = inv_item.getItemGround();
//                gnd_item.setX(x);
//                gnd_item.setY(y);
//                Game.gameController.getHandler().addObject(gnd_item);
//            }
//        }
        SoundEffect.crate_destroy.play();
        destroyedCalled = true;
    }

    @Override
    public boolean destroyedCalled() {
        return destroyedCalled;
    }

    @Override
    public boolean canRemove() {
        return true;
    }

    @Override
    public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
        regen_timer_after_hit = REGEN_DELAY_AFTER_HIT;
        SoundEffect.crate_impact.play();
        healthBar.subtractHealth(damage);
    }

    public void addItemToInv(Item item) {
//        this.inv.addItem(item);
    }

    public void push(DIRECTIONS direction) {
        switch(direction) {
            case up:
                y -= 1;
                break;
            case down:
                y += 1;
                break;
            case left:
                x -= 1;
                break;
            case right:
                x += 1;
                break;
        }
    }

    @Override
    public int getHealth() {
        return healthBar.getHealth();
    }

    @Override
    public HealthBar getHealthBar() {
        return healthBar;
    }

    @Override
    public boolean dead() {
        return healthBar.dead();
    }
}
