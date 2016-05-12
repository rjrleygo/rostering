package employee;

import ga.Constants.Shift;

public class Employee {
	private final String name;
	private final double fte;
	private final Shift[] shifts;

	public Employee(String line) {
		final String[] array = line.split(",");
		this.name = array[0];
		this.fte = Double.valueOf(array[1]);
		this.shifts = new Shift[array.length - 2];
		for (int i = 2; i < array.length; i++) {
			if ((array[i] == null) || "".equals(array[i].trim())) {
				this.shifts[i - 2] = Shift.UNKNOWN;
			} else {
				this.shifts[i - 2] = Shift.valueOf(array[i]);
			}
		}
	}

	public Employee(String name, double fte) {
		this.name = name;
		this.fte = fte;
		this.shifts = new Shift[7];
		for (int i = 0; i < this.shifts.length; i++) {
			this.shifts[i] = Shift.UNKNOWN;
		}
	}

	public String getName() {
		return this.name;
	}

	public double getFte() {
		return this.fte;
	}

	public void setShift(int i, Shift shift) {
		this.shifts[i] = shift;
	}

	public Shift[] getShifts() {
		return this.shifts;
	}

	public String getShiftsAsString() {
		final StringBuffer buf = new StringBuffer();
		for (final Shift shift : this.shifts) {
			buf.append(shift.getCode());
		}
		return buf.toString();
	}
}
