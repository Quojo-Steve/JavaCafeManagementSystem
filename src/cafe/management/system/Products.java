package cafe.management.system;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public final class Products extends JFrame {

    Connection con = null;
    
    public Products() {
        NewProducts();
    }

    public void NewProducts() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafedb", "root", "");
            String query = "SELECT * FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            setSize(1300, 760);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a JLabel with a background image
            JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED\\Documents\\NetBeansProjects\\Lab\\Cafe Management System\\src\\images\\Home page.jpg"));
            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
            add(backgroundLabel);

            // Other components and content setup
            JLabel head = new JLabel();
            head.setText("Welcome to Susie's Cafe");
            head.setForeground(Color.WHITE);
            head.setBounds(500, 20, 300, 40);
            head.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
            backgroundLabel.add(head); // Add components to the background label

            // List of JPanels for items
            List<JPanel> itemPanels = new ArrayList<>(); // Use List<JPanel>

            // Fetch and display items using a loop
            int x = 50;
            int y = 60;
            //rendering on screen
            while (rs.next()) {
                JPanel itemPanel = new JPanel();
                itemPanel.setBounds(x, y, 200, 250);
                backgroundLabel.add(itemPanel);
                itemPanels.add(itemPanel);

                // Fetch item details
                String productName = rs.getString("name");
                double productPrice = rs.getDouble("price");
                byte[] productImageBytes = rs.getBytes("image");
                ImageIcon productImageIcon = new ImageIcon(productImageBytes);

                // Create and add components to the itemPanel
                JLabel imageLabel = new JLabel(new ImageIcon(productImageIcon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH)));
                itemPanel.add(imageLabel);

                JLabel nameLabel = new JLabel("Name: " + productName);
                nameLabel.setBounds(200, 20, 100, 30);
                nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                itemPanel.add(nameLabel);

                JLabel priceLabel = new JLabel("Price: Ghc " + productPrice);
                priceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
                itemPanel.add(priceLabel);

                x += 210;

                if (x >= 900) {
                    y = 330;
                    x = 50;
                }
            }
//            JPanel buttons = new JPanel();
//            buttons.setBounds(900, 610, 380, 90);
//            backgroundLabel.add(buttons);
            
            JButton newproduct = new JButton("Add new product");
            newproduct.setBounds(500, 610, 200, 30);
            newproduct.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Products.this.setVisible(false);
                    new AddProduct().setVisible(true);
                }
            });
            backgroundLabel.add(newproduct);
            
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error1: " + e.getMessage());
        }
    }

}
