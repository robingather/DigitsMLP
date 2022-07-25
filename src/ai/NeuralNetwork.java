package digits.ai;

import java.util.List;
import java.util.Scanner;

import digits.Toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class has the properties of the neural network as well as the logic to propagate and backpropagate.
 * @author Robin Gather
 *
 */
public class NeuralNetwork {

	private Layer inputLayer;
	private Layer outputLayer;
	private List<Layer> layers;
	private double bias;
	private double lowerBound, upperBound;
	
	public NeuralNetwork(int nInputs, int nHidden, int nHiddenLength, int nOutputs, double bias, double lowerBound, double upperBound) {
		
		init(nInputs, nHidden, nHiddenLength, nOutputs, bias, lowerBound, upperBound);
		setRandomWeights();
		
	}

	/**
	 * Importation constructor
	 * @param name, filename
	 */
	public NeuralNetwork(String name) {
		
		File file = new File("export/"+name);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int nInputs = Integer.valueOf(reader.readLine());
			int nHidden = Integer.valueOf(reader.readLine());
			int nHiddenLength = Integer.valueOf(reader.readLine());
			int nOutputs = Integer.valueOf(reader.readLine());
			double bias = Double.valueOf(reader.readLine());
			double lowerBound = Double.valueOf(reader.readLine());
			double upperBound = Double.valueOf(reader.readLine());
			init(nInputs, nHidden, nHiddenLength, nOutputs, bias, lowerBound, upperBound);
			Scanner weightScanner = new Scanner(reader.readLine());
			for (int l = 0; l < layers.size()-1; l++) {
				Layer layer = layers.get(l);
				Layer nextLayer = layers.get(l+1);
				for(int i = 0; i < layer.getLength()+1; i++) {
					for(int j = 0; j < nextLayer.getLength(); j++)
						layer.setWeight(i, j, weightScanner.nextDouble());
				}
			}
			weightScanner.close();
			reader.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void init(int nInputs, int nHidden, int nHiddenLength, int nOutputs, double bias, double lowerBound, double upperBound) {
		
		layers = new ArrayList<>();
		inputLayer = new Layer(nInputs, null);
		layers.add(inputLayer);
		layers.add(new Layer(nHiddenLength, inputLayer));
		if(nHidden>1) {
			for(int i = 1; i < nHidden; i++)
				layers.add(new Layer(nHiddenLength, layers.get(i)));
		}
		outputLayer = new Layer(nOutputs, layers.get(layers.size()-1));
		layers.add(outputLayer);
		this.bias = bias;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		
	}
	
	public void export(LearningAgent la) {
		
		try {
			
			File file = new File("export/digitsMLP"+(int)(la.getTestAccuracy()*10000)+".txt");
			file.getParentFile().mkdir();
			//file.createNewFile();
			PrintWriter writer;
			writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
			writer.println(inputLayer.getLength());
			writer.println(layers.size()-2);
			writer.println(layers.get(1).getLength());
			writer.println(outputLayer.getLength());
			writer.println(bias);
			writer.println(lowerBound);
			writer.println(upperBound);
			for (int l = 0; l < layers.size()-1; l++) {
				Layer layer = layers.get(l);
				Layer nextLayer = layers.get(l+1);
				for(int i = 0; i < layer.getLength()+1; i++) {
					for(int j = 0; j < nextLayer.getLength(); j++)
						writer.print(layers.get(l).getWeight(i, j)+" ");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void propagate() {
		
		Layer lastLayer = null;
		for(Layer layer : layers) {
			
			if(layer == inputLayer) {
				lastLayer = layer;
				continue;
			}
			
			for(int n = 0; n < layer.getLength(); n++)  {
				
				double value = bias*lastLayer.getWeight(lastLayer.getLength(), n); //bias
				for(int i = 0; i < lastLayer.getLength(); i++)
					value += lastLayer.getNode(i)*lastLayer.getWeight(i, n);
				value = Toolkit.sigmoid(value);
				layer.setNode(n, value);
				
			}
			
			lastLayer = layer;
			
		}
		
	}
	
	/**
	 * 
	 * @return average change to weights
	 */
	public double applyNudges() {
		
		double delta = 0;
		for(Layer layer : layers) {
			if(layer!=outputLayer)
				delta += layer.applyNudges();
		}
		delta /= layers.size()-1;
		
		return delta;
		
	}
	
	public void resetNudges() {
		
		for(Layer layer : layers)
			layer.resetNudges();
		
	}
	
	public int getFinalAnswer() {
		
		double[] outputs = getOutputs();
		int digit = 0;
		for(int i = 0; i < outputs.length; i++) {
			if(outputs[i] > outputs[digit])
				digit = i;
		}
		return digit;
		
	}
	
	/**
	 * Backpropagation algorithm
	 * @param expectedOutput, correct output
	 * @param learning_rate
	 */
	public void backpropagate(double[] expectedOutput, double learning_rate) {
		
		double[] output = getOutputs();

		Layer nextLayer = null;
		double[] prevD = null;
		for(int l = layers.size()-1; l > 0; l--) {
			
			Layer layer = layers.get(l);
			Layer lastLayer = layers.get(l-1);
			double[] d = new double[layer.getLength()];
			
			for(int i = 0; i < layer.getLength(); i++) {
				
				if(layer == outputLayer) {
					d[i] = -(expectedOutput[i]-output[i]);
				} else {
					d[i] = 0;
					for(int j = 0; j < nextLayer.getLength(); j++)
						d[i] += prevD[j]*layer.getWeight(i, j);
				}
				d[i] *= layer.getNode(i)*(1.0-layer.getNode(i));
				
				for(int j = 0; j < lastLayer.getLength(); j++) {
					double dEtotDIVdW = d[i]*lastLayer.getNode(j); //derivative of error relative to weight
					lastLayer.addWeightNudge(j, i, -learning_rate*dEtotDIVdW);
				}
				
				double biasdEtotDIVdW = d[i]*bias; 
				lastLayer.addWeightNudge(lastLayer.getLength()-1, i, -learning_rate*biasdEtotDIVdW);
				
			}
			
			nextLayer = layer;
			prevD = d;
			
		}
		
	}
	
	public void setInputs(double[] inputs) {
		
		for(int i = 0; i < inputs.length; i++)
			inputLayer.setNode(i, inputs[i]);
		
	}
	
	public void setRandomWeights() {
		
		for(Layer layer : layers)
			layer.setRandomWeights(lowerBound, upperBound);
		
	}
	
	public double[] getOutputs() {
		
		double[] outputs = new double[outputLayer.getLength()];
		for(int i = 0; i < outputs.length; i++)
			outputs[i] = outputLayer.getNode(i);
		return outputs;
		
	}
	
}
