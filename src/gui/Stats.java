package digits.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import digits.ProgramManager;
import digits.Toolkit;
import digits.ai.LearningAgent;
import digits.framework.ProgramContainer;
import digits.framework.Renderer;
import digits.framework.TextAlign;

/**
 * The panel to the right on the GUI. Displays the output of the NN and its statistics from the LearningAgent
 * @author Robin Gather
 *
 */
public class Stats extends GUIElement{

	private double[] outputs;
	private boolean empty;
	
	private int bigDigit;
	private double confidence;
	
	private double trainError, testError;
	private double trainAccuracy, testAccuracy;
	private int epoch;
	
	private List<Button> buttons;
	
	public Stats(int x, int y) {
		
		super(x,y,ProgramContainer.WIDTH-ProgramContainer.HEIGHT,ProgramContainer.HEIGHT);
		
		int w = width-32;
		int h = 20;
		int offY = ProgramContainer.HEIGHT-32;
		int dY = 26;
		buttons = new ArrayList<Button>();
		buttons.add(new Button("Exit", x+16, offY, w, h)); offY -= dY;
		buttons.add(new Button("Reset NN", x+16, offY, w, h)); offY -= dY;
		buttons.add(new Button("Export NN", x+16, offY, w, h)); offY -= dY;
		buttons.add(new Button("Train NN", x+16, offY, w, h)); offY -= dY;
		buttons.add(new Button("Draw Digit", x+16, offY, w, h)); offY -= dY;
		buttons.add(new Button("Clear Field", x+16, offY, w, h)); offY -= dY;
		
	}
	
	public void update(ProgramManager pm, LearningAgent la, double[] outputs) {
		
		empty = true;
		for(TouchPixel pixel : pm.getPixels()) {
			if(pixel.getAlpha()>0)
				empty = false;
		}
		
		this.outputs = new double[10];
		
		bigDigit = 0;
		for(int i = 0; i < outputs.length; i++) {
			this.outputs[i] = Toolkit.round(outputs[i],2);
			if(outputs[i] > outputs[bigDigit])
				bigDigit = i;
		}
		confidence = Toolkit.round(outputs[bigDigit]*100,1);
		
		if(empty) {
			this.outputs = new double[10];
		}
		
		epoch = la.getEpoch();
		trainError = Toolkit.round(la.getTrainError(),3);
		testError = Toolkit.round(la.getTestError(),3);
		trainAccuracy = Toolkit.round(la.getTrainAccuracy(),3);
		testAccuracy = Toolkit.round(la.getTestAccuracy(),3);
		
		for(Button button : buttons)
			button.update(pm,la);
		
	}
	
	public void render(Renderer r) {
	
		int offY = 120;
		
		r.drawText(""+((empty)?"x":bigDigit), 120, x+width/2, y+offY, Color.black, TextAlign.CENTER); offY += 32;
		r.drawText(((empty)?"xx.x%":confidence+"%"), 24, x+width/2, y+offY, new Color(0,0,0,55+(int)(outputs[bigDigit]*200)), TextAlign.CENTER);
		
		offY = 208;
		int dY = 36;
		
		for(int i = 0; i < 5; i++)
			r.drawText(""+i, 32, x+(width/2-64)+32*(i), y+offY, new Color(0,0,0,55+(int)(outputs[i]*200)), TextAlign.CENTER);
		offY += dY;
		for(int i = 5; i < 10; i++)
			r.drawText(""+i, 32, x+(width/2-64)+32*(i-5), y+offY, new Color(0,0,0,55+(int)(outputs[i]*200)), TextAlign.CENTER);
		
		/*
		for(int i = 1; i < 4; i++)
			r.drawText(""+i, 32, x+(width/2-48)+48*(i-1), y+offY, new Color(0,0,0,55+(int)(outputs[i]*200)), TextAlign.CENTER);
		offY += dY;
		for(int i = 4; i < 7; i++)
			r.drawText(""+i, 32, x+(width/2-48)+48*(i-4), y+offY, new Color(0,0,0,55+(int)(outputs[i]*200)), TextAlign.CENTER);
		offY += dY;
		for(int i = 7; i < 10; i++)
			r.drawText(""+i, 32, x+(width/2-48)+48*(i-7), y+offY, new Color(0,0,0,55+(int)(outputs[i]*200)), TextAlign.CENTER);
		offY += dY;
		r.drawText(""+0, 32, x+(width/2-48)+48*1, y+offY, new Color(0,0,0,55+(int)(outputs[0]*200)), TextAlign.CENTER);
		*/
		
		offY = 286;
		dY = 20;
		int offX = 16;
		
		r.drawText("Epoch", 16, x+offX, y+offY, Color.black, TextAlign.LEFT);
		r.drawText(""+epoch, 16, x+width-offX, y+offY, Color.black, TextAlign.RIGHT); offY += dY;
		r.drawText("Train Error", 16, x+offX, y+offY, Color.black, TextAlign.LEFT);
		r.drawText(""+((trainError!=0)?trainError:"NaN"), 16, x+width-offX, y+offY, Color.black, TextAlign.RIGHT); offY += dY;
		r.drawText("Test Error", 16, x+offX, y+offY, Color.black, TextAlign.LEFT);
		r.drawText(""+((testError!=0)?testError:"NaN"), 16, x+width-offX, y+offY, Color.black, TextAlign.RIGHT); offY += dY;
		r.drawText("Train Accuracy", 16, x+offX, y+offY, Color.black, TextAlign.LEFT);
		r.drawText(""+((trainAccuracy!=0)?trainAccuracy:"NaN"), 16, x+width-offX, y+offY, Color.black, TextAlign.RIGHT); offY += dY;
		r.drawText("Test Accuracy", 16, x+offX, y+offY, Color.black, TextAlign.LEFT);
		r.drawText(""+((testAccuracy!=0)?testAccuracy:"NaN"), 16, x+width-offX, y+offY, Color.black, TextAlign.RIGHT);
		
		for(Button button : buttons)
			button.render(r);
		
	}
	
}
