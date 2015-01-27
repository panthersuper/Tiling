package Tile01;

import java.util.ArrayList;

import elements.Polygen2D;
import elements.Vector2d;
import processing.core.PApplet;
import processing.core.PGraphics;

public class test extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int width = 900;
	int height = 900;
	int scale = 5;
	Polygen2D poly = new Polygen2D();
	Polygen2D poly2 = new Polygen2D();

	ArrayList<Polygen2D> lst;
	ArrayList<Polygen2D> lst2;

	public static ArrayList<Polygen2D> rect = new ArrayList<>();
	public static ArrayList<Polygen2D> rect2 = new ArrayList<>();

	public void setup(){
		smooth();
		background(255);
		size(width,height);
		smooth();
		poly.addVertex(10, 10);
		poly.addVertex(10, height-10);
		//poly.addVertex(width-10, height-10);
		poly.addVertex(width-10, 10);
		//poly2.addVertex(10, height-10);
		//poly2.addVertex(width-10, height-10);
		//poly2.addVertex(width-10, 10);

		//poly.draw(this);
		strokeWeight(0.5f);
		lst = poly.subLnEdge(10);
		//lst2 = poly2.subLnEdge(10);

		//println(lst.size());
		println("drawing");
		for (int i = 0;i<lst.size();i++){
			lst.get(i).draw(this,255, false);
			//lst2.get(i).draw(this,255, false);
		}
		
		for (int i = 0;i<rect.size();i++){
			rect.get(i).draw(this,0,true);

		}
	}
	
	public void draw(){
		//println (poly.PerpendicularPts(new Vector2d(mouseX,mouseY)).size());
		
		
	}
	
	public void keyPressed(){
		PGraphics pg = createGraphics((int) (width * scale), (int) (height * scale));
		pg.beginDraw();
		for (int i = 0;i<lst.size();i++){
			lst.get(i).save(pg, scale, 255, false);
			//lst2.get(i).draw(this,255, false);
		}
		
		for (int i = 0;i<rect.size();i++){
			rect.get(i).save(pg, scale, 0, true);

		}
		pg.endDraw();
		pg.save("edgeMidPts" + ".tiff");
		println("saved");
	}
	
	
	
	
}
