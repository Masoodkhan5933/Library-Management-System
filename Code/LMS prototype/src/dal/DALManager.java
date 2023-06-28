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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.SMSFactory;
import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.PaymentDTO;
import model.dto.Response;
import model.dto.UserDTO;


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
    objConnection = new SQLConnection("MUKHTIAR-WPC\\SQLEXPRESS","Northwind", "sa","7intin");
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

    public Response deleteBook(String selectedId, Response objResponse) {
        try{
            Connection  dbConnection = objConnection.getConnection();
            objModifier.deleteBook(selectedId,objResponse,dbConnection);
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
//        objAdder.registerAccount(objUser, objResponse, dbConnection);

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
 
 public void saveFeedback(String username, String feedback, Response response) {
    // Perform the necessary operations to save the feedback
    // For example, you can write code to insert the feedback into a database or store it in a file
    
    // Simulating a successful feedback save
    boolean savedSuccessfully = true;
    
    if (savedSuccessfully) {
        response.setSuccess(true);
        response.getMessagesList().add(new Message("Feedback saved successfully.", MessageType.NOTIFICATION));
    } else {
        response.setSuccess(false);
        response.getMessagesList().add(new Message("Failed to save feedback.", MessageType.ERROR));
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



}

   

