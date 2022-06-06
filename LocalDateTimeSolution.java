import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

class LocalDateTimeSolution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime ldt1 = LocalDateTime.parse(scanner.next());
        LocalDateTime ldt2 = LocalDateTime.parse(scanner.next());
	// Math.abs() gets rid of the negative sign
        System.out.println(Math.abs(Duration.between(ldt1, ldt2).toHours()));
	scanner.close();
    }
}
