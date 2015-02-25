package me.Simon.nBody;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class body extends Circle {

	/**
	 * A circle with mass, velocity and acceleration.
	 */
	public Random rand = new Random();
	private double mass, vx, vy, ax, ay;
	public Color color;
	
	public body (float x, float y, double d, double mass,
				double vx, double vy, double ax, double ay) {
		super(x, y, (float) d);
		this.mass = mass;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
		color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		
	}
	
	public void setMass (double mass) {
		this.mass = mass;
	}
	
	public double getMass() {
		return this.mass;
	}
	
	public void setVX (double vx) {
		this.vx = vx;
	}
	
	public double getVX() {
		return this.vx;
	}
	
	public void setVY (double vy) {
		this.vy = vy;
	}
	
	public double getVY() {
		return this.vy;
	}
	
	public void setAX (double ax) {
		this.ax = ax;
	}
	
	public double getAX() {
		return this.ax;
	}
	
	public void setAY (double ay) {
		this.ay = ay;
	}
	
	public double getAY() {
		return this.ay;
	}
	
	public void render(GameContainer gc, Graphics g){
    	g.setColor(color);
    	g.draw(this);
    	g.fill(this);
	}
	
	public void resetForce(){
		this.ax = 0;
		this.ay = 0;
	}
	
	public void addForce(body b, double d){
		double G = 6.674e-10;
		double soft = 1e3;
		double dx = this.getCenterX() - b.getCenterX();
		double dy = this.getCenterY() - b.getCenterY();
		double r = Math.sqrt(dx*dx + dy*dy);
		
		double F = -(G * this.getMass() * b.getMass()) / (r*r + soft*soft);
		double ax = F * dx / (r*r + soft*soft);
		double ay = F * dy / (r*r + soft*soft);
		this.setAX(this.getAX() + ax);
		this.setAY(this.getAY() + ay);
	}
	
	public void update(double d) {
		double vx = this.getAX() * d / this.getMass();
		double vy = this.getAY() * d / this.getMass();
		this.setVX(this.getVX() + vx);
		this.setVY(this.getVY() + vy);
		
		double dx = this.getVX() * d;
		double dy = this.getVY() * d;
		
		this.setCenterX((float) (this.getCenterX() + dx));
		this.setCenterY((float) (this.getCenterY() + dy));
//		this.setLocation((float) (this.getX() - dx), (float) (this.getY() - dy));
	}
	
	public void collide(ArrayList<body> pList, body b){
		float dx = this.getCenterX() - b.getCenterX();
		float dy = this.getCenterY() - b.getCenterY();
		if(dx*dx + dy*dy <= this.getRadius()*this.getRadius() || 
		   dx*dx + dy*dy <= b.getRadius()*b.getRadius() ){
//		if (this.contains(b) || this.intersects(b)){
			double nMass = this.getMass() + b.getMass();
			double nVX = (this.getMass()*this.getVX() + b.getMass()*b.getVX()) / nMass;
			double nVY = (this.getMass()*this.getVY() + b.getMass()*b.getVY()) / nMass;
			Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			
			if(this.getRadius() > b.getRadius()){
				this.setMass(nMass);
				this.setRadius((float) ((float) (Math.pow(nMass, (double) 1.0/3.0))/(1e8)));
				this.color = color;
				this.setVX(nVX);
				this.setVY(nVY);
				pList.remove(b);
//				b.setMass(0);
//				b.setRadius(0);
			} else if(b.getRadius() > this.getRadius()) {
				b.setMass(nMass);
				b.setRadius((float) ((float) (Math.pow(nMass, (double) 1.0/3.0))/(1e8)));
				b.color = color;
				b.setVX(nVX);
				b.setVY(nVY);
				pList.remove(this);
//				this.setMass(0);
//				this.setRadius(0);
			}
			
		}
	}

//	public void collidec(ArrayList<body> pList, body b){
//		float dx = this.getCenterX() - b.getCenterX();
//		float dy = this.getCenterY() - b.getCenterY();
//		if(dx*dx + dy*dy <= this.getRadius()*this.getRadius() || 
//		   dx*dx + dy*dy <= b.getRadius()*b.getRadius() ){
//			this.b;
//		}
//	}
}
