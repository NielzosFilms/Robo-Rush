package game.system.systems.gameObject;

import java.awt.*;

public interface Bounds {
	Rectangle getBounds();

	Rectangle getTopBounds();
	Rectangle getBottomBounds();
	Rectangle getLeftBounds();
	Rectangle getRightBounds();
}
