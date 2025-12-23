import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 SMART TASK SCHEDULER – FINAL FIXED VERSION
*/

public class SmartTaskScheduler extends JFrame {

    enum Priority { HIGH, MEDIUM, LOW }

    static class Task implements Serializable {
        UUID id;
        String title;
        Priority priority;
        LocalDateTime deadline;

        Task(String title, Priority priority, LocalDateTime deadline) {
            this.id = UUID.randomUUID();
            this.title = title;
            this.priority = priority;
            this.deadline = deadline;
        }
    }

    PriorityQueue<Task> taskQueue;
    DefaultTableModel tableModel;
    JTable table;

    JTextField titleField, deadlineField;
    JComboBox<Priority> priorityBox;

    static final String FILE_NAME = "tasks.dat";
    static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SmartTaskScheduler() {
        setTitle("Smart Task Scheduler");
        setSize(850, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        taskQueue = createQueue();
        loadTasks();
        initUI();
        startReminder();
    }

    static PriorityQueue<Task> createQueue() {
        return new PriorityQueue<>((a, b) -> {
            int p = b.priority.compareTo(a.priority);
            return (p != 0) ? p : a.deadline.compareTo(b.deadline);
        });
    }

    void initUI() {
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Priority", "Deadline"}, 0);
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(0));
        refreshTable();

        titleField = new JTextField(10);
        priorityBox = new JComboBox<>(Priority.values());
        deadlineField = new JTextField("2025-12-31 18:00", 12);

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton highBtn = new JButton("High Priority");
        JButton todayBtn = new JButton("Today's Tasks");
        JButton allBtn = new JButton("Show All");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Title"));
        panel.add(titleField);
        panel.add(new JLabel("Priority"));
        panel.add(priorityBox);
        panel.add(new JLabel("Deadline"));
        panel.add(deadlineField);
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(highBtn);
        panel.add(todayBtn);
        panel.add(allBtn);

        addBtn.addActionListener(e -> addTask());
        editBtn.addActionListener(e -> editTask());
        deleteBtn.addActionListener(e -> deleteTask());
        highBtn.addActionListener(e -> filter("HIGH"));
        todayBtn.addActionListener(e -> filter("TODAY"));
        allBtn.addActionListener(e -> refreshTable());

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                titleField.setText(tableModel.getValueAt(r, 1).toString());
                priorityBox.setSelectedItem(
                        Priority.valueOf(tableModel.getValueAt(r, 2).toString()));
                deadlineField.setText(tableModel.getValueAt(r, 3).toString());
            }
        });

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    void addTask() {
        try {
            taskQueue.add(new Task(
                    titleField.getText(),
                    (Priority) priorityBox.getSelectedItem(),
                    LocalDateTime.parse(deadlineField.getText(), FORMATTER)
            ));
            saveTasks();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Use format: yyyy-MM-dd HH:mm");
        }
    }

    void editTask() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        UUID id = UUID.fromString(tableModel.getValueAt(row, 0).toString());

        for (Task t : taskQueue) {
            if (t.id.equals(id)) {
                t.title = titleField.getText();
                t.priority = (Priority) priorityBox.getSelectedItem();
                t.deadline = LocalDateTime.parse(deadlineField.getText(), FORMATTER);
                break;
            }
        }
        saveTasks();
        refreshTable();
    }

    void deleteTask() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        UUID id = UUID.fromString(tableModel.getValueAt(row, 0).toString());
        taskQueue.removeIf(t -> t.id.equals(id));
        saveTasks();
        refreshTable();
    }

    void refreshTable() {
        tableModel.setRowCount(0);
        PriorityQueue<Task> temp = new PriorityQueue<>(taskQueue);
        while (!temp.isEmpty()) {
            Task t = temp.poll();
            tableModel.addRow(new Object[]{
                    t.id, t.title, t.priority,
                    t.deadline.format(FORMATTER)
            });
        }
    }

    void filter(String type) {
        tableModel.setRowCount(0);
        LocalDate today = LocalDate.now();

        for (Task t : taskQueue) {
            if (type.equals("HIGH") && t.priority == Priority.HIGH ||
                type.equals("TODAY") && t.deadline.toLocalDate().equals(today)) {
                tableModel.addRow(new Object[]{
                        t.id, t.title, t.priority,
                        t.deadline.format(FORMATTER)
                });
            }
        }
    }

    // ✅ FIXED TIMER (NO AMBIGUITY)
    void startReminder() {
        java.util.Timer timer = new java.util.Timer(true);
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                for (Task t : taskQueue) {
                    if (t.deadline.minusMinutes(1).isBefore(now)
                            && t.deadline.isAfter(now)) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(
                                        null, "⏰ Reminder: " + t.title));
                    }
                }
            }
        }, 0, 60000);
    }

    void saveTasks() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(taskQueue));
        } catch (Exception ignored) {}
    }

    void loadTasks() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            for (Object o : (ArrayList<?>) ois.readObject())
                taskQueue.add((Task) o);
        } catch (Exception ignored) {}
    }

    static void runConsoleMode() {
        System.out.println("HEADLESS MODE");
        PriorityQueue<Task> q = createQueue();
        q.add(new Task("Submit Assignment", Priority.HIGH,
                LocalDateTime.now().plusMinutes(5)));
        q.add(new Task("Watch Tutorial", Priority.LOW,
                LocalDateTime.now().plusHours(2)));

        while (!q.isEmpty()) {
            Task t = q.poll();
            System.out.println(t.title + " | " + t.priority + " | " + t.deadline);
        }
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) runConsoleMode();
        else SwingUtilities.invokeLater(() ->
                new SmartTaskScheduler().setVisible(true));
    }
}
