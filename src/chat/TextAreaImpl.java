package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class TextAreaImpl extends UnicastRemoteObject implements Message
{
	private JTextArea msgArea;
	
	// simple constructor
	public TextAreaImpl (JTextArea rcvdMsgArea) throws RemoteException
	{
		this.msgArea = rcvdMsgArea;
	}
	
	public void sendFrom(String senderName, String msgText) throws RemoteException
	{
		this.msgArea.append(senderName + " says: " + msgText);
	}
}
