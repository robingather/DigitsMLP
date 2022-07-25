package digits.ai;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import digits.ProgramManager;
import digits.Toolkit;
import digits.framework.ProgramContainer;
import digits.mnist.MnistDataReader;
import digits.mnist.MnistMatrix;

/**
 * The machine learning component of the program. This contains the logic to process and train the network as well log its performance statistics
 * @author Robin Gather
 *
 */
public class LearningAgent {
	
	private NeuralNetwork net;
	private List<MnistMatrix> trainMatrices, testMatrices;
	private double learningRate;
	private int trainAmount, batchAmount;
	private int testAmount;
	private int epoch;
	
	//stats
	private double trainError, testError;
	private double trainAccuracy, testAccuracy;
	
	public LearningAgent(NeuralNetwork net, double learning_rate, int trainAmount, int batchAmount, int testAmount) {

		this.net = net;
		
		this.learningRate = learning_rate;
		this.trainAmount = trainAmount;
		this.batchAmount = batchAmount;
		this.testAmount = testAmount;
		
		try {
			trainMatrices = Arrays.asList(new MnistDataReader().readData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte"));
			testMatrices = Arrays.asList(new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public double[] process(double[] inputs) {
		
		net.setInputs(inputs);
		net.propagate();
		return net.getOutputs();
		
	}
	
	/**
	 * Training loop
	 * @param pc
	 * @param pm
	 */
	public void train(ProgramContainer pc, ProgramManager pm) {

		boolean exit = false;
		
		while(pm.isTraining()) {

			double cumTrainError = 0;
			double cumTrainAccuracy = 0;
			
			int index = 1;
			double delta = 0;
			
			Collections.shuffle(trainMatrices);
			
			for(MnistMatrix matrix : trainMatrices) {
				
				double[] inputs = new double[28*28];
				for(int r = 0; r < matrix.getNumberOfRows(); r++) {
					for(int c = 0; c < matrix.getNumberOfColumns(); c++) {
						inputs[28*r+c] = matrix.getValue(r, c)/255d;
					}
				}
				net.setInputs(inputs);
				
				net.propagate();
				
				double[] expectedOutput = new double[10];
				double[] output = net.getOutputs();
				double error = 0;
				for(int i = 0; i < expectedOutput.length; i++) {
					expectedOutput[i] = (i==matrix.getLabel())?1.0:0.0;
					error += 0.5*Math.pow((expectedOutput[i]-output[i]),2);
				}
				
				if(net.getFinalAnswer() == matrix.getLabel())
					cumTrainAccuracy++;

				net.backpropagate(expectedOutput, learningRate);
				
				cumTrainError += error;
				
				if(index%batchAmount==0) 
					delta = net.applyNudges();
				
				if(index%(trainAmount/30)==0)
					System.out.print(".");
				
				if(pm.getInput().isKeyDown(KeyEvent.VK_S)) {
					pm.setTraining(false);
					exit = true;
					break;
				}
				
				index++;
				if(index>=trainAmount)
					break;
				
			}
			
			if(!exit) {
				
				epoch++;
				trainError =  cumTrainError/trainAmount;
				trainAccuracy = cumTrainAccuracy/trainAmount;

				test();
				System.out.println("\nTotal Error = "+Toolkit.round(trainError,3)+" / "+Toolkit.round(testError,3));
				System.out.println("Accuracy = "+Toolkit.round(trainAccuracy*100,3)+"% / "+Toolkit.round(testAccuracy*100,3)+"%");
				
			} else {
				
				net.resetNudges();
				System.out.println();
				
			}
			 
		}
		
	}
	
	/**
	 * Runs through testing set and gets its statistics.
	 */
	public void test() {
		
		testError = 0;
		testAccuracy = 0;
		
		int index = 0;
		
		Collections.shuffle(testMatrices);
		
		for(MnistMatrix matrix : testMatrices) {
			
			double[] inputs = new double[28*28];
			for(int r = 0; r < matrix.getNumberOfRows(); r++) {
				for(int c = 0; c < matrix.getNumberOfColumns(); c++)
					inputs[28*r+c] = matrix.getValue(r, c)/255d;
			}
			net.setInputs(inputs);
			
			net.propagate();
			
			double[] expectedOutput = new double[10];
			double[] output = net.getOutputs();
			double error = 0;
			for(int i = 0; i < expectedOutput.length; i++) {
				expectedOutput[i] = (i==matrix.getLabel())?1.0:0.0;
				error += 0.5*Math.pow((expectedOutput[i]-output[i]),2);
			}
			
			if(net.getFinalAnswer() == matrix.getLabel())
				testAccuracy++;
			
			testError += error;

			index++;
			if(index>=testAmount)
				break;
			
		}
		
		testError /= testAmount;
		testAccuracy /= testAmount;
		
	}
	
	public void reset() {
		net.setRandomWeights();
		epoch = 0;
		trainError = 0;
		trainAccuracy = 0;
		testError = 0;
		testAccuracy = 0;
	}
	
	public void drawTestDigit(ProgramManager pm) {
		
		MnistMatrix matrix = testMatrices.get((int) (Math.random()*(testMatrices.size()-1)));
		for(int r = 0; r < matrix.getNumberOfRows(); r++) {
			for(int c = 0; c < matrix.getNumberOfColumns(); c++) {
				pm.getPixels().get(28*r+c).setAlpha(matrix.getValue(r, c));
			}
		}
		
	}
	
	public void export() {
		net.export(this);
	}

	public double getTrainError() {
		return trainError;
	}

	public double getTestError() {
		return testError;
	}

	public double getTrainAccuracy() {
		return trainAccuracy;
	}

	public double getTestAccuracy() {
		return testAccuracy;
	}

	public int getEpoch() {
		return epoch;
	}
	
}
