package game.system.particles;

import game.system.main.GameObject;

import java.awt.*;
import java.util.LinkedList;

public class ParticleSystem {

	public LinkedList<GameObject> particles = new LinkedList<>();
	
	public ParticleSystem() {
		
	}
	
	public void tick() {
		for(int i=0; i<particles.size(); i++) {
			particles.get(i).tick();
		}
	}

	public void render(Graphics g) {
		for(int i=0; i<particles.size(); i++) {
			particles.get(i).render(g);
		}
	}
	
	public void addParticle(GameObject particle) {
		particles.add(particle);
	}
	
	public void removeParticle(GameObject particle) {
		particles.remove(particle);
	}
	
	public LinkedList<GameObject> getParticles() {
		return particles;
	}
	
}
