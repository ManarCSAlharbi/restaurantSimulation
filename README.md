# 🍽️ Restaurant Simulation Project

This repository contains the source code and resources for a restaurant simulation project developed as part of the CPCS361 course.
The project simulates the operations of a restaurant, including order placement, customer events, and interactions between waiters, chefs, and customers.

## 📋 Table of Contents
- [✨ Features](#-features)
- [🗂️ Project Structure](#️-project-structure)
- [🚀 Getting Started](#-getting-started)
  - [🛠️ Prerequisites](#%EF%B8%8F-prerequisites)
  - [▶️ Running the Simulation](#%EF%B8%8F-running-the-simulation)
  - [💡 Example](#-example)
- [📄 Inputs and Outputs](#-inputs-and-outputs)
- [🛠️ Usage](#%EF%B8%8F-usage)
- [📜 License](#-license)
- [🤝 Contributions](#-Contributions)

## ✨ Features
- Simulates restaurant operations using Java.
- Handles customer events, order processing, and interactions between restaurant staff.
- Includes sample input files for testing different scenarios.

## 🗂️ Project Structure
```
|-- 📁 CPCS361Group16POurCode/             # Source code and resources
     |-- 👨‍🍳 Chef.java                      # Simulates the Chef's operations.
     |-- 🧑‍💼 Customer.java                  # Handles Customer-related logic.
     |-- 🎭 CustomerEvent.java             # Manages customer-specific events.
     |-- 📋 EventTracker.java              # Tracks and handles events in the restaurant.
     |-- 🛒 Order.java                     # Manages orders placed in the simulation.
     |-- 🏢 RestaurantSimulation.java      # Main class to run the simulation.
     |-- 🧑‍🍽️ Waiter.java                    # Simulates the Waiter's tasks.
     |-- 📥 restaurant_simulation_input*.txt    # Input files for simulation.
     |-- 📤 restaurant_simulation_output*.txt   # Sample output files from the simulation.
```

## 🚀 Getting Started

### 🛠️ Prerequisites
- **Java Development Kit (JDK)**: Ensure you have JDK installed on your machine.

### ▶️ Running the Simulation
1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/your-username/restaurant-simulation.git
   cd restaurant-simulation/CPCS361Group16POurCode
   ```

2. Compile the Java files:
   ```bash
   javac *.java
   ```

3. Run the simulation using the main class `RestaurantSimulation`:
   ```bash
   java RestaurantSimulation <input-file>
   ```
   Replace `<input-file>` with one of the provided input files, e.g., `restaurant_simulation_input1.txt`.

### 💡 Example
To run the simulation with the first input file:
```bash
java RestaurantSimulation restaurant_simulation_input1.txt
```

## 📄 Inputs and Outputs
- **📥 Input Files**: Located in the `CPCS361Group16POurCode` directory (`restaurant_simulation_input1.txt`, `restaurant_simulation_input2.txt`, etc.).
  - Define the scenarios for the simulation.
- **📤 Output Files**: Generated in the same directory with names like `restaurant_simulation_output1.txt`.
  - Contain the results of the simulation based on the inputs.

## 🛠️ Usage
1. Prepare your environment by installing the required **Java Development Kit (JDK)**.
2. Clone the repository and navigate to the `CPCS361Group16POurCode` directory.
3. Compile the source code using the `javac` command.
4. Run the simulation by passing an appropriate input file.
5. Review the generated output file to analyze the simulation results.

### Example Workflow:
- Clone the repository: `git clone https://github.com/your-username/restaurant-simulation.git`
- Compile the code: `javac *.java`
- Run the simulation: `java RestaurantSimulation restaurant_simulation_input1.txt`
- Check the output: Open `restaurant_simulation_output1.txt` to see the results.

## 📜 License
This project is licensed under the [MIT License](LICENSE).

## 🤝 Contributions
This project was collaboratively developed by me and my colleague. We worked together to design and implement the system, ensuring its functionality and reliability.
---

Feel free to use this project as a reference or for learning purposes! 🌟
