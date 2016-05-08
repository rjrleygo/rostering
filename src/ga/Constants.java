package ga;

import java.util.Random;

public class Constants {
	public static final int DEFAULT_POPULATION_SIZE = 3;
	public static final int DEFAULT_TOURNAMENT_SIZE = 2;

	public static final double DEFAULT_UNIFORM_RATE = 0.5;
	public static final double DEFAULT_MUTATION_RATE = 0.015;
	public static final boolean ELITISM = true;

	public static final int DEFAULT_FITNESS = Integer.MAX_VALUE;

	public enum Shift {
		UNKNOWN("0", "Unknown"),
		M8 ("1", "Morning 8"),
		A8 ("2", "Afternoon 8"),
		N8 ("3", "Night 8"),
		D12("4", "Day 12"),
		N12("5", "Night 12"),
		SD ("6", "Study Day"),
		AL ("7", "Annual Leave"),
		R  ("8", "Request Day"),
		DO ("9", "Day Off"),
		;

		public static final int CODE_LENGTH = 1;
		private Shift(String code, String desc) {
			this.code = code;
			this.desc = desc;
			if (code.equals(String.valueOf(this.ordinal())) == false) {
				throw new IllegalArgumentException("code " + code + " not equal to ordinal " + this.ordinal());
			}
		}

		private final String desc;
		private final String code;

		public String getCode() {
			return this.code;
		}

		public String getDesc() {
			return this.desc;
		}

		public static Shift getByCode(String code) {
			for (final Shift c : Shift.values()) {
				if (c.getCode().equals(code)) {
					return c;
				}
			}
			return null;
		}

		public static Shift random() {
			// plus 1 to exclude UNKNOWN
			final int index = new Random().nextInt(Shift.values().length - 1) + 1;
			return Shift.values()[index];
		}

		// utility to conver a string to a Shift array
		public static Shift[] toArray(String string) {
			if ((string.length() % Shift.CODE_LENGTH) != 0) {
				throw new RuntimeException("Length is not divisible by " + Shift.CODE_LENGTH);
			}

			final Shift[] array = new Shift[string.length() / Shift.CODE_LENGTH];
			int i = 0;
			int index = 0;
			while (i < string.length()) {
				final String shiftStr = string.substring(i, i + Shift.CODE_LENGTH);
				final Shift shift = Shift.getByCode(shiftStr);
				if (shift == null) {
					throw new RuntimeException("Unknown shift: " + shiftStr);
				}
				array[index++] = shift;
				i += Shift.CODE_LENGTH;
			}
			return array;
		}

	}
}
