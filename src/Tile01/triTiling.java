package Tile01;

import java.util.ArrayList;

import elements.Polygen2D;
import elements.Vector2d;
import processing.core.PApplet;
import processing.core.PGraphics;

public class triTiling extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int width = 900;
	int height = 900;
	int scale = 5;
	Polygen2D poly = new Polygen2D();
	ArrayList<Polygen2D> lst;
	
	
	public void setup(){
		background(255);
		size(width,height);
		smooth();
		poly.addVertex(10, 10);
		poly.addVertex(10, height-10);
		poly.addVertex(width-10, height-10);
		poly.addVertex(width-10, 10);

		//poly.draw(this);
		strokeWeight(0.5f);
		lst = poly.subTri(8);
		//println(lst.size());
		println("drawing");
		for (Polygen2D p : lst){
			p.draw(this);;
		}
	}
	
	public void draw(){
		//println (poly.PerpendicularPts(new Vector2d(mouseX,mouseY)).size());
		
		
	}
	
	public void keyPressed(){
		PGraphics pg = createGraphics((int) (width * scale), (int) (height * scale));
		pg.beginDraw();
		for (Polygen2D p : lst){
			p.save(pg,scale);
		}
		pg.endDraw();
		pg.save("tri" + ".tiff");
		println("saved");
	}
	
	
	
	
}
