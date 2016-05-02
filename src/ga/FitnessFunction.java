package ga;

import java.util.ArrayList;
import java.util.List;

import fitness.ShiftSequencePreferencesEvaluator;
import ga.Constants.Shift;

/**
 * The lower the fitness value, the fitter the candidate.
 * @author Kitin
 *
 */
public class FitnessFunction {
	//	private final String hardFitnessGuide;
	private final Shift[] hardFitnessGuideShifts;

	public FitnessFunction(String hardFitnessGuide) {
		//		this.hardFitnessGuide = hardFitnessGuide;
		this.hardFitnessGuideShifts = Shift.toArray(hardFitnessGuide);
	}

	public FitnessFunction(Shift[] hardFitnessGuide) {
		//		this.hardFitnessGuide = hardFitnessGuide;
		this.hardFitnessGuideShifts = hardFitnessGuide;
	}


	//	private int getHardFitness(String candidate) {
	//		return this.getHardFitness(Shift.toArray(candidate));
	//	}

	private int getHardFitness(Shift[] candidate) {
		if (this.hardFitnessGuideShifts.length != candidate.length) {
			throw new RuntimeException("Length of guide and candidate must match");
		}
		int fitness = 0;

		for (int i = 0; i < this.hardFitnessGuideShifts.length; i++) {
			if (candidate[i] == null) {
				throw new RuntimeException("Unknown candidate gene code: " + candidate[i]);
			} else if (this.hardFitnessGuideShifts[i] == Shift.UNKNOWN) {
				// can be anything
			} else if (this.hardFitnessGuideShifts[i] == candidate[i]) {
				// hard condition satisfied
			} else {
				// hard condition not satisfied; fitness
				fitness += 100;
			}
		}
		return fitness;
	}

	// Calculate inidividuals fitness by comparing it to our candidate solution
	public int getFitness(String candidate) {
		return this.getFitness(Shift.toArray(candidate));
	}
	public int getFitness(Shift[] candidate) {
		if (this.hardFitnessGuideShifts.length != candidate.length) {
			throw new RuntimeException("Length of guide and candidate must match");
		}

		int fitness = 0;

		fitness += this.getHardFitness(candidate);

		// evaluate fitness using preferred shift sequences
		List<Shift> list = null;
		for (int i = 0; i < this.hardFitnessGuideShifts.length; i++) {
			if (this.hardFitnessGuideShifts[i] == Shift.UNKNOWN) {
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(candidate[i]);
			} else {
				if (list != null) {
					fitness += ShiftSequencePreferencesEvaluator.getInstance().evaluate(list.toArray(new Shift[list.size()]));
				}
				list = null;
			}
		}
		if (list != null) {
			fitness += ShiftSequencePreferencesEvaluator.getInstance().evaluate(list.toArray(new Shift[list.size()]));
		}

		return fitness;
	}

	// Get acceptable fitness
	public int getAcceptableFitness() {
		return 0;
	}

	public static void main(String[] args) {
		final FitnessFunction function = new FitnessFunction("00000110000011");
		System.out.println(function.getFitness("20001122000112"));
		System.out.println(function.getFitness("00001220000122"));
		System.out.println(function.getFitness("11223111122311"));
		System.out.println(function.getFitness("11233111123311"));
	}
}
