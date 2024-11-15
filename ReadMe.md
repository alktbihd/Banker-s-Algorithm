# Banker's Algorithm

This repository contains an implementation of the Banker's Algorithm in Java. The Banker's Algorithm is used in operating systems to manage resource allocation and ensure that the system remains in a safe state.

## Overview

The Banker's Algorithm simulates resource allocation for processes while avoiding deadlock. This project allows you to request and release resources, and checks the safety of the system after every change in allocation.

## Features

- Implementation of the Banker's Algorithm for 5 customers and 4 resources.
- Command-line interface for interacting with the resource manager.
- Allows users to request resources (RQ), release resources (RL), and view the system state (*).
- Ensures safe resource allocation, preventing deadlocks.

## Files

- `BankersAlgorithm.java`: The Java program containing the implementation of the Banker's Algorithm.
- `max.txt`: Text file containing the maximum demand for each customer. Each line corresponds to a customer, and each value represents the maximum resource request for each of the 4 resources.

## How to Run

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/BankersAlgorithm.git
   cd BankersAlgorithm
   ```

2. Compile the Java file:
   ```
   javac BankersAlgorithm.java
   ```

3. Run the program with initial available resources (e.g., 10 units for each resource):
   ```
   java BankersAlgorithm 10 10 10 10
   ```

## Commands

Once the program is running, you can use the following commands to interact with the system:

- **RQ customer_num resource1 resource2 resource3 resource4**: Request resources for a customer.
  - Example: `RQ 0 2 1 3 1` - Customer 0 requests 2 of resource 1, 1 of resource 2, etc.
- **RL customer_num resource1 resource2 resource3 resource4**: Release resources from a customer.
  - Example: `RL 0 1 1 0 0` - Customer 0 releases resources.
- **\***: Print the current state of the system.

## File Format (`max.txt`)

The `max.txt` file should contain 5 lines (for each customer), with each line containing 4 comma-separated values representing the maximum demand of that customer. For example:

```
7,5,3,4
3,2,2,2
9,0,2,7
2,2,2,2
4,3,3,5
```

## Example Usage

1. Run the program with available resources:
   ```
   java BankersAlgorithm 10 10 10 10
   ```
2. Use commands to interact:
   - Request resources: `RQ 0 1 0 2 1`
   - Release resources: `RL 1 0 1 1 0`
   - Print state: `*`

## Notes

- Ensure that the `max.txt` file contains valid entries, as it is required for the initialization of maximum demands.
- The program prevents unsafe resource allocation and may deny requests that lead to unsafe states.

## License

This project is open-source and licensed under the MIT License.

## Contributing

Feel free to open issues and submit pull requests. Contributions are welcome!

## Contact

For questions or suggestions, feel free to contact.

