package digits.gui;

public abstract class GUIElement {

	protected int x, y;
	protected int width, height;
	
	public GUIElement(int x, int y, int width, int height) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
