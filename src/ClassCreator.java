import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassCreator {
    String[] classes = {"10Y", "6P"};

    public ClassCreator() {

        JFrame mainWindow = new JFrame("Attendance Manager");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 1200;
        int height = 500;
        mainWindow.setBounds(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2, width, height);        Button classManager = new Button("Class manager");

        mainWindow.setLayout(null);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLbl = new JLabel();
        titleLbl.setText("Create New Class");
        titleLbl.setBounds(width / 2 - 100, 10, 200, 40);
        mainWindow.add(titleLbl);

        JLabel classNameLbl = new JLabel();
        classNameLbl.setText("Class Name: ");
        classNameLbl.setBounds(width / 2 - 100, 50, 80, 20);
        mainWindow.add(classNameLbl);

        JComboBox<String> classSelection = new JComboBox<>(classes);
        classSelection.setBounds(100, 100, 50, 50);
        //classSelection.setSelectedIndex(0);
        //classSelection.addActionListener(null);
        mainWindow.add(classSelection);
        classSelection.setForeground(Color.BLUE);
        classSelection.setFont(new Font("Arial", Font.BOLD, 14));
        classSelection.setMaximumRowCount(10);
        classSelection.setVisible(true);
    }
    public static void main(String[] args) {
        new ClassCreator();
    }

}
