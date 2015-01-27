package elements;

import igeo.IVec2;

import java.awt.geom.Path2D;
import java.util.ArrayList;

import Tile01.test;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Polygen2D {
	private ArrayList<Vector2d> vs;
	private Path2D.Float polygon;
	public int iteration = 0;
	PApplet app;

	public Polygen2D() {
		vs = new ArrayList<Vector2d>();
		setPoly();
	}

	/**
	 * add a vertex at the end of the Polygen2D
	 * */
	public void addVertex(double x, double y) {
		this.vs.add(new Vector2d(x, y));

		this.setPoly();
	}

	/**
	 * edges size is less than 3
	 */
	public boolean isNull() {
		return vs.size() < 3;
	}

	public double area() {
		double s = 0;
		for (int i = 0; i < this.vs.size(); i++) {
			s += (vs.get(i).y() + vs.get((i + 1) % vs.size()).y())
					* (vs.get(i).x() - vs.get((i + 1) % vs.size()).x());

		}

		return s;
	}

	public ArrayList<Vector3d> PerpendicularPts(Vector2d v) {

		ArrayList<Vector3d> lst = new ArrayList<>();
		IVec2 vv = new IVec2(v.x, v.y);

		for (int i = 0; i < this.vs.size(); i++) {
			Line2d l = new Line2d(new IVec2(vs.get(i).x, vs.get(i).y),
					new IVec2(vs.get((i + 1) % vs.size()).x, vs.get((i + 1)
							% vs.size()).y));
			IVec2 pp = l.VerticalPoint(vv);
			if (l.contain(pp)) {
				lst.add(new Vector3d(pp.x, pp.y, i));
			}
		}

		return lst;
	}

	public ArrayList<Polygen2D> subLnEdge(){
		//return the sub regular triangle
		ArrayList<Polygen2D> alst = new ArrayList<>();
		ArrayList<Line> llst = new ArrayList<>();
		
		
		for(int i = 0;i<this.vs.size();i++){
			Line l = new Line(new Vector2d(vs.get(i).x, vs.get(i).y),
					new Vector2d(vs.get((i + 1) % vs.size()).x, vs.get((i + 1)
							% vs.size()).y), false);
			llst.add(l);
		}
		int count = 0;
		double ave = 0;
		
		for(int i = 0;i<llst.size();i++){
			ave+=llst.get(i).len;
		}
		ave/=llst.size();
		for(int i = 0;i<llst.size();i++){
			if(llst.get(i).len>ave){
				count = i;
			}
		}
		
		

		Line ll = new Line();

		for(int i=0;i<llst.size();i++){
			if(i!=count){
				Polygen2D pp = new Polygen2D();
				pp.addVertex(llst.get(count).getPoint(0.5*llst.get(count).len).x,llst.get(count).getPoint(0.5*llst.get(count).len).y);
				pp.addVertex(llst.get(i).getPoint(0).x,llst.get(i).getPoint(0).y);
				pp.addVertex(llst.get(i).getPoint(llst.get(i).len).x,llst.get(i).getPoint(llst.get(i).len).y);
				alst.add(pp);
				ll = new Line(llst.get(count).getPoint(0.5*llst.get(count).len),llst.get(i).getPoint(llst.get(i).len),false);
			}
		}
		
		double d = 2f/5 * ll.len;
		Vector2d A = ll.getPoint(d);
		Vector2d B = ll.getPoint(2*d);
		Vector2d AB = B.subNew(A);
		Vector3d ab = AB.toVector3d();
		Vector2d AC = ab.crs(ab, new Vector3d(0,0,1)).toVector2d().rev().scale(0.5f);
		Vector2d C = A.dup().add(AC);
		Vector2d D = B.dup().add(AC);
		Vector2d E = A.dup().add(AC.rev());
		Vector2d F = E.dup().add(AB);
		Polygen2D r = new Polygen2D();
		r.addVertex(D.x, D.y);
		r.addVertex(C.x, C.y);
		r.addVertex(E.x, E.y);
		r.addVertex(F.x, F.y);

		test.rect.add(r);
		return alst;
		
	}
	
	public ArrayList<Polygen2D> subLnEdge(int num) {
		// num is the iteration number

		if (num == 1) {
			ArrayList<Polygen2D> lst = new ArrayList<>();
			for (Polygen2D r : this.subLnEdge()) {
				r.iteration = 1;
				lst.add(r);
			}
			return lst;

		} else {
			ArrayList<Polygen2D> llst = subLnEdge(num - 1);
			ArrayList<Polygen2D> lmo = new ArrayList<>();
			for (Polygen2D r : llst) {
				lmo.add(r);
				for (Polygen2D rr : r.subLnEdge())
					//rr.iteration = r.iteration+1;
					lmo.add(rr);
					lmo.get(lmo.size()-1).iteration = r.iteration+1;
			}

			return lmo;
		}

	}
	
	
	public ArrayList<Polygen2D> subOnedge(){
		ArrayList<Polygen2D> pls = new ArrayList<>();
		ArrayList<Vector2d> pts = new ArrayList<>();

		Vector2d vr = this.setRandomPt(1).get(0);

		for(int i = 0;i<this.vs.size();i++){
			Line l = new Line(new Vector2d(vs.get(i).x, vs.get(i).y),
					new Vector2d(vs.get((i + 1) % vs.size()).x, vs.get((i + 1)
							% vs.size()).y), false);
			double t = 0.5;
			Vector2d vv = l.getPoint(t*l.len);

			pts.add(vv);
			
		}
		
		for(int i=0;i<pts.size();i++){
			Polygen2D pp = new Polygen2D();
			pp.addVertex(vr.x,vr.y);
			pp.addVertex(pts.get(i).x, pts.get(i).y);
			pp.addVertex(vs.get((i+1)%vs.size()).x, vs.get((i+1)%vs.size()).y);
			pp.addVertex(pts.get((i+1)%pts.size()).x, pts.get((i+1)%pts.size()).y);

			
			pls.add(pp);

			
		}
		
		return pls;
		
	}
	
	public ArrayList<Polygen2D> subOnedge(int num) {
		// num is the iteration number

		if (num == 1) {
			ArrayList<Polygen2D> lst = new ArrayList<>();
			for (Polygen2D r : this.subOnedge()) {
				lst.add(r);
			}
			return lst;

		} else {
			ArrayList<Polygen2D> llst = subOnedge(num - 1);
			ArrayList<Polygen2D> lmo = new ArrayList<>();
			for (Polygen2D r : llst) {
				// lmo.add(r);
				for (Polygen2D rr : r.subOnedge())
					lmo.add(rr);

			}

			return lmo;
		}

	}
	
	
	public ArrayList<Polygen2D> subPerpendicular() {
		// return subdivided rectangle of this -
		// perpendicular subdivision line
		ArrayList<Polygen2D> lst = new ArrayList<>();
		Vector2d vr = this.setRandomPt(1).get(0);

		ArrayList<Vector3d> perlst = PerpendicularPts(vr);

		for (int i = 0; i < perlst.size(); i++) {

			Vector2d a = vr;
			Vector2d b = new Vector2d(perlst.get(i).x, perlst.get(i).y);
			Vector2d c = vs.get(((int) perlst.get(i).z() + 1) % vs.size());
			Vector2d d = vs.get(((int) perlst.get((i + 1) % perlst.size()).z())
					% vs.size());
			Vector2d e = new Vector2d(perlst.get((i + 1) % perlst.size()).x,
					perlst.get((i + 1) % perlst.size()).y);

			Polygen2D pp = new Polygen2D();
			pp.addVertex(a.x, a.y);
			pp.addVertex(b.x, b.y);
			pp.addVertex(c.x, c.y);
			if (d.x != c.x && d.y != c.y)
				pp.addVertex(d.x, d.y);
			pp.addVertex(e.x, e.y);

			PApplet.println(pp.vs.size());

			lst.add(pp);
		}

		return lst;
	}

	public ArrayList<Polygen2D> subPerpendicular(int num) {
		// num is the iteration number

		if (num == 1) {
			ArrayList<Polygen2D> lst = new ArrayList<>();
			for (Polygen2D r : this.subPerpendicular()) {
				lst.add(r);
			}
			return lst;

		} else {
			ArrayList<Polygen2D> llst = subPerpendicular(num - 1);
			ArrayList<Polygen2D> lmo = new ArrayList<>();
			for (Polygen2D r : llst) {
				// lmo.add(r);
				for (Polygen2D rr : r.subPerpendicular())
					lmo.add(rr);

			}

			return lmo;
		}

	}

	public ArrayList<Polygen2D> subTri() {
		// return subdivided rectangle of this -
		// perpendicular subdivision line
		ArrayList<Polygen2D> lst = new ArrayList<>();

		Vector2d vr = this.setRandomPt(1).get(0);

		// if (new Rectangle(this.tp, vr).ratio()<5)
		for (int i = 0; i < this.vs.size(); i++) {
			Polygen2D pp = new Polygen2D();
			pp.addVertex(vs.get(i).x(), vs.get(i).y());
			pp.addVertex(vs.get((i + 1) % vs.size()).x(),
					vs.get((i + 1) % vs.size()).y());
			pp.addVertex(vr.x(), vr.y());

			lst.add(pp);
		}

		return lst;
	}

	public ArrayList<Polygen2D> subTri(int num) {
		// num is the iteration number

		if (num == 1) {
			ArrayList<Polygen2D> lst = new ArrayList<>();
			for (Polygen2D r : this.subTri()) {
				lst.add(r);
			}
			return lst;

		} else {
			ArrayList<Polygen2D> llst = subTri(num - 1);
			ArrayList<Polygen2D> lmo = new ArrayList<>();
			for (Polygen2D r : llst) {
				// lmo.add(r);
				for (Polygen2D rr : r.subTri())
					lmo.add(rr);

			}
			PApplet.println(lmo.size());

			return lmo;
		}

	}

	/**
	 * set num points in the Polygen2D
	 */
	public ArrayList<Vector2d> setRandomPt(int num) {
		ArrayList<Vector2d> pts = new ArrayList<Vector2d>();
		while (pts.size() < num) {
			Vector2d r = setRandomPt();
			if (r != null) {
				pts.add(r);
			}

		}
		return pts;
	}

	/**
	 * set a point within the polygon2d in a random location. return null if the
	 * point is out of the polygen.
	 * */
	private Vector2d setRandomPt() {
		double minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
		for (int i = 0; i < vs.size(); i++) {
			if (vs.get(i).x < minX) {
				minX = vs.get(i).x;
			}
			if (vs.get(i).y < minY) {
				minY = vs.get(i).y;
			}
			if (vs.get(i).x > maxX) {
				maxX = vs.get(i).x;
			}
			if (vs.get(i).y > maxY) {
				maxY = vs.get(i).y;
			}
		}
		Vector2d randPt = new Vector2d((double) (minX + (maxX - minX)
				* Math.random()), (double) (minY + (maxY - minY)
				* Math.random()));
		if (this.contains(randPt))
			return randPt;
		else
			return null;
	}

	/**
	 * whether point(x,y) is in range of the polygen.
	 * */
	public boolean contains(double x, double y) {
		return this.polygon.contains(x, y);
	}

	/**
	 * whether point_v is in range of the polygen.
	 * */
	public boolean contains(Vector2d v) {
		return this.polygon.contains(v.x, v.y);
	}

	/**
	 * delete all vertexes the polygen.
	 * */
	public void reset() {
		this.polygon = new Path2D.Float();
		vs.clear();
	}

	/**
	 * set coordinate Path2D.Float polygon.
	 * */
	private void setPoly() {
		this.polygon = new Path2D.Float();
		int leng = vs.size();
		if (leng > 0) {
			polygon.moveTo(this.vs.get(leng - 1).x, this.vs.get(leng - 1).y);
			for (int i = 0; i < leng; i++) {
				polygon.lineTo(this.vs.get(i).x, this.vs.get(i).y);
			}
		}
	}

	/**
	 * shortest distance of Vector_v to edges of the polygen.
	 * */
	public double distance(Vector2d v) {
		int size = vs.size();
		if (size < 3) {
			// need to debug...
			System.out.println("Edges of polygen shouldn't less than 3");
		}
		double minDist = Double.MAX_VALUE;
		double c_dist = sqDistanceToSegment(v, vs.get(size - 1), vs.get(0));
		if (c_dist < minDist)
			minDist = c_dist;
		for (int i = 0; i < vs.size() - 1; i++) {
			c_dist = sqDistanceToSegment(v, vs.get(i), vs.get(i + 1));
			if (c_dist < minDist)
				minDist = c_dist;
		}
		if (minDist < 0)
			return 0;
		return Math.sqrt(minDist);
	}

	/**
	 * distance of point_p to segment ab.
	 * */
	private double sqDistanceToSegment(Vector2d p, Vector2d a, Vector2d b) {
		Vector2d ab = b.subNew(a);
		Vector2d ac = p.subNew(a);
		Vector2d bc = p.subNew(b);
		double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		double f = ab.dot(ab);
		if (e >= f) {
			return bc.dot(bc);
		}
		return ac.dot(ac) - e * e / f;
	}

	/**
	 * main method for calculating distance need to keep between every points
	 * according to defined conditions (sin()).
	 * */
	public double getDistanceKeep(Vector2d v, double distWillEffect,
			double minDistPTP, double maxDistPTP) {
		double dist2Edge = this.distance(v);
		if (dist2Edge < distWillEffect) {
			double map2Angle = mapData(dist2Edge, 0, distWillEffect, 0,
					(Math.PI / 2.0));
			double sinAngle = Math.sin(map2Angle);

			return minDistPTP + (maxDistPTP - minDistPTP) * sinAngle;
		} else {
			return maxDistPTP;
		}

	}

	/**
	 * rewrite map() method in Processing
	 * */
	private double mapData(double v, double a, double b, double aa, double bb) {
		if (pqEquals(v, a))
			return aa;
		if (pqEquals(a, b))
			return v;
		double k = (b - v) / (v - a);
		return (k * aa + bb) / (1 + k);
	}

	/**
	 * equal at a small different value.
	 * */
	private boolean pqEquals(double p, double q) {
		double delta = p - q;
		delta = delta > 0 ? delta : -delta;
		return delta < 0.00000000001;
	}

	/**
	 * drawing in Processing.
	 * */

	public void draw(PApplet app) {
		app.pushStyle();
		app.stroke(0, 0, 0);
		 app.noFill();
		 //app.println(this.area());
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,5000,0,255));
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,400,0,255));
		app.beginShape();
		for (int i = 0; i < vs.size(); i++) {
			app.vertex((float) vs.get(i).x, (float) vs.get(i).y);
		}
		app.endShape(app.CLOSE);
		app.popStyle();
	}
	
	public void draw(PApplet app,float c1, boolean c2) {
		app.pushStyle();
		app.stroke(c1);
		app.strokeWeight(0.5f);
		if(c2)
		 app.noFill();
		else{
			app.fill(178);
		}
		 //app.println(this.area());
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,5000,0,255));
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,400,0,255));
		app.beginShape();
		for (int i = 0; i < vs.size(); i++) {
			app.vertex((float) vs.get(i).x, (float) vs.get(i).y);
		}
		app.endShape(app.CLOSE);


		app.strokeWeight(0.5f);

		for(int i=0;i<vs.size();i++){
			Line ll = new Line(new Vector2d(vs.get(i).x, vs.get(i).y),
					new Vector2d(vs.get((i + 1) % vs.size()).x, vs.get((i + 1)
							% vs.size()).y), false);
			app.strokeWeight(PApplet.map((float) ll.len,0,150,0,5f));
			app.line((float) vs.get(i).x, (float) vs.get(i).y, (float) vs.get((i+1)%vs.size()).x, (float) vs.get((i+1)%vs.size()).y);
			//app.line((float) vs.get((i+1)%vs.size()).x, (float) vs.get((i+1)%vs.size()).y,(float) vs.get(i).x, (float) vs.get(i).y);
			//ll.drawStroke(app, PApplet.map((float) ll.len,0,150,0,5f), c1);
		}
		app.popStyle();
	}

	public void drawfill(PApplet app,float c1, boolean c2) {
		app.pushStyle();
		app.noStroke();
		app.strokeWeight(0.5f);
			app.fill(c1);
		 //app.println(this.area());
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,5000,0,255));
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,400,0,255));
		app.beginShape();
		for (int i = 0; i < vs.size(); i++) {
			app.vertex((float) vs.get(i).x, (float) vs.get(i).y);
		}
		app.endShape(app.CLOSE);

		app.popStyle();
	}
	
	public void save(PGraphics pg,int scale) {
		
		pg.pushStyle();
		pg.strokeWeight(0.5f*scale);
		 //app.println(this.area());
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,5000,0,255));
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,400,0,255));
		pg.beginShape();
		for (int i = 0; i < vs.size(); i++) {
			pg.vertex((float) vs.get(i).x*scale, (float) vs.get(i).y*scale);
		}
		pg.endShape(pg.CLOSE);


		pg.popStyle();
	}
	public void save(PGraphics pg,int scale,float c1, boolean c2) {
		
		pg.pushStyle();
		pg.stroke(c1);
		pg.strokeWeight(0.5f*scale);
		if(c2)
			pg.noFill();
		else{
			pg.fill(178);
		}
		 //app.println(this.area());
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,5000,0,255));
		 //app.fill(PApplet.map(Math.abs((float)this.area()),0,400,0,255));
		pg.beginShape();
		for (int i = 0; i < vs.size(); i++) {
			pg.vertex((float) vs.get(i).x*scale, (float) vs.get(i).y*scale);
		}
		pg.endShape(pg.CLOSE);


		pg.strokeWeight(0.5f);

		for(int i=0;i<vs.size();i++){
			Line ll = new Line(new Vector2d(vs.get(i).x*scale, vs.get(i).y*scale),
					new Vector2d(vs.get((i + 1) % vs.size()).x*scale, vs.get((i + 1)
							% vs.size()).y*scale), false);
			pg.strokeWeight(PApplet.map((float) ll.len,0,150*scale,0,5f*scale));
			pg.line((float) vs.get(i).x*scale, (float) vs.get(i).y*scale, (float) vs.get((i+1)%vs.size()).x*scale, (float) vs.get((i+1)%vs.size()).y*scale);
			//app.line((float) vs.get((i+1)%vs.size()).x, (float) vs.get((i+1)%vs.size()).y,(float) vs.get(i).x, (float) vs.get(i).y);
			//ll.drawStroke(app, PApplet.map((float) ll.len,0,150,0,5f), c1);
		}
		pg.popStyle();
	}
	

}
