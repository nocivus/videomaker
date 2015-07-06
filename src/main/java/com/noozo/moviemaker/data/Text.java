package com.noozo.moviemaker.data;

import java.awt.Color;
import java.awt.Point;

public class Text {

	public String text = "";
    public Color color = Color.white;
    public Point position = new Point(0, 0);
    public int fontSize = 20;
    
    public Text() { }
    
    public Text(String text, Color color, Point position, int fontSize) {

    	this.text = text;
    	this.color = color;
    	this.position = position;
    	this.fontSize = fontSize;
	}
}
