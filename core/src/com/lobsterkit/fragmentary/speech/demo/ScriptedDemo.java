package com.lobsterkit.fragmentary.speech.demo;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lobsterkit.fragmentary.conversation.ConversationTree;
import com.lobsterkit.fragmentary.speech.PCVocalBridge;

public class ScriptedDemo
{
	private static String[] haultCommands = new String[] { "hault execution", "stop",
			"cease" };
	private ConversationTree<String, String> tree;
	private PCVocalBridge bridge;
	private boolean running;
	
	private URL getResource(String relativePath)
	{
		return VocalBridgeDemo.class.getResource(relativePath);
	}
	
	public ScriptedDemo() throws InstantiationException, JSONException, IOException
	{
		URL configurationURL = getResource("config.xml");
		parseTree("script.json", "intro");
		this.bridge = new PCVocalBridge(configurationURL);
		running = true;
	}
	
	public void end()
	{
		System.out.println("Stopping...");
		bridge.destroy();
		System.out.println("END");
	}
	
	public void run()
	{
		while (running)
		{
			String text = tree.getContent();
			printWrappedText(text, 100);
			
			if (tree.atEndNode())
			{
				System.out.println("---END---");
				running = false;
			}
			else
			{
				processInput();
			}
		}
		
		end();
	}
	
	private void preProcess()
	{
		if (true)
			System.out.println("\nlistening...");
	}
	
	private void postProcess(String input)
	{
		if (true)
			System.out.println("Received: " + input + "\n");
	}
	
	private void processGlobalCommands(String input)
	{
		for (String command : haultCommands)
		{
			if (command.equals(input))
			{
				running = false;
				break;
			}
		}
	}
	
	public void printWrappedText(String text, int lineWidth)
	{
		for (int index = 0; index < text.length(); index += lineWidth)
		{
			int endIndex = index + lineWidth;
			int delta = 0;
			while (index < text.length() && text.charAt(index) == ' ')
			{
				index++;
				endIndex++;
			}
			while (endIndex < text.length() && text.charAt(endIndex) != ' ')
			{
				endIndex--;
				delta--;
			}
			
			if (index >= text.length())
				break;
			if (endIndex > text.length())
				endIndex = text.length();
			
			System.out.println(text.substring(index, endIndex));
			
			index += delta;
		}
	}
	
	public void processInput()
	{
		preProcess();
		String speech = bridge.listen();
		postProcess(speech);
		
		if (tree.currentNodeHasBranch(speech))
		{
			tree.enterBranch(speech);
		}
		else
		{
			System.out.println("\"" + speech + "\" is not recognized");
		}
		
		processGlobalCommands(speech);
	}
	
	public static void main(String[] args)
	{
		try
		{
			(new ScriptedDemo()).run();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initializeTree()
	{
		String rootLine = "The wind howls through the trees of the forrest that surrounds you The sky - barely visible - makes you dizzy as you attempt to stand.\n\n"
				+ "Perhaps you should wonder where you are, or how you got here. Why you feel so weak. Why your clothes feel foreign to you, or why you have no shoes."
				+ "Still, even through the uncertainty, the daylight is slowley fading. Perhaps the questions can standby while you deal with more practical matters...";
		tree = new ConversationTree<>(rootLine, "root");
		
		String westContent = "Putting your mind at bay, you begin walking to the west. The shimmering light of the setting sun is uncomfortable as you walk.\n\"It's evening\" you think. \"The light will soon be gone\"\nAs you walk the light only becomes more intense. Brighter and brighter until you can't see at all. The light consumes you.";
		String eastContent = "Putting your mind at bay, you begin walking eastward. Even through the thick trees, the sun at your back shines through, casting sharp shadows along your path. As the light grows dimmer, you begin to hear the howl of wolves in the distance.";
		
		tree.addNewBranch("go east", "EastPath", eastContent);
		tree.addNewBranch("go west", "WestPath", westContent);
	}
	
	public void parseTree(String filePath, String rootName) throws JSONException,
			IOException
	{
		JSONTokener tokener = new JSONTokener(getResource("script.json").openStream());
		JSONObject jsonObject = new JSONObject(tokener);
		
		Set<String> keys = jsonObject.keySet();
		JSONObject scriptRoot = jsonObject.getJSONObject(rootName);
		instantiateTree(scriptRoot, rootName);
		keys.remove(rootName);
		
		for (String key : keys)
		{
			JSONObject node = jsonObject.getJSONObject(key);
			JSONObject branches = node.getJSONObject("branches");
			String content = node.getString("text");
			
			tree.addNewNode(key, content);
			tree.jumpToNode(key);
			addBranchesFromJSON(branches);
		}
		
		tree.jumpToRoot();
	}
	
	private void instantiateTree(JSONObject root, String rootNodeName)
	{
		String rootLine = root.getString("text");
		tree = new ConversationTree<>(rootLine, rootNodeName);
		
		JSONObject branches = root.getJSONObject("branches");
		addBranchesFromJSON(branches);
	}
	
	private void addBranchesFromJSON(JSONObject branches)
	{
		for (String branch : branches.keySet())
		{
			String branchLink = branches.getString(branch);
			tree.addLinkedBranch(branch, branchLink);
		}
	}
}
