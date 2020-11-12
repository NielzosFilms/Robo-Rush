package game.assets.structures;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.enums.STRUCTURE_TYPE;
import game.system.main.GameObject;

public abstract class Structure {

    protected ArrayList<BufferedImage> imageLayers = new ArrayList<BufferedImage>();
    protected ArrayList<GameObject> objects = new ArrayList<GameObject>();
    protected int x, y;
    protected int width = 16, height = 16;
    protected STRUCTURE_TYPE structureType;
    protected GameObject worldStructure;

    public Structure(int x, int y, STRUCTURE_TYPE structureType) {
        this.x = x;
        this.y = y;
        this.structureType = structureType;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract ArrayList<Rectangle> getBounds();

    public abstract ArrayList<Rectangle> getSelectBounds();

    public void setWorldStructure(GameObject obj) {
        this.worldStructure = obj;
    }

    public GameObject getWorldStructure() {
        return this.worldStructure;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public STRUCTURE_TYPE getStructureType() {
        return this.structureType;
    }

    public void addImageLayer(BufferedImage img) {
        this.imageLayers.add(img);
    }

    public ArrayList<BufferedImage> getImageLayers() {
        return this.imageLayers;
    }

    public void addObject(GameObject obj) {
        this.objects.add(obj);
    }

    public ArrayList<GameObject> getObjects() {
        return this.objects;
    }
}
