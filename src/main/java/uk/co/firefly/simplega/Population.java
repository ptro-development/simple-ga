package uk.co.firefly.simplega;

import java.lang.Math;

public class Population {

	private int populationSize;
	private double[][] data;
	private double[][] newData;
	private double[] fitness;
	private double bestFitness = 0.0f;
	private int bestFitnessIndex = 0;
	double minFitness = Double.MAX_VALUE;
	private double[] solutionX;
	private double[] solutionY;
	double sumFitness = 0.0;

	public final float CROSS_RATE = 1.0f;
	public final float MUTATION_RATE = 0.05f;

	public Population() {
	}

	public Population(double[] solutionX, double[] solutionY, int populationSize) {
		this.solutionX = solutionX;
		this.solutionY = solutionY;
		this.populationSize = populationSize;
		data = new double[populationSize][9];
		newData = new double[populationSize][9];
		fitness = new double[populationSize];
	}

	public double fitness(double[] gene) {
		double d1, d2, d3, dx, dy, x;
		double quality = 0.0f;
		double sigma = 15.0;
		for (int i = 0; i < solutionX.length; i++) {
			dx = (solutionX[i] - gene[0]);
			dy = (solutionY[i] - gene[1]);
			d1 = Math.abs(Math.sqrt(dx * dx + dy * dy) - gene[2]);
			dx = (solutionX[i] - gene[3]);
			dy = (solutionY[i] - gene[4]);
			d2 = Math.abs(Math.sqrt(dx * dx + dy * dy) - gene[5]);
			dx = (solutionX[i] - gene[6]);
			dy = (solutionY[i] - gene[7]);
			d3 = Math.abs(Math.sqrt(dx * dx + dy * dy) - gene[8]);
			x = Math.min(Math.min(d1, d2), d3);
			quality += Math.exp(-(x * x) / (2.0 * sigma * sigma));
		}
		return quality;
	}

	public void computeFitness() {
		bestFitness = 0.0f;
		bestFitnessIndex = 0;
		minFitness = Double.MAX_VALUE;
		for (int i = 0; i < populationSize; i++) {
			fitness[i] = fitness(data[i]);
			if (fitness[i] > bestFitness) {
				bestFitness = fitness[i];
				bestFitnessIndex = i;
			}
			if (fitness[i] < minFitness)
				minFitness = fitness[i];
		}
	}

	public void initData() {
		for (int i = 0; i < this.populationSize; i++) {
			for (int j = 0; j < 9; j++)
				data[i][j] = 100.0 * Math.random();
		}
	}
	
	public void setBestGene(double[] bestGene) {
		for (int i = 0; i < 9; i++) {
			data[0][i] = bestGene[i];
		}
	}

	public void modifyFitness() {
		sumFitness = 0.0;
		for (int i = 0; i < populationSize; i++) {
			fitness[i] -= minFitness;
			sumFitness += fitness[i];
		}
	}

	public int pickRandomGene() {
		double sum = 0.0;
		double fitnessPoint = Math.random() * sumFitness;
		for (int i = 0; i < populationSize; i++) {
			sum += fitness[i];
			if (sum > fitnessPoint)
				return i;
		}
		return 0;
	}

	public void generateNewPopulation() {
		for (int member = 0; member < populationSize; member += 2) {
			// natural selection
			int memberOne = pickRandomGene();
			int memberTwo = pickRandomGene();
			if (Math.random() < CROSS_RATE) {
				for (int i = 0; i < 9; i++) {
					if (Math.random() < 0.5) {
						newData[member][i] = data[memberOne][i];
						newData[member + 1][i] = data[memberTwo][i];
					} else {
						newData[member][i] = data[memberTwo][i];
						newData[member + 1][i] = data[memberOne][i];
					}
				}
				// mutation
				for (int child = 0; child < 2; child++) {
					for (int i = 0; i < 9; i++) {
						if (Math.random() < MUTATION_RATE) {
							double randomValue = 8.0 * Math.sqrt(-2 * Math.log(Math.random()))
									* Math.sin(2 * Math.PI * Math.random());
							newData[member + child][i] += randomValue;
							if (newData[member + child][i] < 0)
								newData[member + child][i] = 0;
							else if (newData[member + child][i] > 100)
								newData[member + child][i] = 100;
						}
					}
				}
			} else {
				for (int i = 0; i < 9; i++) {
					newData[member][i] = data[memberOne][i];
					newData[member + 1][i] = data[memberTwo][i];
				}
			}
		}
	}

	public double preserveTheBest() {
		for (int i = 0; i < 9; i++) {
			newData[0][i] = data[bestFitnessIndex][i];
		}
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				data[i][j] = newData[i][j];
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
		}
		return bestFitness;
	}

	public int getFirstX() {
		return (int) (data[0][0] - data[0][2]);
	}

	public int getFirstY() {
		return (int) (data[0][1] - data[0][2]);
	}

	public int getFirstRadius() {
		return (int) (data[0][2] * 2);
	}

	public int getSecondX() {
		return (int) (data[0][3] - data[0][5]);
	}

	public int getSecondY() {
		return (int) (data[0][4] - data[0][5]);
	}

	public int getSecondRadius() {
		return (int) (data[0][5] * 2);
	}

	public int getThirdX() {
		return (int) (data[0][6] - data[0][8]);
	}

	public int getThirdY() {
		return (int) (data[0][7] - data[0][8]);
	}

	public int getThirdRadius() {
		return (int) (data[0][8] * 2);
	}

	public double[] getBestGene() {
		return data[bestFitnessIndex];
	}
}