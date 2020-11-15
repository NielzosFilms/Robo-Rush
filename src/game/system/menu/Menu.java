package game.system.menu;

import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.menu.buttons.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class Menu {
    protected static final int screenWidth = Game.WIDTH, screenHeight = Game.HEIGHT;
    protected MouseInput mouse;

    protected ArrayList<Button> buttons = new ArrayList<>();

    public Menu(MouseInput mouse) {
        super();
        this.mouse = mouse;
    }

    public void tick() {
        for(Button btn : buttons) {
            btn.setHover(mouse.mouseOverLocalRect(btn.getBounds()));
        }
    }

    public void render(Graphics g, Graphics2D g2d) {
        renderBefore(g, g2d);
        for(Button btn : buttons) {
            btn.render(g, g2d);
        }
        renderAfter(g, g2d);
    }

    public abstract void renderBefore(Graphics g, Graphics2D g2d);
    public abstract void renderAfter(Graphics g, Graphics2D g2d);

    public void mousePressed(MouseEvent e) {
        for(Button btn : buttons) {
            btn.setClick(mouse.mouseOverLocalRect(btn.getBounds()));
        }
    }

    public void mouseReleased(MouseEvent e) {
        for(Button btn : buttons) {
            btn.setClick(false);
            if(mouse.mouseOverLocalRect(btn.getBounds())) {
                btn.handleClick(e);
                return;
            }
        }
    }
}
