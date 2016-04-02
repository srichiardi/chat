package chat;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ClientUI
{
	private String clientName;
	private String clientUri;
	private RemoteList clientsList;
	private Message msgServer;
	private Set <String> rcvdMessages; // stores all msgIDs already received
	private final JTextArea rcvText = new JTextArea();
	
	public ClientUI(String clientName) throws MalformedURLException, RemoteException, NotBoundException
	{
		this.clientName = clientName;
		this.clientUri = "rmi://localhost/" + clientName;
		
		this.clientsList = (RemoteList)Naming.lookup("rmi://localhost/listserver");
		this.clientsList.setClient(clientName, this.clientUri);
		this.rcvdMessages = new HashSet<String>();
		
		
		/** ==================== */
		/** :::GUI ELEMENTS::::: */
		/** ==================== */
		final JFrame frame = new JFrame("Chat Client: " + clientName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,500);
		frame.setLocation(250, 250);
		Container content = frame.getContentPane();
		
		JPanel panel1 = new JPanel();
		panel1.setSize(150, 400);
		content.add(panel1);
		final JLabel lblContacts = new JLabel("Contacts list");
		
		final DefaultListModel<String> lClients = new DefaultListModel<String>();
		
		final JList<String> lstClients = new JList<String>(lClients);
		JScrollPane clntsListScroll = new JScrollPane(lstClients);
		
		final JButton refreshBtn = new JButton("REFRESH LIST");
		
		
		JPanel panel2 = new JPanel();
		content.add(panel2);
		
		final JLabel lblSend = new JLabel("Send text field");
		final JLabel lblReceive = new JLabel("Receive text field");
		final JButton sendBtn = new JButton("SEND");
		final JButton closeBtn = new JButton("CLOSE");
		
		final JTextArea sendText = new JTextArea();
		sendText.setMargin(new Insets(10, 10, 10, 10));
		sendText.setLineWrap(true);
		JScrollPane sendAreaScroll = new JScrollPane(sendText);
		
		this.rcvText.setMargin(new Insets(10, 10, 10, 10));
		this.rcvText.setLineWrap(true);
		JScrollPane rcvAreaScroll = new JScrollPane(this.rcvText);
		
		// setting up the client's internal chat server
		this.msgServer = new MessageImpl();
		Naming.rebind(this.clientUri, this.msgServer);
		
		/** =================== */
		/** :::GUI LAYOUTS::::: */
		/** =================== */
		// Layout Frame
		GroupLayout frameLayout = new GroupLayout(content);
		content.setLayout( frameLayout );
		frameLayout.setAutoCreateGaps(true);
		frameLayout.setAutoCreateContainerGaps(true);
		
		frameLayout.setHorizontalGroup(
			frameLayout.createSequentialGroup()
			.addComponent(panel1)
			.addComponent(panel2)
		);
		
		frameLayout.setVerticalGroup(
			frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(panel1)
			.addComponent(panel2)
		);
		
		// Layout Panel 1: contact list of clients
		GroupLayout panel1Layout = new GroupLayout(panel1);
		panel1.setLayout( panel1Layout );
		panel1Layout.setAutoCreateGaps(true);
		panel1Layout.setAutoCreateContainerGaps(true);
		
		panel1Layout.setHorizontalGroup(
			panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(lblContacts)
			.addComponent(clntsListScroll)
			.addComponent(refreshBtn)
		);
		
		panel1Layout.setVerticalGroup(
			panel1Layout.createSequentialGroup()
			.addComponent(lblContacts)
			.addComponent(clntsListScroll)
			.addComponent(refreshBtn)
		);
		
		// Layout Panel 2: send and receive text fields
		GroupLayout panel2Layout = new GroupLayout(panel2);
		panel2.setLayout( panel2Layout );
		panel2Layout.setAutoCreateGaps(true);
		panel2Layout.setAutoCreateContainerGaps(true);
		
		panel2Layout.setHorizontalGroup(
			panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(lblSend)
			.addComponent(sendAreaScroll)
			.addComponent(sendBtn)
			.addComponent(lblReceive)
			.addComponent(rcvAreaScroll)
			.addComponent(closeBtn)
		);
		
		panel2Layout.setVerticalGroup(
			panel2Layout.createSequentialGroup()
			.addComponent(lblSend)
			.addComponent(sendAreaScroll)
			.addComponent(sendBtn)
			.addComponent(lblReceive)
			.addComponent(rcvAreaScroll)
			.addComponent(closeBtn)
		);
		
		/** ============================= */
		/** :::LISTENERS AND EVENTS:::::: */
		/** ============================= */
		
		sendBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String msgText = sendText.getText() + "\n\n";
				
				Set<String> recipientsList = new HashSet<String>();
				// when no recipient is selected
				if (lstClients.isSelectionEmpty())
				{
					ListModel list = lstClients.getModel();
					for(int i = 0; i < list.getSize(); i++){
						recipientsList.add((String) list.getElementAt(i));
					}
				} else {
					recipientsList.add(lstClients.getSelectedValue());
				}
				
				try {
					sendMessage(null, recipientsList, msgText, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// clearing text from send text area after message is sent 
				sendText.setText(null);
			}
		});
		
		refreshBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try {
					populateJList(lClients);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		// the listener detects when the received text area is modified
		// interpreting it as an indicator of message being received
		this.msgServer.addPropertyChangeListener(new PropertyChangeListener()
		{
	    	@Override
	    	public void propertyChange(PropertyChangeEvent event)
	    	{
	    	    if (event.getPropertyName().equals("messageReceived"))
	    	    {
	    	    	try
	    	    	{
	    	    		messageReceived();
	    	    	} catch (Exception e) {
	    	    		e.printStackTrace();
	    	    	}
	    	    }
	    	}
		});
		
		closeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try {
					unsubscribe();
				} catch (Exception e) {
					e.printStackTrace();
				}
				frame.dispose();
			}
		});
		
		/** ===================== */
		/** :::GUI LAUNCHER:::::: */
		/** ===================== */		
		frame.setVisible(true);
	}
	
	// automatically adds clients name to the contacts list at init
	private void populateJList(DefaultListModel<String> cListModel) throws RemoteException
	{
		Map<String, String> cList = this.clientsList.getList();
		cListModel.clear();
		for(String key : cList.keySet())
		{
			if (!key.equals(this.clientName))
			{
				cListModel.addElement(key);
			}
		}
	}
	
	private void sendMessage(String senderName, Set<String> recipientsList,
			String msgText, String msgID) throws RemoteException, MalformedURLException, NotBoundException
	{
		if (msgID == null)
		{
			msgID = this.clientName + "-" + String.valueOf(System.currentTimeMillis() / 1000L);
		}
		
		if (senderName == null)
		{
			senderName = this.clientName;
		}
		
		for(String recipient : recipientsList)
		{
			String rcpntUri = this.clientsList.getClientAddress(recipient);
			Message rcpntSrv = (Message)Naming.lookup(rcpntUri);
			rcpntSrv.sendFrom(senderName, msgText, msgID, recipientsList);
		}
	}
	
	// method to re-broadcast a message received 
	private void messageReceived() throws RemoteException, MalformedURLException, NotBoundException
	{
		// when the text is new
		if (!this.rcvdMessages.contains(this.msgServer.getTextID()))
		{
			// clear the text area for the new message
			this.rcvText.setText(null);
			String rcvdMsgText = "[" + this.msgServer.getSender() + " says]:\n\n" +
					this.msgServer.getMessageText();
			
			// print the text in the received msg area
			this.rcvText.setText(rcvdMsgText);
			
			// add the msgID to the received list
			this.rcvdMessages.add(this.msgServer.getTextID());
			
			// remove the client from the recipients list
			Set<String> recipientsList = this.msgServer.getRecipients();
			recipientsList.remove(this.clientName);
			
			// re-broadcast same copy of the message
			sendMessage(this.msgServer.getSender(), recipientsList, this.msgServer.getMessageText(),
					this.msgServer.getTextID());
		} else {
			// when the text was already received
			this.rcvText.append("\n\nmessage" + this.msgServer.getTextID() +
					" already received.");
		}
	}
	
	// method that fires when the client shuts down, removes itself from the registry
	private void unsubscribe() throws RemoteException, MalformedURLException, NotBoundException
	{
		this.clientsList.removeClient(this.clientName);
		Naming.unbind(this.clientUri);
	}
	
/*	public static void main(String[] args) throws Exception
	{
		new ClientUI(args[0]);
	}*/
	
}
