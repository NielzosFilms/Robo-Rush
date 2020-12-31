package game.system.systems.gameObject;

import game.system.systems.hitbox.HitboxContainer;

public interface Hitable {
	void hit(HitboxContainer hitboxContainer, int hit_hitbox_index);}
