package game.system.systems.gameObject;

import java.awt.*;

public interface Interactable {
	Rectangle getSelectBounds();
	void interact();
}
