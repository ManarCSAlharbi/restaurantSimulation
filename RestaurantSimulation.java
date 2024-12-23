package RestaurantSimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;


public class RestaurantSimulation {
    // Configuration variables
     public static int numChefs;
    public static int numWaiters;
    public static int numTables;
    public static Map<String, Integer> mealPreparationTimes = new HashMap<>();
    public static List<Customer> customers = new ArrayList<>();
    public static EventTracker eventTracker = new EventTracker();

    // Shared resources
    public static Semaphore tablesSemaphore;
    public static Semaphore orderQueueSemaphore = new Semaphore(1);
    public static Semaphore mealsReadyQueueSemaphore = new Semaphore(1);
    public static Semaphore availableTablesSemaphore = new Semaphore(1);

    public static Queue<Order> orderQueue = new LinkedList<>();
    public static Queue<Order> mealsReadyQueue = new LinkedList<>();
    public static List<Integer> availableTables = Collections.synchronizedList(new ArrayList<>());

    // Simulation time management
    public static long simulationStartRealTime;
    public static int SIMULATION_START_HOUR = 12;
    public static int SIMULATION_START_MINUTE = 0;
    public static final int SCALE_FACTOR = 100;

    // Statistics
    public static int totalCustomersServed = 0;
    public static long totalWaitTime = 0;
    public static long totalPreparationTime = 0;
    public static int simulationStartTime = Integer.MAX_VALUE;
    public static int simulationEndTime = Integer.MIN_VALUE;


    public static void main(String[] args) {
        // List of input and output file pairs
        String[][] files = {
            {"C:\\Users\\lamar\\OneDrive\\Desktop\\codes\\FCIT7\\src\\finalosprojectfrrrrrr\\restaurant_simulation_input1.txt", "restaurant_simulation_output1.txt"},
            {"C:\\Users\\lamar\\OneDrive\\Desktop\\codes\\FCIT7\\src\\finalosprojectfrrrrrr\\restaurant_simulation_input2.txt", "restaurant_simulation_output2.txt"},
            {"C:\\Users\\lamar\\OneDrive\\Desktop\\codes\\FCIT7\\src\\finalosprojectfrrrrrr\\restaurant_simulation_input3.txt", "restaurant_simulation_output3.txt"}
        };

        for (String[] filePair : files) {
            String inputFile = filePair[0];
            String outputFile = filePair[1];

            // Reset static variables before each simulation
            resetSimulation();

            // Run the simulation for each input file
            runSimulation(inputFile, outputFile);
              eventTracker.reset();
        }
    }

