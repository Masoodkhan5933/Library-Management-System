/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dal.DALManager;
import dal.RecordsAdder;
import java.util.ArrayList;
import model.dto.BookDTO;
import model.dto.FeedbackDTO;
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

    public Response deleteBook(String selectedId) {
        Response objResponse = SMSFactory.getResponseInstance();
        objDAL.deleteBook(selectedId, objResponse);
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
//    Connection dbConnection = objDAL.getDBConnection(); // Assuming you have a method to get the database connection in objDAL

//    recordsAdder.addFeedback(feedback, objResponse, dbConnection);



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
