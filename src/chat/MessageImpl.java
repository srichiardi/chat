package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class MessageImpl extends UnicastRemoteObject implements Message
{
	// simple constructor
	public MessageImpl () throws RemoteException {}
	
	public void sendFrom(String senderName, String msgText) throws RemoteException
	{
		System.out.println(senderName + " says: " + msgText);
	}
}
