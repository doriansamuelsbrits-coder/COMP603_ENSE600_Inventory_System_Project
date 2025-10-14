package inventory.system;

import java.util.Scanner;
/* 
The Purpose of this Class is the scan and check that the 
user inputs matches the prompts provided by the CLI. the class contains:
1. a Integer Input Check
2. a double/float input Check
3. a security info check for authorised user
*/
public class Filter {
    /*
    this method looks for a value that is an integer if it doesnt 
    meet the integer requirements or the set value requirements 
    such as a value being negative, it will ask for a re-input by the user
    */
    public static int readIntegerInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Value cannot be negative. Try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }
    //similarly for the double method where is requires a double input
    public static double readDoubleInput(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Value cannot be negative. Try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid price.");
            }
        }
    }
    /*
    this method is for the login section, where the user needs to sign 
    in to have access to the Inventory list, it will first require a 
    username and password before access is allowed, failure through 3 
    attempts result in shutdown of the program 
    */
    public static boolean login(Scanner scanner) { 
        final String USERNAME = "admin";
        final String PASSWORD = "ENSE600";
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter username: ");
            String inputUser = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String inputPass = scanner.nextLine().trim();

            if (inputUser.equals(USERNAME) && inputPass.equals(PASSWORD)) {
                System.out.println("Login successful. Welcome " + USERNAME + "!");
                return true;
            } else {
                attempts--;
                System.out.println("Invalid credentials. Attempts left: " + attempts);
            }
        }
        System.out.println("Too many failed attempts. Program exiting...");
        return false;
    }
}
