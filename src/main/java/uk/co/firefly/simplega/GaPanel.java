package uk.co.firefly.simplega;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GaPanel extends JPanel {

	private Population population;
	double[] solutionX;
	double[] solutionY;
	
	public void setSolution(double[] solutionX, double[] solutionY) {
		this.solutionX = solutionX;
		this.solutionY = solutionY;
	}

	@Override
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		graphic.setColor(Color.BLACK);
		this.setBackground(Color.WHITE);

		for (int i = 0; i < solutionX.length; i++) {
			graphic.fillOval((int) solutionX[i], (int) solutionY[i], 3, 3);
		}

		graphic.setColor(Color.RED);
		Graphics2D g2D = (Graphics2D) graphic;
		g2D.setStroke(new BasicStroke(2F));
		g2D.drawOval(population.getFirstX(), population.getFirstY(), population.getFirstRadius(),
				population.getFirstRadius());
		g2D.drawOval(population.getSecondX(), population.getSecondY(), population.getSecondRadius(),
				population.getSecondRadius());
		g2D.drawOval(population.getThirdX(), population.getThirdY(), population.getThirdRadius(),
				population.getThirdRadius());
	}

	public void setPopulation(Population population) {
		this.population = population;
	}

}