package game.system.systems.menu;

import game.system.helpers.Helpers;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.systems.menu.buttons.Button;
import game.system.systems.menu.menuAssets.SliderInput;
import game.system.systems.menu.menuAssets.TextField;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class Menu {
    protected MouseInput mouse;

    protected ArrayList<Button> buttons = new ArrayList<>();
    protected ArrayList<TextField> textFields = new ArrayList<>();
    protected ArrayList<SliderInput> sliders = new ArrayList<>();

    public Menu(MouseInput mouse) {
        super();
        this.mouse = mouse;
    }

    public void tick() {
        for(Button btn : buttons) {
            btn.setHover(mouse.mouseOverLocalRect(btn.getBounds()));
        }
        for(TextField textField : textFields) {
            textField.tick();
        }
        for(SliderInput slider : sliders) {
            slider.tick();
        }
        tickAbs();
    }

    public abstract void tickAbs();

    public void render(Graphics g, Graphics2D g2d) {
        renderBefore(g, g2d);
        for(Button btn : buttons) {
            btn.render(g);
        }
        for(TextField textField : textFields) {
            textField.render(g, g2d);
        }
        for(SliderInput slider : sliders) {
            slider.render(g);
        }
        renderAfter(g, g2d);
    }

    protected void renderBgTiles(Graphics g) {
        int right = Helpers.getTileCoords(new Point(Game.getGameSize().x-16, 0), 16, 16).x;
        int bottom = Helpers.getTileCoords(new Point(0, Game.getGameSize().y), 16, 16).y;
        for(int y = 0;y < Game.getGameSize().y;y+=16) {
            for(int x = 0;x < Game.getGameSize().x;x+=16) {
                Texture texture = new Texture(TEXTURE_LIST.grass, 1, 1);
                if(y == 0 && x != 0) {
                    texture = new Texture(TEXTURE_LIST.grass, 1, 7);
                } if(y == 16 && x != 0) {
                    texture = new Texture(TEXTURE_LIST.grass, 1, 8);
                } if(x == 0 && y != 0 && y!= 16) {
                    texture = new Texture(TEXTURE_LIST.grass, 2, 6);
                } if(x == 0 && y == 0) {
                    texture = new Texture(TEXTURE_LIST.grass, 0, 9);
                } if(x == 0 && y == 16) {
                    texture = new Texture(TEXTURE_LIST.grass, 2, 9);
                } if(x == right && y!= 0 && y!=16) {
                    texture = new Texture(TEXTURE_LIST.grass, 0, 6);
                } if(x == right && y == 0) {
                    texture = new Texture(TEXTURE_LIST.grass, 1, 9);
                } if(x == right && y == 16) {
                    texture = new Texture(TEXTURE_LIST.grass, 2, 10);
                } if(y == bottom) {
                    texture = new Texture(TEXTURE_LIST.grass, 1, 5);
                } if(y == bottom && x == 0) {
                    texture = new Texture(TEXTURE_LIST.grass, 3, 10);
                } if(y == bottom && x == right) {
                    texture = new Texture(TEXTURE_LIST.grass, 4, 10);
                }
                g.drawImage(texture.getTexure(), x, y, 16, 16, null);
            }
        }
        g.setColor(new Color(25, 60, 62, 50));
        g.fillRect(0, 0, Game.windowSize.x, Game.windowSize.y);
    }

    public abstract void renderBefore(Graphics g, Graphics2D g2d);
    public abstract void renderAfter(Graphics g, Graphics2D g2d);

    public void mousePressed(MouseEvent e) {
        for(Button btn : buttons) {
            btn.setClick(mouse.mouseOverLocalRect(btn.getBounds()));
        }
        for(SliderInput slider : sliders) {
            if(mouse.mouseOverLocalRect(slider.getBounds())) {
                slider.mousePressed(e);
            }
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
        for(TextField textField : textFields) {
            textField.setFocus(mouse.mouseOverLocalRect(textField.getBounds()));
        }
        for(SliderInput slider : sliders) {
            slider.mouseReleased(e);
        }
    }

	public void keyPressed(KeyEvent e) {
        for(TextField textField : textFields) {
            textField.keyPressed(e);
        }
    }
}
