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
import java.util.ArrayList;
import model.SMSFactory;
import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

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

    public void saveBook(BookDTO objBook, Response objResponse) {
        System.out.println("I am in savebook method of dalmanager class");
        try{
            Connection  dbConnection = objConnection.getConnection();
            objAdder.addBook(objBook,objResponse,dbConnection); 
        }catch(Exception e){
        objResponse.messagesList.add(new Message("Ooops! Failed to create book, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));;
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
    
}
