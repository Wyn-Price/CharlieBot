package com.wynprice.discord.chatbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Main 
{
	public static final IDiscordClient CLIENT = createClient(StartUp.args[0], true);
		
	public static final boolean OBSERVING = false;
	
	public static IDiscordClient createClient(String token, boolean login) 
	{ 
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {	
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }
    }	
}
