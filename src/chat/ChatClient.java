package chat;

import java.rmi.Naming;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ChatClient {
	
	private String clientName;
	private String clientUri;
	
	public ChatClient(String name)
	{
		this.clientName = name;
		this.clientUri = "rmi://localhost/" + name;
	}
	
	public void printClients(Map<String, String> clientsList)
	{
		Iterator<Entry<String, String>> it = clientsList.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public static void main(String[] args) throws Exception{
		RemoteList clientsList = (RemoteList)Naming.lookup("rmi://localhost/listserver");
		ChatClient cOne = new ChatClient("client_one");
		clientsList.setClient("client_one", "client_one_uri");
		ChatClient cTwo = new ChatClient("client_two");
		clientsList.setClient("client_two", "client_two_url");
		Map<String, String> cList = clientsList.getList("client_two");
		cOne.printClients(cList);
	}

}
