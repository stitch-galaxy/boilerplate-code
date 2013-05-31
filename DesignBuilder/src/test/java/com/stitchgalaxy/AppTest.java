package com.stitchgalaxy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	ImageReader reader = new ImageReader();
    	Design design = reader.readDesign();
    	if (design != null)
    	{
    		JsonSerializer serializer = new JsonSerializer();
    		serializer.serializeDesign(design);
    	}
    }
}
