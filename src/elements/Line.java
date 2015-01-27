package elements;


import processing.core.PApplet;
import processing.core.PGraphics;

public class Line {
	/** Origin of line. */
	protected Vector2d origin;

	/** Direction of line. */
	protected Vector2d direction;
	double len;
	public Line() {
		origin = new Vector2d();
		direction = new Vector2d(1, 0);
	}

	/**
	 * o -> by value, d -> by value, adDirection: direction or normal of the line.
	 * */
	public Line(Vector2d o, Vector2d d, boolean asDirection) {
		if (asDirection) {
			origin = new Vector2d(o.x, o.y);
			direction = new Vector2d(d.x, d.y);
			direction.nor();
		} else {
			origin = o.dup();
			direction = d.subNew(origin);
			direction.nor();
			len = o.dist(d);
		}
	}


	/**
	 * o -> by value, d -> by value, adDirection: direction or normal of the line.
	 * */
	public void set(Vector2d o, Vector2d d, boolean asDirection) {
		if (asDirection) {
			origin.set(o);
			direction.set(d);
			direction.nor();
		} else {
			origin.set(o);// = o.dup();
			direction.set(d.subNew(o));
			direction.nor();
		}
	}

	/**
	 * get a point on the line
	 * */
	public Vector2d getPoint(double t) {
		Vector2d result = new Vector2d(this.direction);
		result.scale(t);
		result.add(origin);
		return result;
	}

	/**
	 * distance from v to the line
	 * */
	public double getDistance(Vector2d v) {
		Vector2d ab = this.direction.dup();
		Vector2d ac = v.subNew(this.origin);
		double e = ac.dot(ab);
		double f = ab.dot(ab);
		return (double) (ac.dot(ac) - e * e / f);
	}

	/**
	 * whether segment v1_v2 intersects the line
	 * */
	public boolean intersects(Vector2d v1, Vector2d v2) {
		Vector2d ov1 = this.origin.subNew(v1);
		Vector2d ov2 = this.origin.subNew(v2);
		Vector3d crs_d_ov1 = Vector3d.crs(this.direction.toVector3d(), ov1
				.toVector3d());
		Vector3d crs_d_ov2 = Vector3d.crs(this.direction.toVector3d(), ov2
				.toVector3d());
		return crs_d_ov1.z * crs_d_ov2.z <= 0;
	}
	
	/**
	 * whether the line intersects rectangle_r.
	 * */
	public boolean intersects(Rectangle r){
		Vector2d tp = r.tp;
		Vector2d bl = tp.dup().add(0,r.height);
		Vector2d br = tp.dup().add(r.width,r.height);
		Vector2d tr = tp.dup().add(r.width,0);
		if(this.intersects(tp,bl)) return true;
		if(this.intersects(bl,br)) return true;
		if(this.intersects(br,tr)) return true;
		if(this.intersects(tr,tp)) return true;
		return false;
	}
	
	/**
	 * normal of the line
	 * */
	public Vector2d getNormal() {
		Vector3d n = Vector3d
				.crs(new Vector3d(0, 0, 1), direction.toVector3d());
		n.nor();
		return n.toVector2d();
	}

	public void draw(PApplet app) {
		double length = 5000.0f;
		double halfLn = length / 2.0f;

		Vector2d start = this.getPoint(halfLn);
		Vector2d end = this.getPoint(-1 * halfLn);

		app.pushStyle();

		app.noFill();
		app.stroke(0);
		app.line((float) start.x, (float) start.y, (float) end.x,
				(float) end.y);

		/*app.noStroke();
		app.fill(255, 0, 0);
		app.ellipse(this.origin.x, this.origin.y, 4, 4);*/
		app.popStyle();
	}
	
	public void draw(PGraphics app) {
		double length = 5000.0f;
		double halfLn = length / 2.0f;

		Vector2d start = this.getPoint(halfLn);
		Vector2d end = this.getPoint(-1 * halfLn);

		app.pushStyle();

		app.noFill();
		app.stroke(0);
		app.line((float) start.x, (float) start.y, (float) end.x,
				(float) end.y);

		/*app.noStroke();
		app.fill(255, 0, 0);
		app.ellipse(this.origin.x, this.origin.y, 4, 4);*/
		app.popStyle();
	}
	
	public void drawStroke(PApplet app,float s, float c1){
		Vector2d end = this.getPoint(this.len);
		Vector2d rot = this.direction.toVector3d().crs(this.direction.toVector3d(), new Vector3d(0,0,1)).toVector2d().nor();
		Vector2d A = this.origin.dup().add(rot.scaleNew(s/2));
		Vector2d B = end.dup().add(rot.scaleNew(s/2));
		Vector2d C = end.dup().add(rot.scaleNew(s/2).rev());
		Vector2d D = this.origin.dup().add(rot.scaleNew(s/2).rev());

		Polygen2D pp = new Polygen2D();
		pp.addVertex(A.x, A.y);
		pp.addVertex(B.x, B.y);
		pp.addVertex(C.x, C.y);
		pp.addVertex(D.x, D.y);
		
		pp.drawfill(app, c1, true);
	}
}
