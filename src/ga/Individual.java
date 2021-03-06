package ga;

import java.util.ArrayList;
import java.util.List;

import employee.Staff;
import fitness.ShiftSequencePreferencesEvaluator;
import ga.Constants.Shift;

public class Individual {
	public static class Factory {
		//		public static Individual generate(Shift[] guide) {
		//			final List<Shift> list = new ArrayList<>();
		//			// build genes of individual based on the guide
		//			for (int i = 0; i < guide.length; i++) {
		//				if (Shift.UNKNOWN.equals(guide[i])) {
		//					list.add(Shift.random());
		//				} else {
		//					list.add(guide[i]);
		//				}
		//			}
		//
		//			final Individual individual = new Individual(guide.length);
		//			list.toArray(individual.shifts);
		//			return individual;
		//		}
		public static Individual generate(Shift[] guide) {
			final List<Shift> list = new ArrayList<>();
			int counter = 0;
			// build genes of individual based on the guide
			for (int i = 0; i < guide.length; i++) {
				if (Shift.UNKNOWN.equals(guide[i])) {
					counter++;
				} else {
					if (counter > 0) {
						list.addAll(ShiftSequencePreferencesEvaluator.getInstance().random(counter).getShifts());
					}
					counter = 0;
					list.add(guide[i]);
				}
			}
			// in case last element in guide is NOT a hard condition (UNKNOWN)
			if (counter > 0) {
				list.addAll(ShiftSequencePreferencesEvaluator.getInstance().random(counter).getShifts());
				//counter = 0;
			}

			final Individual individual = new Individual(guide.length);
			list.toArray(individual.shifts);
			return individual;
		}
	}

	private final Shift[] shifts;

	private Individual(int size) {
		this.shifts = new Shift[size];
	}

	private int fitness = Constants.DEFAULT_FITNESS;


	public Shift[] getShifts() {
		return this.shifts;
	}

	public Shift getShift(int index) {
		return this.shifts[index];
	}

	public void setShift(int index, Shift value) {
		this.shifts[index] = value;
		this.fitness = Constants.DEFAULT_FITNESS;
	}

	/* Public methods */
	public int size() {
		return this.shifts.length;
	}

	public int getFitness() {
		if (this.fitness == Constants.DEFAULT_FITNESS) {
			final FitnessFunction fitnessFunction = new FitnessFunction(Staff.getInstance().getShiftGuide());
			this.fitness = fitnessFunction.getFitness(this.getShifts());
		}
		return this.fitness;
	}

	// mutate based on guide
	public void mutate(Shift[] guide) {

		final List<Shift> list = new ArrayList<>();
		List<Shift> temp = null;
		for (int i = 0; i < guide.length; i++) {
			if (Shift.UNKNOWN.equals(guide[i])) {
				// guide is unknown, store current shift in temp
				if (temp == null) {
					temp = new ArrayList<>();
				}
				temp.add(this.shifts[i]);
			} else {
				// guide is a hard condition
				// if temp is NOT null, then element was preceded by UNKNOWNs
				// therefore, either store temp or mutate, depending on mutation rate
				if (temp != null) {
					if (Math.random() <= Constants.DEFAULT_MUTATION_RATE) {
						list.addAll(ShiftSequencePreferencesEvaluator.getInstance().random(temp.size()).getShifts());
					} else {
						list.addAll(temp);
					}
				}
				temp = null;
				list.add(guide[i]);
			}
		}
		// in case last element in guide is NOT a hard condition
		if (temp != null) {
			if (Math.random() <= Constants.DEFAULT_MUTATION_RATE) {
				list.addAll(ShiftSequencePreferencesEvaluator.getInstance().random(temp.size()).getShifts());
			} else {
				list.addAll(temp);
			}
		}

		list.toArray(this.shifts);
	}

	public void mutate() {
		// Loop through genes
		for (int i = 0; i < this.size(); i++) {
			if (Math.random() <= Constants.DEFAULT_MUTATION_RATE) {
				// Create random gene
				this.setShift(i, Shift.random());
			}
		}
	}

	@Override
	public String toString() {
		String geneString = "";
		for (final Shift shift : this.shifts) {
			geneString += shift.getCode();
		}
		return geneString;
	}
}
