package chat;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ClientUI
{
	private String clientName;
	private String clientUri;
	private RemoteList clientsList;
	private Message msgServer;
	
	public ClientUI(String clientName) throws MalformedURLException, RemoteException, NotBoundException
	{
		this.clientName = clientName;
		this.clientUri = "rmi://localhost/" + clientName;
		
		this.clientsList = (RemoteList)Naming.lookup("rmi://localhost/listserver");
		this.clientsList.setClient(clientName, this.clientUri);
		
		
		/** ==================== */
		/** :::GUI ELEMENTS::::: */
		/** ==================== */
		final JFrame frame = new JFrame("Chat Client: " + clientName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setLocation(250, 250);
		Container content = frame.getContentPane();
		
		JPanel panel1 = new JPanel();
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
		
		final JTextArea rcvText = new JTextArea();
		rcvText.setMargin(new Insets(10, 10, 10, 10));
		rcvText.setLineWrap(true);
		JScrollPane rcvAreaScroll = new JScrollPane(rcvText);
		
		// setting up the client's internal chat server
		this.msgServer = new TextAreaImpl(rcvText);
		Naming.rebind(this.clientUri, this.msgServer);
		
		/** =============== */
		/** :::LAYOUTS::::: */
		/** =============== */
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
		
		/** =============== */
		/** :::EVENTS:::::: */
		/** =============== */
		
		sendBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String recipient = lstClients.getSelectedValue();
				String msgText = sendText.getText() + "\n\n";
				try
				{
					sendMessage(recipient, msgText);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
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
		
		closeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				frame.dispose();
			}
		});
		
		frame.setVisible(true);
	}
	
	// automatically adds clients name to the contacts list at init
	private void populateJList(DefaultListModel<String> cListModel) throws RemoteException
	{
		Map<String, String> cList = this.clientsList.getList();
		Iterator<Entry<String, String>> it = cList.entrySet().iterator();
		cListModel.clear();
	    while (it.hasNext())
	    {
	        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        if (!pair.getKey().equals(this.clientName))
	        {
	        	cListModel.addElement(pair.getKey());
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	private void sendMessage(String recipient, String msgText) throws RemoteException, MalformedURLException, NotBoundException
	{
		Map<String, String> cList = this.clientsList.getList();
		String rcpntUri = cList.get(recipient);
		Message rcpntSrv = (Message)Naming.lookup(rcpntUri);
		rcpntSrv.sendFrom(this.clientName, msgText);
	}
	
/*	public static void main(String[] args) throws Exception
	{
		new ClientUI(args[0]);
	}*/
	
}
