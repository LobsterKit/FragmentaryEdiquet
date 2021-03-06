package com.lobsterkit.fragmentary.speech;

import java.io.IOException;
import java.net.URL;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

public class PCVocalBridge implements VocalBridge
{
	private Recognizer recognizer;
	private Microphone microphone;
	private boolean perpeturalRecordingEnabled;
	
	public PCVocalBridge(URL configurationURL, boolean perpeturalRecordingEnabled)
			throws InstantiationException
	{
		this.perpeturalRecordingEnabled = perpeturalRecordingEnabled;
		initializeAudioInfrastructure(configurationURL);
	}
	
	public PCVocalBridge(URL configurationURL) throws InstantiationException
	{
		this(configurationURL, true);
	}
	
	private void initializeAudioInfrastructure(URL configurationURL)
			throws InstantiationException
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
		catch (IOException loadingException)
		{
			throw new IllegalArgumentException("Invalid configuration file",
					loadingException);
		}
		catch (PropertyException propertyException)
		{
			throw new InstantiationException("Unable to create recognizer or microphone.");
		}
	}
	
	private void preListen()
	{
		if (!microphone.isRecording())
		{
			microphone.startRecording();
			try
			{
				this.wait();
			}
			catch (InterruptedException e)
			{
				// continue
			}
		}
	}
	
	private void postListen()
	{
		if (!perpeturalRecordingEnabled)
		{
			microphone.stopRecording();
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
		preListen();
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
		postListen();
		
		return resultString;
	}
	
	@Override
	public void speak(String speech)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
