package fitness;

import java.util.ArrayList;
import java.util.List;

import ga.Constants.Shift;

public class ShiftSequence {
	private final int preferenceValue;
	private final List<Shift> shifts;

	public ShiftSequence(String line) {
		final String array[] = line.split(":");
		if (array.length != 2) {
			throw new RuntimeException("Expected format is [preference]:[shift,[shift ...]]. Line = " + line);
		}
		this.preferenceValue = Integer.valueOf(array[0]);
		this.shifts = new ArrayList<>();
		for (final String elem : array[1].split(",")) {
			this.shifts.add(Shift.valueOf(elem));
		}
	}

	public int getPreferenceValue() {
		return this.preferenceValue;
	}

	public boolean equivalent(Shift[] array) {
		if (this.shifts.size() != array.length) {
			return false;
		}
		for (int i = 0; i < this.shifts.size(); i++) {
			if (this.shifts.get(i) != array[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Preference Value = " + this.preferenceValue).append(" : ");
		for (final Shift shift : this.shifts) {
			sb.append(shift).append(",");
		}
		return sb.toString();
	}
}
