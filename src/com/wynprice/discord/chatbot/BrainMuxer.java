package com.wynprice.discord.chatbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Brain Muxer handle demuxing and remuxing the brain from a savefile.
 */
class BrainMuxer {

	public static boolean saveBrain(ChatbotBrain brain) {
		String filename = "brain.wyn";
		try {
			File file = new File(filename);

			ObjectOutputStream saveOut = new ObjectOutputStream(new FileOutputStream(file));
			saveOut.writeObject(brain);
			saveOut.flush();
			saveOut.close();
		} catch (IOException iof) {
			System.err.println("Could not access file: " + filename);
			iof.printStackTrace();
			return false;
		}

		return true;
	}

	public static ChatbotBrain loadBrain() 
	{
		String filename = "brain.wyn";
		try {
			// Check if file exists.
			File file = new File(filename);
			if (!file.exists()) {
				System.err.println("File does not exist: " + filename);
				return new ChatbotBrain();
			}

			// load
			ObjectInputStream loadIn = new ObjectInputStream(new FileInputStream(file));
			ChatbotBrain chatbrain = (ChatbotBrain) loadIn.readObject();
			loadIn.close();

			return chatbrain;
		} catch (IOException iof) {
			System.err.println("Could not access file: " + filename);
			iof.printStackTrace();
			return null;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("That file does not contain a valid brain: " + filename);
			cnfe.printStackTrace();
			return null;
		}
	}

}

