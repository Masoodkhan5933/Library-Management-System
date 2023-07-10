/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dal.DALManager;
import dal.RecordsAdder;
import dal.RecordsModifier;
import java.util.ArrayList;
import model.dto.BookDTO;
import model.dto.FeedbackDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.PaymentDTO;
import model.dto.Response;
import model.dto.UserDTO;
import model.validators.PaymentValidator;

public class LMSController {
    DALManager objDAL;
    public LMSController(){
    objDAL = SMSFactory.getInstanceOfDALManager();
    }

    public ArrayList<BookDTO> viewBooks(String searchKey) {
        return objDAL.getBooksList(searchKey);
    }

    //login
    public Response login(String username, String password) {
    Response objResponse = SMSFactory.getResponseInstance();
    try {
        ObjAuthenticator.authenticateUser(username, password);
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("An error occurred during login.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    return objResponse;
}
    //returnbook
    public Response returnBook(String bookId) {
    Response objResponse = SMSFactory.getResponseInstance();
    try {
        RecordsModifier recordsModifier = new RecordsModifier();
        recordsModifier.returnBook( bookId, objResponse);
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("An error occurred during book return.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
    return objResponse;
}


    //generatefine
      public Response generateFine(String searchQuery) {
        Response objResponse = SMSFactory.getResponseInstance();
        objDAL.generateFine(searchQuery, objResponse);
        return objResponse;
    }
    
    public Response addBook(BookDTO objBook) {
        System.out.println("i am addbook method in LMs Controller");
        Response objResponse = SMSFactory.getResponseInstance();
//        CommonValidator.validateBook(objBook,objResponse);
        if(objResponse.isSuccessful()){
            objDAL.saveBook(objBook,objResponse);
        }
        return objResponse;
    }
    
    
    
    //USE CASE BY SOFIA (DELETE BOOK)

    public Response deleteBook(String isbn) {
        Response objResponse = SMSFactory.getResponseInstance();
        objDAL.deleteBook(isbn, objResponse);
        return objResponse;
    }
    
    //USE CASE BY SOFIA (BORROW BOOK)
          
     public Response borrowBook(String selectedId, String userId) {
       System.out.println("i am borrow book method in LMs Controller");  
        Response objResponse = SMSFactory.getResponseInstance();
        objDAL.borrowBook(selectedId, userId, objResponse);
        return objResponse;
    }
     
     
 public Response registerAccount(String username, String password) {
            System.out.println("i am register account method in LMs Controller");  

    Response objResponse = SMSFactory.getResponseInstance();
    UserDTO objUser = new UserDTO(username, password);
    objDAL.registerAccount(objUser, objResponse);
    return objResponse;
}
 
public Response giveFeedback(FeedbackDTO feedback) {
    System.out.println("I am giveFeedback method in LMS Controller");
    Response objResponse = SMSFactory.getResponseInstance();
    RecordsAdder recordsAdder = new RecordsAdder();
////    Connection dbConnection = objDAL.getDBConnection(); // Assuming you have a method to get the database connection in objDAL
//
////    recordsAdder.addFeedback(feedback, objResponse, dbConnection);
//


    return objResponse;
}

    public Response makePayment(PaymentDTO payment) {
        System.out.println("I am makePayment method in PaymentController");
        Response objResponse = SMSFactory.getResponseInstance();
        PaymentValidator validator = new PaymentValidator();
        validator.validatePayment(payment, objResponse);
        if (objResponse.isSuccessful()) {
            objDAL.savePayment(payment, objResponse);

        }
        return objResponse;
    }
    
    public Response deleteAccount(String username) {
    System.out.println("I am deleteAccount method in LMS Controller");
    Response objResponse = SMSFactory.getResponseInstance();
    objDAL.deleteAccount(username, objResponse);
    return objResponse;
}


    public Response orderBook(String bookISBN, String userId) {
    System.out.println("I am orderBook method in LMS Controller");
    Response objResponse = SMSFactory.getResponseInstance();
    objDAL.orderBook(bookISBN, userId, objResponse);
    return objResponse;
}
    
    public Response reserveBook(String bookISBN, String userId) {
    System.out.println("I am reserveBook method in LMS Controller");
    Response objResponse = SMSFactory.getResponseInstance();
    objDAL.reserveBook(bookISBN, userId, objResponse);
    return objResponse;
}

    
    public Response blockAccount(String username) {
    System.out.println("I am blockAccount method in LMS Controller");
    Response objResponse = SMSFactory.getResponseInstance();
    objDAL.blockAccount(username, objResponse);
    return objResponse;
}


}
