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

public final class Order extends JFrame {

    Connection con = null;
    List<String> selectedItems = new ArrayList<>();
    private String loggedInUsername;
    int orderId = (int) (Math.random() * 101);

    public Order(String username) {
        loggedInUsername = username;
        System.out.print(username);
        NewOrder();
    }

    public void NewOrder() {
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

                JButton addButton = new JButton("Add to list");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (selectedItems.size() < 10) {
                            addItemToSelectedList(productName);
                        } else {
                            JOptionPane.showMessageDialog(Order.this, "You can't add more than 10 items.");
                        }
                    }
                });
                itemPanel.add(addButton);

                x += 210;

                if (x >= 681) {
                    y = 330;
                    x = 50;
                }
            }

            JScrollPane wishlistScrollPane = new JScrollPane();
            wishlistScrollPane.setBounds(900, 60, 380, 550); // Set the bounds as required
            backgroundLabel.add(wishlistScrollPane);

            JPanel wishlistPanel = new JPanel();
            wishlistPanel.setLayout(new BoxLayout(wishlistPanel, BoxLayout.Y_AXIS));
            wishlistScrollPane.setViewportView(wishlistPanel);

            JLabel top = new JLabel("Susie's Cafe");
            top.setBounds(10, 70, 200, 30);
            top.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
            wishlistPanel.add(top);

            JLabel yourorder = new JLabel("Your Selected List");
            yourorder.setBounds(100, 100, getWidth(), 30);
            yourorder.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            wishlistPanel.add(yourorder);

            JButton clear = new JButton("Clear");
            clear.setBounds(920, 630, 150, 35);
            clear.setBackground(Color.red);
            clear.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedItems.clear();
                    updateWishlist();
                }
            });
            backgroundLabel.add(clear);
            JButton placeorder = new JButton("Place Order");
            placeorder.setBounds(1100, 630, 150, 35);
            placeorder.setBackground(Color.CYAN);
            placeorder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedItems.isEmpty()) {
                        JOptionPane.showMessageDialog(Order.this, "Your selected list is empty.");
                        return;
                    }

                    int userId = getCurrentUserId(loggedInUsername);
                    int a = JOptionPane.showConfirmDialog(null, "Do you really want to place this order ?", "Select", JOptionPane.YES_NO_OPTION);
                    if (a == 0) {
                        try {
                            String insertOrderQuery = "INSERT INTO `order` ( order_id, user_id, product_name, product_price) VALUES ( ?, ?, ?, ?)";
                            PreparedStatement insertOrderStmt = con.prepareStatement(insertOrderQuery);
                            for (String selectedItem : selectedItems) {
                                int itemPrice = getItemPrice(selectedItem);
                                insertOrderStmt.setInt(1, orderId);
                                insertOrderStmt.setInt(2, userId);
                                insertOrderStmt.setString(3, selectedItem);
                                insertOrderStmt.setInt(4, itemPrice);
                                insertOrderStmt.executeUpdate();
                            }
                            JOptionPane.showMessageDialog(Order.this, "Order placed successfully!");
                            selectedItems.clear();
                            updateWishlist();
                            Order.this.setVisible(false);
                            new Thankyou().setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Order.this, "Error placing the order.");
                        }
                    }
                }
            });
            backgroundLabel.add(placeorder);
            JPanel buttons = new JPanel();
            buttons.setBounds(900, 610, 380, 90);
            backgroundLabel.add(buttons);
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error1: " + e.getMessage());
        }
    }
    private void updateWishlist() {
        JPanel wishlistPanel = new JPanel();
        wishlistPanel.setLayout(new BoxLayout(wishlistPanel, BoxLayout.Y_AXIS));

        JLabel top = new JLabel("Susie's Cafe");
        top.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        wishlistPanel.add(top);

        JLabel yourorder = new JLabel("Your Selected List");
        yourorder.setBounds(100, 100, getWidth(), 30);
        yourorder.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        wishlistPanel.add(yourorder);

        double total = calculateTotalPrice();
        for (String selectedItem : selectedItems) {
            JLabel itemLabel = new JLabel(selectedItem + " - Price: Ghc " + getItemPrice(selectedItem));
            itemLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            wishlistPanel.add(itemLabel);
        }

        JLabel totalLabel = new JLabel("Total Price: Ghc " + total);
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        wishlistPanel.add(totalLabel);

        JScrollPane wishlistScrollPane = new JScrollPane(wishlistPanel);
        wishlistScrollPane.setPreferredSize(new Dimension(380, 550));

        wishlistScrollPane.setBounds(900, 60, 380, 550); 
        getContentPane().add(wishlistScrollPane);

        wishlistPanel.revalidate();
        wishlistPanel.repaint();
    }
    private int getItemPrice(String itemName) {
        try {
            String query = "SELECT price FROM products WHERE name = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, itemName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; 
    }
    private void addItemToSelectedList(String item) {
        selectedItems.add(item);
        updateWishlist(); 
    }
    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (String selectedItem : selectedItems) {
            totalPrice += getItemPrice(selectedItem);
        }
        return totalPrice;
    }
    private int getCurrentUserId(String username1) {
        try {
            String query = "SELECT id FROM user WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username1);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

}
