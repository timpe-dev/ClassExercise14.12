import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    MainMenu() {
        JFrame mainWindow = new JFrame("Attendance Manager");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = 1400;
        int height = 700;
        mainWindow.setBounds(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2, width, height);        Button classManager = new Button("Class manager");
        classManager.setBounds(10, 10, 100, 100);
        mainWindow.add(classManager);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);

        classManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassCreator classCreator = new ClassCreator();
            }
        });

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
