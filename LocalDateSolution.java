import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Scanner;

class LocalDateSolution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int month = scanner.nextInt();
        int day = scanner.nextInt();
        LocalDate date = LocalDate.of(year, month, day);
	/* What is asked here is to count BACKWARDS from the last day of the
	 * month (last day = 1, second last day = 2, and so on).
	 */
        System.out.println(date.withDayOfMonth(date.lengthOfMonth()));
	scanner.close();
    }
}
