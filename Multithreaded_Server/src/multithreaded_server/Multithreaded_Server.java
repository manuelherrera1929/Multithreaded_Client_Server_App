package multithreaded_server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static sun.rmi.transport.DGCAckHandler.received;
/**
 *
 * @author alejandrocruz
 */
public class Multithreaded_Server extends JFrame 
{
    private JTextField enterField; //input message form user
    private JTextArea displayArea; //display information to user
    private ObjectOutputStream output; //output stream to client
    private ObjectInputStream input; //input stream from client
    private ServerSocket server; //server socket
    private Socket connection; //connection to client
    private int counter = 1; //counter of number of connection
    
    //set up GUI
    public Multithreaded_Server()
    {
        super( "Server");
        
        enterField = new JTextField(); //create enterField
        enterField.setEditable(false);
        enterField.addActionListener(
                new ActionListener()
                {
                    //sends message to client
                    public void actionPerformed( ActionEvent event )
                    {
                        sendData( event.getActionCommand() );
                        enterField.setText( "" );
                    }// end method actionPerformed                           
                }// end anonymous inner class
        );// end call to addActionListener
        
        add( enterField, BorderLayout.NORTH );
        
        displayArea = new JTextArea();// create displayArea
        add( new JScrollPane( displayArea ), BorderLayout.CENTER);
        
        setSize( 300, 150 );// set size of window
        setVisible( true );// show window
    }// end server constructor
    
    //set up and run server
    public void runServer()
    {
        try// set up server to receive connections; precess connections
        {
            server = new ServerSocket( 3333, 100);// create server socket
            
            while ( true )
            {
                try
                {
                    waitForConnection();// wait for a connection
                    getStreams();// get input & output streams
                    processConnection();// process connection
                }// end try
                catch ( EOFException eofException )
                {
                    displayMessage( "\nServer terminated connection" );
                }//end catch
                finally
                {
                    closeConnection();// close connection
                    ++counter;
                }//end finally
            }//end while
        }//end try
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }//end catch
    }// end method runServer
    
    // wait for connection to arrive, then display connection info
    private void waitForConnection() throws IOException
    {
        displayMessage("Wating fro connection\n" );
        connection = server.accept();// allow server to accept connection
        displayMessage("Connection" + counter + "received from: " +
                connection.getInetAddress() );
    }// end method waitForConnection
    
    // get streams to send and receive data
    private void getStreams() throws IOException
    {
        //set up output stream for objects
        output = new ObjectOutputStream( connection.getOutputStream() );
        output.flush();// flush output buffer to send header information
        
        // set up input stream for objects
        input = new ObjectInputStream( connection.getInputStream() );
        
        displayMessage( "\nGot I/O stream\n");
    } // end of get streams
    
    //process connection with client
    private void processConnection() throws IOException
    {
        String message = "Connection successful";
        sendData(message);// send connection succesful message
        
        // enable enterfield so server user can send messages
        setTextFieldEditable( true );
        
        do //process messages sent from client
        {
            try // read message and display it
            {
                message = (String) input.readObject();// read new message
                displayMessage( "\n" + message );// display message
            }// end try
            catch ( ClassNotFoundException classNotFoundException )
            {
                displayMessage( "\nUnknown object typpe received");
            }// end catch
        }while ( !message.equals( "CLIENT... TERMINATE" ) ); 
    }// end method processConnection
    
    // close streams and socket
    private void closeConnection()
    {
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable(false);// diable enterField
        
        try
        {
            output.close(); //close output stream
            input.close();// close input stream
            connection.close();// close socket
        }// end try
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }// end catch
    }// end method closeConnection
    
    // send message to client
    private void sendData( String message)
    {
        try // send object to client
        {
            output.writeObject("SERVER>>> " + message );
            output.flush();// flush output to client
            displayMessage("\nSERVER>>> " + message );
        }// end try
        catch ( IOException ioExeception )
        {
            displayArea.append( "\nError writing object");
        }// end catch
    }// end method sendData
    
    // manipulates displayArea in the event-dispatch thread
    private void displayMessage( final String messageToDisplay )
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()// updates displayArea
                {
                    displayArea.append( messageToDisplay );// append message
                }// end method run
            }// end anonymous inner class
        );// end call to SwingUtilities.invokeLater
    }// end method displayMessage
    //manipulate enterField in the event-dispatch thread
    private void setTextFieldEditable( final boolean editable )
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()// sets enterField's editability
                    {
                        enterField.setEditable( editable );
                    }// end method run
                }// end inner class
        );// end call to SwingUtilities.invokeLater
    }// end method setTextFieldEditable
}// end class Server

