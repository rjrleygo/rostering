package ga;

import java.util.ArrayList;
import java.util.List;

import employee.Staff;
import ga.Constants.Shift;

public class Population {

	private final Individual[] individuals;
	private final Staff staff;
	private final Shift[] shiftGuide;

	/*
	 * Constructors
	 */
	public Population(int size, Staff staff) {
		// this(size);
		this.individuals = new Individual[size];
		this.staff = staff;
		this.shiftGuide = staff.getShiftGuide();

		// Initialise population
		// Loop and create individuals
		for (int i = 0; i < this.size(); i++) {
			final Individual individual = Individual.Factory.generate(this.shiftGuide);
			this.save(i, individual);
		}
	}

	/* Getters */
	public Individual getIndividual(int index) {
		return this.individuals[index];
	}

	public Individual getFittest() {
		Individual fittest = this.individuals[0];
		// Loop through individuals to find fittest
		for (final Individual individual : this.individuals) {
			if (fittest.getFitness() >= individual.getFitness()) {
				fittest = individual;
			}
		}
		return fittest;
	}

	/* Public methods */
	public Population evolve() {
		final Population newPopulation = new Population(this.size(), this.staff);

		// Keep our best individual
		if (Constants.ELITISM) {
			newPopulation.save(0, this.getFittest());
		}

		// Crossover population
		int elitismOffset;
		if (Constants.ELITISM) {
			elitismOffset = 1;
		} else {
			elitismOffset = 0;
		}
		// Loop over the population size and create new individual with
		// crossover
		for (int i = elitismOffset; i < this.size(); i++) {
			final Individual fittest1 = this.runTournament();
			final Individual fittest2 = this.runTournament();
			final Individual newIndividual = this.crossover(fittest1, fittest2);
			newPopulation.save(i, newIndividual);
		}

		// Mutate population
		for (int i = elitismOffset; i < newPopulation.size(); i++) {
			newPopulation.getIndividual(i).mutate(this.shiftGuide);
		}

		return newPopulation;
	}

	// Crossover individuals
	public Individual crossover(Individual individual1, Individual individual2) {
		final Individual newIndividual = Individual.Factory.generate(this.shiftGuide);

		final List<Shift> list = new ArrayList<>();
		List<Shift> temp1 = null;
		List<Shift> temp2 = null;
		for (int i = 0; i < this.shiftGuide.length; i++) {
			if (Shift.UNKNOWN.equals(this.shiftGuide[i])) {
				// guide is unknown, store current shift in temp
				if ((temp1 == null) && (temp2 == null)) {
					temp1 = new ArrayList<>();
					temp2 = new ArrayList<>();
				}
				temp1.add(individual1.getShift(i));
				temp2.add(individual2.getShift(i));
			} else {
				// guide is a hard condition
				// if temp is NOT null, then element was preceded by UNKNOWNs
				// therefore, either store temp or mutate, depending on mutation rate
				if ((temp1 != null) && (temp2 != null)) {
					if (Math.random() <= Constants.DEFAULT_UNIFORM_RATE) {
						list.addAll(temp1);
					} else {
						list.addAll(temp2);
					}
				}
				temp1 = null;
				temp2 = null;
				list.add(this.shiftGuide[i]);
			}
		}
		// in case last element in guide is NOT a hard condition
		if ((temp1 != null) && (temp2 != null)) {
			if (Math.random() <= Constants.DEFAULT_UNIFORM_RATE) {
				list.addAll(temp1);
			} else {
				list.addAll(temp2);
			}
		}

		list.toArray(newIndividual.getShifts());
		return newIndividual;
	}

	// Crossover individuals
	//	protected Individual crossover(Individual individual1, Individual individual2) {
	//		final Individual newIndividual = Individual.Factory.generate(this.shiftGuide);
	//		// Loop through genes
	//		for (int i = 0; i < individual1.size(); i++) {
	//			// Crossover
	//			if (Math.random() <= Constants.DEFAULT_UNIFORM_RATE) {
	//				newIndividual.setShift(i, individual1.getShift(i));
	//			} else {
	//				newIndividual.setShift(i, individual2.getShift(i));
	//			}
	//		}
	//		return newIndividual;
	//	}

	// Select individuals for crossover
	protected Individual runTournament() {
		// Create a tournament population
		final Population tournament = new Population(Constants.DEFAULT_TOURNAMENT_SIZE, this.staff);
		// For each place in the tournament get a random individual
		for (int i = 0; i < Constants.DEFAULT_TOURNAMENT_SIZE; i++) {
			final int randomID = (int) (Math.random() * this.size());
			tournament.save(i, this.getIndividual(randomID));
		}
		// Get the fittest
		final Individual fittest = tournament.getFittest();
		return fittest;
	}

	public int size() {
		return this.individuals.length;
	}

	public void save(int index, Individual individual) {
		this.individuals[index] = individual;
	}
}
