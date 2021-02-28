package game.assets.entities;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.Texture;

import java.awt.*;

public class Effect_Texture extends GameObject {
    private float start_alpha;
    private int lifetime, start_lifetime;
    public Effect_Texture(int x, int y, Texture texture, int lifetime, float start_alpha) {
        super(x, y, 5, ID.effect);
        this.tex = texture;
        this.lifetime = lifetime;
        this.start_alpha = start_alpha;
        this.start_lifetime = lifetime;
    }

    @Override
    public void tick() {
        if(lifetime <= 0) {
            Game.gameController.getPs().removeParticle(this);
        }
        lifetime--;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, start_alpha/start_lifetime*lifetime));
        g.drawImage(tex.getTexure(), x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
