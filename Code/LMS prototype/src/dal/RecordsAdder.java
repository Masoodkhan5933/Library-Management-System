/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;
import model.dto.UserDTO;
import dal.IConnection;

import dal.SQLConnection;
import model.dto.FeedbackDTO;
import model.dto.PaymentDTO;



public class RecordsAdder {
    
    private IConnection objConnection;

    public RecordsAdder() {
      
    }
   

    void addBook(BookDTO objBook, Response objResponse, Connection dbConnection) {
        try{
            System.out.println("i am adder to database");
            PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Books (ISBN , name , authorname , category , publishername) VALUES (?,?,?,?,?);");
            p.setString(1, objBook.ISBN);
            p.setString(2, objBook.authorname);
            p.setString(3, objBook.category);
            p.setString(4, objBook.name);
            p.setString(5, objBook.publishername);
            int rowsInserted = p.executeUpdate();
            if(rowsInserted > 0){
                objResponse.messagesList.add(new Message("Book added successfully.", MessageType.NOTIFICATION));
            }
        }catch(SQLException e){
            objResponse.messagesList.add(new Message("Ooops! Failed to create employee, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    public void deleteBook(String bookISBN, Response objResponse, Connection dbConnection) {
        try {
            PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Books WHERE ISBN = ?;");
            p.setString(1, bookISBN);
            int rowsDeleted = p.executeUpdate();
            if (rowsDeleted > 0) {
                objResponse.messagesList.add(new Message("Book deleted successfully.", MessageType.NOTIFICATION));
            } else {
                objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Ooops! Failed to delete book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    
     public void borrowBook(String bookISBN, String userId, Response objResponse, Connection dbConnection) {
        try {
            System.out.println("i am borrow book record modifier");
            // Check if the book exists
            PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Books WHERE ISBN = ?;");
            checkBook.setString(1, bookISBN);
            if (checkBook.executeQuery().next()) {
                // Check if the user exists
                PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE id = ?;");
                checkUser.setString(1, userId);
                if (checkUser.executeQuery().next()) {
                    // Update the book's availability status
                    PreparedStatement borrowBook = dbConnection.prepareStatement("UPDATE Books SET availability = 'Unavailable' WHERE ISBN = ?;");
                    borrowBook.setString(1, bookISBN);
                    int rowsUpdated = borrowBook.executeUpdate();
                    if (rowsUpdated > 0) {
                        objResponse.messagesList.add(new Message("Book borrowed successfully.", MessageType.NOTIFICATION));
 }
                } else {
                    objResponse.messagesList.add(new Message("User with ID " + userId + " does not exist.", MessageType.WARNING));
                }
            } else {
                objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Oops! Failed to borrow the book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
     
   public void registerAccount(UserDTO objUser, Response objResponse,Connection dbConnection) {
    try {
//        Connection dbConnection = objConnection.getConnection();
       System.out.println("i am regisyer account method in record adder");  

        PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?);");
        p.setString(1, objUser.getUsername());
        p.setString(2, objUser.getPassword());
        int rowsInserted = p.executeUpdate();
        if (rowsInserted > 0) {
            objResponse.messagesList.add(new Message("Account registered successfully.", MessageType.NOTIFICATION));
        } else {
            objResponse.messagesList.add(new Message("Failed to register the account. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to register the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
   
   public void addFeedback(FeedbackDTO feedback, Response objResponse, Connection dbConnection) {
    try {
        System.out.println("I am addFeedback method in RecordsAdder");
        PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Feedback (username, feedback) VALUES (?, ?);");
        p.setString(1, feedback.getUsername());
        p.setString(2, feedback.getFeedback());
        int rowsInserted = p.executeUpdate();
        if (rowsInserted > 0) {
            objResponse.getMessagesList().add(new Message("Feedback added successfully.", MessageType.NOTIFICATION));
        } else {
            objResponse.getMessagesList().add(new Message("Failed to add feedback. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to add feedback. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

  public void addPayment(PaymentDTO payment, Response response, Connection dbConnection) {
    try {
        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO Payments (paymentId, amount, paymentDate) VALUES (?, ?, ?);");
        statement.setString(1, payment.getPaymentId());
        statement.setDouble(2, payment.getAmount());
        statement.setDate(3, java.sql.Date.valueOf(payment.getPaymentDate()));
        
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            response.setSuccess(true);
            response.getMessagesList().add(new Message("Payment added successfully.", MessageType.NOTIFICATION));
        } else {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Failed to add payment.", MessageType.ERROR));
        }
    } catch (SQLException e) {
        response.setSuccess(false);
        response.getMessagesList().add(new Message("Oops! Failed to add payment. Please contact support for assistance.", MessageType.ERROR));
        response.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
 
  public void deleteAccount(String username, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Users WHERE username = ?");
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


}
