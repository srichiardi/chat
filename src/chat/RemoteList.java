package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteList extends Remote
{
	public Map<String, String> getList() throws RemoteException;
	
	public void setClient(String clientName, String clientURI) throws RemoteException;
	
	public void removeClient(String clientName) throws RemoteException;

}
