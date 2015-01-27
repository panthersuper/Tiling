package elements;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Rectangle {

	protected Vector2d tp;
	protected Vector2d bt;
	int n = 6;//division threshold
	protected double width, height;
	public Vector2d thePt;   //the Ratio of the point inside the rectrangle to split
	public Rectangle parent;
	public ArrayList<Rectangle> children = new ArrayList<Rectangle>();
	public int generation = 0;
	public int item;
	
	public Rectangle() {
		tp = new Vector2d();
		bt = new Vector2d(1, 1);
		this.width = 1;
		this.height = 1;
	}

	public Rectangle(Vector2d tp, double w, double h) {
		this.tp = tp;
		this.width = w;
		this.height = h;
		bt = new Vector2d(tp.x + w, tp.y + h);

	}

	public Rectangle(Vector2d tp, Vector2d bt) {
		this.tp = tp;
		this.bt = bt;
		this.width = bt.x - tp.x;
		this.height = bt.y - tp.y;
	}

	/**
	 * get AABB of points, return as rectangle.
	 * */
	public static Rectangle getAABB(Vector2d... pnts) {
		double minX = Float.MAX_VALUE;
		double maxX = Float.NEGATIVE_INFINITY;
		double minY = Float.MAX_VALUE;
		double maxY = Float.NEGATIVE_INFINITY;

		for (int i = 0; i < pnts.length; i++) {
			if (pnts[i].x < minX) {
				minX = pnts[i].x;
			}
			if (pnts[i].x > maxX) {
				maxX = pnts[i].x;
			}

			if (pnts[i].y < minY) {
				minY = pnts[i].y;
			}
			if (pnts[i].y > maxY) {
				maxY = pnts[i].y;
			}
		}
		return new Rectangle(new Vector2d(minX, minY), maxX - minX, maxY - minY);
	}

	/**
	 * set top-left point as value of v.
	 * */
	public void setTp(Vector2d v) {
		this.tp.set(v);
	}

	/**
	 * set top-left as [x,y]
	 * */
	public void setTp(double x, double y) {
		this.tp.set(x, y);
	}

	/**
	 * set width.
	 * */
	public void setWidth(double w) {
		this.width = w;
		this.bt = new Vector2d(this.tp.x+w,this.bt.y);
	}

	/**
	 * set height
	 * */
	public void setHeight(double h) {
		this.height = h;
		this.bt = new Vector2d(this.bt.x,this.tp.y+h);
	}

	/**
	 * whether the Rectangle includes another rectangle_r.
	 * */
	public boolean contains(Rectangle r) {
		if (this.tp.x > r.tp.x)
			return false;
		if (this.tp.y > r.tp.y)
			return false;
		if (this.tp.x + this.width < r.tp.x + r.width)
			return false;
		if (this.tp.y + this.height < r.tp.y + r.height)
			return false;
		return true;
	}
	
	public void setThePt(PApplet app){
		this.thePt = new Vector2d((this.ranPt(app).x-this.tp.x)/this.width,(this.ranPt(app).y-this.tp.y)/this.height);
	}
	
	public void setThePt(double ratioX, double ratioY){
		this.thePt = new Vector2d(ratioX,ratioY);
	}
	
			

	
	
	public void moveThePt(double R){
		int count = 0;
		Vector2d pos = new Vector2d(this.thePt.x*this.width+this.tp.x,this.thePt.y*this.height+this.tp.y);
		Vector2d newPt = pos.dup().add((Math.random()-0.5)*2*R,(Math.random()-0.5)*2*R);
		while(count<10 && !this.contain(newPt)){
			count++;
			newPt = pos.dup().add((Math.random()-0.5)*2*R,(Math.random()-0.5)*2*R);
		}
		if(count<10)
		this.thePt = new Vector2d((newPt.x-this.tp.x)/this.width,(newPt.y-this.tp.y)/this.height);
		
		if(this.children.size()!=0){
		this.children.get(0).setWidth(this.thePt.x*this.width);
		this.children.get(0).setHeight(this.thePt.y*this.height);
		
		this.children.get(1).setTp(this.tp.x+this.thePt.x*this.width,this.tp.y);
		this.children.get(1).setWidth(this.width-this.children.get(0).width);
		this.children.get(1).setHeight(this.children.get(0).height);

		this.children.get(2).setTp(this.tp.x,this.thePt.y*height+this.tp.y);
		this.children.get(2).setWidth(this.thePt.x*this.width);
		this.children.get(2).setHeight(this.height-this.children.get(0).height);

		this.children.get(3).setTp(this.tp.x+this.thePt.x*this.width, this.thePt.y*height+this.tp.y);
		this.children.get(3).setWidth(this.children.get(1).width);
		this.children.get(3).setHeight(this.children.get(2).height);
		}
	}
	
	public boolean contain(Vector2d v){
		if (this.tp.x<=v.x &&this.bt.x>=v.x && this.tp.y<=v.y && this.bt.y>=v.y){
			return true;
			
		}
		return false;
		
	}

	
	public Vector2d ranPt(PApplet app) {
		// return a random point in the boundary of the ractangle
		double xx = app.random((float) (this.tp.x+width/n), (float) (this.bt.x-width/n));
		double yy = app.random((float) (this.tp.y+height/n), (float) (this.bt.y-height/n));

		return new Vector2d(xx, yy);
	}
	


	public ArrayList<Rectangle> subRatio(PApplet app,double ratioX, double ratioY) {
		ArrayList<Rectangle> lst = new ArrayList<>();

		if(ratioX==0 && ratioY==0){
		
	Vector2d vr = this.ranPt(app);
	Vector2d v1 = new Vector2d(vr.x, this.tp.y);
	Vector2d v2 = new Vector2d(this.bt.x, vr.y);
	Vector2d v3 = new Vector2d(vr.x, this.bt.y);
	Vector2d v4 = new Vector2d(this.tp.x, vr.y);
	
	//if (new Rectangle(this.tp, vr).ratio()<5)
	lst.add(new Rectangle(this.tp, vr));
	//if (new Rectangle(v1, v2).ratio()<5)
	lst.add(new Rectangle(v1, v2));
	//if (new Rectangle(v4, v3).ratio()<5)
	lst.add(new Rectangle(v4, v3));
	//if (new Rectangle(vr, this.bt).ratio()<5)
	lst.add(new Rectangle(vr, this.bt));
		}
		else{
			Vector2d vr = new Vector2d(this.width*ratioX+this.tp.x, this.height*ratioY+this.tp.y);
			//vr = new Rectangle(this.tp, vr).ranPt(app);
			
			Vector2d v1 = new Vector2d(vr.x, this.tp.y);
			Vector2d v2 = new Vector2d(this.bt.x, vr.y);
			Vector2d v3 = new Vector2d(vr.x, this.bt.y);
			Vector2d v4 = new Vector2d(this.tp.x, vr.y);
			
			
			//if (new Rectangle(this.tp, vr).ratio()<5)
			lst.add(new Rectangle(this.tp, vr));
			//if (new Rectangle(v1, v2).ratio()<5)
			lst.add(new Rectangle(v1, v2));
			//if (new Rectangle(v4, v3).ratio()<5)
			lst.add(new Rectangle(v4, v3));
			//if (new Rectangle(vr, this.bt).ratio()<5)
			lst.add(new Rectangle(vr, this.bt));

			
			
			
		}
	
		
		for (int i=0;i<lst.size();i++){
			lst.get(i).generation = this.generation+1;
			lst.get(i).item = i;
			
		}
		
		return lst;

		
	}
	
	
	
	
	public ArrayList<Rectangle> subRatio(PApplet app, int num,double ratioX, double ratioY) {
		// num is the iteration number
		if (num == 1) {
			ArrayList<Rectangle> lst = new ArrayList<>();
			this.setThePt(ratioX,ratioY);

			if(ratioX==0 && ratioY==0)
			this.setThePt((this.ranPt(app).x-this.tp.x)/this.width,(this.ranPt(app).y-this.tp.y)/this.height);
			for (Rectangle r : this.subRatio(app,this.thePt.x,this.thePt.y)) {
				r.setThePt(ratioX,ratioY);
				if(ratioX==0 && ratioY==0)
					r.setThePt((r.ranPt(app).x-r.tp.x)/r.width,(r.ranPt(app).y-r.tp.y)/r.height);
				lst.add(r);
				this.children.add(r);
				
			}
			return lst;
					
		} else {
			ArrayList<Rectangle> llst = subRatio(app, num - 1,ratioX,ratioY);
			ArrayList<Rectangle> lmo = new ArrayList<>();
			this.setThePt(ratioX,ratioY);
			if(ratioX==0 && ratioY==0)
			this.setThePt((this.ranPt(app).x-this.tp.x)/this.width,(this.ranPt(app).y-this.tp.y)/this.height);

			for (Rectangle r : llst) {
				r.setThePt(ratioX,ratioY);

				if(ratioX==0 && ratioY==0)
					r.setThePt((r.ranPt(app).x-r.tp.x)/r.width,(r.ranPt(app).y-r.tp.y)/r.height);

				for (Rectangle rr : r.subRatio(app,r.thePt.x,r.thePt.y)){
					rr.setThePt(r.thePt.x,r.thePt.y);

					if(ratioX==0 && ratioY==0)
						rr.setThePt((rr.ranPt(app).x-rr.tp.x)/rr.width,(rr.ranPt(app).y-rr.tp.y)/rr.height);

					lmo.add(rr);
					r.children.add(rr);
				}
			}
			return lmo;
		}
		
	}
	
	
	public void moveRandom(double R){
		if (this.children.size()==0) {
			
			this.moveThePt(R);
		} else {

		this.moveThePt(R);

		ArrayList<Rectangle> llst = this.children;
			for (Rectangle r : llst) {
				r.moveRandom(R);
			}
		}
		
	}
	
	public double ratio(){
		double r = this.width/this.height;
		if (r<1) r = 1/r;
		
		return r;
		
	}
	
	public double area(){
		return this.width*this.height;
		
	}

	
	public void drawChildren(PApplet app){
		
		if(this.children.size()>0){
			float v = PApplet.map(this.generation,0,5,0,255);

			//this.draw(app,v,v,v,v);
			for(Rectangle r:this.children){
			r.drawChildren(app);
			}
			
		}
		else{

			//this.draw(app);
			float v = PApplet.map(this.generation,0,5,255,0);
			if (this.item==0)
			this.draw(app,25,76,204);
			if (this.item==1)
			this.draw(app,0,127,153);
			if (this.item==2)
			this.draw(app,204,0,0);
			if (this.item==3)
			this.draw(app,127,102,229);

		}
		
	}

	public void saveChildren(PGraphics app,int scale){
		if(this.children.size()>0){
			float v = PApplet.map(this.generation,0,5,0,255);

			//this.draw(app,v,v,v,v);
			for(Rectangle r:this.children){
			r.saveChildren(app,scale);
			}
			
		}
		else{

			//this.draw(app);
			float v = PApplet.map(this.generation,0,5,255,0);
			if (this.item==0)
			this.draw(app,scale,25,76,204);
			if (this.item==1)
			this.draw(app,scale,0,127,153);
			if (this.item==2)
			this.draw(app,scale,204,0,0);
			if (this.item==3)
			this.draw(app,scale,127,102,229);

		}
		
		
	}
	
	public void draw(PApplet app, float r, float g,float b){
		app.pushStyle();
		app.stroke(0, 100);
		app.fill(r,g,b);
		app.strokeWeight(1.5f);
		app.beginShape();
		app.vertex((float) this.tp.x, (float) this.tp.y);
		app.vertex((float) this.tp.x, (float) this.tp.y + (float) this.height);
		app.vertex((float) this.tp.x + (float) this.width, (float) this.tp.y
				+ (float) this.height);
		app.vertex((float) this.tp.x + (float) this.width, (float) this.tp.y);
		app.endShape(app.CLOSE);
		app.popStyle();
	}
	
	public void draw(PGraphics app,int scale,float r, float g,float b) {
		app.pushStyle();
		app.stroke(0, 100);
		app.fill(r,g,b);
		app.beginShape();
		app.vertex((float) this.tp.x*scale, (float) this.tp.y*scale);
		app.vertex((float) this.tp.x*scale, (float) this.tp.y*scale + (float) this.height*scale);
		app.vertex((float) this.tp.x*scale + (float) this.width*scale, (float) this.tp.y*scale
				+ (float) this.height*scale);
		app.vertex((float) this.tp.x*scale + (float) this.width*scale, (float) this.tp.y*scale);
		app.endShape(app.CLOSE);
		app.popStyle();
	}
	
	
	public void draw(PApplet app) {
		app.pushStyle();
		app.stroke(0, 100);
		//app.fill(PApplet.map((float)this.area(),0,100,0,255));
		app.noFill();
		app.strokeWeight(1.5f);
		app.beginShape();
		app.vertex((float) this.tp.x, (float) this.tp.y);
		app.vertex((float) this.tp.x, (float) this.tp.y + (float) this.height);
		app.vertex((float) this.tp.x + (float) this.width, (float) this.tp.y
				+ (float) this.height);
		app.vertex((float) this.tp.x + (float) this.width, (float) this.tp.y);
		app.endShape(app.CLOSE);
		app.popStyle();
	}

	public void draw(PGraphics app,int scale) {
		app.pushStyle();
		app.stroke(0, 100);
		app.fill(PApplet.map((float)this.area(),0,100,0,255));
		app.beginShape();
		app.vertex((float) this.tp.x*scale, (float) this.tp.y*scale);
		app.vertex((float) this.tp.x*scale, (float) this.tp.y*scale + (float) this.height*scale);
		app.vertex((float) this.tp.x*scale + (float) this.width*scale, (float) this.tp.y*scale
				+ (float) this.height*scale);
		app.vertex((float) this.tp.x*scale + (float) this.width*scale, (float) this.tp.y*scale);
		app.endShape(app.CLOSE);
		app.popStyle();
	}

	public String toString() {
		return this.tp + " wid: " + this.width + ", hei: " + this.height;
	}

}
