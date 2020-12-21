package game.system.systems.gameObject;

public interface Destroyable {
	void destroyed();
	boolean destroyedCalled();
	boolean canRemove();
}
