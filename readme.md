
## Requirements

- JDK 23.0.x



## Convert to Maven

1. Pull and merge
2. Close IntelliJ
3. Go to Project folder
4. Delete any `.idea/` and `.iml` files
5. Open IntelliJ
6. `File->Open->Chatter/pom.xml` as a project, not a file
7. Ensure JDK 23



## Run

### Option 1

1. Open terminal

- For Windows run: `mvnw.cmd clean javafx:run`
- For Mac/Linux run: `./mvnw clean javafx:run`

### Option 2

1. Open run/debug configurations
2. Add a new Maven configuration
3. Add to Command line: `javafx:run`
4. Run the configuration
