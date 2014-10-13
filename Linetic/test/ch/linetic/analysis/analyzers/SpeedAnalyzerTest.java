package ch.linetic.analysis.analyzers;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.linetic.analysis.Analyzer;
import ch.linetic.analysis.AnalyzerAbstractTest;
import ch.linetic.analysis.AnalyzerInterface;

public class SpeedAnalyzerTest extends AnalyzerAbstractTest {
	
	private AnalyzerInterface analyzer = Analyzer.SPEED;

	@Test
	public void testFrozenMovementSpeedIsZero() {
		assertEquals(0, analyzer.analyze(blankMovement(), false), 0);
	}
	
	@Test
	public void testNotFrozenMovementSpeedIsNotZero() {
		assertNotEquals(0, analyzer.analyze(randomValidMovement(), false), 0.0000001);
	}
	
	@Test
	public void testNotFrozenMovementSpeedIsBiggerThanFrozenMovementSpeed() {
		float r1 = analyzer.analyze(blankMovement());
		float r2 = analyzer.analyze(randomValidMovement());
		assertTrue(r1 < r2);
	}
	
	@Test
	public void testVerifySpeedValueForAKnownMovement() {
		assertEquals(Math.sqrt(3.0), analyzer.analyze(movementA(), false), 0.000001);
	}
}
