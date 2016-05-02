package employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ga.Constants.Shift;

public class Staff {
	private static Staff instance;

	public static Staff getInstance() {
		if (instance == null) {
			instance = new Staff();
		}
		return instance;
	}

	private final Employee[] employees;

	private Staff() {
		this.employees = new Employee[1];
		final Employee employee = new Employee("Employee1", 1);
		employee.setShift(5, Shift.DO);
		employee.setShift(6, Shift.DO);
		this.employees[0] = employee;
	}

	public Shift[] getShiftGuide() {
		final List<Shift> list = new ArrayList<>();
		for (final Employee employee : this.employees) {
			list.addAll(Arrays.asList(employee.getShifts()));
		}
		return list.toArray(new Shift[list.size()]);
	}

	public String getShiftGuideAsString() {
		final StringBuffer guide = new StringBuffer();
		for (final Employee employee : this.employees) {
			guide.append(employee.getShiftsAsString());
		}
		return guide.toString();
	}
}
