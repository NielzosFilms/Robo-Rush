package game.system.main;

import game.assets.entities.player.Character_Robot;
import game.assets.entities.player.Player;
import game.assets.levels.def.Level;
import game.assets.levels.level_1.Level_1;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.inputs.KeyInput;
import game.system.inputs.MouseInput;
import game.system.systems.Collision;
import game.system.systems.gameObject.GameObject;
import game.system.systems.hitbox.HitboxSystem;
import game.system.systems.hud.HUD;
import game.system.systems.levelGeneration.LevelGenerator;
import game.system.systems.levelGeneration.Room;
import game.system.systems.lighting.LightingSystem;
import game.system.systems.particles.ParticleSystem;
import game.textures.Textures;

import java.awt.*;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class GameController {
    //    public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
    public static boolean loaded = false;
    private transient Textures textures;
    private transient KeyInput keyInput;
    private Player player;
    private Random r;

    private Handler handler;
    private Camera cam;
    private static Collision collision;
    private static HitboxSystem hitboxSystem;

    private HUD hud;

    //    private InventorySystem inventorySystem;
    private static LightingSystem lightingSystem;
    private static ParticleSystem ps;

    private Level active_level;

    public GameController() {
        handler = new Handler();
        cam = new Camera(0, 0);
//        inventorySystem = new InventorySystem();

        hud = new HUD();
    }

    public void setRequirements(Textures textures, KeyInput keyInput, MouseInput mouseInput) {
        collision = new Collision();
        hitboxSystem = new HitboxSystem();

        ps = new ParticleSystem();
        lightingSystem = new LightingSystem();

        this.textures = textures;
        this.keyInput = keyInput;

        this.keyInput = keyInput;
        keyInput.setRequirements(this);
        mouseInput.setGameController(this);

        handler.setRequirements(this, cam, ps);
        collision.setRequirements(handler, this);
        hitboxSystem.setRequirements(handler);

//        inventorySystem.setRequirements(handler, mouseInput, this, this.player, cam);
        lightingSystem.setRequirements(handler, this, cam);

        hud.setRequirements(handler, this.player, mouseInput, this, cam);
//        handler.addObject(this.player);
    }

    public void tick() {
        if (!loaded) return;
        int camX = (Math.round(-cam.getX() / 16));
        int camY = (Math.round(-cam.getY() / 16));
        int camW = (Math.round(Game.WIDTH / 16));
        int camH = (Math.round(Game.HEIGHT / 16));

        handler.tick();
        ps.tick();

        if(active_level != null)
            active_level.tick();

        runWaterAnimations();
//        player.tick();
//        generateNewChunksOffScreen(camX, camY, camW, camH);
//        tickChunksOnScreen(camX, camY, camW, camH);

        collision.tick();
        hitboxSystem.tick();

//        inventorySystem.tick();
        cam.tick(player);
        hud.tick();
    }

    private void runWaterAnimations() {
        for (int key : Textures.water_red.keySet()) {
            Textures.water_red.get(key).runAnimation();
        }
        for (int key : Textures.generated_animations.keySet()) {
            Textures.generated_animations.get(key).runAnimation();
        }
    }

    public void render(Graphics g, Graphics2D g2d) {
        g2d.translate(cam.getX(), cam.getY()); // start of cam

        handler.render(g, Game.WIDTH, Game.HEIGHT);
        ps.render(g);
        hitboxSystem.render(g);

//        if(active_level != null)
//            active_level.render(g);
//        player.render(g);

//        this.gen.render(g);


        // ongeveer 30-35 ms
        Long start = System.currentTimeMillis();
        //lightingSystem.render(g);
        Long finish = System.currentTimeMillis();
        // System.out.println("Light System Render Time: " + (finish - start));

//        inventorySystem.renderCam(g);
        hud.renderCam(g, g2d);
        g2d.translate(-cam.getX(), -cam.getY()); // end of cam
        hud.render(g, g2d);
//        inventorySystem.render(g);
    }

    public void generate() {
        loaded = false;
        this.player = new Character_Robot(0, 0, 10);
        this.handler.object_entities.clear();
        setRequirements(Game.textures, Game.keyInput, Game.mouseInput);

        Logger.print("World Generating...");

        active_level = new Level_1();
        active_level.generate();//6092317668945018905L);
        Room mainRoom = active_level.getGenerator().getMainRoom();
        this.player.setX((int)mainRoom.rect.getCenterX() * 16);
        this.player.setY((int)mainRoom.rect.getCenterY() * 16);

        loaded = true;
    }

    // ArrayLists are basically faster, because the links between objects aren't there any more
    // Also instead of using ArrayList as parameter use the parent e.g. List
    // var initializes the variable to the correct type
    // also streams are a lot more extensive
    public ArrayList<ArrayList<GameObject>> getAllGameObjects() {
        var ret = new ArrayList<ArrayList<GameObject>>();
        var levelObjects = active_level.getObjects();

        var player_z_index = player.getZIndex();
        for (var i = ret.size(); i <= player_z_index; i++) {
            ret.add(new ArrayList<>());
        }
        if (!ret.get(player_z_index).contains(player))
            ret.get(player_z_index).add(player);

        for(var object : levelObjects) {
//            if(Helpers.isEntityOnScreen(object, this.cam)) {
            if((object.getId() == ID.Static_Tile || object.getId() == ID.Animation_tile || object.getId() == ID.Tile) && !Helpers.isEntityOnScreen(object, this.cam)) continue;

            for (int i = ret.size(); i <= object.getZIndex(); i++) {
                ret.add(new ArrayList<>());
            }
            ret.get(object.getZIndex()).add(object);
//            }
        }

        return ret;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        handler.removeObject(this.player);
        this.player = player;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public Collision getCollision() {
        return collision;
    }

    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    public HitboxSystem getHitboxSystem() {
        return hitboxSystem;
    }

    public void setHitboxSystem(HitboxSystem hitboxSystem) {
        this.hitboxSystem = hitboxSystem;
    }

    public HUD getHud() {
        return hud;
    }

    public void setHud(HUD hud) {
        this.hud = hud;
    }

//    public InventorySystem getInventorySystem() {
//        return inventorySystem;
//    }
//
//    public void setInventorySystem(InventorySystem inventorySystem) {
//        this.inventorySystem = inventorySystem;
//    }

    public LightingSystem getLightingSystem() {
        return lightingSystem;
    }

    public void setLightingSystem(LightingSystem lightingSystem) {
        this.lightingSystem = lightingSystem;
    }

    public ParticleSystem getPs() {
        return ps;
    }

    public void setPs(ParticleSystem ps) {
        this.ps = ps;
    }

    public void updatePlayerPosition(int x, int y) {
        player.setX(x);
        player.setY(y);
        cam.setCoordsWithPlayerCoords(x, y);
    }

    public Level getActiveLevel() {
        return this.active_level;
    }
}
