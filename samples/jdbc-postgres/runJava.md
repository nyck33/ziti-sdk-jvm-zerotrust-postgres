To compile and run the Java application using Gradle (as indicated by the presence of `build.gradle` and the Gradle wrapper scripts `gradlew` and `gradlew.bat`), follow these steps:

### 1. Navigate to the project directory:
You're already in the correct directory based on your provided terminal prompt, so you can skip this step.

### 2. Compile the Java application:
Using the Gradle wrapper script (`gradlew` for Unix-like systems), compile the Java code:
```bash
./gradlew build
```

This command will generate the compiled bytecode and place it in the `build` directory within the project.

### 3. Run the Java application:
After the build is successful, you can run the application using the Gradle `run` task:
```bash
./gradlew run
```

This command will execute the main method of your `App.java` class. If the `App.java` requires any arguments to run, you can provide them like this:
```bash
./gradlew run --args='arg1 arg2 arg3'
```
Replace `arg1 arg2 arg3` with the appropriate arguments for your application.

### Note:
Before running the Java application, ensure that all the necessary dependencies and configurations are correctly defined in the `build.gradle` file. The `gradlew` commands will use the configurations defined in that file to compile and run the Java application. If there are external libraries or specific configurations required, they should be specified in the `build.gradle` file.