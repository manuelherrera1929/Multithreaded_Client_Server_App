package multithreaded_server;

import javax.swing.JFrame;

/**
 *
 * @author alejandrocruz
 */
public class ServerTest 
{
    public static void main( String[] args )
    {
    Multithreaded_Server application = new Multithreaded_Server(); // create server
    application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    application.runServer();
    }   
}
