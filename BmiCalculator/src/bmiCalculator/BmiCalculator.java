package bmiCalculator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class BmiCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        boolean repeat;

        do {
            System.out.println("\n==========================================");
            System.out.println("    Hello, Welcome to the BMI Calculator ");
            System.out.println("==========================================\n");

            System.out.print(" First Name: ");
            String name = scanner.nextLine();

            System.out.print(" Last Name: ");
            String lastName = scanner.nextLine();

            String gender;
            while (true) {
                System.out.print(" Gender (Male/Female/Other): ");
                gender = scanner.nextLine().trim();
                if (gender.equalsIgnoreCase("M") ||
                    gender.equalsIgnoreCase("F") ||
                    gender.equalsIgnoreCase("other")) {
                    break;
                } else {
                    System.out.println("❗ Invalid input. Please enter M, F, or Other.\n");
                }
            }

            int age;
            while (true) {
                System.out.print(" Age: ");
                if (scanner.hasNextInt()) {
                    age = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    if (age > 0 && age <= 120) {
                        break;
                    } else {
                        System.out.println("❗ Please enter a valid age between 1 and 120.\n");
                    }
                } else {
                    System.out.println("❗ Invalid input. Please enter a number.\n");
                    scanner.next(); // consume invalid input
                }
            }

            int unitChoice = getUnitChoice(scanner);

            double weight = (unitChoice == 1)
                    ? getValidInput(scanner, "  Enter your weight in kilograms: ", 10, 600)
                    : getValidInput(scanner, "  Enter your weight in pounds: ", 22, 1300);

            double height = (unitChoice == 1)
                    ? getValidInput(scanner, " Enter your height in meters: ", 0.5, 2.5)
                    : getValidInput(scanner, " Enter your height in inches: ", 20, 100);

            double bmi = calculateBMI(unitChoice, weight, height);
            String category = getBMICategory(bmi);
            String unit = (unitChoice == 1) ? "Metric (kg/m)" : "Imperial (lbs/in)";

            System.out.printf("\n Your BMI is: %.2f\n", bmi);
            System.out.println(" Category: " + category + "\n");
            giveHealthRecommendation(category);

            // Show Report
            showReport(name, lastName, gender, age, unit, weight, height, bmi, category);

            // Log
            logResult(name, lastName, gender, age, unitChoice, weight, height, bmi, category);

            System.out.println("\n Your details have been saved to 'bmi_results_log.txt'.");

            // Ask to repeat
            System.out.print("\nWould you like to calculate another BMI? (Y/N): ");
            String response = scanner.nextLine().trim();
            repeat = response.equalsIgnoreCase("Y");

        } while (repeat);

        System.out.println("\n Thank you for using the BMI Calculator. Stay healthy!\n");
        scanner.close();
    }

    public static int getUnitChoice(Scanner scanner) {
        int choice;
        while (true) {
            System.out.println("\n Choose your preferred unit system:");
            System.out.println("1️  Metric (kg, m)");
            System.out.println("2️  Imperial (lbs, in)");
            System.out.print("  Enter 1 or 2: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice == 1 || choice == 2) {
                    break;
                } else {
                    System.out.println("❗ Invalid choice. Please enter 1 or 2.\n");
                }
            } else {
                System.out.println("❗ Invalid input. Please enter a number.\n");
                scanner.next();
            }
        }
        return choice;
    }

    public static double getValidInput(Scanner scanner, String prompt, double min, double max) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.printf("❗ Please enter a value between %.2f and %.2f.\n", min, max);
                }
            } else {
                System.out.println("❗ Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return value;
    }

    public static double calculateBMI(int unitChoice, double weight, double height) {
        if (unitChoice == 1) {
            return weight / (height * height);
        } else {
            return (703 * weight) / (height * height);
        }
    }

    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public static void giveHealthRecommendation(String category) {
        switch (category) {
            case "Underweight":
                System.out.println(" Tip: Increase your calorie intake with nutritious food. Consult a healthcare provider if needed.");
                break;
            case "Normal weight":
                System.out.println(" Great job! Maintain your healthy habits and keep moving!");
                break;
            case "Overweight":
                System.out.println(" Consider adjusting your diet and increasing physical activity.");
                break;
            case "Obese":
                System.out.println(" It's advisable to consult a doctor. Focus on sustainable lifestyle changes.");
                break;
        }
    }

    public static void logResult(String name, String lastName, String gender, int age,
                                 int unitChoice, double weight, double height, double bmi, String category) {
        String unit = (unitChoice == 1) ? "Metric (kg/m)" : "Imperial (lbs/in)";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logEntry = String.format(
                "[%s] Name: %s %s | Gender: %s | Age: %d | Unit: %s | Weight: %.2f | Height: %.2f | BMI: %.2f | Category: %s%n",
                timestamp, name, lastName, gender, age, unit, weight, height, bmi, category
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bmi_results_log.txt", true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.out.println(" Error writing to log file: " + e.getMessage());
        }
    }

    public static void showReport(String name, String lastName, String gender, int age, String unit,
                                  double weight, double height, double bmi, String category) {
        System.out.println("\n==========================================");
        System.out.println("               BMI REPORT                 ");
        System.out.println("==========================================");
        System.out.printf(" Name       : %s %s%n", name, lastName);
        System.out.printf(" Gender     : %s%n", gender);
        System.out.printf(" Age        : %d%n", age);
        System.out.printf(" Unit System: %s%n", unit);
        System.out.printf(" Weight     : %.2f%n", weight);
        System.out.printf(" Height     : %.2f%n", height);
        System.out.printf(" BMI        : %.2f%n", bmi);
        System.out.printf(" Category   : %s%n", category);
        System.out.println("==========================================");
    }
}
