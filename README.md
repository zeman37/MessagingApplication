# REST Messaging Application

## Description

By using this application, it will be possible to:

- Import bank statement for one or several bank accounts via
  CSV.
- Export bank statement for one or several bank
  accounts via CSV.
- Calculate account balance for a given
  date.

## Requirements

- Newest JAVA Version.
- Gradle to "build" application
- Application to send HTTP REQUESTS (recommended - PostMan)
- Git (optional)

## Installation

Run `./gradlew build` in main project directory. The packaged .jar file will appear in `\build\libs` folder.

## Running application
To run application, open PowerShell/Command Prompt in the same folder where "REST-0.0.1-SNAPSHOT.jar"
is located. Use command:
`java -jar application-0.0.1-SNAPSHOT.jar` to run the application.
Alternatively, you can use `java -jar application-0.0.1-SNAPSHOT.jar --server.port=x`
where x = port which you would like to use for this application (default is 8080)

## Using Application
Please run the application and go to `/swagger_ui.html` in order to see the documentation.