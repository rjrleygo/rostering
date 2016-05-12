package employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

	private final List<Employee> employees;

	private Staff() {
		this.employees = new ArrayList<>();

		final File file = new File("C:\\Users\\KristineToniMari\\git\\rostering\\resources\\data\\Input.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				this.employees.add(new Employee(line));
			}
		} catch (final Exception e) {
			throw new RuntimeException("Unable to read input: " + file.getAbsolutePath(), e);
		}
		//		this.employees = new Employee[5];
		//		this.employees[0] = new Employee("Employee1", 1);
		//		this.employees[0].setShift(5, Shift.DO);
		//		this.employees[0].setShift(6, Shift.DO);
		//		this.employees[1] = new Employee("Employee2", 1);
		//		this.employees[1].setShift(5, Shift.DO);
		//		this.employees[1].setShift(6, Shift.DO);
		//		this.employees[2] = new Employee("Employee3", 1);
		//		this.employees[2].setShift(5, Shift.SD);
		//		this.employees[2].setShift(6, Shift.DO);
		//		this.employees[3] = new Employee("Employee4", 1);
		//		this.employees[3].setShift(5, Shift.SD);
		//		this.employees[3].setShift(6, Shift.DO);
		//		this.employees[4] = new Employee("Employee5", 1);
		//		this.employees[4].setShift(5, Shift.SD);
		//		this.employees[4].setShift(6, Shift.DO);
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
