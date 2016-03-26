package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class RemoteListImpl extends UnicastRemoteObject implements RemoteList
{
	
	private Map < String , String > clientsRegistry = new HashMap < String , String >();

	// simple constructor
	public RemoteListImpl () throws RemoteException {}

	// getList function, checks if the client requesting is in the list first
	public Map<String, String> getList() throws RemoteException
	{
		return this.clientsRegistry;
	}

	// setClient function, adds a client to the registry
	public void setClient(String clientName, String clientURI) throws RemoteException
	{
		if (!this.clientsRegistry.containsKey(clientName))
		{
			this.clientsRegistry.put(clientName, clientURI);
		}
	}
	
	public void removeClient(String clientName) throws RemoteException
	{
		this.clientsRegistry.remove(clientName);
	}
}
