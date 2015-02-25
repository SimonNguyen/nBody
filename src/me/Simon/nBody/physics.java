package me.Simon.nBody;

import java.util.ArrayList;

public class physics {
    public static void processForces(ArrayList<body> pList, double d) {
        for(int i =0; i < pList.size(); i++){
        	try {
	        	pList.get(i).resetForce();
				for(int j = 0; j < pList.size(); j++){
					try {
						if(i != j){
							pList.get(i).addForce(pList.get(j), d);
							pList.get(i).collide(pList, pList.get(j));
//							pList.get(i).collidec(pList, pList.get(j));
						} 
					} catch (IndexOutOfBoundsException e) {
						pList.trimToSize();
					}
				}
        	} catch (IndexOutOfBoundsException e) {
        		pList.trimToSize();
        		System.out.print("Index Out of Bounds: " + i);
        	}
        }
        for(int i = 0; i < pList.size(); i++){
        	pList.get(i).update(d);
        }
	}
}
