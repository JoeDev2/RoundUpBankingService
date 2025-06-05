# Financial RoundUp Service

A Java-based transaction roundup savings feature that automatically rounds up outgoing transactions for a specified time period and adds the rounded amounts to a savings goal. 

Originally developed as part of a technical challenge for a recruitment process, this project has been generalized to use mock data instead of real banking APIs for portfolio demonstration purposes.

## Features

- **Transaction Analysis**: Retrieves and processes financial transactions for a specified date range
- **Smart Roundup Calculation**: Automatically calculates roundup amounts from transaction data
- **Savings Goal Management**: Creates savings goals and transfers roundup amounts
- **Interactive CLI**: User-friendly command-line interface for account and date selection
- **Mock Data Integration**: Demonstrates API integration patterns without requiring real banking credentials

## Architecture

The application follows a clean, layered architecture:
- **Service Layer**: Business logic for accounts, transactions, and savings goals
- **Model Layer**: Domain objects representing accounts, transactions, and financial data
- **UI Layer**: Command-line interface for user interaction
- **Configuration**: Centralized API configuration and settings

## Technology Stack

- **Java 11+**: Core application development
- **Maven**: Build automation and dependency management
- **Jackson**: JSON processing and API response handling
- **JUnit 4**: Unit testing framework
- **Mockito**: Mocking framework for testing
- **Java Time API**: Date/time handling

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Setup and Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd RoundUpAPIFeature
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   java -cp target/roundup-service-1.0.0.jar com.bank.roundup.RoundUpLauncher
   ```
   
   **Alternative**: Run the `RoundUpLauncher` class directly from your IDE

4. **Run tests**
   ```bash
   mvn test
   ```

   Alternatively, run the `RoundUpLauncher` class directly from your IDE.

For demonstration purposes, this application uses mock data instead of real banking APIs. This allows you to:
- See full functionality without banking credentials
- Understand API integration patterns
- Test roundup calculations with realistic transaction data


## Sample Output

The application will guide you through:
1. Selecting an account from available options
2. Choosing a date range for transaction analysis
3. Creating or selecting a savings goal
4. Viewing calculated roundup amounts and transfer confirmation