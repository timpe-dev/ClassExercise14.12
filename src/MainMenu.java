import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    MainMenu() {
        JFrame mainWindow = new JFrame("Attendance Manager");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        mainWindow.add(panel);
        JButton classManagerBtn = new JButton("Class manager");
        panel.add(classManagerBtn);
        JButton attendanceBtn = new JButton("Attendance manager");
        panel.add(attendanceBtn);
        JButton gradeManagerBtn = new JButton("Grade manager");
        panel.add(gradeManagerBtn);

        classManagerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassCreator classCreator = new ClassCreator();
                mainWindow.dispose();
            }
        });

        attendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attendance attendance = new Attendance();
                mainWindow.dispose();

            }
        });

        gradeManagerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Grades grades = new Grades();
                mainWindow.dispose();

            }
        });
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }

    public static void main(String[] args) {
        LoginScreen loginScreen = new LoginScreen();
    }

}
