# Smart Task Scheduler (Java)

A Java-based task manager using a *Priority Queue* with support for *GUI (Swing)* and *Console (Headless)* modes. It includes reminders and file-based persistence.

---

## 🚀 Features

* Add, Edit, Delete tasks
* Priority-based scheduling (HIGH, MEDIUM, LOW)
* Deadline sorting & reminder alerts
* Filter: High Priority & Today’s Tasks
* Persistent storage (tasks.dat)
* GUI + Console mode
* Executable .jar support

---

## 🛠 Technologies

* Java (JDK 8+)
* Swing
* PriorityQueue
* File I/O & Serialization
* Timer & Date-Time API

---

## ▶ Run (VS Code)

bash
javac SmartTaskScheduler.java
java SmartTaskScheduler


---

## 📦 Executable JAR

txt
Main-Class: SmartTaskScheduler


bash
jar cfm SmartTaskScheduler.jar manifest.txt SmartTaskScheduler.class
java -jar SmartTaskScheduler.jar


---

## 🧠 Concepts Used

Collections, Swing UI, Event Handling, Serialization, Multithreading, Headless Mode

---


## 📸 Screenshots

### Sceduled tasks
<img width="1909" height="620" alt="TASK SCHEDULED" src="https://github.com/user-attachments/assets/ef717d83-f6d3-483c-be53-6fca2721ec9c" />


### High Priority Tasks
<img width="1909" height="740" alt="HIGH PRIORITY TASKS" src="https://github.com/user-attachments/assets/efd1577b-ccd9-4728-a5ef-b504349085b7" />


### Todays Tasks
<img width="1910" height="699" alt="TODAYS TASKS" src="https://github.com/user-attachments/assets/06664fa5-b1aa-42c2-9301-e396d39b3ab4" />

