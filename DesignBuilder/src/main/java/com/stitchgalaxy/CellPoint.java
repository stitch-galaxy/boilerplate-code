package com.stitchgalaxy;

public enum CellPoint {
	
	LEFT_UP(0, 0),
	LEFT_CENTER(0, 1),
	LEFT_DOWN(0, 2),
	CENTER_UP(1, 0),
	CENTER_CENTER(1, 1),
	CENTER_DOWN(1, 2),
	RIGHT_UP(2,0),
	RIGHT_CENTER(2, 1),
	RIGHT_DOWN(2, 2);
	
	private final int xSeed;
	private final int ySeed;
	
	CellPoint(int xSeed, int ySeed)
	{
		this.xSeed = xSeed;
		this.ySeed = ySeed;
	}
	
	public int getXSeed()
	{
		return this.xSeed;
	}
	
	public int getYSeed()
	{
		return this.ySeed;
	}

}
