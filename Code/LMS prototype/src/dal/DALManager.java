/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

//import dal.IConnection;
//import dal.RecordsAdder;
//import dal.RecordsMapper;
//import dal.RecordsModifier;
//import dal.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.SMSFactory;
import model.dto.BookDTO;
import model.dto.FeedbackDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.PaymentDTO;
import model.dto.Response;
import model.dto.UserDTO;
import ui.librarianfunctionalitypage;


/**
 *
 * @author Mukhtiar
 */
public class DALManager {
    IConnection objConnection;
    DBReader objReader;
    RecordsMapper objMapper;
    RecordsAdder objAdder;
    RecordsModifier objModifier;
 

    public DALManager(RecordsMapper mapper){
    objConnection = new SQLConnection("","LMS", "sa","123456");
    objReader = new DBReader();
    objAdder = SMSFactory.getInstanceOfAdder();
    this.objMapper=mapper;
    objModifier = SMSFactory.getInstanceOfModifier();
    }
    public ArrayList<BookDTO> getBooksList(String searchKey) {
                
        Connection  dbConnection = objConnection.getConnection();
        String viewBooksQuery = "Select * from LMSdatabase";
        if(searchKey == null || searchKey.length() > 0)
        {
            viewBooksQuery += " where isbn LIKE '%"+searchKey+"%' OR name LIKE '%"+searchKey+"%' OR authorname LIKE '%"+searchKey+"%' OR category LIKE '%"+searchKey+"%' OR publishername LIKE '%";
        }
        ResultSet rs = objReader.getRecords(viewBooksQuery, dbConnection);
        return objMapper.getBooks(rs);        
    }  
    
