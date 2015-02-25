package me.Simon.nBody;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
//import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class nBody extends BasicGame{
  
	public static int width = 800;
	public static int height = 600;
	public static float factor = 3.5f;
	
	public double speed = 0.5;
	public int planets = 20;
	public int renderedObjects = 0;
	
	public static Rectangle viewPort;
	
	public static Random rand;
	public static ArrayList<body> pList = new ArrayList<body>();
	public static float dt = 0.0000015f;
	
    public nBody()
    {
        super("nBody simulations");
    }
  
    @Override
    public void init(GameContainer gc)
            throws SlickException {
    	viewPort = new Rectangle(0, 0, width, height);
    	
    	rand = new Random();
    	rand.setSeed(System.currentTimeMillis());
    	for(int i = 0; i < planets; i++){
    		float x = (rand.nextFloat()*(width*factor));
    		float y = (rand.nextFloat()*(height*factor));
    		randPlanet(x, y, false);
    	}
    }
  
    @Override
    public void update(GameContainer gc, int delta)
            throws SlickException    
    {

        planets = pList.size();
        processInput.update(gc, delta, pList);
        if(!processInput.paused && !pList.isEmpty()){
        	physics.processForces(pList, dt);
        }
        
    }

	public void render(GameContainer gc, Graphics g)
            throws SlickException
    {
//    	Renders the planets at the translated coordinates.
		g.setAntiAlias(true);
    	g.scale(processInput.zoom, processInput.zoom);
    	width = (int) (width / processInput.zoom);
    	height = (int) (height / processInput.zoom);
    	viewPort.setSize(width, height);
    	g.translate(-viewPort.getX(), -viewPort.getY());
//    	g.draw(viewPort);
    	g.setWorldClip(viewPort);
    	if(!pList.isEmpty()){
	    	for (int i = 0; i < pList.size(); i++){
	    		body curP = pList.get(i);
	    		if (viewPort.contains(curP) || viewPort.intersects(curP)) {
	        		g.setColor(pList.get(i).color);
	        		g.draw(pList.get(i));
	        		g.fill(pList.get(i));
	        		renderedObjects += 1;
	    		}
	    	}
    	}
    	g.translate(viewPort.getX(), viewPort.getY());
    	g.scale(1/processInput.zoom, 1/processInput.zoom);
    	width = gc.getWidth();
    	height = gc.getHeight();
    	
//    	Display information on top of the screen. 
    	g.setColor(Color.white);
    	g.drawString("Number of Planets, Planets Rendered: " + pList.size() + ", " 
    				+ renderedObjects, 10, 25);
    	g.drawString("Current Planet Radius and Mass: " + processInput.curPrad + ", " +
    				processInput.curPmass, 10, 40);
    	g.drawString("Current planet velocity, acceleration, and angle: " + processInput.curPV
    			+ ", " + processInput.curPA + ", " + processInput.curPT, 10, 55);
    	g.drawString("Current time step: " + (dt), 10, 70);

    	g.setColor(Color.red);
    	if(processInput.paused){
    		g.drawString("PAUSED", width / 2, height / 2);
	    	g.drawString("Controls:\nWASD/Arrows - Move\nZXCV - Zoom in, out, reset, focus\n" + 
	    				"Tab - Change Focus\nSpacebar - Reset movement\nP - Paused/UnPause\n" + 
	    				"Left click - Place New Planet\nControl+click - Increase Mass\n" +
	    				"Shift+click - Increase Radius\nAlt+click - Center on selected planet/Make new planet" +
	    				"\n1 - Clear all planets\n2 - Randomly create planets within screen\n" +
	    				"R - speed up\nF - Speed down", 10, 85);
    	}
    	renderedObjects = 0;

    }
  
    public static void main(String[] args)
            throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new nBody());
         app.setDisplayMode(width, height, false);
         app.setAlwaysRender(true);
         app.setUpdateOnlyWhenVisible(false);
         app.setResizable(true);
//         app.setTargetFrameRate(120);
         app.start();
    }
    
	public static void randPlanet(float x, float y, boolean k){
		double neg = Math.pow(-1, rand.nextInt(100));
		double pMass = rand.nextDouble() * (2e30);
		double third = 1.0/3.0;
		
		if(k == false){
			body newPlanet = new body(x, y, Math.pow(pMass, third)/(1e8), pMass,
					neg*rand.nextDouble() * 1e3, neg*rand.nextDouble() * 1e3, 
					neg*rand.nextDouble() * 1e3, neg*rand.nextDouble() * 1e3);
			pList.add(newPlanet);
		} else if(k == true) {
			body newPlanet = new body(x, y, Math.pow(pMass*(5e-21), third)/(1e8), pMass*(5e-21),
					neg*rand.nextDouble() * 1e3, neg*rand.nextDouble() * 1e3, 
					neg*rand.nextDouble() * 1e3, neg*rand.nextDouble() * 1e3);
			pList.add(newPlanet);
		}
	}
}