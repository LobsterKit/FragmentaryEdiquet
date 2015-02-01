package com.lobsterkit.fragmentary.speech;

import java.net.URL;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class PCVocalBridge implements VocalBridge
{
	private Recognizer recognizer;
	private Microphone microphone;
	
	public PCVocalBridge(URL configurationURL)
	{
		try
		{
			ConfigurationManager configuration = new ConfigurationManager(
					configurationURL);
			
			this.recognizer = (Recognizer) configuration.lookup("recognizer");
			this.microphone = (Microphone) configuration.lookup("microphone");
			
			recognizer.allocate();
			if (!microphone.startRecording())
			{
				recognizer.deallocate();
				throw new IllegalStateException("Could not start microphone.");
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public void destroy()
	{
		microphone.stopRecording();
		recognizer.deallocate();
		
		this.microphone = null;
		this.recognizer = null;
	}
	
	@Override
	public String listen()
	{
		Result result = recognizer.recognize();
		String resultString;
		
		if (result == null)
		{
			resultString = null;
		}
		else
		{
			resultString = result.getBestFinalResultNoFiller();
		}
		
		return resultString;
	}
	
	@Override
	public void speak(String speech)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
