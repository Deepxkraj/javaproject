import java.awt.*;
import java.util.Optional;
import javax.swing.*;

public class BmiCalculation extends JFrame {

    private JTextField nameField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextArea displayArea;
    private SinglyLinkedList studentList = new SinglyLinkedList();

    public BmiCalculation() {
        setupUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        setBackground(Color.decode("#F0F8FF"));

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = createLabel("Name:", "#006400");
        add(nameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        nameField = createTextField(15);
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel heightLabel = createLabel("Height (cm):", "#006400");
        add(heightLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        heightField = createTextField(5);
        add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel weightLabel = createLabel("Weight (kg):", "#006400");
        add(weightLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        weightField = createTextField(5);
        add(weightField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton("Add", "#32CD32"));
        buttonPanel.add(createButton("Calculate BMI", "#FFA500"));
        buttonPanel.add(createButton("Delete", "#8B0000"));
        buttonPanel.add(createButton("Search", "#008080"));
        buttonPanel.add(createButton("Sort by Name", "#9400D3"));
        buttonPanel.add(createButton("Display", "#008B8B"));
        buttonPanel.add(createButton("Show BMI Range Table", "#4682B4"));
        buttonPanel.add(createButton("Unit Conversion", "#FFD700"));
        buttonPanel.add(createButton("Show BMI Advice", "#DAA520"));
        add(buttonPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        displayArea = new JTextArea(10, 60);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        displayArea.setBackground(Color.WHITE);
        displayArea.setForeground(Color.DARK_GRAY);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), gbc);

        setTitle("BMI Calculator with Nutrition");
        setSize(600, 400);
        setVisible(true);
    }

    private JLabel createLabel(String text, String colorHex) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.decode(colorHex));
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(Color.decode("#ADD8E6"));
        textField.setForeground(Color.decode("#000000"));
        return textField;
    }

    private JButton createButton(String label, String colorHex) {
        JButton button = new JButton(label);
        button.setBackground(Color.decode(colorHex));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(e -> handleButtonAction(label));
        return button;
    }

    private void handleButtonAction(String actionCommand) {
        switch (actionCommand) {
            case "Add":
                addStudent();
                break;
            case "Calculate BMI":
                calculateBMI();
                break;
            case "Delete":
                deleteStudent();
                break;
            case "Search":
                searchStudent();
                break;
            case "Sort by Name":
                sortStudentsByName();
                break;
            case "Display":
                displayStudents();
                break;
            case "Show BMI Range Table":
                showBMIRangeTable();
                break;
            case "Unit Conversion":
                unitConversion();
                break;
            case "Show BMI Advice":
                showBMIAdvice();
                break;
        }
    }

    private void addStudent() {
        String name = nameField.getText();
        String heightText = heightField.getText();
        String weightText = weightField.getText();

        if (name.isEmpty() || heightText.isEmpty() || weightText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double height = Double.parseDouble(heightText);
            double weight = Double.parseDouble(weightText);

            Student student = new Student(name, height, weight);
            studentList.add(student);

            displayArea.append("Student added: " + student + "\n");

            nameField.setText("");
            heightField.setText("");
            weightField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for height and weight.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateBMI() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to calculate BMI.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Student> student = studentList.find(name);
        if (student.isPresent()) {
            double bmi = student.get().calculateBMI();
            String bmiCategory = getBMICategory(bmi);
            displayArea.append("BMI for " + name + ": " + bmi + " (" + bmiCategory + ")\n");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.", "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 24.9) return "Normal weight";
        if (bmi < 29.9) return "Overweight";
        return "Obesity";
    }

    private void showBMIAdvice() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to show BMI advice.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Student> student = studentList.find(name);
        if (student.isPresent()) {
            double bmi = student.get().calculateBMI();
            String bmiCategory = getBMICategory(bmi);
            String advice = getAdviceForBMICategory(bmiCategory);
            displayArea.append("BMI Advice for " + name + " (" + bmiCategory + "):\n" + advice + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.", "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getAdviceForBMICategory(String bmiCategory) {
        switch (bmiCategory) {
            case "Underweight": return "Eat nutrient-dense foods, consult a nutritionist.";
            case "Normal weight": return "Maintain a balanced diet and exercise.";
            case "Overweight": return "Low-calorie diet, exercise more, consult a trainer.";
            case "Obesity": return "Structured diet, regular exercise, consult a professional.";
            default: return "No advice available.";
        }
    }

    private void deleteStudent() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to delete a student.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = studentList.delete(name);
        if (success) {
            displayArea.append("Student " + name + " deleted.\n");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search for a student.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Student> student = studentList.find(name);
        if (student.isPresent()) {
            displayArea.append("Found student: " + student.get() + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.", "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortStudentsByName() {
        studentList.sort();
        displayArea.append("Students sorted by name.\n");
    }

    private void displayStudents() {
        displayArea.append("All Students:\n" + studentList.display() + "\n");
    }

    private void showBMIRangeTable() {
        displayArea.append("BMI Range Table:\n");
        displayArea.append("Underweight: < 18.5\nNormal weight: 18.5 - 24.9\nOverweight: 25.0 - 29.9\nObesity: >= 30\n");
    }

    private void unitConversion() {
        String heightText = heightField.getText();
        String weightText = weightField.getText();

        if (heightText.isEmpty() || weightText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both height and weight fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double heightInCm = Double.parseDouble(heightText);
            double weightInKg = Double.parseDouble(weightText);

            double heightInInches = heightInCm / 2.54;
            double weightInLbs = weightInKg * 2.205;

            displayArea.append("Height in inches: " + heightInInches + "\n");
            displayArea.append("Weight in lbs: " + weightInLbs + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BmiCalculation::new);
    }
}

class Student {
    private String name;
    private double height;
    private double weight;

    public Student(String name, double height, double weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double calculateBMI() {
        double heightInMeters = height / 100;
        return weight / (heightInMeters * heightInMeters);
    }

    @Override
    public String toString() {
        return name + " - Height: " + height + " cm, Weight: " + weight + " kg";
    }
}

class SinglyLinkedList {
    private Node head;

    private class Node {
        Student data;
        Node next;

        Node(Student data) {
            this.data = data;
        }
    }

    public void add(Student student) {
        Node newNode = new Node(student);
        newNode.next = head;
        head = newNode;
    }

    public Optional<Student> find(String name) {
        Node current = head;
        while (current != null) {
            if (current.data.getName().equals(name)) {
                return Optional.of(current.data);
            }
            current = current.next;
        }
        return Optional.empty();
    }

    public boolean delete(String name) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.data.getName().equals(name)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public void sort() {
        if (head == null || head.next == null) return;

        boolean swapped;
        do {
            swapped = false;
            Node current = head;

            while (current.next != null) {
                if (current.data.getName().compareTo(current.next.data.getName()) > 0) {
                    Student temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data.toString()).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}