    //login

public void login(String username, String password) {
    try {
        Connection dbConnection = objConnection.getConnection();
        
        // Create a prepared statement to check the validity of username and password
        String query = "SELECT status FROM users WHERE email = ? AND password = ?";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        
        // Execute the query
        ResultSet resultSet = statement.executeQuery();
        
        // Check if the query returned a result
        if (resultSet.next()) {
            String status = resultSet.getString("status");
            
            // Check if the user's status is blocked
            if (status.equals("Blocked")) {
                // User is blocked, display error dialog box
                JOptionPane.showMessageDialog(null, "User is blocked. Please contact support for assistance.", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (username.equals("admin")) {
                    new librarianfunctionalitypage();
                }
                
                // Display success dialog box
                JOptionPane.showMessageDialog(null, "Login successful!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Invalid credentials, display error dialog box
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Close the result set, statement, and database connection
        resultSet.close();
        statement.close();
        dbConnection.close();
    } catch (Exception e) {
        // Exception occurred during login, display error dialog box
        JOptionPane.showMessageDialog(null, "An error occurred during login.\n" + e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}


//search
public void searchBook(String searchQuery, Response objResponse) {
    System.out.println("I am in searchBook method of dalmanager class");
    try {
        Connection dbConnection = objConnection.getConnection();

        String sql = "SELECT * FROM Books WHERE name LIKE ? OR ISBN LIKE ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + searchQuery + "%");
        preparedStatement.setString(2, "%" + searchQuery + "%");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String ISBN = resultSet.getString("ISBN");
            String name = resultSet.getString("name");
            String authorname = resultSet.getString("authorname");
            String category = resultSet.getString("category");
            String publishername = resultSet.getString("publishername");

            // Do something with the retrieved data
        }

        if (objResponse.messagesList.isEmpty()) {
            objResponse.messagesList.add(new Message("No books found.", MessageType.ERROR));
        }
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("Ooops! Failed to search for books. Please contact support.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

//returnbook

public Response returnBook(String bookId) {
    Response objResponse = new Response();

    try {
        Connection dbConnection = objConnection.getConnection();

        // Create a prepared statement to update the book status as returned
        String query = "UPDATE books SET status = ? WHERE book_id = ?";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, "Returned");
        statement.setString(2, bookId);

        // Execute the update
        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            // Book returned successfully
            objResponse.messagesList.add(new Message("Book returned successfully!", MessageType.SUCCESS));
        } else {
            // Failed to return book (book not found or already returned)
            objResponse.messagesList.add(new Message("Failed to return the book. Please check the book ID.", MessageType.ERROR));
        }

        // Close the statement and database connection
        statement.close();
        dbConnection.close();
    } catch (Exception e) {
        // Exception occurred during book return
        objResponse.messagesList.add(new Message("An error occurred during book return.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }

    return objResponse;
}

 

    //GENERATEFINE
     public void generateFine(String searchQuery, Response objResponse) {
        try {
            Connection dbConnection = objConnection.getConnection();
            PreparedStatement p = dbConnection.prepareStatement("SELECT * FROM Books WHERE name LIKE ? OR ISBN LIKE ?");
            p.setString(1, "%" + searchQuery + "%");
            p.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = p.executeQuery();

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String name = resultSet.getString("name");
                String authorname = resultSet.getString("authorname");
                String category = resultSet.getString("category");
                String publishername = resultSet.getString("publishername");

                // Calculate fine based on due date and current date
                LocalDate dueDate = resultSet.getDate("dueDate").toLocalDate();
                LocalDate currentDate = LocalDate.now();
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
                double fineAmount = daysOverdue * 0.5; // Assuming a fine of $0.50 per day

                // Add the fine information to the response
                String fineMessage = "Fine for book \"" + name + "\" (ISBN: " + ISBN + "): $" + fineAmount;
                objResponse.messagesList.add(new Message(fineMessage, MessageType.INFO));
            }

            if (objResponse.messagesList.isEmpty()) {
                objResponse.messagesList.add(new Message("No books found.", MessageType.ERROR));
            }
        } catch (Exception e) {
            objResponse.messagesList.add(new Message("Ooops! Failed to generate fine. Please contact support.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }


    public void saveBook(BookDTO objBook, Response objResponse) {
        System.out.println("I am in savebook method of dalmanager class");
        try{
            Connection  dbConnection = objConnection.getConnection();
            objAdder.addBook(objBook,objResponse,dbConnection); 
        }catch(Exception e){
        objResponse.messagesList.add(new Message("Ooops! Failed to create book, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }

    public Response deleteBook(String ISBN, Response objResponse) {
        try{
            Connection  dbConnection = objConnection.getConnection();
            objModifier.deleteBook(ISBN,objResponse,dbConnection);
            return  objResponse;           
        }catch(Exception e){
        objResponse.messagesList.add(new Message("Ooops! Failed to delete book, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
        return null;
    }
    
     public void borrowBook(String selectedId, String userId, Response objResponse) {
        try {
            Connection dbConnection = objConnection.getConnection();
            objModifier.borrowBook(selectedId, userId, objResponse, dbConnection);
        } catch (Exception e) {
            objResponse.messagesList.add(new Message("Oops! Failed to borrow the book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
     
    
    // ...

 public void registerAccount(UserDTO objUser, Response objResponse) {
    try {
        Connection dbConnection = objConnection.getConnection();
        objAdder.registerAccount(objUser, objResponse, dbConnection);

        if (objResponse.isSuccessful()) {
            objResponse.messagesList.add(new Message("Account registered successfully.", MessageType.NOTIFICATION));
        } else {
            objResponse.messagesList.add(new Message("Failed to register the account. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("Oops! Failed to register the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
 
 public void saveFeedback(FeedbackDTO feedback, Response response) {
    try {
        Connection dbConnection = objConnection.getConnection();
        objAdder.addFeedback(feedback, response, dbConnection);

        if (response.isSuccessful()) {
            response.getMessagesList().add(new Message("Feedback registered successfully.", MessageType.NOTIFICATION));
        } else {
            response.getMessagesList().add(new Message("Failed to register the feedback. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (Exception e) {
        response.getMessagesList().add(new Message("Oops! Failed to register the feedback. Please contact support for assistance.", MessageType.ERROR));
        response.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
 
public void savePayment(PaymentDTO payment, Response response) {
    try {
        Connection dbConnection = objConnection.getConnection();
        objAdder.addPayment(payment, response, dbConnection);
    } catch (Exception e) {
        response.setSuccess(false);
        response.getMessagesList().add(new Message("Oops! Failed to save payment. Please contact support for assistance.", MessageType.ERROR));
        response.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

    public void deleteAccount(String username, Response objResponse) {
               try {
        Connection dbConnection = objConnection.getConnection();
        objModifier.deleteAccount(username, objResponse, dbConnection);
    } catch (Exception e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to delete the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    }

    public void orderBook(String bookISBN, String userId, Response objResponse) {
           try {
        Connection dbConnection = objConnection.getConnection();
        objModifier.orderBook(bookISBN, userId, objResponse, dbConnection);
    } catch (Exception e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to order the book. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    }


    public void reserveBook(String bookISBN, String userId, Response objResponse) {
           try {
        Connection dbConnection = objConnection.getConnection();
        objModifier.reserveBook(bookISBN, userId, objResponse, dbConnection);
    } catch (Exception e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to reserve the book. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    }
    

    public void blockAccount(String username, Response objResponse) {
          try {
        Connection dbConnection = objConnection.getConnection();
        objModifier.blockAccount(username, objResponse, dbConnection);
    } catch (Exception e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to block the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    }

 //logout
    public Response logout() {
    Response objResponse = new Response();

    try {
        // Perform logout operations, such as clearing session data, closing connections, etc.

        // Add a success message to the response
        objResponse.messagesList.add(new Message("Logout successful!", MessageType.SUCCESS));
    } catch (Exception e) {
        // Exception occurred during logout
        objResponse.messagesList.add(new Message("An error occurred during logout.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }

    return objResponse;
}

    

}

   

