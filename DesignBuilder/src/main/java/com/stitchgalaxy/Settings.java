package com.stitchgalaxy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Settings 
{
    private static volatile Settings instance;

    public static Settings getInstance() 
    {
    	Settings localInstance = instance;
	    if (localInstance == null) 
	    {
	        synchronized (Settings.class) 
	        {
	            localInstance = instance;
	            if (localInstance == null) 
	            {
	                instance = localInstance = new Settings();
	            }
	        }
	    }
	    return localInstance;
    }
    
    Properties prop = null;
    
    private Settings()
    {
    	prop = new Properties();
        String fileName = "app.cfg";
        try
        {
        	InputStream is = new FileInputStream(fileName);
        	prop.load(is);
        }
        catch(FileNotFoundException e)
        {
        	e.printStackTrace();
        } 
        catch (IOException e) 
        {
        	e.printStackTrace();
		}
    }
    
    public String getInputDirectory()
    {
    	return prop.getProperty("inputDirectory");
    }
    
    public String getOutputDirectory()
    {
    	return prop.getProperty("outputDirectory");
    }
    
    public int getHalfCellSizePx()
    {
    	String sSize =  prop.getProperty("halfCellSizePx", "5");
    	return Integer.parseInt(sSize);
    }
}