# Task Manager

This is a simple task management web application built with Spring Boot and Thymeleaf. 
It includes a user interface for adding and completing tasks.

## Requirements

* Java 21+ (see instructions below for installation and setting JAVA_HOME on macOS)
* Gradle
* Node.js 23.7.0
* npm 10.2.4+ (for running end-to-end tests)

### Installing Java 21+ on macOS

1. Install Homebrew if you don't have it.
   
2. Install OpenJDK 21:
   ```sh
   brew install openjdk@21
   ```
3. Add Java to your PATH and set JAVA_HOME (add these lines to your ~/.zshrc or ~/.bash_profile):
   ```sh
   export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
   export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
   ```
   Then reload your shell:
   ```sh
   source ~/.zshrc  # or source ~/.bash_profile
   ```
4. Verify installation:
   ```sh
   java -version
   echo $JAVA_HOME
   ```

## Running the Application

1. Clone the repository:

   ```sh
   git clone <repository-url>
   cd task-manager
   ```

2. Build the project:

   ```sh
   ./gradlew build
   ```

3. Run the application:

   ```sh
   ./gradlew bootRun
   ```

4. Open your browser and visit:

   ```
   http://localhost:8080
   ```

## Running Tests

### Running Unit Tests

To run all unit tests:

```sh
./gradlew test
```

## End-to-End Testing with Playwright and VS Code

This project uses [Playwright](https://playwright.dev/) for end-to-end browser testing. Follow these steps to set up and use Playwright with VS Code integration.

### Prerequisites

- Node.js and npm (see Requirements above)
- VS Code

### 1. Install Playwright and Dependencies

In the `tests` directory, run:

```sh
npm install
npx playwright install
```

### 2. Install Playwright Test for VSCode Extension

1. Open VS Code
2. Go to Extensions (Ctrl+Shift+X or Cmd+Shift+X)
3. Search for "Playwright Test for VSCode"
4. Install the extension by Microsoft (`ms-playwright.playwright`)

### 3. Install Copilot MCP VS Code Extension

For advanced test authoring with Model Context Protocol (MCP):
- Open VS Code
- Go to Extensions
- Search for "Copilot MCP"
- Install the extension by Automata Labs (`automatalabs.copilot-mcp`)

### 4. Install `code` Command in PATH (VS Code)

1. Press Command+Shift+P in VS Code
2. Type `shell command`
3. Select `Install 'code' command in PATH`

### 5. Register Playwright MCP Server with VS Code

Run in your terminal:

```sh
code --add-mcp '{"name":"playwright","command":"npx","args":["@playwright/mcp@latest"]}'
```

### 6. MCP Server Configuration

You must now run the MCP server manually in a separate terminal:

```sh
cd tests
npx @playwright/mcp@latest
```

> **Note:** The MCP server will not start automatically. Always start it manually before running Playwright tests.

### 7. Running Playwright Tests

To run all Playwright tests:

```sh
cd tests
npm test
```

To run tests with UI mode (for debugging):

```sh
cd tests
npm run test:ui
```

To run a specific test file:

```sh
cd tests
npx playwright test e2e/garfield-search.spec.ts
```

For more on developing new end-to-end tests, see the [MCP Test Development Guide](docs/mcp-test-development.md).

## Project Structure

### Backend (Spring Boot)

* `Task` – The model representing a single task.
* `TaskRepository` – A simple repository using `JdbcTemplate` for data access.
* `TaskService` – The service layer for managing task logic.
* `TaskController` – Handles web requests and renders the Thymeleaf UI.
* `TaskRestController` – REST API endpoints for task management (API version).
* `templates/tasks.html` – The Thymeleaf front-end.
* `static/style.css` – Styling for the UI.

### Tests

* `src/test/` – Unit and integration tests for the Spring Boot application.
* `tests/` – End-to-end tests using Playwright:
  * `e2e/` – Contains the Playwright test files (`.spec.ts`)
  * `playwright.config.ts` – Configuration for Playwright tests
  * `mcp-client.js` – Example MCP client for test development


## Notes

* Task data is stored in an embedded H2 in-memory database.
* H2 is pulled automatically by Gradle, there is no need to install it manually.
* Tables are created automatically on startup.
* You can view the H2 console at `http://localhost:8080/h2-console`.
