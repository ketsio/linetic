package ch.linetic.analysis;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class StatisticsTest {
	
	private Statistics stats;
	private Random rand = new Random();

	@Before
	public void init() {
		stats = new Statistics();
	}

	@Test
	public void testInitialValues() {
		assertEquals(0, stats.getMean(), 0);
		assertEquals(0, stats.getVariance(), 0);
	}
	
	@Test
	public void testAddingOneValue() {
		double x = rand.nextDouble();
		stats.addData(x);
		assertEquals(x, stats.getMean(), 0);
		assertEquals(0, stats.getVariance(), 0);
	}
	
	@Test
	public void testArrayOfDate() {
		int size = rand.nextInt(50) + 10;
		double[] data = new double[size];
		
		// Mean
		double sum = 0.0;
		for (int i = 0; i < size; i++) {
			double x = rand.nextDouble();
			data[i] = x;
			stats.addData(x);
			sum += x;
		}
		double mean = sum / (double) size;
		
		// Variance
		sum = 0.0;
		for (int i = 0; i < data.length; i++) {
			double x = data[i] - mean;
			sum += x*x;
		}
		double var = sum / (double) size;
		
		assertEquals(mean, stats.getMean(), 0);
		assertEquals(var, stats.getVariance(), 0.05);
	}
	
	@Test
	public void testConfidanceIntervalle() {
		int size = 1000;
		for (int i = 0; i < size; i++) {
			stats.addData(rand.nextInt(300));
		}
		// TODO : try with kinect
		//System.out.println(stats);
	}

}
