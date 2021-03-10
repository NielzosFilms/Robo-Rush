package game.system.systems.menu;

import game.assets.entities.player.PLAYER_STAT;
import game.assets.entities.player.Player;
import game.assets.items.item.Item;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.textures.Fonts;
import game.textures.Textures;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class MenuInventory extends Menu {
    private Player player;

    public MenuInventory(MouseInput mouse) {
        super(mouse);
    }

    @Override
    public void tickAbs() {

    }

    @Override
    public void renderBefore(Graphics g, Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g.setColor(Color.decode("#181425"));
        g.fillRect(0, 0, Game.windowSize.x, Game.windowSize.y);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g.drawImage(Textures.inventory_mockup, 0, 0, null);
    }

    @Override
    public void renderAfter(Graphics g, Graphics2D g2d) {
        g.setColor(Color.decode("#fee761"));
        this.player = Game.gameController.getPlayer();
        g.setFont(Fonts.default_fonts.get(3));
        drawPlayerItems(g, 16, 48);
        g.setFont(Fonts.default_fonts.get(4));
        drawPlayerStats(g, 275, 48, 40, 5);
    }

    private void drawPlayerStats(Graphics g, int x, int y, int x_valueOffset, int lineHeight) {
        HashMap<PLAYER_STAT, Float> player_stats = player.getPlayerStats();
        HashMap<PLAYER_STAT, Float> base_stats = player.getBasePlayerStats();
        int index = 1;
        g.drawString("Base", x + x_valueOffset, y);
        for(PLAYER_STAT stat_name : base_stats.keySet()) {
            g.drawString(stat_name.displayName, x, y + lineHeight*index);

            g.drawString(base_stats.get(stat_name).toString(), x + x_valueOffset, y + lineHeight*index);

            g.drawString(">", x + x_valueOffset + 15, y + lineHeight*index);

            g.drawString(player_stats.get(stat_name).toString(), x + x_valueOffset + 20, y + lineHeight*index);
            index++;
        }
    }

    private void drawPlayerItems(Graphics g, int x, int y) {
        for(Item item : player.getItems()) {
            item.drawItemForInventory(g, x, y);
            x += 16;
        }
    }
}
