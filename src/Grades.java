import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Grades {
    private static final int MAX_HEIGHT = 350; // Set your maximum height here
    FileAccess fileAccess = new FileAccess();
    SchoolClass currentClass = null;

    void addGradeInputPanel(String first, String last, Vector<JPanel> studentInputFields, JPanel inputsPanel, JFrame mainWindow, String grade, String comment, boolean completed) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel firstNameLbl = new JLabel(first + " " + last);
        panel.add(firstNameLbl);

        JCheckBox completedBox = new JCheckBox();
        completedBox.setSelected(completed);
        panel.add(completedBox);

        JTextField gradeFld = new JTextField();
        gradeFld.setPreferredSize(new Dimension(40, 30)); // Set the preferred size
        gradeFld.setText(grade);
        panel.add(gradeFld);

        JTextField commentFld = new JTextField();
        commentFld.setPreferredSize(new Dimension(70, 30)); // Set the preferred size
        commentFld.setText(comment);
        panel.add(commentFld);

        studentInputFields.add(panel);
        inputsPanel.add(studentInputFields.lastElement());
        inputsPanel.revalidate();
        inputsPanel.repaint();
        packWithMaxHeight(mainWindow, MAX_HEIGHT);

    }

    public Grades() {
        Vector<JPanel> studentInputFields = new Vector<>();

        JFrame mainWindow = new JFrame("Grade Manager");
        mainWindow.setSize(new Dimension(500, 450));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainWindow.add(contentPanel);

        JPanel classDetailsPanel = new JPanel();
        classDetailsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(classDetailsPanel);
        JLabel titleLbl = new JLabel("Assigment for: ");
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        classDetailsPanel.add(titleLbl);
        Vector<String> classNames = fileAccess.FetchClassNames();
        JComboBox classNameBox = new JComboBox<>(classNames);
        classDetailsPanel.add(classNameBox);

        JTextField dateFld = new JTextField("dd:MM:YYYY");
        dateFld.setPreferredSize(new Dimension( 15*8, 30)); // Set the preferred size
        classDetailsPanel.add(dateFld);

        JTextField nameFld = new JTextField("Assignment name");
        nameFld.setPreferredSize(new Dimension( 15*6, 30)); // Set the preferred size

        contentPanel.add(nameFld);

        JTextField descriptionFld = new JTextField("Description");
        descriptionFld.setPreferredSize(new Dimension( 15*8, 40)); // Set the preferred size
        contentPanel.add(descriptionFld);

        JScrollPane inputsContainer = new JScrollPane();
        contentPanel.add(inputsContainer);

        JPanel inputsPanel = new JPanel();
        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
        inputsContainer.setViewportView(inputsPanel);

        JPanel buttonPanel = new JPanel();
        JButton backBtn = new JButton();
        backBtn.setText("back");
        buttonPanel.add(backBtn);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainMenu = new MainMenu();
                mainWindow.dispose();
            }
        });
        JButton saveBtn = new JButton();
        saveBtn.setText("save");
        buttonPanel.add(saveBtn);
        contentPanel.add(buttonPanel);



        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLbl = new JLabel("Name");
        panel.add(nameLbl);

        JLabel completedLbl = new JLabel("Completed");
        panel.add(completedLbl);

        JLabel gradeLbl = new JLabel("Grade");
        panel.add(gradeLbl);

        JLabel commentLbl = new JLabel("Comment");
        panel.add(commentLbl);

        studentInputFields.add(panel);
        inputsPanel.add(studentInputFields.lastElement());
        inputsPanel.revalidate();
        inputsPanel.repaint();
        packWithMaxHeight(mainWindow, MAX_HEIGHT);

        try {
            currentClass = fileAccess.FetchClass(classNameBox.getSelectedItem().toString());
            for (Student student : currentClass.students) {
                addGradeInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow, "", "", false);
            }
        } catch (Exception e) {
            currentClass = null;
        }
        //SchoolClass finalCurrentClass = currentClass;
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Assignment assignment = new Assignment(currentClass.className, dateFld.getText(), nameFld.getText(), descriptionFld.getText());
                for (int i = 1; i< studentInputFields.size(); i++) {
                    String name = "";
                    boolean completed = false;
                    String grade = "";
                    String comment = "";
                    JPanel input = studentInputFields.get(i);
                    int componentIndex = 1;
                    for (Component component : input.getComponents()) {
                        if (componentIndex == 1) {
                            try {
                                JLabel nameLbl = (JLabel) component;
                                name = nameLbl.getText();

                                componentIndex = 2;

                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 2) {
                            try {
                                JCheckBox isPresentBox = (JCheckBox) component;
                                completed = isPresentBox.isSelected();
                                componentIndex = 3;
                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 3) {
                            try {
                                JTextField timeFld = (JTextField) component;
                                grade = timeFld.getText();
                                componentIndex = 4;

                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 4) {
                            try {
                                JTextField notesFld = (JTextField) component;
                                comment = notesFld.getText();
                            } catch (Exception ex) {
                                ;
                            }
                        }
                    }

                    AssignmentEntry assignmentEntry = new AssignmentEntry(name, completed, grade, comment);
                    assignment.assignmentEntries.add(assignmentEntry);

                }
            }
        });
        classNameBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentClass = fileAccess.FetchClass(classNameBox.getSelectedItem().toString());
                for (Component component : inputsPanel.getComponents()) {
                    inputsPanel.remove(component);
                }
                studentInputFields.clear();
                for (Student student : currentClass.students) {
                    addGradeInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow, "hh:mm", "", false);
                }
                inputsPanel.repaint();
            }
        });
        packWithMaxHeight(mainWindow, MAX_HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);

    }

    private void packWithMaxHeight(JFrame frame, int maxHeight) {
        frame.pack();
        Dimension currentSize = frame.getSize();
        currentSize.width = currentSize.width + 40;
        if (currentSize.height > maxHeight) {
            currentSize.height = maxHeight;
            frame.setSize(currentSize);
        }
    }

}