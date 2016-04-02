package chat;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Message extends Remote
{
	public void sendFrom(String senderName, String msgText, String msgID,
			Set<String> recipientsList) throws RemoteException;
	
	public String getMessageText() throws RemoteException;
	
	public String getSender() throws RemoteException;
	
	public String getTextID() throws RemoteException;
	
	public Set<String> getRecipients() throws RemoteException;
	
	public void addPropertyChangeListener(PropertyChangeListener listener) throws RemoteException;
}
