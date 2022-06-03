
// Java code to illustrate
// set() method

import java.util.*;
public class CalendarSet1 {
	public static void main(String args[])
	{

		// Creating a calendar
		Calendar calndr = Calendar.getInstance();

		// Displaying the month
		System.out.println("The Current Month is: "
						+ calndr.get(
								Calendar.MONTH));

		// Replacing with a new value
		calndr.set(Calendar.MONTH, 11);

		// Displaying the modified result
		System.out.println("Altered Month is: "
						+ calndr.get(
								Calendar.MONTH));
	}
}
