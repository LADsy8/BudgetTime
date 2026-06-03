package TestSupport;

public final class Assertions {
	private Assertions() {
	}

	public static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}

	public static void assertEquals(Object expected, Object actual, String message) {
		if (expected == null ? actual != null : !expected.equals(actual)) {
			throw new AssertionError(message + " | expected=" + expected + ", actual=" + actual);
		}
	}

	public static void assertDoubleEquals(double expected, double actual, double epsilon, String message) {
		if (Math.abs(expected - actual) > epsilon) {
			throw new AssertionError(message + " | expected=" + expected + ", actual=" + actual);
		}
	}
}
