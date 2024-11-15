import java.io.*;
import java.util.*;

public class BankersAlgorithm {
    static final int NUMBER_OF_CUSTOMERS = 5;
    static final int NUMBER_OF_RESOURCES = 4;

    static int[] available = new int[NUMBER_OF_RESOURCES];
    static int[][] maximum = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] allocation = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] need = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

    public static void main(String[] args) {
        if (args.length != NUMBER_OF_RESOURCES) {
            System.out.println("Usage: java BankersAlgorithm <resource1> <resource2> ...");
            return;
        }

        // Initialize available resources from command line arguments
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            available[i] = Integer.parseInt(args[i]);
        }

        // Initialize data from the file "max.txt"
        initializeData("max.txt");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine();

            if (command.startsWith("RQ")) {
                String[] parts = command.split(" ");
                int customerNum = Integer.parseInt(parts[1]);
                int[] request = new int[NUMBER_OF_RESOURCES];
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    request[i] = Integer.parseInt(parts[i + 2]);
                }
                requestResources(customerNum, request);
            } else if (command.startsWith("RL")) {
                String[] parts = command.split(" ");
                int customerNum = Integer.parseInt(parts[1]);
                int[] release = new int[NUMBER_OF_RESOURCES];
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    release[i] = Integer.parseInt(parts[i + 2]);
                }
                releaseResources(customerNum, release);
            } else if (command.equals("*")) {
                printState();
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    static void initializeData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
                String[] values = br.readLine().split(",");
                if (values.length != NUMBER_OF_RESOURCES) {
                    throw new IllegalArgumentException("Invalid file format. Each line must contain " + NUMBER_OF_RESOURCES + " comma-separated values.");
                }
                for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                    maximum[i][j] = Integer.parseInt(values[j]);
                    need[i][j] = maximum[i][j];
                    allocation[i][j] = 0;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    // Function to handle resource requests from a customer
    static boolean requestResources(int customerNum, int[] request) {
        // Check if the request is valid
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            if (request[i] > need[customerNum][i]) {
                System.out.println("Request exceeds maximum demand.");
                return false;
            }
            if (request[i] > available[i]) {
                System.out.println("Not enough resources available.");
                return false;
            }
        }

        // Temporarily allocate resources to test if system stays safe
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            available[i] -= request[i];
            allocation[customerNum][i] += request[i];
            need[customerNum][i] -= request[i];
        }

        System.out.println("\nAfter temporary allocation for safety check:");
        printState();

        // Check if the system is in a safe state after the allocation
        if (!isSafe()) {
            // Rollback if state is unsafe
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                available[i] += request[i];
                allocation[customerNum][i] -= request[i];
                need[customerNum][i] += request[i];
            }
            System.out.println("Request denied (unsafe state).");
            return false;
        }

        System.out.println("Request granted.");
        return true;
    }

    // Function to handle release of resources from a customer
    static void releaseResources(int customerNum, int[] release) {
        // Check if the release is valid
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            if (release[i] > allocation[customerNum][i]) {
                System.out.println("Error: Cannot release more resources than allocated.");
                return;
            }
        }

        // Release resources from customer and update available resources
        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            allocation[customerNum][i] -= release[i];
            available[i] += release[i];
            need[customerNum][i] += release[i];
        }
        System.out.println("Resources released by Customer " + customerNum + ".");
        System.out.println("Updated Available Resources: " + Arrays.toString(available));
    }

    // Function to print the current state of the system
    static void printState() {
        System.out.println("\nAvailable Resources:");
        System.out.println(Arrays.toString(available));

        System.out.println("\nMaximum Resources:");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            System.out.println(Arrays.toString(maximum[i]));
        }

        System.out.println("\nAllocation:");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            System.out.println(Arrays.toString(allocation[i]));
        }

        System.out.println("\nNeed:");
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            System.out.println(Arrays.toString(need[i]));
        }
    }

    // Function to determine if the system is in a safe state
    static boolean isSafe() {
        int[] work = Arrays.copyOf(available, NUMBER_OF_RESOURCES);
        boolean[] finish = new boolean[NUMBER_OF_CUSTOMERS];

        System.out.println("\n--- Safety Check Start ---");
        System.out.println("Work (initially available): " + Arrays.toString(work));
        System.out.println("Finish: " + Arrays.toString(finish));

        boolean madeProgress;
        do {
            madeProgress = false;
            for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
                if (!finish[i]) {
                    boolean canProceed = true;
                    for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                        if (need[i][j] > work[j]) {
                            canProceed = false;
                            break;
                        }
                    }
                    if (canProceed) {
                        System.out.println("Customer " + i + " can proceed. Adding allocated resources to work.");
                        for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                            work[j] += allocation[i][j];
                        }
                        finish[i] = true;
                        madeProgress = true;
                        System.out.println("Updated Work: " + Arrays.toString(work));
                        System.out.println("Updated Finish: " + Arrays.toString(finish));
                    } else {
                        System.out.println("Customer " + i + " cannot proceed. Needs: " + Arrays.toString(need[i]) + ", Available: " + Arrays.toString(work));
                    }
                }
            }
        } while (madeProgress);

        for (boolean f : finish) {
            if (!f) {
                System.out.println("System is not in a safe state.\n--- Safety Check End ---");
                return false;
            }
        }

        System.out.println("System is in a safe state.\n--- Safety Check End ---");
        return true;
    }
}
