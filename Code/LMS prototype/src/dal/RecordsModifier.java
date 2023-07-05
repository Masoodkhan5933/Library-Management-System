/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

/**
 *
 * @author Mukhtiar-HPC
 */
public class RecordsModifier {

    void deleteBook(String selectedId, Response objResponse, Connection dbConnection) {
        try{
            PreparedStatement p;// (FirstName,LastName,Title) VALUES (?,?,?);");
            p = dbConnection.prepareStatement("delete from Employees where EmployeeID=?");
            p.setString(1, selectedId);
            int rowsInserted = p.executeUpdate();
            if(rowsInserted > 0){
                objResponse.messagesList.add(new Message("Employee deleted successfully.", MessageType.NOTIFICATION));
            }
        }catch(SQLException e){
            objResponse.messagesList.add(new Message("Ooops! Failed to create employee, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    
     void borrowBook(String bookISBN, String userId, Response objResponse, Connection dbConnection) {
        try {
            PreparedStatement p = dbConnection.prepareStatement("UPDATE Books SET Availability = ? WHERE ISBN = ? AND Availability = ?");
            p.setString(1, "Unavailable");
            p.setString(2, bookISBN);
            p.setString(3, "Available");
            int rowsUpdated = p.executeUpdate();
            if (rowsUpdated > 0) {
                objResponse.messagesList.add(new Message("Book borrowed successfully.", MessageType.NOTIFICATION));
            } else {
                objResponse.messagesList.add(new Message("Failed to borrow the book. Please check the availability or contact support for assistance.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Oops! Failed to borrow the book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
     
      void createAccount(String username, String password, Response objResponse, Connection dbConnection) {
        try {
            PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Accounts (Username, Password) VALUES (?, ?)");
            p.setString(1, username);
            p.setString(2, password);
            int rowsInserted = p.executeUpdate();
            if (rowsInserted > 0) {
                objResponse.messagesList.add(new Message("Account created successfully.", MessageType.NOTIFICATION));
            } else {
                objResponse.messagesList.add(new Message("Failed to create the account. Please contact support for assistance.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Oops! Failed to create the account. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
      
 void deleteAccount(String username, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Accounts WHERE Username = ?");
        p.setString(1, username);
        int rowsDeleted = p.executeUpdate();
        if (rowsDeleted > 0) {
            objResponse.getMessagesList().add(new Message("Account deleted successfully.", MessageType.NOTIFICATION));
        } else {
            objResponse.getMessagesList().add(new Message("Failed to delete the account. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to delete the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

 public void orderBook(String bookISBN, String userId, Response objResponse, Connection dbConnection) {
    try {
        System.out.println("I am orderBook method in RecordsModifier");
        // Check if the book exists
        PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Books WHERE ISBN = ?;");
        checkBook.setString(1, bookISBN);
        if (checkBook.executeQuery().next()) {
            // Check if the user exists
            PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE id = ?;");
            checkUser.setString(1, userId);
            if (checkUser.executeQuery().next()) {
                // Perform the order logic here
                // ...
                
                // Example: Update the book's availability status
                PreparedStatement orderBook = dbConnection.prepareStatement("UPDATE Books SET availability = 'Ordered' WHERE ISBN = ?;");
                orderBook.setString(1, bookISBN);
                int rowsUpdated = orderBook.executeUpdate();
                if (rowsUpdated > 0) {
                    objResponse.messagesList.add(new Message("Book ordered successfully.", MessageType.NOTIFICATION));
                }
            } else {
                objResponse.messagesList.add(new Message("User with ID " + userId + " does not exist.", MessageType.WARNING));
            }
        } else {
            objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to order the book. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

public void reserveBook(String bookISBN, String userId, Response objResponse, Connection dbConnection) {
    try {
        System.out.println("I am reserveBook method in RecordsModifier");
        // Check if the book exists
        PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Books WHERE ISBN = ?;");
        checkBook.setString(1, bookISBN);
        if (checkBook.executeQuery().next()) {
            // Check if the user exists
            PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE id = ?;");
            checkUser.setString(1, userId);
            if (checkUser.executeQuery().next()) {
                // Perform the reservation logic here
                // ...
                
                // Example: Update the book's availability status
                PreparedStatement reserveBook = dbConnection.prepareStatement("UPDATE Books SET availability = 'Reserved' WHERE ISBN = ?;");
                reserveBook.setString(1, bookISBN);
                int rowsUpdated = reserveBook.executeUpdate();
                if (rowsUpdated > 0) {
                    objResponse.messagesList.add(new Message("Book reserved successfully.", MessageType.NOTIFICATION));
                }
            } else {
                objResponse.messagesList.add(new Message("User with ID " + userId + " does not exist.", MessageType.WARNING));
            }
        } else {
            objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to reserve the book. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

public void blockAccount(String username, Response objResponse, Connection dbConnection) {
    try {
        System.out.println("I am blockAccount method in RecordsModifier");
        // Check if the user exists
        PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE username = ?;");
        checkUser.setString(1, username);
        ResultSet userResultSet = checkUser.executeQuery();
        if (userResultSet.next()) {
            // Perform the account blocking logic here
            // ...
            
            // Example: Update the user's account status to blocked
            PreparedStatement blockUser = dbConnection.prepareStatement("UPDATE Users SET status = 'Blocked' WHERE username = ?;");
            blockUser.setString(1, username);
            int rowsUpdated = blockUser.executeUpdate();
            if (rowsUpdated > 0) {
                objResponse.messagesList.add(new Message("Account blocked successfully.", MessageType.NOTIFICATION));
            }
        } else {
            objResponse.messagesList.add(new Message("User with username " + username + " does not exist.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to block the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

    
}