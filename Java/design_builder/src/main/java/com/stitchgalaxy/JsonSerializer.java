package com.stitchgalaxy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer 
{
	
	public JsonSerializer()
	{
	}
	
	public void serializeDesign(Design design)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String fileName = Settings.getInstance().getOutputDirectory() + File.separator + "design.json";
		try 
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false),"UTF8"));
			out.write(gson.toJson(design));
			out.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
