package com.example.server;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.protocol.Message;
import com.example.server.ChatClientHandler;

public class DirectMessage implements Runnable {
	private ChatServer chatServer;
	private Message		message;

	public DirectMessage(ChatServer chatServer) {
		this.chatServer = chatServer;
	}

	@Override
	public void run() {
		while (true) {
			if (chatServer.chatQueue.hasNext()) {
				message = chatServer.chatQueue.getNext();
				direct(message);
			}
		}
	}
	
	public void direct(Message msg) {
		String receiverName = msg.getReceiver();
		msg.setReceiveMethod();
		ChatClientHandler receiver = findClient(receiverName, this.chatServer.getClientList());
		if (receiver != null) {
			receiver.chatQueue.add(msg);
		}
	}
	
	public ChatClientHandler findClient(String name, ArrayList<ChatClientHandler> clients) {
	    for (ChatClientHandler client : clients) {
	    	if (client.getClientName().equals(name)) {
	    		return client;
	    	}
	    }
	    return null;
	}
}
