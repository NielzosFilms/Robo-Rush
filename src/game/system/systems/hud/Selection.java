package game.system.systems.hud;

import game.enums.ID;
import game.system.helpers.Timer;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Selection extends GameObject {
    private Texture
            top_left = new Texture(TEXTURE_LIST.gui_list, 4, 0),
            top_right = new Texture(TEXTURE_LIST.gui_list, 5, 0),
            bot_right = new Texture(TEXTURE_LIST.gui_list, 5, 1),
            bot_left = new Texture(TEXTURE_LIST.gui_list, 4, 1);

    private Timer change_size = new Timer(20);
    private int change_size_offset = 0;

    private int tileSize = 16;

    public Selection() {
        super(0, 0, 0, null);
    }

    @Override
    public void tick() {
        change_size.tick();
        if(change_size.timerOver()) {
            change_size.resetTimer();

            if(change_size_offset == 0) {
                change_size_offset = 1;
            } else {
                change_size_offset = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {

    }

    public void renderSelection(Graphics g, Rectangle obj_select_bounds, int margin) {
        this.x = obj_select_bounds.x - margin + change_size_offset;
        this.y = obj_select_bounds.y - margin + change_size_offset;
        this.width = obj_select_bounds.width + margin*2 - change_size_offset*2;
        this.height = obj_select_bounds.height + margin*2 - change_size_offset*2;

        g.drawImage(top_left.getTexure(), x, y, tileSize, tileSize, null);
        g.drawImage(top_right.getTexure(), x + width - tileSize, y, tileSize, tileSize, null);
        g.drawImage(bot_right.getTexure(), x + width - tileSize, y + height - tileSize, tileSize, tileSize, null);
        g.drawImage(bot_left.getTexure(), x, y + height - tileSize, tileSize, tileSize, null);
    }
}
