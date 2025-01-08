import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryManagementSystem extends JFrame implements ActionListener {
    JTextField bookNameField, authorField;
    JButton addButton, viewButton;
    Connection connection;

    public LibraryManagementSystem() {
        super("Library Management System");

        bookNameField = new JTextField(20);
        authorField = new JTextField(20);
        addButton = new JButton("Add Book");
        viewButton = new JButton("View Books");

        setLayout(new GridLayout(3, 2));
        add(new JLabel("Book Name: "));
        add(bookNameField);
        add(new JLabel("Author: "));
        add(authorField);
        add(addButton);
        add(viewButton);

        addButton.addActionListener(this);
        viewButton.addActionListener(this);

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            // Establishing connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String bookName = bookNameField.getText();
            String author = authorField.getText();

            try {
                // Creating a PreparedStatement object
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO books (name, author) VALUES (?, ?)");
                preparedStatement.setString(1, bookName);
                preparedStatement.setString(2, author);

                // Executing the SQL statement
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book added successfully!");

                // Closing the PreparedStatement
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == viewButton) {
            try {
                // Creating a Statement object
                Statement statement = connection.createStatement();

                // Executing SQL query
                ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

                // Displaying the results
                StringBuilder result = new StringBuilder();
                result.append("Books:\n");
                while (resultSet.next()) {
                    result.append(resultSet.getString("name")).append(", ")
                            .append(resultSet.getString("author")).append("\n");
                }
                JOptionPane.showMessageDialog(this, result.toString());

                // Closing the Statement and ResultSet
                statement.close();
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new LibraryManagementSystem();
    }
}
