package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Health;
import game.system.systems.gameObject.Hitable;
import game.system.world.JsonStructureLoader;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONObject;

import java.awt.*;

public class Golem_Stone extends GameObject implements Collision, Hitable, Health {
    private Animation idle = new Animation(200,
            new Texture(TEXTURE_LIST.stone_golem_idle_list, 0, 0),
            new Texture(TEXTURE_LIST.stone_golem_idle_list, 1, 0),
            new Texture(TEXTURE_LIST.stone_golem_idle_list, 2, 0),

            new Texture(TEXTURE_LIST.stone_golem_idle_list, 0, 1),
            new Texture(TEXTURE_LIST.stone_golem_idle_list, 1, 1),
            new Texture(TEXTURE_LIST.stone_golem_idle_list, 2, 1)
            );

    private HealthBar health = new HealthBar(0, 0, 0, 100, 1);
    public Golem_Stone(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    public Golem_Stone(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(json, z_index, division, loader);
    }

    @Override
    public void tick() {
        idle.runAnimation();
        health.setXY(x + 16, y - 8);

        /*if(health.dead()) {
            Game.world.getHandler().findAndRemoveObject(this);
            health.kill();
        }*/

        int player_cenX = Game.world.getPlayer().getX() + 8;
        int player_cenY = Game.world.getPlayer().getY() + (16 + 8) / 2;
        int this_cenY = (int) getBounds().getCenterY();

        if (player_cenY > this_cenY) {
            this.setZIndex(Game.world.getPlayer().getZIndex() - 1);
        } else {
            this.setZIndex(Game.world.getPlayer().getZIndex() + 1);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Textures.entity_shadow, getBounds().x + getBounds().width / 2 - 8, getBounds().y + getBounds().height - 12, 16, 16, null);
        idle.drawAnimation(g, x, y);
        if(Game.DEBUG_MODE) {
            g.setColor(new Color(62, 39, 49));
            Rectangle rect = getBounds();
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 16, y + 16 + 8, 64 - 32, 48 - 16 - 8);
    }

    @Override
    public void hit(int damage) {
        health.subtractHealth(damage);
    }

    @Override
    public int getHealth() {
        return health.getHealth();
    }

    @Override
    public HealthBar getHealthBar() {
        return health;
    }

    @Override
    public boolean dead() {
        return health.dead();
    }
}
