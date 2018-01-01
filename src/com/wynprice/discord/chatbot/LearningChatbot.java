package com.wynprice.discord.chatbot;
/* "Zero"-knowledge Learning ChatBot  Copyright (C) 2014-2016 Daniel Boston (ProgrammerDan)
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the 
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class LearningChatbot {
	/**
	 * Static definition of final word in a statement. It never has 
	 * any descendents, and concludes all statements. This is the only
	 * "starting knowledge" granted the bot.
	 */
	public static final ChatWord ENDWORD = new ChatWord("\n");

	/**
	 * The Brain of this operation.
	 */
	private ChatbotBrain brain;
	
	/**
	 * Starts LearningChatbot with restored brain.
	 */
	public LearningChatbot(IGuild guild) 
	{
		brain = BrainMuxer.loadBrain(guild);
		if (brain == null) {
			throw new RuntimeException("That brain file is missing or invalid!");
		}
	}

	/**
	 * Invocation method.
	 */
	public void continueConverstation(IGuild guild, IChannel channel, String input) 
	{
		digestSentance(input);
		if(!Main.OBSERVING || channel.isPrivate())
		{	String sentance = brain.buildSentence();
			if(sentance.isEmpty() || sentance.equals(" "))
				channel.sendMessage("...");
			else
				channel.sendMessage(sentance);
			System.out.println("INPUT: " + input);
			System.out.println("OUTPUT: " + sentance);
		}
		BrainMuxer.saveBrain(brain, guild);
	}
	
	public void digestSentance(String input)
	{
		brain.decay();
		brain.digestSentence(input);
	}
	
}


