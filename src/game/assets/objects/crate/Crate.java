package game.assets.objects.crate;

import java.awt.*;
import java.util.Random;

import game.assets.HealthBar;
import game.assets.items.*;
import game.assets.items.item.Item;
import game.enums.DIRECTIONS;
import game.enums.LOOT_TABLES;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.*;
import game.system.systems.inventory.Inventory;
import game.system.systems.inventory.InventorySlot;
import game.system.main.Game;
import game.enums.ID;
import game.system.helpers.Settings;
import game.system.world.JsonStructureLoader;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import org.json.simple.JSONObject;

public class Crate extends GameObject implements Collision, Pushable, Interactable, Hitable, Destroyable {
    private final int REGEN_DELAY_AFTER_HIT = 60*10;
    private final int REGEN_DELAY = 30;
    private final int REGEN_AMOUNT = 1;
    private int regen_timer_after_hit = 0;
    private int regen_timer = 0;

    private Inventory inv;
    private Random rand = new Random();
    private HealthBar healthBar;

    public Crate(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        inv = new Inventory(3, 2);
        inv.setXY(300, 100);
        inv.setInitXY(300, 100);

        healthBar = new HealthBar(x - 4, y - 8, 0, 7);
        this.tex = new Texture(TEXTURE_LIST.wood_list, 1, 0);
    }

    public Crate(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Crate);

        inv = new Inventory(
                Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "inv_x")),
                Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "inv_y")));
        String loot_table = StructureLoaderHelpers.getCustomProp(json, "loot_table");
        if(loot_table != null && !loot_table.equals("")) {
            try {
                inv.fillRandom(LOOT_TABLES.valueOf(loot_table).getGeneratedItems());
            } catch(Exception e) {
                Logger.printError("Loot table: [" + loot_table + "] not found, at: [" + x * division + "," + y* division + "]");
            }
        }
        inv.setXY(300, 100);
        inv.setInitXY(300, 100);

        healthBar = new HealthBar(x - 4, y - 8, 0, 7);
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
        healthBar.tick();
        if(healthBar.dead()) Game.world.getHandler().findAndRemoveObject(this);

        Rectangle obj_bounds = this.getSelectBounds();
        Rectangle player_bounds = Game.world.getPlayer().getBounds();
        double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
                * (obj_bounds.getCenterX() - player_bounds.getCenterX())
                + (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
        if (dis > 50) {
            Game.world.getInventorySystem().removeOpenInventory(inv);
        }
    }

    public void render(Graphics g) {
        g.drawImage(this.tex.getTexure(), x, y, width, height, null);
        if(Game.world.getInventorySystem().openInventoriesContains(this.inv)) {
            g.setColor(new Color(255, 255, 255, 127));
            g.drawRect(getSelectBounds().x, getSelectBounds().y, getSelectBounds().width, getSelectBounds().height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
        
    }

    public void interact() {
        inv.open();
    }

    public void destroyed() {
        Game.world.getHud().removeHealthBar(healthBar);
        Game.world.getInventorySystem().removeOpenInventory(inv);
        for(InventorySlot slot : inv.getSlots()) {
            if(slot.hasItem()) {
                game.assets.items.item.Item inv_item = slot.getItem();
                Item_Ground gnd_item = inv_item.getItemGround();
                gnd_item.setX(x);
                gnd_item.setY(y);
                Game.world.getHandler().addObject(gnd_item);
            }
        }
        AudioPlayer.playSound(AudioFiles.crate_destroy, Settings.sound_vol, false, 0);
    }

    public void hit(int damage) {
        regen_timer_after_hit = REGEN_DELAY_AFTER_HIT;
        AudioPlayer.playSound(AudioFiles.crate_impact, Settings.sound_vol, false, 0);
        healthBar.subtractHealth(damage);
    }

    public void addItemToInv(Item item) {
        this.inv.addItem(item);
    }

    public void push(DIRECTIONS direction) {
        switch(direction) {
            case up -> y -= 1;
            case down -> y += 1;
            case left -> x -= 1;
            case right -> x+= 1;
        }
    }
}
