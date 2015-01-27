package Tile01;

import java.util.ArrayList;

import controlP5.ControlP5;
import elements.Polygen2D;
import elements.Rectangle;
import elements.Vector2d;
import processing.core.PApplet;
import processing.core.PGraphics;

public class recTiling extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int width = 900;
	int height = 900;
	int scale = 5;
	ControlP5 cp5;

	Rectangle rec = new Rectangle(new Vector2d(10,10),width-20, height-20);
	
	
	
	public void setup(){
		cp5 = new ControlP5(this);

		size(width+100,height);
		smooth();
		rec.subRatio(this,7,0,0);

		strokeWeight(5);
	}
	
	public void draw(){
		background(255);
		rec.moveRandom(2.5f);
		rec.drawChildren(this);
	}
	
	public void mousePressed(){
		rec = new Rectangle(new Vector2d(10,10),width-20, height-20);
		rec.subRatio(this,7,(float)mouseX/width,(float)mouseY/height);
		
	}
	
	public void keyPressed(){
		PGraphics pg = createGraphics((int) (width * scale), (int) (height * scale));
		pg.beginDraw();
		rec.saveChildren(pg,scale);
		pg.endDraw();
		pg.save("rects" + ".tiff");
		println("saved");
	}

	
	
	
}
