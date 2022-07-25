package digits.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;

import digits.ProgramManager;
import digits.ai.LearningAgent;
import digits.framework.Renderer;
import digits.framework.TextAlign;

/**
 * Button on the GUI
 * @author Robin Gather
 *
 */
public class Button extends GUIElement {

	private String string;
	private int highlight;
	
	public Button(String string, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.string = string;
	}
	
	public void update(ProgramManager pm, LearningAgent la) {
	
		double mouseX = pm.getInput().getMouseX();
		double mouseY = pm.getInput().getMouseY();
		if(mouseX > x && mouseX < x+width && mouseY > y && mouseY < y+height) {
			if(pm.getInput().isButtonUp(MouseEvent.BUTTON1) && highlight == 2) {
				press(pm,la);
				highlight = 0;
			} else if(pm.getInput().isButtonDown(MouseEvent.BUTTON1))
				highlight = 2;
			else
				highlight = 1;
		} else {
			highlight = 0;
		}
		
	}
	
	public void press(ProgramManager pm, LearningAgent la) {
		
		switch(string) {
		case "Clear Field":
			pm.clearField();
			break;
		case "Draw Digit":
			la.drawTestDigit(pm);
			break;
		case "Train NN":
			pm.setTraining(true);
			break;
		case "Export NN":
			la.export();
			break;
		case "Reset NN":
			la.reset();
			break;
		case "Exit":
			pm.setExit(true);
			break;
		}
		
	}
	
	public void render(Renderer r) {
		
		r.drawRect(x, y, width, height, Color.black);
		r.drawText(string, 18, x+width/2, y+17, Color.black, TextAlign.CENTER);
		if(highlight==1)
			r.fillRect(x+1, y+1, width-1, height-1, new Color(100,100,100,100));
		else if(highlight==2)
			r.fillRect(x+1, y+1, width-1, height-1, new Color(0,0,0,200));
		
	}

	
	
}
