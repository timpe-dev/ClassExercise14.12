import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Attendance {
    private static final int MAX_HEIGHT = 350; // Set your maximum height here
    FileAccess fileAccess = new FileAccess();
    SchoolClass currentClass = null;

    void addAttendanceInputPanel(String first, String last, Vector<JPanel> studentInputFields, JPanel inputsPanel, JFrame mainWindow, String time, String notes, boolean isPresent) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel firstNameLbl = new JLabel(first + " " + last);
        panel.add(firstNameLbl);

        JCheckBox presentBox = new JCheckBox();
        presentBox.setSelected(isPresent);
        panel.add(presentBox);

        JTextField timeFld = new JTextField();
        timeFld.setPreferredSize(new Dimension(40, 30)); // Set the preferred size
        timeFld.setText(time);
        panel.add(timeFld);

        JTextField notesFld = new JTextField();
        notesFld.setPreferredSize(new Dimension(70, 30)); // Set the preferred size
        notesFld.setText(notes);
        panel.add(notesFld);

        studentInputFields.add(panel);
        inputsPanel.add(studentInputFields.lastElement());
        inputsPanel.revalidate();
        inputsPanel.repaint();
        packWithMaxHeight(mainWindow, MAX_HEIGHT);

    }

    public Attendance() {
        Vector<JPanel> studentInputFields = new Vector<>();

        JFrame mainWindow = new JFrame("Attendance Manager");
        mainWindow.setSize(new Dimension(500, 450));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainWindow.add(contentPanel);

        JPanel classDetailsPanel = new JPanel();
        classDetailsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(classDetailsPanel);
        JLabel titleLbl = new JLabel("Attendance for: ");
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        classDetailsPanel.add(titleLbl);
        Vector<String> classNames = fileAccess.FetchClassNames();
        JComboBox classNameBox = new JComboBox<>(classNames);
        classDetailsPanel.add(classNameBox);

        JTextField dateFld = new JTextField("dd:MM:YYYY");
        dateFld.setPreferredSize(new Dimension( 15*8, 30)); // Set the preferred size
        classDetailsPanel.add(dateFld);


        JScrollPane inputsContainer = new JScrollPane();
        contentPanel.add(inputsContainer);

        JPanel inputsPanel = new JPanel();
        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
        inputsContainer.setViewportView(inputsPanel);

        JPanel buttonPanel = new JPanel();
        JButton backBtn = new JButton();
        backBtn.setText("back");
        buttonPanel.add(backBtn);
        JButton saveBtn = new JButton();
        saveBtn.setText("save");
        buttonPanel.add(saveBtn);
        contentPanel.add(buttonPanel);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainMenu = new MainMenu();
                mainWindow.dispose();
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
                    addAttendanceInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow, "hh:mm", "", false);
                }
                inputsPanel.repaint();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel firstNameLbl = new JLabel("Name");
        panel.add(firstNameLbl);

        JLabel presentLbl = new JLabel("Present");
        panel.add(presentLbl);

        JLabel timeLbl = new JLabel("Time");
        panel.add(timeLbl);

        JLabel notesLbl = new JLabel("Notes");
        panel.add(notesLbl);

        studentInputFields.add(panel);
        inputsPanel.add(studentInputFields.lastElement());
        inputsPanel.revalidate();
        inputsPanel.repaint();
        packWithMaxHeight(mainWindow, MAX_HEIGHT);

        try {
            currentClass = fileAccess.FetchClass(classNameBox.getSelectedItem().toString());
            for (Student student : currentClass.students) {
                addAttendanceInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow, "hh:mm", "", false);
            }
        } catch (Exception e) {
            currentClass = null;
        }
        //SchoolClass finalCurrentClass = currentClass;
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttendaceData attendaceData = new AttendaceData(currentClass.className, dateFld.getText());
                for (int i = 1; i< studentInputFields.size(); i++) {
                    String name = "";
                    Boolean isPresent = false;
                    String time = "";
                    String notes = "";
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
                                isPresent = isPresentBox.isSelected();
                                componentIndex = 3;
                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 3) {
                            try {
                                JTextField timeFld = (JTextField) component;
                                time = timeFld.getText();
                                componentIndex = 4;

                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 4) {
                            try {
                                JTextField notesFld = (JTextField) component;
                                notes = notesFld.getText();
                            } catch (Exception ex) {
                                ;
                            }
                        }
                    }

                    AttendanceEntry attendanceEntry = new AttendanceEntry(name, isPresent, time, notes);
                    attendaceData.attendanceEntries.add(attendanceEntry);

                }
                fileAccess.StoreAttendane(attendaceData);
            }
        });

        dateFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateFld.getText();
                AttendaceData attendaceData = fileAccess.FetchAttendance(currentClass.className, date);
                if (attendaceData != null) {
                    for (Component component : inputsPanel.getComponents()) {
                        inputsPanel.remove(component);
                    }
                    studentInputFields.clear();
                    for (AttendanceEntry attendanceEntry : attendaceData.attendanceEntries) {
                        addAttendanceInputPanel(attendanceEntry.name, "", studentInputFields ,inputsPanel, mainWindow, attendanceEntry.time, attendanceEntry.notes, attendanceEntry.isPresent);

                    }
                }
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