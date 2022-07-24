import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class CustomScanner {
    static String getEntry(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message + " > ");
        return scanner.nextLine();
    }
}

abstract class Contact {
    public String number = null;
    private LocalDateTime createdAt = null;
    private LocalDateTime modifiedAt = null;

    protected Contact() {
        var time = LocalDateTime.now().withSecond(0).withNano(0);
        setCreatedAt(time);
        setModifiedAt(time);
    }

    protected Field[] getEditableFields(Object obj) {
        return obj.getClass().getFields();
    }

    abstract String getFullName();

    abstract void edit() throws InvocationTargetException, IllegalAccessException;

    private boolean hasNumber() {
        return this.number.trim().isEmpty();
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNumber() {
        if (this.hasNumber()) {
            return "[no number]";
        }
        return this.number;
    }

    public void setNumber() {
        var phone = CustomScanner.getEntry("Enter the number:");
        Matcher matcher = Pattern.compile(
                        "^\\+?((?=[a-zA-Z\\d])\\w+[\\s-]?(\\(?\\w\\w+\\)?)?|\\(\\w+\\))([\\s-]\\w\\w+)*$"
                )
                .matcher(phone);
        if (!matcher.matches()) {
            System.out.println("Wrong number format!");
            this.number = "";
            return;
        }
        this.number = phone;
    }

    protected Method getMethod(Object obj, String input) {
        return Arrays.stream(obj.getClass().getMethods())
                .filter(method -> method.getName().toLowerCase().equals("set" + input))
                .findFirst()
                .orElse(null);
    }
}

class PhoneBook {
    private final ArrayList<Contact> phoneBook = new ArrayList<>();

    void search() throws InvocationTargetException, IllegalAccessException {
        boolean running = true;
        while (running) {
            String pattern = CustomScanner.getEntry("Enter search query: ");
            ArrayList<Contact> names = new ArrayList<>();
            Matcher matcher = Pattern.compile("\\w+").matcher(pattern.toLowerCase());
            if (matcher.matches()) {
                for (Contact contact : phoneBook) {
                    if (contact.getFullName().toLowerCase().contains(pattern) || contact.getNumber().contains(pattern)) {
                        names.add(contact);
                    }
                }
		
                if (!names.isEmpty()) {
                    System.out.printf("Found %d results:%n", names.size());
                    for (int i = 0; i < names.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, names.get(i).getFullName());
                    }
                }
                var choice = CustomScanner.getEntry("[search] Enter action ([number], back, again): ");
                if ("again".equalsIgnoreCase(choice)) {
                    running = false;
                    search();
                } else if ("back".equalsIgnoreCase(choice)) {
                    running = false;
                } else if (Integer.parseInt(choice) <= names.size() && Integer.parseInt(choice) >= 1) {
                    System.out.println(names.get(Integer.parseInt(choice) - 1));
                    System.out.println();
                    var option = CustomScanner.getEntry("[record] Enter action (edit, delete, menu): ");
                    switch (option.toLowerCase()) {
                        case "edit": {
                            var record = this.phoneBook.indexOf(names.get(Integer.parseInt(choice) - 1));
                            var contact = this.phoneBook.get(record);
                            contact.edit();
                            System.out.println();
                            break;
                        }
                        case "delete": {
                            this.phoneBook.remove(names.get(Integer.parseInt(choice) - 1));
                            System.out.println();
                            break;
                        }
                        case "menu": {
                            running = false;
                            break;
                        }
                        default:
                            System.out.println("Unexpected value: " + option.toLowerCase());
                    }
                } else {
                    running = false;
                }
            }
        }
    }

    void count() {
        System.out.printf("The Phone Book has %d records.%n", this.phoneBook.size());
    }

    void list() throws InvocationTargetException, IllegalAccessException {
        var running = true;
        for (int i = 0; i < this.phoneBook.size(); i++) {
            System.out.printf("%d. %s%n", (i + 1), this.phoneBook.get(i).getFullName());
        }
        System.out.println();
        var choice = CustomScanner.getEntry("[list] Enter action ([number], back):");
        if ("back".equalsIgnoreCase(choice)) {
            return;
        } else if (Integer.parseInt(choice) <= phoneBook.size() && Integer.parseInt(choice) >= 1) {
            System.out.println(phoneBook.get(Integer.parseInt(choice) - 1));
            System.out.println();

            while (running) {
                System.out.println();
                var option = CustomScanner.getEntry("[record] Enter action (edit, delete, menu): ");
                switch (option.toLowerCase()) {
                    case "edit": {
                        var contact = this.phoneBook.get(Integer.parseInt(choice) - 1);
                        contact.edit();
                        System.out.println();
                        break;
                    }
                    case "delete": {
                        this.phoneBook.remove(Integer.parseInt(choice) - 1);
                        System.out.println();
                        break;
                    }
                    case "menu": {
                        running = false;
                        break;
                    }
                    default:
                        System.out.println("Unexpected value: " + option.toLowerCase());
                }
            }
        }
    }


    void add() {


        var type = CustomScanner.getEntry("Enter the type (person, organization):").toLowerCase();
        switch (type) {
            case "person":
                phoneBook.add(new Person());
                break;
            case "organization":
                phoneBook.add((new Organisation()));
                break;
            default:
                System.out.println("No records to edit!");
                return;
        }

        System.out.println("The record added.");
    }

