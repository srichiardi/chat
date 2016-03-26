package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Message extends Remote
{
	public void sendFrom(String senderName, String msgText) throws RemoteException;
}
