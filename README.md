Smart Task Scheduler (Java)

A Java-based task manager using a Priority Queue with support for GUI (Swing) and Console (Headless) modes. It includes reminders and file-based persistence.

🚀 Features

Add, Edit, Delete tasks

Priority-based scheduling (HIGH, MEDIUM, LOW)

Deadline sorting & reminder alerts

Filter: High Priority & Today’s Tasks

Persistent storage (tasks.dat)

GUI + Console mode

Executable .jar support

🛠 Technologies

Java (JDK 8+)

Swing

PriorityQueue

File I/O & Serialization

Timer & Date-Time API

▶ Run (VS Code)
javac SmartTaskScheduler.java
java SmartTaskScheduler

📦 Executable JAR
Main-Class: SmartTaskScheduler

jar cfm SmartTaskScheduler.jar manifest.txt SmartTaskScheduler.class
java -jar SmartTaskScheduler.jar

🧠 Concepts Used

Collections, Swing UI, Event Handling, Serialization, Multithreading, Headless Mode