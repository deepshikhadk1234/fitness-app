package com.fitnessbooking;

import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;

import com.fitnessbooking.models.Admin;
import com.fitnessbooking.models.FitnessClass;
import com.fitnessbooking.models.User;
import com.fitnessbooking.data.DataStore;
import com.fitnessbooking.exceptions.CustomException;
import com.fitnessbooking.utils.Validators;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DataStore dataStore = DataStore.getInstance();
    private static boolean isRunning = true;

    public static void main(String[] args) {
        try {
            System.out.println("Welcome to the Fitness Class Booking System!");

            while (isRunning) {
                System.out.println("\nPlease select an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loginUser();
                        break;
                    case 3:
                        System.out.println("Thank you for using the system. Goodbye!");
                        scanner.close();
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void registerUser() {
        try {
            System.out.println("Are you registering as an Admin? (yes/no)");
            String isAdminStr = scanner.nextLine();
            boolean isAdmin = isAdminStr.equalsIgnoreCase("yes");

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            String email;
            while (true) {
                System.out.print("Enter your email: ");
                email = scanner.nextLine();
                if (Validators.isValidEmail(email)) {
                    break;
                } else {
                    System.out.println("Invalid email format. Please try again.");
                }
            }

            String password;
            while (true) {
                System.out.print("Enter your password: ");
                password = scanner.nextLine();
                if (Validators.isValidPassword(password)) {
                    break;
                } else {
                    System.out.println("Password must be at least 6 characters long. Please try again.");
                }
            }

            if (isAdmin) {
                Admin admin = new Admin(name, email, password);
                admin.register();
            } else {
                String tier;
                while (true) {
                    System.out.print("Select package tier (Platinum/Gold/Silver): ");
                    tier = scanner.nextLine();
                    if (Validators.isValidTier(tier)) {
                        break;
                    } else {
                        System.out.println("Invalid tier. Please select Platinum, Gold, or Silver.");
                    }
                }
                User user = new User(name, email, password, tier);
                user.register();
            }
            System.out.println("Registration successful.");
        } catch (CustomException e) {
            System.err.println(e.getMessage());
            System.out.println("Please retry !!!");
            // Registration failed, prompt to try again
        }

    }

    private static void loginUser() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            User user = dataStore.getUsers().get(email);
            if (user == null) {
                System.out.println("User not found. Please register first.");
                return;
            }

            if (user.login(password)) {
                if (user instanceof Admin) {
                    adminMenu((Admin) user);
                } else {
                    userMenu(user);
                }
            }

        } catch (CustomException e) {
            System.err.println(e.getMessage());
        }

    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create a class");
            System.out.println("2. Schedule a class");
            System.out.println("3. Cancel a class");
            System.out.println("4. Logout");
            System.out.println("5. Exit");
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }


            switch (choice) {
                case 1:
                    createClass(admin);
                    break;
                case 2:
                    scheduleClass(admin);
                    break;
                case 3:
                    cancelClass(admin);
                    break;
                case 4:
                    System.out.println("Logged out successfully.");
                    return;
                    case 5:
                        System.out.println("Thank you for using the system. Goodbye!");
                        scanner.close(); // Close the scanner resource
                        System.exit(0); //Exit
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Book a class");
            System.out.println("2. Cancel a booking");
            System.out.println("3. Logout");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    bookClass(user);
                    break;
                case 2:
                    cancelBooking(user);
                    break;
                case 3:
                    System.out.println("Logged out successfully.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void createClass(Admin admin) {
        System.out.print("Enter class type (e.g., Yoga, Dance): ");
        String type = scanner.nextLine();

        System.out.print("Enter class capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        admin.createClass(type, capacity);

    }

    private static void scheduleClass(Admin admin) {
        System.out.print("Enter class ID to schedule: ");
        String classId = scanner.nextLine();

        FitnessClass fitnessClass = dataStore.getClasses().get(classId);
        if (fitnessClass == null) {
            System.out.println("Class not found.");
            return;
        }

        System.out.print("Enter scheduled time (YYYY-MM-DD HH:MM): ");
        String dateTimeStr = scanner.nextLine();

        try {
            Date scheduledTime = parseDateTime(dateTimeStr);
            admin.scheduleClass(fitnessClass, scheduledTime);
        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
        }
    }

    private static void cancelClass(Admin admin) {
        System.out.print("Enter class ID to cancel: ");
        String classId = scanner.nextLine();

        FitnessClass fitnessClass = dataStore.getClasses().get(classId);
        if (fitnessClass == null) {
            System.out.println("Class not found.");
            return;
        }

        admin.cancelClass(fitnessClass);

    }

    private static void bookClass(User user) {
        System.out.println("Available Classes:");

        boolean classesAvailable = false;

        for (FitnessClass fitnessClass : dataStore.getClasses().values()) {
            // Only display classes that are scheduled and not in the past
            if (fitnessClass.getScheduledTime() != null && fitnessClass.getScheduledTime().after(new Date())) {
                String status = fitnessClass.isFull() ? "Full" : "Available";
                int availableSlots = fitnessClass.getCapacity() - fitnessClass.getAttendees().size();

                System.out.println("ID: " + fitnessClass.getId());
                System.out.println("Type: " + fitnessClass.getType());
                System.out.println("Scheduled Time: " + fitnessClass.getScheduledTime());
                System.out.println("Available Slots: " + availableSlots);
                System.out.println("Status: " + status);
                System.out.println("-----------------------------------");

                classesAvailable = true;
            }
        }

        if (!classesAvailable) {
            System.out.println("No classes are currently available for booking.");
            return;
        }

        System.out.print("Enter class ID to book: ");
        String classId = scanner.nextLine();

        FitnessClass fitnessClass = dataStore.getClasses().get(classId);
        if (fitnessClass == null) {
            System.out.println("Class not found.");
            return;
        }

        // Check if the class is scheduled and not in the past
        if (fitnessClass.getScheduledTime() == null || fitnessClass.getScheduledTime().before(new Date())) {
            System.out.println("This class is not available for booking.");
            return;
        }

        try {
            user.bookClass(fitnessClass);
        } catch (CustomException e) {
            System.err.println(e.getMessage());
        }
    }


    private static void cancelBooking(User user) {
        System.out.println("Your Bookings:");
        for (String classId : user.currentBookings) {
            FitnessClass fitnessClass = dataStore.getClasses().get(classId);
            if (fitnessClass != null) {
                System.out.println("ID: " + fitnessClass.getId() + ", Type: " + fitnessClass.getType()
                        + ", Scheduled Time: " + fitnessClass.getScheduledTime());
            }
        }

        System.out.print("Enter class ID to cancel booking: ");
        String classId = scanner.nextLine();

        FitnessClass fitnessClass = dataStore.getClasses().get(classId);
        if (fitnessClass == null) {
            System.out.println("Class not found.");
            return;
        }

        try {
            user.cancelBooking(fitnessClass);
        } catch (CustomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Date parseDateTime(String dateTimeStr) throws Exception {
        String[] parts = dateTimeStr.split(" ");
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(dateParts[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1); // Months are 0-based
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }
}
