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
 * @author Susana
 */
public final class Signup extends JFrame {

    Connection con = null;
    int randomNum = (int)(Math.random() * 2000);

    public Signup() {
        NewSignup();
    }

    public void NewSignup() {

        setSize(1300, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED\\Documents\\NetBeansProjects\\Lab\\Cafe Management System\\src\\images\\Signup page.jpg"));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        JLabel sign = new JLabel();
        sign.setText("Sign Up");
        sign.setBounds(520, 80, 350, 45);
        sign.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        sign.setForeground(Color.GREEN);
        backgroundLabel.add(sign);

        JTextField txtname = new JTextField();
        txtname.setBounds(620, 150, 450, 30);
        backgroundLabel.add(txtname);

        JLabel name = new JLabel();
        name.setText("Name");
        name.setBounds(520, 150, 350, 30);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(name);

        JTextField txtemail = new JTextField();
        txtemail.setBounds(620, 200, 450, 30);
        backgroundLabel.add(txtemail);

        JLabel head = new JLabel();
        head.setText("Email");
        head.setBounds(520, 200, 350, 30);
        head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(head);

        JTextField txtmn = new JTextField();
        txtmn.setBounds(620, 250, 450, 30);
        backgroundLabel.add(txtmn);

        JLabel mn = new JLabel();
        mn.setText("Mobile Number");
        mn.setBounds(420, 250, 350, 30);
        mn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(mn);

        JTextField txtaddress = new JTextField();
        txtaddress.setBounds(620, 300, 450, 30);
        backgroundLabel.add(txtaddress);

        JLabel address = new JLabel();
        address.setText("Address");
        address.setBounds(500, 300, 350, 30);
        address.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(address);

        JTextField txtpassword = new JTextField();
        txtpassword.setBounds(620, 350, 450, 30);
        backgroundLabel.add(txtpassword);

        JLabel pass = new JLabel();
        pass.setText("Password");
        pass.setBounds(480, 350, 350, 30);
        pass.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(pass);

        JTextField confirmpassword = new JTextField();
        confirmpassword.setBounds(620, 400, 450, 30);
        backgroundLabel.add(confirmpassword);

        JLabel conf = new JLabel();
        conf.setText("Confirm Password");
        conf.setBounds(380, 400, 400, 30);
        conf.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(conf);

        JButton btnlogin = new JButton("Signup");
        btnlogin.setBounds(620, 450, 150, 40);
        btnlogin.setBackground(Color.green);
        btnlogin.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        btnlogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtname.getText();
                String email = txtemail.getText();
                String mn = txtmn.getText();
                String address = txtaddress.getText();
                String password = txtpassword.getText();
                String conpassword = confirmpassword.getText();

                if (!name.equals("") && !email.equals("") && !mn.equals("") && !address.equals("") && !password.equals("") && password.equals(conpassword)) {
                    try {
                        Class.forName("com.mysql.jdbc.Driver"); // Load the driver class
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafedb", "root", "");

                        // Using PreparedStatement to prevent SQL injection
                        String sql = "INSERT INTO `user` (`id`, `name`, `email`, `number`, `address`, `password`) VALUES (?,?,?,?,?,?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, Integer.toString(randomNum));
                        pstmt.setString(2, name);
                        pstmt.setString(3, email);
                        pstmt.setString(4, mn);
                        pstmt.setString(5, address);
                        pstmt.setString(6, password);

                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Sucessfully Submitted");
                        Signup.this.setVisible(false);
                        new Login().setVisible(true);

                        con.close();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Please make sure all inputs are correct");
                }
            }
        });
        backgroundLabel.add(btnlogin);

        JButton btnsignup = new JButton("Login");
        btnsignup.setBounds(800, 450, 150, 40);
        btnsignup.setBackground(Color.blue);
        btnsignup.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        btnsignup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                Signup.this.setVisible(false);
            }
        });
        backgroundLabel.add(btnsignup);

        setVisible(true);

    }
}
