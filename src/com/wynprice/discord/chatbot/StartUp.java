package com.wynprice.discord.chatbot;

import java.util.Arrays;

public class StartUp 
{
	public static String[] args;
	
	public static void main(String[] args) 
	{
		StartUp.args = args;
		if(args.length < 1)
			System.exit(1);
        Main.CLIENT.getDispatcher().registerListener(new EventSystem());
        if(Arrays.asList(args).contains("-GUI"))
        	new Gui().setup();
    }
}
