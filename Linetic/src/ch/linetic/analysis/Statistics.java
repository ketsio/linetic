package ch.linetic.analysis;

/**
 * Helper class that enables you to do quick statistics progressively.
 * @author ketsio
 *
 */
public class Statistics {

	private long numSamples = 0;
	private double sampleTotal = 0;
	private double sampleMean = 0;
	private double sampleVar = 0;

	/**
	 * Add a point of data to the statistic
	 * It automatically updates all the statistics available inside the class
	 * @param x the data to be added
	 */
	public void addData(double x) {
		// Increment number of samples obtained.
		numSamples++;

		// Accumulate total for sample mean.
		sampleTotal += x;

		// First, save current mean.
		double oldMean = sampleMean;

		// Now update sample mean.
		sampleMean = sampleTotal / numSamples;

		// Use recurrence relation for variance.
		if (numSamples == 1) {
			sampleVar = 0;
		} else {
			sampleVar = (1 - 1.0 / (double) (numSamples - 1)) * sampleVar
					+ numSamples * (sampleMean - oldMean)
					* (sampleMean - oldMean);
		}
	}
	
	/**
	 * Getter
	 * @return the number of samples of the statistic
	 */
	public long getSize() {
		return numSamples;
	}

	/**
	 * Getter
	 * @return the mean of the statistic
	 */
	public double getMean() {
		return sampleMean;
	}

	/**
	 * Getter
	 * @return the variance of the statistic
	 */
	public double getVariance() {
		return sampleVar;
	}

	/**
	 * Getter
	 * @return the confidence interval of the statistic
	 */
	public double getConfidenceHalfInterval() {
		// mean +- this value, i.e., this is half the interval.
		return 1.96 * Math.sqrt(sampleVar);
	}
	
	/**
	 * Getter
	 * @return the minimum point of the confidence interval
	 */
	public double getMinOfConfidenceInterval() {
		return getMean() - getConfidenceHalfInterval();
	}
	
	/**
	 * Getter
	 * @return the maximum point of the confidence interval
	 */
	public double getMaxOfConfidenceInterval() {
		return getMean() + getConfidenceHalfInterval();
	}

	@Override
	public String toString() {
		if (numSamples == 0)
			return "No samples";
		else
			return getMean() + " +- " + getConfidenceHalfInterval()
					+ "  #samples=" + numSamples;
	}
}
