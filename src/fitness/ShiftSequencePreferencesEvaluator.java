package fitness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ga.Constants.Shift;

public class ShiftSequencePreferencesEvaluator {
	private static ShiftSequencePreferencesEvaluator instance = null;

	private List<ShiftSequence> sequences = null;

	public static ShiftSequencePreferencesEvaluator getInstance() {
		if (instance == null) {
			instance = new ShiftSequencePreferencesEvaluator();
		}
		return instance;
	}

	private ShiftSequencePreferencesEvaluator() {
		this.sequences = new ArrayList<>();

		final File file = new File("resources/data/ShiftSequencePreferences.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				this.sequences.add(new ShiftSequence(line));
			}
		} catch (final Exception e) {
			throw new RuntimeException("Unable to read PreferredShifts.txt", e);
		}
	}

	public int evaluate(Shift[] shifts) {
		for (final ShiftSequence group : this.sequences) {
			if (group.equivalent(shifts)) {
				return group.getPreferenceValue();
			}
		}
		return 100;
	}
	public ShiftSequence random(int size) {
		final Map<Integer, List<ShiftSequence>> map = new HashMap<>();
		for (final ShiftSequence sequence : this.sequences) {
			List<ShiftSequence> list = map.get(sequence.length());
			if (list == null) {
				list = new ArrayList<>();
				map.put(sequence.length(), list);
			}
			list.add(sequence);
		}
		final List<ShiftSequence> list = map.get(size);
		if (list == null) {
			throw new RuntimeException("No shift sequence for size: " + size);
		}
		return list.get(new Random().nextInt(list.size()));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final ShiftSequence shiftGroup : this.sequences) {
			sb.append(shiftGroup.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		final ShiftSequencePreferencesEvaluator shifts = new ShiftSequencePreferencesEvaluator();
		System.out.println(shifts.toString());
		Shift[] array = null;
		array = new Shift[] { Shift.M8, Shift.M8, Shift.A8, Shift.A8, Shift.N8 };
		System.out.println(shifts.evaluate(array));
		array = new Shift[] { Shift.M8, Shift.M8, Shift.A8, Shift.N8, Shift.N8 };
		System.out.println(shifts.evaluate(array));
		array = new Shift[] { Shift.M8, Shift.M8, Shift.A8, Shift.N8, Shift.N12 };
		System.out.println(shifts.evaluate(array));
	}
}
