package com.wynprice.discord.chatbot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;

public class EventSystem 
{
	private ArrayList<Long> currentUsers = new ArrayList<>();
	
	public static ArrayList<Long> optedToUseLocal = new ArrayList<>();
	
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
			else if(args.length > 1 && args[1].equals("resetbrain") && event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER))
			{
				new File(event.getGuild().getLongID() + ".wyn").delete();
				BrainMuxer.loadBrain(event.getGuild());
				for(long userID : new ArrayList<Long>(currentUsers))
					currentUsers.add(userID);
				event.getChannel().sendMessage("Brain Reset");
			}
			else if(args.length > 2 && args[1].equals("setlocal") && event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER) &&
					Arrays.asList("true", "false").contains(args[2].toLowerCase()))
			{
				boolean setlocal = Boolean.parseBoolean(args[2]);
				if(setlocal)
					if(optedToUseLocal.contains(event.getGuild().getLongID()))
						event.getChannel().sendMessage("Already Opted In");
					else
					{
						event.getChannel().sendMessage("Opted to use local brain");
						optedToUseLocal.add(event.getGuild().getLongID());
					}
				else
				{
					if(optedToUseLocal.contains(event.getGuild().getLongID()))
					{
						event.getChannel().sendMessage("Opted to use Main Brain");
						optedToUseLocal.remove(event.getGuild().getLongID());
					}
					else
						event.getChannel().sendMessage("Already Using Main Brain");
				}
				
			}
			else if(args.length > 1 && args[1].equals("help"))
				event.getChannel().sendMessage("CharlieBot Help\n\nAll commands start with !charliebot\n\nSUBCOMMANDS:\nstart - start the conversation with charliebot\nstop - stop the conversation with charliebot\nresetbrain - reset the local brain for charliebot. Need MANAGE SERVER permission\n setlocal <local> - set the use the local brain. <local> must be either true or false");
			else
				event.getChannel().sendMessage("Command not found. Please veiw the help with !charliebot help");
		}
		else if(!event.getMessage().getContent().startsWith("!") && currentUsers.contains(event.getAuthor().getLongID()) || event.getChannel().isPrivate() || Main.OBSERVING)
			new LearningChatbot(event.getGuild()).continueConverstation(event.getGuild(), event.getChannel(), event.getMessage().getContent());
	}
}
