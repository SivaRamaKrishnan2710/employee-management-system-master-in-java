import java.io.*;
import java.util.*;

class Employee implements Serializable {
    int roll, bps, empId, salary, age;
    String name, fname, designation;

    public void getData(Scanner scanner) {
        System.out.print("Enter name: ");
        name = scanner.next();

        System.out.print("Enter father's name: ");
        fname = scanner.next();

        String[] validDesignations = {"Admin", "Tech", "Managerial", "Other"};
        do {
            System.out.print("Enter designation (Admin, Tech, Managerial, Other): ");
            designation = scanner.next();
            if (Arrays.asList(validDesignations).contains(designation)) {
                break;
            } else {
                System.out.println("Invalid designation. Try again.");
            }
        } while (true);

        do {
            System.out.print("Enter employee BPS (1-22): ");
            bps = scanner.nextInt();
        } while (bps < 1 || bps > 22);

        int y = ((bps * 10000) * 45) / 100;
        salary = (bps * 10000) + y;
        System.out.println("Salary: " + salary);

        do {
            System.out.print("Enter employee ID (7777 - 9999): ");
            empId = scanner.nextInt();
        } while (empId < 7777 || empId >= 9999);

        do {
            System.out.print("Enter age (18 - 60): ");
            age = scanner.nextInt();
        } while (age < 18 || age > 60);
    }

    public void showData() {
        System.out.println("Designation: " + designation);
        System.out.println("Name: " + name);
        System.out.println("Father's Name: " + fname);
        System.out.println("BPS: " + bps);
        System.out.println("Age: " + age);
        System.out.println("Employee ID: " + empId);
        System.out.println("---------------------------------");
    }
}

public class EmployeeManagement {
    static final String FILE_NAME = "employees.ser";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Employee> employees = readFromFile();

        char repeat;
        do {
            System.out.println("1. Insert new record\n2. Display records\n3. Search\n4. Delete record\n5. Update designation");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    Employee emp = new Employee();
                    emp.getData(scanner);
                    employees.add(emp);
                    writeToFile(employees);
                }
                case 2 -> employees.forEach(Employee::showData);
                case 3 -> searchEmployee(scanner, employees);
                case 4 -> deleteEmployee(scanner, employees);
                case 5 -> updateEmployee(scanner, employees);
                default -> System.out.println("Invalid choice!");
            }

            System.out.print("Perform another operation? (y/n): ");
            repeat = scanner.next().charAt(0);
        } while (repeat == 'y' || repeat == 'Y');

        scanner.close();
    }

    static void writeToFile(ArrayList<Employee> employees) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Employee> readFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<Employee>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    static void searchEmployee(Scanner scanner, ArrayList<Employee> employees) {
        System.out.println("1. By ID\n2. By Name\n3. By Age\n4. Eldest\n5. Youngest\n6. By BPS\n7. By Designation");
        int option = scanner.nextInt();
        boolean found = false;

        switch (option) {
            case 1 -> {
                System.out.print("Enter ID: ");
                int id = scanner.nextInt();
                for (Employee e : employees) {
                    if (e.empId == id) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 2 -> {
                System.out.print("Enter name: ");
                String name = scanner.next();
                for (Employee e : employees) {
                    if (e.name.equalsIgnoreCase(name)) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 3 -> {
                System.out.print("Enter age: ");
                int age = scanner.nextInt();
                for (Employee e : employees) {
                    if (e.age == age) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 4 -> {
                int maxAge = employees.stream().mapToInt(e -> e.age).max().orElse(-1);
                for (Employee e : employees) {
                    if (e.age == maxAge) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 5 -> {
                int minAge = employees.stream().mapToInt(e -> e.age).min().orElse(-1);
                for (Employee e : employees) {
                    if (e.age == minAge) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 6 -> {
                System.out.print("Enter BPS: ");
                int bps = scanner.nextInt();
                for (Employee e : employees) {
                    if (e.bps == bps) {
                        e.showData();
                        found = true;
                    }
                }
            }
            case 7 -> {
                System.out.print("Enter Designation: ");
                String des = scanner.next();
                for (Employee e : employees) {
                    if (e.designation.equalsIgnoreCase(des)) {
                        e.showData();
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No matching record found.");
        }
    }

    static void deleteEmployee(Scanner scanner, ArrayList<Employee> employees) {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();
        employees.removeIf(emp -> emp.empId == id);
        writeToFile(employees);
        System.out.println("Record deleted (if found).");
    }

    static void updateEmployee(Scanner scanner, ArrayList<Employee> employees) {
        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        for (Employee e : employees) {
            if (e.empId == id) {
                System.out.println("Current designation: " + e.designation);
                String[] validDesignations = {"Admin", "Tech", "Managerial", "Other"};
                do {
                    System.out.print("Enter new designation: ");
                    String des = scanner.next();
                    if (Arrays.asList(validDesignations).contains(des)) {
                        e.designation = des;
                        System.out.println("Updated successfully.");
                        break;
                    } else {
                        System.out.println("Invalid designation.");
                    }
                } while (true);
                break;
            }
        }
        writeToFile(employees);
    }
}
