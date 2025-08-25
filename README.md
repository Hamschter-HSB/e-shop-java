# E-Shop

### City University of Applied Science - International Degree Programme in Media Computer Science B.Sc.

Course: "Prog-2"
Project: E-Shop by Markus Vogel, Maximilian Goldmann and Amir Valibeygi

## Getting started

### Setup:
The entire project was set up and developed using IntelliJ IDEA Ultimate Edition.
We would recommend to use the IntelliJ IDEA Ultimate Edition to avoid issues.
For the purposes of this tutorial we will be assuming that IntelliJ IDEA is being used.

How to start the software:
Open the project in IntelliJ IDEA Ultimate Edition. To do so, open the folder "shop" by going into the folder "e-shop"
in
your IDE. After IntelliJ states, the gradle module should start importing:

1. Start IntelliJ IDEA
    - Launch the IDE and wait for the Welcome screen or your existing project to open.

2. Detect and Load the Gradle Module
    - IntelliJ will automatically detect the build.gradle file and offer to import the Gradle project.
    - Click "Import Gradle Project" or "Load Gradle Changes" if prompted.

3. Wait for the Gradle Sync Process
    - A progress bar will appear at the bottom of the IDE (status bar), usually showing "Syncing" or "Gradle build
      running".
    - The "Event Log" (bottom-right corner) also shows messages about Gradle sync progress.

4. Know When It’s Done
    - When the sync is complete, the progress bar disappears. You will see a message like "Gradle sync finished" in the
      Event Log. All modules and dependencies should now appear in the Project panel (on the left).

If you don't have the gradle plugin installed, IntelliJ might suggest you to do so. If IntelliJ doesn't, please install
Gradle manually. Here is how: Go to Settings -> Plugins -> MarketPlace and search for "Gradle" and click to install and
repeat
from stop 1.

The use of Gradle is necessary, since this project uses Foreign libraries, sich as JUnit and SQLite.

### Start Application

Once the above steps have been completed, the programme can be used by first starting the main method in the Class:
src/main/java/de.eshop/server/main/ServerMain.java

With that, the Server should start up, wich can be checked in the respective Terminal. If the Server has been started
successfully the client(s) can be started with the main method in the class
src/main/java/de.eshop/client/main/SwingMain.java

If you wish to test the program with several clients simultaneously, it may be necessary to configure the SwingMain
main method so that multiple instances of this main method can be run.
In order to achieve this, click the button "more actions" (to the right of the "run" and "debug" buttons in the top
right corner). Then, click "Edit...".
In the Window that opens, choose the SwingMain Class on the left side and then click "modify options". Following that,
one of the first options should be to "Allow multiple instances". Click this option so that a checkmark appears next to
it. Afterward, click "Apply" and "OK" to save the configuration changes. Now several instances of the client can be
run.

____________________

### Bonus-Features:
1. Search function: On the left side of the window, all currently available articles can be filtered by entering a
   search term into the search field and hitting the "search" button. Works for words, partial words and numbers (
   price/bulk size)
2. Auto update: Whenever a client causes a change in the program data (i.e. A new article is created or stock is
   changed), all clients automatically receive an updated and display the updated available articles.
3. Password-Hashing: When created, passwords are being hashed so that they can no longer be read by simply looking inside the customer/staffmember files.
4. Logging
5. Unit tests

#### Exception handling
We did exception handling on server and client. We created domain specific exception that we use to do exception
handling on client side.
The server side does exception handling but with non domain specific exceptions, since it was not specified in the
project requirements. (Task paper's)

As we understand, exception handling should be implemented to A, have domain-specific exceptions and output them in a
user-friendly manner and B, so that the software
is robust and does not crash.

## Documentation (JavaDocs)
Our public API'S is documented with JavaDocs, as it supposed to be.
Comment blocks in the source code offer other developers a good orientation and understanding on Decisions made by the
core team.

## Testing (JUnit)
JUnit is a widely used testing framework for Java that allows developers to write and run automated unit tests.
It helps ensure that individual parts of the code(like methods or classes) work as expected. JUnit plays a key role in
building reliable, maintainable software.

That's why we have added tests. Unfortunately, we went out of time and didn't manage to write more unit tests than for
our Encrypt/Decrypt algorithm.

## Design Patterns
This project includes many design patterns, such as: 

- DAO-Pattern
- ObserverPattern
- SingletonPattern