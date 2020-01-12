package digits;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import digits.ai.LearningAgent;
import digits.ai.NeuralNetwork;
import digits.framework.Input;
import digits.framework.ProgramContainer;
import digits.framework.Renderer;
import digits.framework.TextAlign;
import digits.gui.Stats;
import digits.gui.TouchPixel;

/**
 * Main program class. This represents the program. It is built upon the ProgramContainer and within it holds all the entities and logic specific to this program.
 * @author Robin Gather
 *
 */
public class ProgramManager {

	public static int PIXELSIZE = 20;
	private Input input;
	private ArrayList<TouchPixel> pixels;
	private LearningAgent la;
	private Stats stats;
	private boolean training;
	private boolean exit;
	
	public ProgramManager() {
	
	}
	
	public void init(ProgramContainer pc) {
		
		input = new Input(pc);
		
		pixels = new ArrayList<>();
		for(int i = 0; i < 28; i++) {
			for(int j = 0; j < 28; j++) {
				pixels.add(new TouchPixel(i*28+j, j*PIXELSIZE,i*PIXELSIZE));
			}
		}
		
		createAI();
		
		stats = new Stats(560,0);
		
		training = false;
		
	}
	
	/**
	 * Creates the NeuralNetwork and LearningAgent. Here you can specify the different variables or specify a file to import the NN from.
	 */
	private void createAI() {
		
		double weightsLowerBound = -1.0, weightsUpperBound = 1.0;
		int hiddenLayersAmount = 2;
		int hiddenLayersLength = 24;
		double bias = 5;
		NeuralNetwork nn = new NeuralNetwork(784, hiddenLayersAmount, hiddenLayersLength, 10, bias, weightsLowerBound, weightsUpperBound);
		//NeuralNetwork nn = new NeuralNetwork("digitsMLP9213.txt");
		
		double learningRate = 1;
		int trainingSampleAmount = 60000;
		int batchAmount = 100;
		int testAmount = 10000;
		la = new LearningAgent(nn, learningRate, trainingSampleAmount, batchAmount, testAmount);
		
	}
	
	public void update(ProgramContainer pc) {
		
		if(!training) {
			
			for(TouchPixel pixel : pixels)
				pixel.update(this);
			
			double[] userInput = new double[28*28];
			for(int i = 0; i < pixels.size(); i++)
				userInput[i] = pixels.get(i).getAlpha()/255d;
			double[] outputs = la.process(userInput);
			stats.update(this, la, outputs);
			
		} else {
			
			System.out.println("TRAINING");
			la.train(pc, this);
			
		}
		
		if(input.isKeyDown(KeyEvent.VK_T))
			training = true;
		
		if(input.isKeyDown(KeyEvent.VK_R))
			clearField();
			
		if(training) {
			clearField();
			stats.update(this, la, new double[] {0,0,0,0,0,0,0,0,0,0});
		}
		
		input.update(pc);
		
	}
	
	public void clearField() {
		
		for(TouchPixel pixel : pixels)
			pixel.setAlpha(0);
		
	}
	
	public void render(Renderer r) {
		
		for(TouchPixel pixel : pixels)
			pixel.render(r);
		
		stats.render(r);
		
		if(training) {
			r.fillRect(0, 0, ProgramContainer.HEIGHT, ProgramContainer.HEIGHT, Color.black);
			r.drawText("Training", 64, ProgramContainer.HEIGHT/2, ProgramContainer.HEIGHT/2, Color.white, TextAlign.CENTER);
			r.drawText("press 's' to stop", 16, ProgramContainer.HEIGHT/2, ProgramContainer.HEIGHT/2+24, Color.lightGray, TextAlign.CENTER);
		}
		
	}
	
	public Input getInput() {
		return input;
	}

	public ArrayList<TouchPixel> getPixels() {
		return pixels;
	}

	public boolean isTraining() {
		return training;
	}

	public void setTraining(boolean training) {
		this.training = training;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public boolean isExit() {
		return exit;
	}
	
}
