package me.Simon.nBody;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class processInput {
	

	public static boolean locked = false;
	public static boolean onPlanet = false;
	public static boolean paused = true;
	
	public static int width = 800;
	public static int height = 600;
	public static int chosen = 0;
	
	public static float mX = 0;
	public static float mY = 0;
	public static float zoom = (float) 0.5;
	public static float curPrad = 0;
	public static float wait = 0;
	
	public static double curPmass = 0;
	public static double curPV = 0;
	public static double curPA = 0;
	public static double curPT = 0;
	public static double speed = 0.5;

	public static body curPlanet = null;
	public static body largest = null;
	public static body newPlanet = null;
	public Random rand;
	
    public static void update(GameContainer gc, int delta, ArrayList<body> pList) throws SlickException {
       	
		Vector2f trans = new Vector2f(0,0);
		Input input = gc.getInput();

//		Gets mouse position relative to the world.
		mX = input.getAbsoluteMouseX() / zoom + nBody.viewPort.getX();
		mY = input.getAbsoluteMouseY() / zoom + nBody.viewPort.getY();
		
    	if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			speed += speed / 1e2;
			for (int i = 0; i < pList.size(); i++){
				curPlanet = pList.get(i);
				float dx = Math.abs(mX - curPlanet.getCenterX());
				float dy = Math.abs(mY - curPlanet.getCenterY());
				float radius = curPlanet.getRadius();

				if ( dx*dx + dy*dy <= radius*radius){
					chosen = i;
					onPlanet = true;
					curPrad = Math.round(curPlanet.getRadius());
					curPmass = curPlanet.getMass();
					curPV = Math.round(Math.sqrt(curPlanet.getVX()*curPlanet.getVX() + curPlanet.getVY()*curPlanet.getVY()));
					curPA = Math.round(Math.sqrt(curPlanet.getAX()*curPlanet.getAX() + curPlanet.getAY()*curPlanet.getAY()));
					curPT = Math.round(Math.toDegrees(Math.atan2(curPlanet.getVY(), curPlanet.getVX())));
					if(input.isKeyDown(Input.KEY_LSHIFT)){
						curPlanet.setRadius((float) (curPlanet.getRadius() + 0.05*delta));
						curPrad = curPlanet.getRadius();
						curPmass = curPlanet.getMass();
					} else if(input.isKeyDown(Input.KEY_LCONTROL)){
						curPlanet.setMass(curPlanet.getMass() + 1e23 + speed*delta);
						curPmass = curPlanet.getMass();
						curPrad = curPlanet.getRadius();
//						System.out.print(speed + "\n");
					} else if(input.isKeyDown(Input.KEY_LALT)){
						locked = true;
						onPlanet = true;
						largest = curPlanet;
					}
				}
			}

			int max = 1;
			if (!onPlanet && max == 1){
		    	nBody.randPlanet(mX, mY, false);
			}
    	} else {
    		onPlanet = false;
			speed = 0.5;
    	}
  
    	
//    	Remove/Add Planets
    	if(input.isKeyDown(Input.KEY_1)){
    		pList.clear();
    	}
    	if(input.isKeyDown(Input.KEY_2)){
        	float x = (float) ((float) nBody.viewPort.getX() + Math.random()*nBody.viewPort.getWidth());
        	float y = (float) ((float) nBody.viewPort.getY() + Math.random()*nBody.viewPort.getHeight());
        	nBody.randPlanet(x, y, false);
    	}
    	if(input.isKeyDown(Input.KEY_3) || input.isKeyPressed(Input.MOUSE_RIGHT_BUTTON)){
    		if(wait == 0){
    			nBody.randPlanet(mX, mY, true);
    		}
    		wait += .1;
    		if(wait > 1){
    			wait = 0;
    		}
    	}
    	if(input.isKeyDown(Input.KEY_4) || input.isKeyPressed(Input.MOUSE_RIGHT_BUTTON)){
    		if(wait == 0){
    			nBody.randPlanet(mX, mY, false);
    		}
    		wait += .1;
    		if(wait > 1){
    			wait = 0;
    		}
    	}
    	
//    	Camera Zoom
    	if(input.isKeyDown(Input.KEY_Z)){
    		if(input.isKeyDown((Input.KEY_LSHIFT))){
    			zoom += 0.0001f;
	    		width = (int) (width / zoom);
    		} else {
	    		zoom += 0.001f;
	    		width = (int) (width / zoom);
    		}
    		if(zoom >= 1.75f)
            	zoom = 1.75f;
    	}
    	if(input.isKeyDown(Input.KEY_X)){
    		if(input.isKeyDown(Input.KEY_LSHIFT)){
    			zoom -= 0.0001f;
    		} else {
	    		zoom -= 0.001f;
    		}

            if(zoom <= 0.001f)
            	zoom = 0.001f;
    	}
    	if(input.isKeyDown(Input.KEY_C)){
    		zoom = 1;
    	}
        
//    	Focuses the camera on the largest planet.
    	if(input.isKeyPressed(Input.KEY_V)){
    		locked = !locked;
			onPlanet = true;
    		largest = new body(0, 0, 0, 0, 0, 0, 0, 0);
    		for(int i = 0; i < pList.size(); i++){
    			if(pList.get(i).getMass() > largest.getMass())
    				largest = pList.get(i);
    		}
			curPrad = Math.round(largest.getRadius());
			curPmass = largest.getMass();
			curPV = Math.round(Math.sqrt(largest.getVX()*largest.getVX() + largest.getVY()*largest.getVY()));
			curPA =Math.sqrt(largest.getAX()*largest.getAX() + largest.getAY()*largest.getAY());
			curPT = Math.round(Math.toDegrees(Math.atan2(largest.getVY(), largest.getVX())));
    	}
    		
//   	Changes the focus of the camera.
    	if(input.isKeyPressed(Input.KEY_TAB)){
	    	int curPlan = pList.indexOf(largest);
	    	if (curPlan >= pList.size() - 1){
	    		curPlan = 0;
	    	} else if(curPlan < pList.size()){
	    		curPlan += 1;
	    	}
	    	largest = pList.get(curPlan);
			curPrad = Math.round(largest.getRadius());
			curPmass = largest.getMass();
			curPV = Math.round(Math.sqrt(largest.getVX()*largest.getVX() + largest.getVY()*largest.getVY()));
			curPA =Math.sqrt(largest.getAX()*largest.getAX() + largest.getAY()*largest.getAY());
			curPT = Math.round(Math.toDegrees(Math.atan2(largest.getVY(), largest.getVX())));
    	}
    	
    	if(locked){
			curPrad = Math.round(largest.getRadius());
			curPmass = largest.getMass();
			curPV = Math.round(Math.sqrt(largest.getVX()*largest.getVX() + largest.getVY()*largest.getVY()));
			curPA =Math.sqrt(largest.getAX()*largest.getAX() + largest.getAY()*largest.getAY());
			curPT = Math.round(Math.toDegrees(Math.atan2(largest.getVY(), largest.getVX())));
    	}
    	
//    	Handles camera movement
		if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP))
			trans.y = -0.5f * delta / zoom;
	 
		if(input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN))
			trans.y = 0.5f * delta / zoom;
	 
		if(input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))
			trans.x = 0.5f * delta / zoom;
	 
		if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))
			trans.x = -0.5f * delta / zoom;
//		Resets camera to default view
		if(input.isKeyDown(Input.KEY_SPACE)){
			nBody.viewPort.setX(0);
			nBody.viewPort.setY(0);
		}
//		Move the camera to the set viewing frame.
		if(locked){
    		nBody.viewPort.setCenterX(largest.getCenterX());
    		nBody.viewPort.setCenterY(largest.getCenterY());
		} else {
			nBody.viewPort.setX(nBody.viewPort.getX() + trans.x);
			nBody.viewPort.setY(nBody.viewPort.getY() + trans.y);
		}
		
//		Pausing.
		if(input.isKeyPressed(Input.KEY_P)){
			if(paused){
				paused = false;
			} else {
				paused = true;
			}
		}
		
//		Time
		if(input.isKeyPressed(Input.KEY_R)){
			nBody.dt *= 2;
		}
		if(input.isKeyPressed(Input.KEY_F)){
			nBody.dt *= .5;
		}
    }
    
}
