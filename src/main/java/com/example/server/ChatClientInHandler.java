package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.protocol.Message;

public class ChatClientInHandler implements Runnable {
	
	private InputStream inputStream;
	private ChatClientHandler chatClientHandler;
	private BufferedReader reader;
	
	public ChatClientInHandler(InputStream inputStream, ChatClientHandler chatClientHandler) {
		this.inputStream = inputStream;
		this.chatClientHandler = chatClientHandler;
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}

	@Override
	public void run() {
		while (chatClientHandler.isConnected()) {
			Message newMsg = getMessage();
			if (newMsg != null) {
				this.chatClientHandler.chatQueue.add(newMsg);
			} else {
				continue;
			}
		}
	}
	
	public Message getMessage() {
		String line;
		String msg = "";
		Message message = new Message();
		try {
			if ((line = reader.readLine()) != null) {
				msg = "";
				if (line.equals("<start>")) {
					line = "";
					while (!(line = reader.readLine()).equals("<end>")) {
						msg += line + "\n";
					}
					message.createNew(msg);
				}
			}
			if (message.good()) {
				return message;
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
