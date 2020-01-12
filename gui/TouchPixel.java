package digits.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import digits.ProgramManager;
import digits.framework.Renderer;

public class TouchPixel extends GUIElement {

	private int index;
	private int size;
	private double alpha;
	private boolean highlight;
	
	public TouchPixel(int index, int x, int y) {
		
		super(x,y,ProgramManager.PIXELSIZE,ProgramManager.PIXELSIZE);
		this.index = index;
		this.size = width;
		
		alpha = 0;
		highlight = false;
	}
	
	public void update(ProgramManager pm) {
		
		double mouseX = pm.getInput().getMouseX();
		double mouseY = pm.getInput().getMouseY();
		if(mouseX > x && mouseX < x+size && mouseY > y && mouseY < y+size) {
			if(pm.getInput().isButton(MouseEvent.BUTTON1) && alpha < 255)
				draw(pm, 80);
			else if(pm.getInput().isButton(MouseEvent.BUTTON3))
				draw(pm, -80);
			else
				highlight = true;
		} else {
			highlight = false;
		}

	}
	
	private void draw(ProgramManager pm, int alpha) {
		
		this.alpha += alpha;
		
		ArrayList<TouchPixel> pxs = pm.getPixels();
		if(index+1 < pxs.size() && pxs.get(index+1).getX()==x+size)
			pxs.get(index+1).addAlpha(alpha/3);
		if(index-1 >= 0 && pxs.get(index-1).getX()==x-size)
			pxs.get(index-1).addAlpha(alpha/3);
		if(index+28 < pxs.size())
			pxs.get(index+28).addAlpha(alpha/3);
		if(index-28 >= 0)
			pxs.get(index-28).addAlpha(alpha/3);
		if(index+27 < pxs.size() && pxs.get(index+27).getX()==x-size)
			pxs.get(index+27).addAlpha(alpha/8);
		if(index-27 >= 0 && pxs.get(index-27).getX()==x+size)
			pxs.get(index-27).addAlpha(alpha/8);
		if(index+29 < pxs.size() && pxs.get(index+29).getX()==x+size)
			pxs.get(index+29).addAlpha(alpha/8);
		if(index-29 >= 0 && pxs.get(index-29).getX()==x-size)
			pxs.get(index-29).addAlpha(alpha/8);

	}
	
	public void correctAlpha() {
		
		if(alpha>255)
			alpha = 255;
		
	}
	
	public void render(Renderer r) {
		
		r.drawRect(x, y, size, size, Color.black);
		
		if(alpha>255)
			alpha = 255;
		
		if(alpha>0)
			r.fillRect(x, y, size, size, new Color(0,0,0,(int)alpha));
		else {
			if(highlight)
				r.fillRect(x+1, y+1, size-1, size-1, new Color(100,100,100,100));
		}
		
	}
	
	public void addAlpha(double alpha) {
		this.alpha += alpha;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

}
