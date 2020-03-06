package game.entities.particles;

import java.util.LinkedList;

public class ParticleSystem {

	public LinkedList<Particle> particles = new LinkedList<Particle>();
	
	public ParticleSystem() {
		
	}
	
	public void tick() {
		for(Particle particle : particles) {
			particle.tick();
		}
	}
	
	public void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	public void removeParticle(Particle particle) {
		particles.remove(particle);
	}
	
	public LinkedList<Particle> getParticles() {
		return particles;
	}
	
}
