/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author AHMED
 */
public final class Login extends JFrame {

    Connection con = null;

    public Login() {
        NewLogin();
    }

    public void NewLogin() {

        setSize(1300, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED\\Documents\\NetBeansProjects\\Lab\\Cafe Management System\\src\\images\\Login page.jpg"));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        JLabel login = new JLabel();
        login.setText("Login");
        login.setBounds(520, 80, 350, 45);
        login.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        login.setForeground(Color.GREEN);
        backgroundLabel.add(login);

        JTextField txtemail = new JTextField();
        txtemail.setBounds(620, 200, 450, 30);
        backgroundLabel.add(txtemail);

        JLabel head = new JLabel();
        head.setText("Email");
        head.setForeground(Color.WHITE);
        head.setBounds(620, 170, 350, 30);
        head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(head);

        JPasswordField txtpassword = new JPasswordField();
        txtpassword.setBounds(620, 300, 450, 30);
        backgroundLabel.add(txtpassword);

        JLabel password = new JLabel();
        password.setText("Password");
        password.setForeground(Color.WHITE);
        password.setBounds(620, 270, 350, 30);
        password.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(password);

        JButton btnlogin = new JButton("Login");
        btnlogin.setBounds(620, 370, 150, 40);
        btnlogin.setBackground(Color.green);
        btnlogin.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        btnlogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver"); // Load the driver class
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafedb", "root", "");
                    String name = txtemail.getText();
                    String password = txtpassword.getText();

                    // Using PreparedStatement to prevent SQL injection
                    String sql = "SELECT * FROM user WHERE email=? AND password=?";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, password);

                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        if (name.equals("admin") && password.equals("admin")) {
                            JOptionPane.showMessageDialog(Login.this, "Login Successful");
                            Login.this.setVisible(false);
                            new Products().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(Login.this, "Login Successful");
                            new Order(name).setVisible(true);
                            Login.this.setVisible(false);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Username or password is incorrect");
                        txtemail.setText("");
                        txtpassword.setText("");
                    }

                    con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
        backgroundLabel.add(btnlogin);

        JButton btnsignup = new JButton("Sign Up");
        btnsignup.setBounds(800, 370, 150, 40);
        btnsignup.setBackground(Color.blue);
        btnsignup.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        btnsignup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Signup().setVisible(true);
                Login.this.setVisible(false);
            }
        });
        backgroundLabel.add(btnsignup);

        setVisible(true);

    }
}
