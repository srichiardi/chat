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
	
	private Map<String, String> getMap() throws RemoteException
	{
		return this.clientsRegistry.getList();
	}
	
	private void dropClient(String clientName) throws RemoteException
	{
		this.clientsRegistry.removeClient(clientName);
	}
	
	public void checkConnectedClients() throws RemoteException
	{
		Set<String> clientSet = getMap().keySet();
		for (String key : clientSet)
		{
			try {
				String rcpntUri = getMap().get(key);
				Message rcpntSrv = (Message)Naming.lookup(rcpntUri);
			} catch (Exception e) {
				// assuming the exception is caused by the client being off-line
				dropClient(key);
			}
		}
	}
	
    public static void main(String args[]) throws Exception
    {
    	ChatServer srv = new ChatServer();
    	for(;;)
    	{
    		srv.checkConnectedClients();
    		// pause the scanning process for 10 seconds
			Thread.sleep(10000);
    	}
    }
}
