package chat;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.Dimension;
import java.awt.event.FocusListener;

import javax.swing.*;

public class ClientUI
{
	public ClientUI()
	{
		/** ================ */
		/** :::ELEMENTS::::: */
		/** ================ */
		final JFrame frame = new JFrame("Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,500);
		frame.setLocation(250, 250);
		Container content = frame.getContentPane();
		
		JPanel panel1 = new JPanel();
		content.add(panel1);
		final JLabel lblContacts = new JLabel("Contacts list");
		
		final DefaultListModel<String> lClients = new DefaultListModel<String>();
		
		final JList<String> lstClients = new JList<String>(lClients);
		JScrollPane clntsListScroll = new JScrollPane(lstClients);
		
		final JButton fakeBtn = new JButton("FAKE");
		
		
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
		);
		
		panel1Layout.setVerticalGroup(
			panel1Layout.createSequentialGroup()
			.addComponent(lblContacts)
			.addComponent(clntsListScroll)
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
		
		closeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				frame.dispose();
			}
		});
		
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new ClientUI();
	}

}
