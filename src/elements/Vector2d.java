package elements;

import processing.core.PApplet;

public class Vector2d {
	public double x, y;

	public Vector2d() {

	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d v) {
		this.x = v.x;
		this.y = v.y;
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}
	
	public Vector2d set(double x, double y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2d set(Vector2d v){
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	public Vector2d scale(double s){
		this.x *= s;
		this.y *= s;
		return this;
	}
	
	public Vector2d scaleNew(double s){
		return new Vector2d(s*this.x,s*this.y);
	}
	
	public Vector2d add(Vector2d v){
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector2d add(double x,double y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2d sub(double x,double y){
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2d subNew(Vector2d v){
		/*this.x -= v.x;
		this.y -= v.x;
		return this;*/
		return new Vector2d(this.x-v.x,this.y-v.y);
	}
	
	public Vector2d rev(){
		this.x *= -1;
		this.y *= -1;
		return this;
	}
	
	public Vector2d dup(){
		return new Vector2d(this.x,this.y);
	}
	
	public Vector2d nor(){
		double len = this.len();
		if(len==0){
			//need debug: throws Exception here
			//...
		}
		this.x *= 1.0f/len;
		this.y *= 1.0f/len;
		return this;
	}
	
	public double len(){
		return (double)Math.sqrt(this.x * this.x + this.y*this.y);
	}
	
	public double dot(Vector2d v){
		return this.x*v.x+this.y*v.y;
	}
	
	public double dist2(Vector2d that){
		double deltaX = this.x-that.x;
		double deltaY = this.y-that.y;
		return deltaX*deltaX+deltaY*deltaY;
	}
	
	public double dist(Vector2d that){
		double deltaX = this.x-that.x;
		double deltaY = this.y-that.y;
		return (double) Math.sqrt(deltaX*deltaX+deltaY*deltaY);
	}
	
	public static Vector2d getPoint(Vector2d fromV,Vector2d toV,double ratio){
		Vector2d di = toV.subNew(fromV);
		
		di.scale(ratio);
		return di.add(fromV);
	}
	
	public Vector3d toVector3d(){
		return new Vector3d(this.x, this.y, 0);
	}
	
	public String toString(){
		return "["+this.x+", "+ this.y+"]";
	}
	
	
	private double drawDimeter = 0; 
	public boolean isBalance = true;
	
	public void setDrawDimeter(double d){
		this.drawDimeter = d;
	}
	
	public void draw(PApplet app) {
		app.pushStyle();
		app.stroke(0);
		app.fill(255, 0, 0);
		app.ellipse((float)this.x, (float)this.y, 4, 4);
		if(!this.isBalance){
			app.stroke(255);
			app.noFill();
			app.ellipse((float)this.x, (float)this.y, (float)drawDimeter, (float)drawDimeter);
		}
		app.popStyle();
	}
	
	
}
