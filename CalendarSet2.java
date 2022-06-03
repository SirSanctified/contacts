
// Java code to illustrate
// set() method

import java.util.*;
public class CalendarSet2 {
	public static void main(String args[])
	{

		// Creating a calendar
		Calendar calndr = Calendar.getInstance();

		// Displaying the Year
		System.out.println("The Current year is: "
						+ calndr.get(
								Calendar.YEAR));

		// Replacing with a new value
		calndr.set(Calendar.YEAR, 1996);

		// Displaying the modified result
		System.out.println("Altered year is: "
						+ calndr.get(
								Calendar.YEAR));
	}
}