    public static void runSimulation(String inputFileName, String outputFileName) {
        try {
            PrintStream fileOut = new PrintStream(outputFileName);
            System.setOut(fileOut);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create output file: " + outputFileName);
            System.exit(1);
        }

        // Read input from file
        readInputFile(inputFileName);

        // Initialize semaphore and tables
        tablesSemaphore = new Semaphore(numTables);
        for (int i = 1; i <= numTables; i++) {
            availableTables.add(i);
        }

        // Adjust simulation start time based on earliest customer arrival
        adjustSimulationStartTime();

        // Record the real-world start time of the simulation
        simulationStartRealTime = System.currentTimeMillis();

        // Print simulation initialization message
        System.out.println("Simulation Started with " + numChefs + " Chefs, " + numWaiters + " Waiters, and " + numTables + " Tables.\n");

        // Create and start chef threads
        List<Chef> chefs = new ArrayList<>();
        for (int i = 1; i <= numChefs; i++) {
            Chef chef = new Chef(i);
            chefs.add(chef);
            chef.start();
        }

        // Create and start waiter threads
        List<Waiter> waiters = new ArrayList<>();
        for (int i = 1; i <= numWaiters; i++) {
            Waiter waiter = new Waiter(i);
            waiters.add(waiter);
            waiter.start();
        }

        // Start customer threads
        for (Customer customer : customers) {
            customer.start();
        }

        // Wait for all customer threads to finish
        for (Customer customer : customers) {
            try {
                customer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Interrupt chef threads after all customers are served
        for (Chef chef : chefs) {
            chef.interrupt();
        }

        // Wait for chef threads to finish
        for (Chef chef : chefs) {
            try {
                chef.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Interrupt waiter threads after all customers are served
        for (Waiter waiter : waiters) {
            waiter.interrupt();
        }

        // Wait for waiter threads to finish
        for (Waiter waiter : waiters) {
            try {
                waiter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Output end of simulation and summary statistics
//        System.out.println("\n[End of Simulation]\n");
        printSummary();
    }

    // Method to reset static variables before each simulation
    public static void resetSimulation() {
        // Reset all static variables
        numChefs = 0;
        numWaiters = 0;
        numTables = 0;
        mealPreparationTimes = new HashMap<>();
        customers = new ArrayList<>();
        tablesSemaphore = null;
        orderQueue = new LinkedList<>();
        mealsReadyQueue = new LinkedList<>();
        availableTables = Collections.synchronizedList(new ArrayList<>());
        totalCustomersServed = 0;
        totalWaitTime = 0;
        totalPreparationTime = 0;
        SIMULATION_START_HOUR = 12;
        SIMULATION_START_MINUTE = 0;
        simulationStartTime = Integer.MAX_VALUE;
        simulationEndTime = Integer.MIN_VALUE;
    }

    // Method to read input from the specified input file
    private static void readInputFile(String inputFileName) {
        try {
            File file = new File(inputFileName);
            Scanner scanner = new Scanner(file);

            // Read the configuration line (number of chefs, waiters, and tables)
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                parseConfigLine(line);
            }

            // Read the meal preparation times line
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                parseMealTimesLine(line);
            }

            // Read customer information lines
            while (scanner.hasNextLine()) {
                String customerLine = scanner.nextLine();
                if (!customerLine.trim().isEmpty()) {
                    parseCustomerLine(customerLine);
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + inputFileName);
            System.exit(1);
        }
    }

    // Method to parse the configuration line from the input file
    private static void parseConfigLine(String line) {
        String[] tokens = line.split("\\s+");
        for (String token : tokens) {
            String[] parts = token.split("=");
            if (parts.length < 2) continue;
            switch (parts[0]) {
                case "NC":
                    numChefs = Integer.parseInt(parts[1]);
                    break;
                case "NW":
                    numWaiters = Integer.parseInt(parts[1]);
                    break;
                case "NT":
                    numTables = Integer.parseInt(parts[1]);
                    break;
                default:
                    break;
            }
        }
    }

    // Method to parse meal preparation times from the input file
    private static void parseMealTimesLine(String line) {
        String[] tokens = line.split("\\s+");
        for (String token : tokens) {
            String[] parts = token.split("=");
            if (parts.length < 2) continue;
            String mealName = parts[0];
            int prepTime = parseTimeToMinutes(parts[1]);
            mealPreparationTimes.put(mealName, prepTime);
        }

        // Debug: Print the mealPreparationTimes map
        // System.out.println("Meal Preparation Times: " + mealPreparationTimes);
    }

    // Method to parse customer information lines from the input file
    private static void parseCustomerLine(String line) {
        String[] tokens = line.split("\\s+");
        int customerId = 0;
        String arrivalTime = "";
        String order = "";
        for (String token : tokens) {
            String[] parts = token.split("=");
            if (parts.length < 2) continue;
            switch (parts[0]) {
                case "CustomerID":
                    customerId = Integer.parseInt(parts[1]);
                    break;
                case "ArrivalTime":
                    arrivalTime = parts[1];
                    break;
                case "Order":
                    order = parts[1];
                    break;
                default:
                    break;
            }
        }
        if (!arrivalTime.isEmpty() && !order.isEmpty()) {
                
            Customer customer = new Customer(customerId, arrivalTime, order);
            customers.add(customer);
        } else {
            System.err.println("Error parsing customer line: " + line);
        }
    }

    // Helper method to parse time in "HH:MM" format to total minutes
    private static int parseTimeToMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } else if (parts.length == 1) {
            return Integer.parseInt(parts[0]);
        } else {
            return 0;
        }
    }

    // Adjust simulation start time based on earliest customer arrival
    private static void adjustSimulationStartTime() {
        int earliestArrival = Integer.MAX_VALUE;
        for (Customer customer : customers) {
            int arrival = parseTimeToMinutes(customer.getArrivalTime());
            if (arrival < earliestArrival) {
                earliestArrival = arrival;
            }
        }
        SIMULATION_START_HOUR = earliestArrival / 60;
        SIMULATION_START_MINUTE = earliestArrival % 60;
           
    }

//     Make parseArrivalTimeToMinutes public and static for other classes to access
      public static int parseArrivalTimeToMinutes(String time) {
    String[] parts = time.split(":");
    if (parts.length < 2) return 0;
    return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
}

    // Helper method to format minutes since simulation start to "HH:MM" format
    public static String formatTime(int minutesSinceStart) {
        int totalMinutes = SIMULATION_START_HOUR * 60 + SIMULATION_START_MINUTE + minutesSinceStart;
        

        int hour = (totalMinutes / 60) % 24;
        int minute = totalMinutes % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    // Method to get the current simulated time in minutes since simulation start
    public static synchronized int getCurrentSimulatedTime() {
        long currentRealTime = System.currentTimeMillis();
        long elapsedRealTime = currentRealTime - simulationStartRealTime;
        return (int) (elapsedRealTime / SCALE_FACTOR);
    }

    // Method to update simulation start and end times
    public static synchronized void updateSimulationTime(int time) {
        if (time < simulationStartTime) {
            simulationStartTime = time;
        }
        if (time > simulationEndTime) {
            simulationEndTime = time;
        }
    }

    // Method to print summary statistics at the end of the simulation
   private static void printSummary() {

        eventTracker.printEvents();
        
        double averageWaitTime = totalCustomersServed > 0 ? (double) totalWaitTime / totalCustomersServed : 0;
        double averagePrepTime = totalCustomersServed > 0 ? (double) totalPreparationTime / totalCustomersServed : 0;
        int totalSimulatedMinutes = simulationEndTime - simulationStartTime;

        System.out.println("\nSummary:");
        System.out.println("- Total Customers Served: " + totalCustomersServed);
        System.out.printf("- Average Wait Time for Table: %.2f Minutes%n", averageWaitTime);
        System.out.printf("- Average Order Preparation Time: %.2f Minutes%n", averagePrepTime);
        System.out.println("- Total Simulation Time: " + totalSimulatedMinutes + " Minutes");
        
        System.out.println("\n[End of Simulation]\n");
    }
}
