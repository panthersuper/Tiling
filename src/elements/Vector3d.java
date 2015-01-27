package elements;

import processing.core.PApplet;

public class Vector3d {
	public double x, y, z;

	public Vector3d() {
		this.x = this.y = this.z = 0;
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}
	
	public double z() {
		return this.z;
	}
	
	public Vector3d set(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3d set(Vector3d v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}
	
	public Vector3d scale(double s){
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	
	public Vector3d add(Vector3d v){
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}
	
	public Vector3d add(double x,double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vector3d sub(Vector3d v){		
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	public Vector3d subNew(Vector3d v){		
		return new Vector3d(this.x-v.x,this.y-v.y,this.z-v.z);
	}
	
	public Vector3d rev(){
		this.x *= -1.0;
		this.y *= -1.0;
		this.z *= -1.0;
		return this;
	}	
	
	public Vector3d nor(){
		double len = this.len();
		if(len==0){
			//need debug: throws Exception here
			//...
		}
		this.x *= 1.0/len;
		this.y *= 1.0/len;
		this.z *= 1.0/len;
		//System.out.println("this: "+this);
		return this;
	}
	
	public double len(){
		return (double)Math.sqrt(this.x * this.x + this.y*this.y + this.z*this.z);
	}
	
	public double dot(Vector3d v){
		return this.x*v.x+this.y*v.y+this.z*v.z;
	}
	
	public static Vector3d crs(final Vector3d p, final Vector3d q) {
		return new Vector3d(p.y * q.z - p.z * q.y, p.z * q.x - p.x * q.z, p.x
				* q.y - p.y * q.x);
	}
	
	public double dist2(Vector3d that){
		double deltaX = this.x - that.x;
		double deltaY = this.y - that.y;
		double deltaZ = this.z - that.z;
		return deltaX*deltaX+deltaY*deltaY+deltaZ*deltaZ;
	}
	
	public Vector3d dup(){
		return new Vector3d(this.x,this.y,this.z);
	}
	
	public static double agl(Vector3d p,Vector3d q){
		Vector3d pCopy = p.dup().nor();
		Vector3d qCopy = q.dup().nor();
		
		return (double) Math.acos(pCopy.x * qCopy.x + pCopy.y * qCopy.y + pCopy.z * qCopy.z);
	}
	
	/**
	 * rotate new instance of this based on input base, axis and angle rotated.
	 * */
	public Vector3d rot(Vector3d base, Vector3d axis, double angleCos, double angleSin){
		Vector3d copyThis = this.dup();
		Vector3d copyBase = base.dup();
		Vector3d axisCopy = axis.dup();		
		
		Vector3d v = copyThis.subNew(copyBase);
		Vector3d u = axisCopy.nor();
		
		Vector3d vCos = v.dup().scale(angleCos);
		Vector3d uScl = u.dup().scale((1-angleCos)*(v.dot(u)));
		Vector3d cros = Vector3d.crs(u, v).scale(angleSin);
		Vector3d t = vCos.add(uScl).add(cros);
		return t.add(copyBase);
	}
	
	public static Vector3d[] rotVS(Vector3d base, Vector3d axis, double angleCos, double angleSin, Vector3d...vs){
		Vector3d copyBase = base.dup();
		Vector3d axisCopy = axis.dup();		
		
		Vector3d v;
		Vector3d u = axisCopy.nor();
		
		int len = vs.length;
		Vector3d[] vvs = new Vector3d[len];
		for(int i=0;i<len;i++){
			v = vs[i].dup().subNew(copyBase);
			
			Vector3d vCos = v.dup().scale(angleCos);
			Vector3d uScl = u.dup().scale((1-angleCos)*(v.dot(u)));
			Vector3d cros = Vector3d.crs(u, v).scale(angleSin);
			Vector3d t = vCos.add(uScl).add(cros);
			vvs[i] = t.add(copyBase);
		}
		return vvs;
	}
	
	public double dist(Vector3d that){
		return (double) Math.sqrt(this.dist2(that));
	}
	
	public Vector2d toVector2d(){
		return new Vector2d(this.x, this.y);
	}
	
	public String toString(){
		return "["+this.x+", "+ this.y+", "+this.z+"]";
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
		app.popStyle();
	}
}
