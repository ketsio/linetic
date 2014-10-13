package ch.linetic.analysis;

import static org.junit.Assert.*;
import org.junit.Test;

public class AnalyzerTest extends AnalyzerAbstractTest {
	
	@Test
	public void testListOfAnalyzersNotEmpty() {
		assertNotEquals(0, analyzers.size(), 0);
	}
	
	@Test
	public void testMovementWithNoPoseShouldReturnZero() {
		for (AnalyzerInterface analyzer : analyzers) {
			assertEquals(0, analyzer.analyze(emptyMovement), 0);
		}
	}
	
	@Test
	public void testAnalyzersRange() {
		int numberOfRandomMovementsTested = 50;
		float result;
		
		for (AnalyzerInterface analyzer : analyzers) {
			for (int i = 0; i < numberOfRandomMovementsTested; i++) {
				result = analyzer.analyze(randomMovement());
				assertTrue(result >= AnalyzerInterface.RANGE_MIN);
				assertTrue(result <= AnalyzerInterface.RANGE_MAX);
			}
		}
	}

}
