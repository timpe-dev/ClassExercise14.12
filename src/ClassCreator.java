import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.Vector;

public class ClassCreator {
    private static final int MAX_HEIGHT = 350; // Set your maximum height here
    FileAccess fileAccess = new FileAccess();
    SchoolClass currentClass = null;

    void addStudentInputPanel(String first, String last, Vector<JPanel> studentInputFields, JPanel inputsPanel, JFrame mainWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel firstNameLbl = new JLabel("First name: ");
        panel.add(firstNameLbl);

        JTextField firstNameFld = new JTextField();
        firstNameFld.setPreferredSize(new Dimension(120, 30)); // Set the preferred size
        firstNameFld.setText(first);
        panel.add(firstNameFld);

        JLabel surNameLbl = new JLabel("Last name: ");
        panel.add(surNameLbl);

        JTextField surNameFld = new JTextField();
        surNameFld.setPreferredSize(new Dimension(60*2, 30)); // Set the preferred size
        surNameFld.setText(last);
        panel.add(surNameFld);

        JButton removeStudentBtn = new JButton();
        removeStudentBtn.setText("-");
        panel.add(removeStudentBtn);
        removeStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentInputFields.remove(panel);
                inputsPanel.remove(panel);
                inputsPanel.repaint();
                packWithMaxHeight(mainWindow, MAX_HEIGHT);

            }
        });
        studentInputFields.add(panel);
        inputsPanel.add(studentInputFields.lastElement());
        inputsPanel.revalidate();
        inputsPanel.repaint();
        packWithMaxHeight(mainWindow, MAX_HEIGHT);

    }
    public ClassCreator() {
        Vector<JPanel> studentInputFields = new Vector<>();

        JFrame mainWindow = new JFrame("Attendance Manager");
        mainWindow.setSize(new Dimension(500, 450));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainWindow.add(contentPanel);

        JLabel titleLbl = new JLabel("Create New Class");
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLbl);

        JPanel classDetailsPanel = new JPanel();
        classDetailsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(classDetailsPanel);
/*
        JTextField classNameFld = new JTextField();
        classNameFld.setPreferredSize(new Dimension(40, 30)); // Set the preferred size
        classDetailsPanel.add(classNameFld);*/
        Vector<String> classNames = fileAccess.FetchClassNames();
        JComboBox classNameBox = new JComboBox<>(classNames);
        classDetailsPanel.add(classNameBox);
        JLabel classNameLbl = new JLabel("Class Name: ");
        classDetailsPanel.add(classNameLbl);
        JTextField classNameFld = new JTextField();
        classNameFld.setPreferredSize(new Dimension( 15*8, 30)); // Set the preferred size
        classDetailsPanel.add(classNameFld);
        JButton addClassNameBtn = new JButton();
        addClassNameBtn.setText("create");
        classDetailsPanel.add(addClassNameBtn);

        JButton addStudentBtn = new JButton("+");
        contentPanel.add(addStudentBtn);

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
        addClassNameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchoolClass newClass = new SchoolClass(classNameFld.getText());
                fileAccess.StoreClass(newClass);
                classNames.add(newClass.className);
                classDetailsPanel.repaint();
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
                    addStudentInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow);
                }
                inputsPanel.repaint();
            }
        });
        try {
            currentClass = fileAccess.FetchClass(classNameBox.getSelectedItem().toString());
            for (Student student : currentClass.students) {
                addStudentInputPanel(student.firstName, student.surName, studentInputFields, inputsPanel, mainWindow);
            }
        } catch (Exception e) {
            currentClass = null;
        }
        SchoolClass finalCurrentClass = currentClass;
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentClass.students.clear();
                for (int i = 0; i< studentInputFields.size(); i++) {
                    String firstName = "";
                    String lastName = "";
                    JPanel input = studentInputFields.get(i);
                    int componentIndex = 1;
                    for (Component component : input.getComponents()) {
                        if (componentIndex == 1) {
                            try {
                                JTextField firstNameFld = (JTextField) component;
                                firstName = firstNameFld.getText();
                                componentIndex = 2;
                            }catch (Exception ex) {
                                ;
                            }
                        }
                        else if (componentIndex == 2) {
                            try {
                                JTextField lastNameFld = (JTextField) component;
                                lastName = lastNameFld.getText();
                            }catch (Exception ex) {
                                ;
                            }
                        }
                    }
                    currentClass.students.add(new Student(firstName, lastName));

                }
                fileAccess.StoreClass(currentClass);
            }
        });
        addStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentInputPanel("", "", studentInputFields, inputsPanel, mainWindow);
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