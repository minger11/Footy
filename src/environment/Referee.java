package environment;

import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Referee makes decisions and declarations about the simulation
 * @author user
 *
 */

public class Referee {
	
	Context<Object> context;
	ContinuousSpace<Object> space;
	Grid<Object> grid;
	Parameters params;
	
	Referee(Context<Object> context){
		this.context = context;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		params = RunEnvironment.getInstance().getParameters();
	}
	
	public void init(){
		startGame();
	}

	public void step(){
		checkOut();
		checkTouch();
		checkTry();
	}

	public void checkOut(){
		
		Iterator<Object> it = context.getObjects(Attacker.class).iterator();
		while(it.hasNext()){
			int upperSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
			int upperEdge = upperSideline - (Integer)params.getValue("lineRadius");
			int lowerSideline = (Integer)params.getValue("fieldInset");
			int lowerEdge = lowerSideline + (Integer)params.getValue("lineRadius");
			Attacker attacker = (Attacker)it.next();
			double attackerUpperEdge = attacker.currentPosition.getY()+(Integer)params.getValue("body_radius");
			double attackerLowerEdge = attacker.currentPosition.getY()-(Integer)params.getValue("body_radius");
				if(attackerUpperEdge>=upperEdge||attackerLowerEdge<=lowerEdge){
					communicate("Out!");
					endGame();
				}
			}
	}
	
	public void checkTry(){
		int tryPoint = (Integer)params.getValue("fieldInset") + (Integer)params.getValue("fieldIncrement");
		int tryEdge = tryPoint + (Integer)params.getValue("lineRadius");
		Iterator<Object> it = context.getObjects(Attacker.class).iterator();
		while(it.hasNext()){
			Attacker attacker = (Attacker)it.next();
			double attackerEdge = attacker.currentPosition.getX()-(Integer)params.getValue("body_radius");
				if(attackerEdge<=tryEdge){
					communicate("Try!");
					endGame();
				}
			}
	}
	
	public void checkTouch(){
		Iterator<Object> it = context.getObjects(Defender.class).iterator();
		while(it.hasNext()){
			Defender defender = (Defender)it.next();
			Iterator<Object> iter = context.getObjects(Attacker.class).iterator();	
			while(iter.hasNext()){
				Attacker attacker = (Attacker)iter.next();
				if (space.getDistance(defender.getPosition(), attacker.getPosition())<(Integer)params.getValue("body_radius")*2){
					communicate("Touched!");
					endGame();
				}
			}			
		}
	}
	
	public void communicate(String message){
		System.out.println("Referee: " + message);
	}
	
	public void startGame(){
		communicate("Begin!");
	}
	
	public void endGame(){
		RunEnvironment.getInstance().endRun();
	}
	
}
