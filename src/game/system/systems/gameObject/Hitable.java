package game.system.systems.gameObject;

import game.system.systems.hitbox.HitboxContainer;

public interface Hitable {
	void hit(int damage, int knockback_angle, float knockback, GameObject hit_by);}
