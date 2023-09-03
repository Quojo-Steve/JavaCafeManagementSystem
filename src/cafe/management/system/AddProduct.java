package cafe.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddProduct extends JFrame {

    private Connection con = null;
    private int randomNum = (int) (Math.random() * 2000);
    private JTextField txtname;
    private JTextField price;
    private byte[] imageBytes; // Store image as bytes

    public AddProduct() {
        NewAddProduct();
    }

    public void NewAddProduct() {
        setSize(1300, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);

        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED\\Documents\\NetBeansProjects\\Lab\\Cafe Management System\\src\\images\\Signup page.jpg"));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        backgroundPanel.add(backgroundLabel);

        JLabel sign = new JLabel();
        sign.setText("Add Product");
        sign.setBounds(520, 80, 350, 45);
        sign.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        sign.setForeground(Color.GREEN);
        backgroundLabel.add(sign);

        txtname = new JTextField();
        txtname.setBounds(620, 150, 450, 30);
        backgroundLabel.add(txtname);

        JLabel name = new JLabel();
        name.setText("Name");
        name.setBounds(520, 150, 350, 30);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(name);

        price = new JTextField();
        price.setBounds(620, 200, 450, 30);
        backgroundLabel.add(price);

        JLabel head = new JLabel();
        head.setText("Price");
        head.setBounds(520, 200, 350, 30);
        head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        backgroundLabel.add(head);

        imageBytes = null; // Initialize imageBytes to null

        JTextField imagePathField = new JTextField();
        imagePathField.setBounds(620, 250, 300, 30);
        backgroundLabel.add(imagePathField);

        JButton browseImageButton = new JButton("Browse");
        browseImageButton.setBounds(930, 250, 120, 30);
        browseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePathField.setText(selectedFile.getAbsolutePath());
                    try {
                        FileInputStream imageInputStream = new FileInputStream(selectedFile);
                        imageBytes = new byte[(int) selectedFile.length()];
                        imageInputStream.read(imageBytes);
                        imageInputStream.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error reading image: " + ex.getMessage());
                    }
                }
            }
        });
        backgroundLabel.add(browseImageButton);

        JButton btnlogin = new JButton("Add Item");
        btnlogin.setBounds(620, 450, 150, 40);
        btnlogin.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtname.getText();
                String productPrice = price.getText();

                if (!name.isEmpty() && !productPrice.isEmpty() && imageBytes != null) {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafedb", "root", "");

                        String sql = "INSERT INTO `products` (`id`, `name`, `price`, `image`) VALUES (?,?,?,?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, Integer.toString(randomNum));
                        pstmt.setString(2, name);
                        pstmt.setString(3, productPrice);
                        pstmt.setBytes(4, imageBytes); // Set image as bytes

                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Successfully added item");
                        AddProduct.this.setVisible(false);
                        new Products().setVisible(true);
                        con.close();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields and select an image");
                }
            }
        });
        backgroundLabel.add(btnlogin);

        backgroundPanel.setVisible(true);
        add(backgroundPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AddProduct();
    }
}
