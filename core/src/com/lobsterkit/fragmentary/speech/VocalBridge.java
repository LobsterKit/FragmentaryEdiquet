package com.lobsterkit.fragmentary.speech;

public interface VocalBridge
{
	
	void destroy();
	
	String listen();
	
	void speak(String speech);
	
}
