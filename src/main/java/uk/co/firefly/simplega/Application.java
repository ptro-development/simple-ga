package uk.co.firefly.simplega;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import uk.co.firefly.simplega.GaPanel;

public class Application {

	private static GaPanel panel;
	private static JFrame frame;
	private static final int GENERATIONS_COUNT = 1000;
	private static final int THREAD_COUNT = 4;
	private static final int POPULATION_SIZE = 100;
	private static Population[] populations;
	
	// X,Y data points of circles we are looking for by GA ...
	private static final double[] solutionX = { 127, 41, 54, 76, 82, 125, 30, 128, 68, 34, 29, 95, 30, 34, 128, 93, 45,
			29, 105, 40, 39, 106, 55, 120, 117, 129, 128, 92, 125, 125, 69, 117, 32, 125, 91, 116, 108, 127, 58, 88,
			119, 51, 123, 126, 100, 60, 29, 37, 121, 45, 77, 96, 30, 119, 111, 30, 112, 129, 109, 108, 84, 130, 54, 127,
			65, 47, 30, 79, 121, 92, 101, 92, 111, 45, 129, 128, 107, 52, 116, 54, 84, 109, 108, 48, 39, 42, 48, 117,
			44, 31, 29, 128, 33, 86, 129, 110, 118, 101, 129, 63, 69, 82, 88, 80, 133, 129, 103, 70, 121, 129, 70, 92,
			78, 118, 133, 115, 97, 89, 122, 134, 86, 119, 71, 93, 125, 135, 107, 75, 127, 79, 135, 108, 70, 68, 103, 73,
			70, 83, 95, 135, 100, 73, 134, 125, 77, 72, 93, 122, 133, 131, 88, 121, 120, 136, 71, 77, 131, 95, 119, 130,
			95, 111, 75, 96, 135, 136, 115, 113, 76, 79, 129, 133, 74, 120, 123, 87, 126, 82, 135, 74, 119, 81, 131,
			125, 76, 124, 132, 71, 92, 122, 136, 132, 129, 120, 113, 136, 136, 70, 69, 99, 31, 82, 153, 125, 23, 47,
			131, 75, 25, 149, 20, 21, 20, 156, 27, 60, 45, 32, 141, 78, 26, 123, 47, 104, 157, 77, 148, 21, 139, 32, 65,
			88, 37, 30, 127, 27, 35, 149, 134, 113, 98, 51, 127, 143, 84, 46, 106, 21, 106, 156, 137, 130, 156, 21, 116,
			64, 25, 65, 126, 26, 27, 89, 122, 21, 84, 122, 19, 39, 157, 29, 139, 80, 142, 140, 143, 64, 22, 152, 158,
			74, 151, 156, 119, 152, 82, 24, 20, 138, 49, 134, 47, 122, 27, 128, 151, 114, 135, 35, 120, 96 };

	private static final double[] solutionY = { 89, 67, 141, 50, 149, 81, 92, 110, 149, 120, 104, 146, 89, 77, 90, 148,
			63, 105, 142, 69, 69, 58, 144, 72, 66, 103, 107, 51, 80, 78, 50, 131, 85, 119, 149, 133, 59, 114, 54, 50,
			128, 140, 123, 119, 145, 146, 97, 73, 127, 63, 51, 51, 105, 131, 138, 88, 61, 97, 59, 137, 150, 95, 57, 87,
			54, 62, 92, 150, 128, 148, 54, 51, 138, 135, 109, 92, 141, 142, 134, 142, 50, 60, 58, 61, 128, 133, 62, 67,
			134, 84, 97, 110, 119, 148, 93, 61, 69, 55, 94, 52, 65, 35, 31, 87, 74, 81, 95, 52, 90, 83, 72, 29, 38, 32,
			48, 93, 95, 92, 88, 51, 32, 91, 50, 30, 87, 59, 95, 82, 85, 39, 60, 29, 55, 59, 28, 45, 70, 34, 29, 58, 27,
			45, 53, 38, 84, 47, 95, 89, 76, 45, 92, 89, 33, 57, 51, 39, 43, 94, 89, 80, 29, 93, 42, 95, 60, 62, 31, 94,
			83, 87, 41, 75, 42, 89, 89, 32, 38, 35, 68, 43, 90, 88, 79, 36, 83, 87, 46, 77, 30, 35, 61, 46, 81, 90, 94,
			65, 60, 50, 61, 28, 39, 146, 54, 136, 59, 131, 24, 10, 106, 46, 64, 59, 88, 66, 49, 16, 130, 39, 122, 145,
			47, 136, 134, 144, 68, 144, 112, 75, 30, 38, 143, 145, 121, 42, 133, 107, 35, 46, 26, 12, 145, 135, 133, 38,
			146, 130, 143, 68, 13, 86, 30, 132, 85, 72, 16, 14, 103, 13, 22, 104, 50, 9, 137, 84, 9, 138, 77, 125, 75,
			44, 34, 146, 34, 123, 36, 142, 91, 53, 74, 11, 51, 91, 138, 50, 145, 52, 86, 30, 133, 28, 131, 16, 47, 22,
			105, 141, 28, 119, 17, 10 };
	
	public static void main(String[] args) {
		double bestFitness = 0.0f;
		double[] bestGene = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
		Population bestPopulation = new Population(solutionX, solutionY, POPULATION_SIZE);

		initGraphic(bestPopulation);
		initPopulations();
		
		for (int g = 0; g < GENERATIONS_COUNT; g++) {
			
			// populate threads
			ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
			PopulationThread[] threads = new PopulationThread[THREAD_COUNT];
			for (int i = 0; i < THREAD_COUNT; i++) {
				populations[i].setBestGene(bestGene);
				threads[i] = new PopulationThread(populations[i]);
				service.submit(threads[i]);
			}
			service.shutdown();
			
			// collect results
			try {
				service.awaitTermination(1, TimeUnit.HOURS);
				for (PopulationThread thread : threads) {
					if (thread.getBestFitness() > bestFitness) {
						bestFitness = thread.getBestFitness();
						bestGene = thread.getBestGene();
						bestPopulation = thread.getPopulation();
					}
				}
				
				// show results
				System.out.println("Generation " + g + ", Fitness: " + bestFitness);
				panel.setPopulation(bestPopulation);
				panel.repaint();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void initGraphic(Population population) {
		panel = new GaPanel();
		panel.setSolution(solutionX, solutionY);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setSize(600, 400);
		frame.setVisible(true);
		panel.setPopulation(population);
	}

	public static void initPopulations() {
		populations = new Population[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			populations[i] =  new Population(solutionX, solutionY, POPULATION_SIZE);
			populations[i].initData();
		}
	}
}
