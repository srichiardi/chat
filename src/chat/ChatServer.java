package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatServer
{
	private Map < String , String > clientsRegistry = new HashMap < String , String >();

	// simple constructor
	public ChatServer () throws RemoteException, MalformedURLException
	{
		RemoteList srv = new RemoteListImpl();
    	Naming.rebind("rmi://localhost/listserver", srv);
	}
	
    public static void main(String args[]) throws Exception
    {
    	new ChatServer();
    }
}
