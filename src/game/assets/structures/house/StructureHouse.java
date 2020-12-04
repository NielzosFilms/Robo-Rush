package game.assets.structures.house;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import game.assets.structures.Structure;
import game.enums.STRUCTURE_TYPE;
import game.enums.ID;

public class StructureHouse extends Structure {

    public StructureHouse(int x, int y, STRUCTURE_TYPE structureType) {
        super(x, y, structureType);
        // this.addImageLayer(Textures);
        this.setWorldStructure(new House(x, y, 1, ID.House));
    }

    public void tick() {

    }

    public void render(Graphics g) {

    }

    public ArrayList<Rectangle> getBounds() {
        return null;
    }

    public ArrayList<Rectangle> getSelectBounds() {
        return null;
    }

}
