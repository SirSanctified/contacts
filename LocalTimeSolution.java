import java.time.LocalTime;
import java.util.Scanner;

class LocalTimeSolution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
	// Create a LocalTime instance from the time supplied by the user
	// Look up LocalTime.parse() on the standard API documentation
        LocalTime time = LocalTime.parse(scanner.next().substring(0, 5));
        int hour = scanner.nextInt();
        int minute = scanner.nextInt();
        System.out.println(time.minusHours(hour).minusMinutes(minute));
	scanner.close();
    }
}
