/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dal.DALManager;
import java.util.ArrayList;
import model.dto.BookDTO;
import model.dto.Response;
import model.validators.CommonValidator;

/**
 *
 * @author Mukhtiar
 */
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
        if(objResponse.isSuccessfull()){
            objDAL.saveBook(objBook,objResponse);
        }
        return objResponse;
    }

    public Response deleteBook(String isbn) {
        Response objResponse = SMSFactory.getResponseInstance();
        objDAL.deleteBook(isbn, objResponse);
        return objResponse;
    }
       
}
