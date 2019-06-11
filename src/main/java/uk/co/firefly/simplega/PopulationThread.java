package uk.co.firefly.simplega;

public class PopulationThread implements Runnable {

	private static double[] solutionX;
	private static double[] solutionY;
	private Population population;
	private double bestFitness = 0.0f;
	
	public PopulationThread(Population population) {
		this.population = population;
	}

	public void run() {
		population.computeFitness();
		population.modifyFitness();
		population.generateNewPopulation();
		bestFitness = population.preserveTheBest();
	}

	public double getBestFitness() {
		return bestFitness;
	}
	
	public double[] getBestGene() {
		return population.getBestGene();
	}

	public Population getPopulation() {
		return population;
	}
}
