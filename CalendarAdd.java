
// Program to demonstrate add() method
// of Calendar class

import java.util.*;
public class CalendarAdd {
	public static void main(String[] args)
	{
		// creating calendar object
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -15);
		System.out.println("15 days ago: " + calendar.getTime());
		calendar.add(Calendar.MONTH, 4);
		System.out.println("4 months later: " + calendar.getTime());
		calendar.add(Calendar.YEAR, 2);
		System.out.println("2 years later: " + calendar.getTime());
	}
}
