package com.lobsterkit.fragmentary.speech.demo;

import java.net.URL;

import com.lobsterkit.fragmentary.speech.PCVocalBridge;

public class VocalBridgeDemo
{
	public static void main(String[] args)
	{
		URL configurationURL = VocalBridgeDemo.class.getResource("config.xml");
		PCVocalBridge bridge = new PCVocalBridge(configurationURL);
		
		while (true)
		{
			System.out.println("listening...");
			String speech = bridge.listen();
			System.out.println(speech);
			
			if ("quit".equals(speech) || "stop".equals(speech))
			{
				break;
			}
		}
		
		System.out.println("Stopping...");
		bridge.destroy();
		System.out.println("END");
	}
}
