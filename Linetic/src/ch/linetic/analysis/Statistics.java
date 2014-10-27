package ch.linetic.analysis;

public class Statistics {

	private long numSamples = 0;
	private double sampleTotal = 0;
	private double sampleMean = 0;
	private double sampleVar = 0;

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
	
	public long getSize() {
		return numSamples;
	}

	public double getMean() {
		return sampleMean;
	}

	public double getVariance() {
		return sampleVar;
	}

	public double getConfidenceHalfInterval() {
		// mean +- this value, i.e., this is half the interval.
		return 1.96 * Math.sqrt(sampleVar);
	}
	
	public double getMinOfConfidenceInterval() {
		return getMean() - getConfidenceHalfInterval();
	}
	
	public double getMaxOfConfidenceInterval() {
		return getMean() + getConfidenceHalfInterval();
	}

	public String toString() {
		if (numSamples == 0)
			return "No samples";
		else
			return getMean() + " +- " + getConfidenceHalfInterval()
					+ "  #samples=" + numSamples;
	}
}
