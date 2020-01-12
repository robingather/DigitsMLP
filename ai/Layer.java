package digits.ai;

/**
 * Holds all nodes and weights for this layer as well as multiple manipulation methods.
 * @author Robin Gather
 *
 */
public class Layer {

	private double[] nodes;
	private double[][] weights;
	private double[][] weightNudges;
	private int nudgeAmount;
	
	public Layer(int length, Layer lastLayer) {
		
		nodes = new double[length];
		if(lastLayer!=null)
			lastLayer.setWeights(new double[lastLayer.getLength()+1][nodes.length]);
		
	}
	
	public double getNode(int index) {
		return nodes[index];
	}
	
	public void setNode(int index, double value) {
		nodes[index] = value;
	}
	
	public int getLength() {
		 return nodes.length;
	}
	
	public double getWeight(int index, int nextIndex) {
		return weights[index][nextIndex];
	}
	
	public void setWeights(double[][] newWeights) {
		this.weights = newWeights;
		this.weightNudges = newWeights;
		nudgeAmount = 0;
	}
	
	public void setWeight(int index, int nextIndex, double value) {
		
		this.weightNudges[index][nextIndex] = value;

	}
	
	public void addWeightNudge(int index, int nextIndex, double value) {
		this.weightNudges[index][nextIndex] += value;
		nudgeAmount++;
	}
	
	/**
	 * Applies the weight nudges to the weight matrix
	 * @return the average change to the weights
	 */
	public double applyNudges() {
		
		if(weights == null)
			return 0;
		
		nudgeAmount /= nodes.length;
		double delta = 0;
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[0].length; j++) {
				weights[i][j] += weightNudges[i][j]/nudgeAmount;
				delta += weightNudges[i][j]/nudgeAmount;
			}
		}
		delta /= weightNudges.length*weightNudges[0].length;
		resetNudges();
		
		return delta;
		
	}
	
	public void resetNudges() {
		
		if(weights==null)
			return;
		
		weightNudges = new double[weights.length][weights[0].length];
		nudgeAmount = 0;
		
	}
	
	public void setRandomWeights(double lowerBound, double upperBound) {
		
		if(weights == null)
			return;
		
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[0].length; j++)
				weights[i][j] = lowerBound+Math.random()*(upperBound-lowerBound);
		}
		
	}
	
}
