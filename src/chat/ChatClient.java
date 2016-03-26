package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ChatClient {
	
	private String clientName;
	private String clientUri;
	private RemoteList clientsList;
	private Message msgServer;
	
	public ChatClient(String name) throws MalformedURLException, RemoteException, NotBoundException
	{
		this.clientName = name;
		this.clientUri = "rmi://localhost/" + name;
		
		this.clientsList = (RemoteList)Naming.lookup("rmi://localhost/listserver");
		this.clientsList.setClient(name, this.clientUri);
		
		// setting up the client's internal chat server
		this.msgServer = new MessageImpl();
    	Naming.rebind(this.clientUri, this.msgServer);
	}
	
	public void printClients() throws RemoteException
	{
		Map<String, String> cList = this.clientsList.getList();
		Iterator<Entry<String, String>> it = cList.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public void sendMessage(String recipient, String msgText) throws RemoteException, MalformedURLException, NotBoundException
	{
		Map<String, String> cList = this.clientsList.getList();
		String rcpntUri = cList.get(recipient);
		Message rcpntSrv = (Message)Naming.lookup(rcpntUri);
		rcpntSrv.sendFrom(this.clientName, msgText);
	}
}
