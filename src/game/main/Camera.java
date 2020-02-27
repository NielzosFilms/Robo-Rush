package game.main;

public class Camera {

	public float x, y;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject Player) {
		float xTarg = -Player.getX() + Game.WIDTH/2-16;
		x += (xTarg - x) * (0.1f);
		
		float yTarg = -Player.getY() + Game.HEIGHT/2-16;
		y += (yTarg - y) * (0.1f);
		/*if(Game.survival){
			x = Game.clamp(x, -496, 496);
		}else if(!Game.survival) {
			x = Game.clamp(x, -2480, 2480);
		}*/
		
		//camera is inverted
		/*x = Game.clampFloat(x, -300, 0);
		y = Game.clampFloat(y, -44, 0);*/
	}
	
	public void setX(float x) { 
		this.x = x;
	}
	public void setY(float x) {
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
}
