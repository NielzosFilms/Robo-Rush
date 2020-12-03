package game.system.menu.elements;

import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class LoadingAnimation {
    private boolean loading = false;
    private Animation loading_animation;
    private Animation loading_start_animation;
    private Animation loading_end_animation;
    private boolean running = true;

    private int x, y, w, h;

    public LoadingAnimation(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.loading_animation = new Animation(5,
                new Texture(TEXTURE_LIST.loading_list, 0, 1),
                new Texture(TEXTURE_LIST.loading_list, 1, 1),
                new Texture(TEXTURE_LIST.loading_list, 2, 1),
                new Texture(TEXTURE_LIST.loading_list, 3, 1),
                new Texture(TEXTURE_LIST.loading_list, 4, 1),

                new Texture(TEXTURE_LIST.loading_list, 0, 2),
                new Texture(TEXTURE_LIST.loading_list, 1, 2),
                new Texture(TEXTURE_LIST.loading_list, 2, 2),
                new Texture(TEXTURE_LIST.loading_list, 3, 2),
                new Texture(TEXTURE_LIST.loading_list, 4, 2),

                new Texture(TEXTURE_LIST.loading_list, 0, 3),
                new Texture(TEXTURE_LIST.loading_list, 1, 3),
                new Texture(TEXTURE_LIST.loading_list, 2, 3),
                new Texture(TEXTURE_LIST.loading_list, 3, 3),
                new Texture(TEXTURE_LIST.loading_list, 4, 3),

                new Texture(TEXTURE_LIST.loading_list, 0, 4));

        this.loading_start_animation = new Animation(5,
                new Texture(TEXTURE_LIST.loading_list, 0, 0),
                new Texture(TEXTURE_LIST.loading_list, 1, 0),
                new Texture(TEXTURE_LIST.loading_list, 2, 0),
                new Texture(TEXTURE_LIST.loading_list, 3, 0),
                new Texture(TEXTURE_LIST.loading_list, 4, 0),
                new Texture(TEXTURE_LIST.loading_list, 0, 1),
                new Texture(TEXTURE_LIST.loading_list, 0, 1),
                new Texture(TEXTURE_LIST.loading_list, 0, 1));

        this.loading_end_animation = new Animation(5,
                new Texture(TEXTURE_LIST.loading_list, 0, 1),
                new Texture(TEXTURE_LIST.loading_list, 4, 0),
                new Texture(TEXTURE_LIST.loading_list, 3, 0),
                new Texture(TEXTURE_LIST.loading_list, 2, 0),
                new Texture(TEXTURE_LIST.loading_list, 1, 0));
    }

    public void tick() {
        if(loading || running) {
            if(loading_start_animation.animationEnded()) {
                if(loading_animation.animationEnded() && !loading) {
                    loading_end_animation.runAnimation();
                    if(loading_end_animation.animationEnded()) running = false;
                } else {
                    if(loading_animation.animationEnded()) loading_animation.resetAnimation();
                    loading_animation.runAnimation();
                }
            } else {
                loading_start_animation.runAnimation();
            }
        }
    }

    public void render(Graphics g) {
        if(loading || running) {
            if (loading_start_animation.animationEnded()) {
                if(loading_animation.animationEnded()) {
                    loading_end_animation.drawAnimation(g, x, y, w, h);
                } else {
                    loading_animation.drawAnimation(g, x, y, w, h);
                }
            } else {
                loading_start_animation.drawAnimation(g, x, y, w, h);
            }
        }
    }

    private void resetAnimations() {
        loading_end_animation.resetAnimation();
        loading_animation.resetAnimation();
        loading_start_animation.resetAnimation();
    }

    public void setLoading(boolean loading) {
        if(!running) resetAnimations();
        this.loading = loading;
        this.running = true;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public boolean isRunning() {
        return this.running;
    }
}
