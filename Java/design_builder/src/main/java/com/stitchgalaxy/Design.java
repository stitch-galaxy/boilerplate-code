package com.stitchgalaxy;

import java.util.ArrayList;
import java.util.List;

public class Design {
	
	private long width;
	private long height;
	
	private List<DesignCell> cells = new ArrayList<DesignCell>();

	public Design()
	{
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public List<DesignCell> getCells() {
		return cells;
	}
	
}
