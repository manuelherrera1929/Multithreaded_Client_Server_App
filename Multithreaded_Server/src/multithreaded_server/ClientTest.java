package multithreaded_server;

import javax.swing.JFrame;

/**
 *
 * @author alejandrocruz
 */
public class ClientTest 
{
    public static void main( String [] args )
    {
        Client application; // declare client application
        
        // if no command line args
        if ( args.length == 0 )
            application = new Client( "localhost"); // connect to localhost
        else
            application = new Client( args[ 0 ]); // use args to connect
        
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runClient();
    } // end main
} // end class ClientTest
