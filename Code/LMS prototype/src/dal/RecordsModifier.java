/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

/**
 *
 * @author Mukhtiar-HPC
 */
public class RecordsModifier {

    void deleteBook(String isbn, Response objResponse, Connection dbConnection) {
        try{
            PreparedStatement p;// (FirstName,LastName,Title) VALUES (?,?,?);");
            p = dbConnection.prepareStatement("delete from book where ISBN=?");
            p.setString(1, isbn);
            int rowsInserted = p.executeUpdate();
            if(rowsInserted > 0){
                objResponse.messagesList.add(new Message("Employee deleted successfully.", MessageType.NOTIFICATION));
            }
        }catch(SQLException e){
            objResponse.messagesList.add(new Message("Ooops! Failed to create employee, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    
}
