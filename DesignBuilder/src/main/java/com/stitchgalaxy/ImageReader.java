package com.stitchgalaxy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageReader 
{

	public ImageReader()
	{		
	}
	
	private static class SizeChecker
	{
		private boolean initialized = false;
		private int width;
		private int height;
		
		public SizeChecker()
		{
		}
		
		public boolean checkSize(BufferedImage img)
		{
			if (img == null)
			{
				return true;
			}
			if (!initialized)
			{
				width = img.getWidth();
				height = img.getHeight();
						
				initialized = true;
				
				int designCellSizePx = Settings.getInstance().getHalfCellSizePx() * 2;
				
				if (width % designCellSizePx != 0 ||
						height % designCellSizePx != 0)
				{
					return false;
				}
					
				return true;
			}
			return img.getWidth() == width && img.getHeight() == height;
		}
		
		public int getDesignWidth()
		{
			if (initialized)
			{
				return width / (Settings.getInstance().getHalfCellSizePx() * 2);
			}
			return 0;
		}
		
		public int getDesignHeight()
		{
			if (initialized)
			{
				return height / (Settings.getInstance().getHalfCellSizePx() * 2);
			}
			return  0;
		}
	}
	
	public Design readDesign()
	{
		BufferedImage crossStitchImg = filterOutDefaultImage(readImage(getFullFileName("crossStitch.png")));
		BufferedImage slashHalfStitchImg = filterOutDefaultImage(readImage(getFullFileName("slashHalfStitch.png")));
		BufferedImage backslashHalfStitchImg = filterOutDefaultImage(readImage(getFullFileName("backslashHalfStitch.png")));
		BufferedImage quarterStitchesImg = filterOutDefaultImage(readImage(getFullFileName("quarterStitches.png")));
		BufferedImage petiteStitchesImg = filterOutDefaultImage(readImage(getFullFileName("petiteStitches.png")));
		BufferedImage threeQuarterStitchesImg = filterOutDefaultImage(readImage(getFullFileName("threeQuarterStitches.png")));
		BufferedImage frenchKnotsImg = filterOutDefaultImage(readImage(getFullFileName("frenchKnots.png")));
		
		SizeChecker sizeChecker = new SizeChecker();
		boolean sizeConsistent = sizeChecker.checkSize(crossStitchImg) &&
				sizeChecker.checkSize(slashHalfStitchImg) &&
				sizeChecker.checkSize(backslashHalfStitchImg) &&
				sizeChecker.checkSize(quarterStitchesImg) &&
				sizeChecker.checkSize(petiteStitchesImg) &&
				sizeChecker.checkSize(threeQuarterStitchesImg) && 
				sizeChecker.checkSize(frenchKnotsImg);
				
		if (!sizeConsistent)
		{
			System.out.println("Inconsistent design slices size");
			return null;
		}
		
		Design design = new Design();
		design.setWidth(sizeChecker.getDesignWidth());		
		design.setHeight(sizeChecker.getDesignHeight());

		
		//main build cycle
		for (int w = 0; w < design.getWidth(); ++w)
		{
			for (int h = 0; h < design.getHeight(); ++h)
			{
				DesignCell cell = new DesignCell();
				if(crossStitchImg != null)
				{		
					Color c = getColorAt(crossStitchImg, w, h, CellPoint.CENTER_CENTER);
					if (c != null)
					{
						cell.setCrossStitch(buildThreadBlend(c));
					}
				}
				if (slashHalfStitchImg != null)
				{
					Color c = getColorAt(crossStitchImg, w, h, CellPoint.CENTER_CENTER);
					if (c != null)
					{
						cell.setSlashHalfStitch(buildThreadBlend(c));
					}
				}
				if (backslashHalfStitchImg != null)
				{
					Color c = getColorAt(crossStitchImg, w, h, CellPoint.CENTER_CENTER);
					if (c != null)
					{
						cell.setBackslashHalfStitch(buildThreadBlend(c));
					}
				}
				if (quarterStitchesImg != null)
				{
					Color c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_UP);
					if (c!= null)
					{
						cell.setLeftUpQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_DOWN);
					if (c!= null)
					{
						cell.setLeftDownQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_UP);
					if (c!= null)
					{
						cell.setRightUpQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_DOWN);
					if (c!= null)
					{
						cell.setRightDownQuarterStitch(buildThreadBlend(c));
					}
				}
				if (petiteStitchesImg != null)
				{
					Color c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_UP);
					if (c!= null)
					{
						cell.setLeftUpPetiteStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_DOWN);
					if (c!= null)
					{
						cell.setLeftDownPetiteStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_UP);
					if (c!= null)
					{
						cell.setRightUpPetiteStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_DOWN);
					if (c!= null)
					{
						cell.setRightDownPetiteStitch(buildThreadBlend(c));
					}
				}
				if (threeQuarterStitchesImg != null)
				{
					Color c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_UP);
					if (c!= null)
					{
						cell.setLeftUpThreeQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_DOWN);
					if (c!= null)
					{
						cell.setLeftDownThreeQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_UP);
					if (c!= null)
					{
						cell.setRightUpThreeQuarterStitch(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_DOWN);
					if (c!= null)
					{
						cell.setRightDownThreeQuarterStitch(buildThreadBlend(c));
					}
				}
				if (frenchKnotsImg != null)
				{
					Color c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_UP);
					if (c!= null)
					{
						cell.setFrenchKnot00(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.CENTER_UP);
					if (c!= null)
					{
						cell.setFrenchKnot01(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_UP);
					if (c!= null)
					{
						cell.setFrenchKnot02(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_CENTER);
					if (c!= null)
					{
						cell.setFrenchKnot10(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.CENTER_CENTER);
					if (c!= null)
					{
						cell.setFrenchKnot11(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_CENTER);
					if (c!= null)
					{
						cell.setFrenchKnot12(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.LEFT_DOWN);
					if (c!= null)
					{
						cell.setFrenchKnot20(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.CENTER_DOWN);
					if (c!= null)
					{
						cell.setFrenchKnot21(buildThreadBlend(c));
					}
					c = getColorAt(quarterStitchesImg, w, h, CellPoint.RIGHT_DOWN);
					if (c!= null)
					{
						cell.setFrenchKnot22(buildThreadBlend(c));
					}
				}
				
				design.getCells().add(cell);
			}
		}
		
		return design;
	}
	
	private ThreadsBlend buildThreadBlend(Color color)
	{
		return buildThreadBlend(color, (short) 2);
	}
	
	private ThreadsBlend buildThreadBlend(Color color, short flossCount)
	{
		Thread thread = new Thread(color);
		ThreadInBlend threadInBlend = new ThreadInBlend();
		threadInBlend.setThread(thread);
		threadInBlend.setFlossCount(flossCount);
		ThreadsBlend blend = new ThreadsBlend();
		blend.getThreadsInBlend().add(threadInBlend);
		return blend;
	}
	
	private BufferedImage filterOutDefaultImage(BufferedImage img)
	{
		if (img.getWidth() == 1)
		{
			return null;
		}
		return img;
	}
	
	private BufferedImage readImage(String fileName)
	{
		BufferedImage img = null;
		try
    	{
    		File file = new File(fileName);
    		img = ImageIO.read(file);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return img;
	}
	
	private String getFullFileName(String fileName)
	{
		return Settings.getInstance().getOutputDirectory() + File.separator + fileName;
	}
	
	
	private Color getColorAt(BufferedImage image, int x, int y, CellPoint cellPoint)
	{
		int halfCellSizePx = Settings.getInstance().getHalfCellSizePx();
		
		int xPx = x * halfCellSizePx * 2 + halfCellSizePx * cellPoint.getXSeed();
		int yPx = y * halfCellSizePx * 2 + halfCellSizePx * cellPoint.getYSeed();
		
		return new Color(image.getRGB(xPx, yPx));
	}
}
