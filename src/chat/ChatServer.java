package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatServer
{
	private RemoteList clientsRegistry = new RemoteListImpl();

	// simple constructor
	public ChatServer () throws RemoteException, MalformedURLException
	{
    	Naming.rebind("rmi://localhost/listserver", this.clientsRegistry);
	}
	
	public Map<String, String> getMap() throws RemoteException
	{
		return this.clientsRegistry.getList();
	}
	
	public void dropClient(String clientName) throws RemoteException
	{
		this.clientsRegistry.removeClient(clientName);
	}
	
    public static void main(String args[]) throws Exception
    {
    	ChatServer srv = new ChatServer();
    	for(;;)
    	{
    		Set<String> clientSet = srv.getMap().keySet();
    		for (String key : clientSet)
    		{
    			try {
    				String rcpntUri = srv.getMap().get(key);
    				Message rcpntSrv = (Message)Naming.lookup(rcpntUri);
    			} catch (RemoteException e) {
    				// assuming the exception is caused by the client offline
    				srv.dropClient(key);
    			}
    		}
    		// pause the scanning process for 10 seconds
			Thread.sleep(10000);
    	}
    }
}
