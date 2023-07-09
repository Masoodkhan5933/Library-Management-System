
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

/**
 *
 * @author Mukhtiar-HPC
 */
public class RecordsAdder {

    void addBook(BookDTO objBook, Response objResponse, Connection dbConnection) {
        try{
            System.out.println("i am adder to database");
            PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Book (ISBN , title , authorname , publishername,category) VALUES (?,?,?,?,?);");
            p.setString(1, objBook.ISBN);
            p.setString(2, objBook.name);
            p.setString(3, objBook.authorname);
            p.setString(4, objBook.publishername);
            p.setString(5, objBook.category);
            int rowsInserted = p.executeUpdate();
            if(rowsInserted > 0){
                JOptionPane.showMessageDialog(null, "Book added Sucessfully");
                objResponse.messagesList.add(new Message("Book added successfully.", MessageType.NOTIFICATION));
            }
        }catch(SQLException e){
            objResponse.messagesList.add(new Message("Ooops! Failed to create employee, Please contact support that there an issue while saving new employee.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    public void deleteBook(String bookISBN, Response objResponse, Connection dbConnection) {
        try {
            PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Book WHERE ISBN = ?;");
            p.setString(1, bookISBN);
            int rowsDeleted = p.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Book delete Sucessfully");
                
                objResponse.messagesList.add(new Message("Book deleted successfully.", MessageType.NOTIFICATION));
            } else {
                objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Ooops! Failed to delete book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }

}
