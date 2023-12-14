import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    Authenticator auth = new Authenticator();

    LoginScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame mainWindow =new JFrame("Attendance Manager");
        //submit button
        JButton loginBtn = new JButton("Log in");
        loginBtn.setBounds(100,110,100, 40);
        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.setBounds(100,160,100, 40);
        //enter name hLabel
        JLabel userNameLbl = new JLabel();
        userNameLbl.setText("Username:");
        userNameLbl.setBounds(10, 10, 100, 40);
        //Text Field for USER ENTRY
        JTextField userNameFld = new JTextField("");
        JTextField passwordFld = new JTextField("");
        userNameFld.setBounds(130, 10, 140, 40);
        passwordFld.setBounds(130, 60, 140, 40);

        JLabel bLabel = new JLabel();
        bLabel.setText("Password:");
        bLabel.setBounds(10, 60, 100, 40);

        JLabel errorLabel = new JLabel();
        errorLabel.setText("Login Error");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(100, 200, 100, 60);
        mainWindow.add(errorLabel);

        errorLabel.setVisible(false);
//Text Field for USER ENTRY
        mainWindow.add(bLabel);
        mainWindow.add(userNameLbl);
//f.add(textfield);
        mainWindow.add(userNameFld);
        mainWindow.add(passwordFld);
        mainWindow.add(loginBtn);//add button to GUI FRAME
        mainWindow.add(createAccountBtn);//add button to GUI FRAME
        int width = 300;
        int height = 300;
        mainWindow.setBounds(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2, width, height);
        mainWindow.setLayout(null);
        mainWindow.setVisible(true);


        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userNameFld.getText();
                String password = passwordFld.getText();
                try {
                    auth.CreateUser(username, password);
                    mainWindow.dispose();

                    new MainMenu();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);

                }
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userNameFld.getText();
                String password = passwordFld.getText();
                try {
                    if (auth.AuthenticateUser(username, password)) {
                        mainWindow.dispose();

                        new MainMenu();
                    } else {
                        errorLabel.setVisible(true);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
