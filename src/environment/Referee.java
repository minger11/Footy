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
	MessageBoard messageBoard;
	
	Referee(Context<Object> context){
		this.context = context;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		params = RunEnvironment.getInstance().getParameters();
		Iterator<Object> iter = context.getObjects(MessageBoard.class).iterator();
		this.messageBoard = (MessageBoard) iter.next();
	}
	
	public void init(){
		startGame();
	}

	public void step(){
		checkOut();
		checkTouch();
		checkTry();
		checkForward();
	}

	
	public void checkForward(){
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		if(ball.player!=null){
		} else {
			if(ball.movement.velocity.getX()<0){
				messageBoard.addMessage(this, "Forward!");
				endGame();
			}
		}
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
					messageBoard.addMessage(this, "Out!");
					endGame();
				}
			}
	}
	
	public void checkTry(){
		int tryPoint = (Integer)params.getValue("fieldInset") + (Integer)params.getValue("fieldIncrement");
		int tryEdge = tryPoint + (Integer)params.getValue("lineRadius");
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		if(ball.player!=null){
			double ballEdge = ball.currentPosition.getX()-(Integer)params.getValue("ball_radius");
				if(ballEdge<=tryEdge){
					messageBoard.addMessage(this, "Try!");
					endGame();
				}
		}
	}
	
	public void checkTouch(){
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		if(ball.player!=null){
				Player ballHandler = ball.player;
				Iterator<Object> iter = context.getObjects(Defender.class).iterator();
				while(iter.hasNext()){
					Defender defender = (Defender)iter.next();	
					if (space.getDistance(defender.getPosition(), ballHandler.getPosition())<(Integer)params.getValue("body_radius")*2){
						messageBoard.addMessage(this, "Touched!");
						endGame();
					}
				}
		}
	}
	
	public void startGame(){
		messageBoard.addMessage(this, "Begin!");
	}
	
	public void endGame(){
		RunEnvironment.getInstance().endRun();
	}
	
}
