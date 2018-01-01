package com.wynprice.discord.chatbot;

import java.io.File;
import java.util.ArrayList;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EventSystem 
{
	private ArrayList<Long> currentUsers = new ArrayList<>();
	
	@EventSubscriber
	public void onMessageRecieved(MessageReceivedEvent event)
	{
		if(!event.getChannel().isPrivate() && event.getMessage().getContent().startsWith("!charliebot"))
		{
			String[] args = event.getMessage().getContent().split(" ");
			if(args.length > 1 && args[1].equals("start"))
					if(currentUsers.contains(event.getAuthor().getLongID()))
							event.getChannel().sendMessage("You are already in a conversation with the chatbot");
					else
					{
						event.getChannel().sendMessage("Conversation with CharlieBot initiated");
						currentUsers.add(event.getAuthor().getLongID());
					}
			else if (args.length > 1 && args[1].equals("stop"))
				if(currentUsers.contains(event.getAuthor().getLongID()))
				{
					event.getChannel().sendMessage("Conversation with CharlieBot stopped");
					currentUsers.remove(event.getAuthor().getLongID());
				}
				else
					event.getChannel().sendMessage("You aren't in a conversation with the chatbot");
			else if(args.length > 1 && args[1].equals("resetbrain") && event.getAuthor().getLongID() == 268078090465443842L)
			{
				new File("brain.wyn").delete();
				BrainMuxer.loadBrain();
				for(long userID : new ArrayList<Long>(currentUsers))
					currentUsers.add(userID);
				event.getChannel().sendMessage("Brain Reset");
			}
		}
		else if(!event.getMessage().getContent().startsWith("!") && currentUsers.contains(event.getAuthor().getLongID()) || event.getChannel().isPrivate() || Main.OBSERVING)
			new LearningChatbot().continueConverstation(event.getChannel(), event.getMessage().getContent());
	}
}
