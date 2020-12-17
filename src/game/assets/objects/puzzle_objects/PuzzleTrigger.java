package game.assets.objects.puzzle_objects;

import game.system.systems.gameObject.Interactable;

public interface PuzzleTrigger {
	void setReciever(PuzzleReciever object);
	PuzzleReciever getReciever();

	int getRecieverId();
	void triggered();
}