    void edit() throws InvocationTargetException, IllegalAccessException {
        if (this.phoneBook.size() == 0) {
            System.out.println("No records to edit!");
            return;
        }
        this.list();
        var record = (Integer.parseInt(CustomScanner.getEntry("Select a record:")) - 1);
        if (this.isValidIndex(record)) {
            System.out.println("Invalid option!");
            return;
        }
        if (this.phoneBook.get(record) == null) {
            System.out.println("No records to edit!");
            return;
        }

        var contact = this.phoneBook.get(record);
        contact.edit();
    }

    private boolean isValidIndex(int index) {
        return index < 0 || index > this.phoneBook.size() - 1;
    }
}

class Person extends Contact {
    public String name;
    public String surname;
    public Character gender;
    public LocalDate birth;

    public Person() {
        super();
        this.setName();
        this.setSurname();
        this.setBirth();
        this.setGender();
        this.setNumber();
    }

    @Override
    String getFullName() {
        return String.format("%s %s", this.getFirstname(), this.getLastname());
    }

    @Override
    void edit() throws InvocationTargetException, IllegalAccessException {
        var fields = Arrays.stream(this.getEditableFields(this)).map(Field::getName).collect(Collectors.toList());
        var fieldsString = String.join(", ", fields);
        var field = CustomScanner.getEntry("Select a field (" + fieldsString + "):").toLowerCase();

        if (fields.contains(field)) {
            Method method = this.getMethod(this, field);
            method.invoke(this);
            this.setModifiedAt(LocalDateTime.now().withSecond(0).withNano(0));
            System.out.println("Saved");
            System.out.println(this);
            return;
        }
        System.out.println("Wrong field!");
    }

    public String getBirth() {
        if (this.birth == null) {
            return "[no data]";
        }
        return this.birth.toString();
    }

    public void setSurname() {
        var input = CustomScanner.getEntry("Enter the surname:");
        this.surname = this.validateString(input);
    }

    public void setName() {
        var input = CustomScanner.getEntry("Enter the name:");
        this.name = this.validateString(input);
    }

    public void setBirth() {
        var date = CustomScanner.getEntry("Enter the birth date:");
        this.birth = validateBirth(date);
    }

    public String getGender() {
        if (this.gender == null) {
            return "[no data]";
        }
        return this.gender.toString().toUpperCase();
    }

    public void setGender() {
        var choice = CustomScanner.getEntry("Enter the gender (M, F):");
        if (!Objects.equals(choice, "M") && !Objects.equals(choice, "F")) {
            this.gender = null;
            System.out.println("Bad gender!");
        } else {
            this.gender = choice.charAt(0);
        }
    }

    public String getFirstname() {
        return Objects.requireNonNullElse(this.name, "[no data]");
    }

    public String getLastname() {
        return Objects.requireNonNullElse(this.surname, "[no data]");
    }

    private String validateString(String input) {
        if (input.isEmpty() || input.isBlank()) {
            return null;
        }
        return input;
    }

    private LocalDate validateBirth(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException ex) {
            System.out.println("Bad birth date!");
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s%n" +
                        "Surname: %s%n" +
                        "Birth date: %s%n" +
                        "Gender: %s%n" +
                        "Number: %s%n" +
                        "Time created: %s%n" +
                        "Time last edit: %s",
                this.getFirstname(), this.getLastname(), this.getBirth(), this.getGender(), this.getNumber(),
                this.getCreatedAt(), this.getModifiedAt());
    }
}

class Organisation extends Contact {
    public String name;
    public String address;

    public Organisation() {
        super();
        this.setName();
        this.setAddress();
        this.setNumber();
    }

    @Override
    String getFullName() {
        return getName();
    }

    @Override
    void edit() throws InvocationTargetException, IllegalAccessException {
        var fields = Arrays.stream(this.getEditableFields(this)).map(Field::getName).collect(Collectors.toList());
        var fieldsString = String.join(", ", fields);
        var field = CustomScanner.getEntry("Select a field (" + fieldsString + "):").toLowerCase();

        if (fields.contains(field)) {
            Method method = this.getMethod(this, field);
            method.invoke(this);
            this.setModifiedAt(LocalDateTime.now().withSecond(0).withNano(0));
            System.out.println("The record updated!");
            return;
        }
        System.out.println("Wrong field!");
    }

    public String getName() {
        return this.name;
    }

    public void setName() {
        this.name = CustomScanner.getEntry("Enter the organization name:");
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress() {
        this.address = CustomScanner.getEntry("Enter the address:");
    }

    @Override
    public String toString() {
        return String.format("Organization name: %s%n" +
                        "Address: %s%n" +
                        "Number: %s%n" +
                        "Time created: %s%n" +
                        "Time last edit: %s",
                this.getName(), this.getAddress(), this.getNumber(),
                this.getCreatedAt(), this.getModifiedAt());
    }
}

public class ContactsMain {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        var contacts = new PhoneBook();
        var running = true;
        while (running) {
            var choice = CustomScanner
                    .getEntry("[menu] Enter action (add, list, search, count, exit):")
                    .toLowerCase();
            switch (choice) {
                case "exit":
                    running = false;
                    System.out.println();
                    break;
                case "add":
                    contacts.add();
                    System.out.println();
                    break;
                case "count":
                    contacts.count();
                    System.out.println();
                    break;
                case "search":
                    contacts.search();
                    System.out.println();
                    break;
                case "edit":
                    contacts.edit();
                    System.out.println();
                    break;
                case "list":
                    contacts.list();
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid command.");
                    System.out.println();
                    running = false;
                    break;
            }
        }
    }
}
