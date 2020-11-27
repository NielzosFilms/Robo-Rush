package game.assets.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import game.assets.HealthBar;
import game.assets.items.*;
import game.audioEngine.AudioFiles;
import game.audioEngine.AudioPlayer;
import game.enums.ITEM_ID;
import game.system.inventory.Inventory;
import game.system.inventory.InventorySlot;
import game.system.inventory.InventorySystem;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.system.main.Helpers;
import game.system.main.Settings;
import game.textures.Textures;

public class Crate extends GameObject {
    private Inventory inv;
    private Random rand = new Random();
    private HealthBar healthBar;

    public Crate(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
        inv = new Inventory(3, 2);
        //fillInventory();
        inv.setXY(300, 100);

        healthBar = new HealthBar(x - 4, y - 8, 0, 10);
    }

    private void fillInventory() {
        for(int i = 0;i < rand.nextInt(3 * 2);i++) {
            boolean again = true;
            Item item;
            if(rand.nextInt(2) == 0) {
                item = new ItemRock(rand.nextInt(InventorySystem.stackSize), ITEM_ID.Rock);
            } else {
                item = new ItemStick(rand.nextInt(InventorySystem.stackSize), ITEM_ID.Stick);
            }
            item.setAmount(rand.nextInt(InventorySystem.stackSize));
            while(again) {
                again = !inv.addItemAtPos(item, rand.nextInt(3*2));
            }

        }
    }

    public void tick() {
        healthBar.tick();
        if(healthBar.dead()) Game.handler.findAndRemoveObject(this);

        Rectangle obj_bounds = this.getSelectBounds();
        Rectangle player_bounds = Game.player.getBounds();
        double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
                * (obj_bounds.getCenterX() - player_bounds.getCenterX())
                + (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
        if (dis > 50) {
            Game.inventorySystem.removeOpenInventory(inv);
        }
    }

    public void render(Graphics g) {
        g.drawImage(Textures.tileSetHouseBlocks.get(6), x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
        
    }

    public Item getItem() {
        return null;
    }

    public void interact() {
        Game.inventorySystem.addOpenInventory(inv);
    }

    public void destroyed() {
        Game.hud.removeHealthBar(healthBar);
        Game.inventorySystem.removeOpenInventory(inv);
        for(InventorySlot slot : inv.getSlots()) {
            if(slot.hasItem()) {
                Item inv_item = slot.getItem();
                ItemGround gnd_item = inv_item.getItemGround();
                gnd_item.setX(x);
                gnd_item.setY(y);
                Game.handler.addObject(gnd_item);
            }
        }
        AudioPlayer.playSound(AudioFiles.crate_destroy, Settings.sound_vol, false, 0);
    }

    public void hit(int damage) {
        AudioPlayer.playSound(AudioFiles.crate_impact, Settings.sound_vol, false, 0);
        healthBar.subtractHealth(damage);
    }

    public void addItemToInv(Item item) {
        this.inv.addItem(item);
    }

}
