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
	
	private Context<Object> context;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private Parameters params;
	private MessageBoard messageBoard;
	private Integer countDown;
	private boolean gameOn;
	private int standardCountDown = 500;
	
	Referee(Context<Object> context){
		this.context = context;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		params = RunEnvironment.getInstance().getParameters();
		Iterator<Object> iter = context.getObjects(MessageBoard.class).iterator();
		this.messageBoard = (MessageBoard) iter.next();
	}
	
	void init(){
		startGame();
	}

	void step(){
		checkGame();
		checkGameOver();
	}

	/**
	 * Checks the countdown variable and reduces it or ends the game based on its value
	 */
	void checkGameOver(){
		if(countDown!=null){
			if(countDown==0){
				endGame();
			} else countDown--;
		}
	}
	
	/**
	 * If the gameOn bool is true, performs the various gameplay checks
	 */
	void checkGame(){
		if(gameOn){
			checkOut();
			checkTouch();
			checkTry();
			checkForward();
		}
	}
	
	/**
	 * Checks if the ball has travelled forward
	 */
	void checkForward(){
		
		//Get the ball
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball does not have a player
		if(ball.getPlayer()!=null){
		} else {
			
			//If the balls velocity is negative on the xaxis, call forward
			if(ball.getVelocity().getX()<0){
				makeCall("Forward!");
			}
		}
	}
	
	/**
	 * Checks if the ball carrier or the ball has gone out
	 */
	void checkOut(){
		
		//Get the ball
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		while(it.hasNext()){
			Ball ball = (Ball)it.next();
				
				//Define the sidelines
				int upperSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
				int upperEdge = upperSideline - (Integer)params.getValue("lineRadius");
				int lowerSideline = (Integer)params.getValue("fieldInset");
				int lowerEdge = lowerSideline + (Integer)params.getValue("lineRadius");
				
				
				//If the ball has a player
				if(ball.getPlayer()!=null){
					//Define the attackers upper and lower reaches
					Attacker attacker = (Attacker)ball.getPlayer();
					double attackerUpperEdge = attacker.getPositionPoint().getY()+(Integer)params.getValue("body_radius");
					double attackerLowerEdge = attacker.getPositionPoint().getY()-(Integer)params.getValue("body_radius");
					
					//If the attacker crosses the sideline, ball is out
					if(attackerUpperEdge>=upperEdge||attackerLowerEdge<=lowerEdge){
						makeCall("Out!");
					}
				}
				
				//If the ball has a player
				if(!(ball.getPlayer()!=null)){
					//Define the balls upper and lower reaches
					double ballUpperEdge = ball.getPositionPoint().getY()+(Integer)params.getValue("ball_radius");
					double ballLowerEdge = ball.getPositionPoint().getY()-(Integer)params.getValue("ball_radius");
						
					//If the ball crosses the sideline, ball is out
					if(ballUpperEdge>=upperEdge||ballLowerEdge<=lowerEdge){
						makeCall("Out!");
					}
				}
		}
	}
	
	/**
	 * Checks if the player with the ball has reached the tryline
	 */
	void checkTry(){
		
		//Find the x value of the tryline
		int tryPoint = (Integer)params.getValue("fieldInset") + (Integer)params.getValue("fieldIncrement");
		int tryEdge = tryPoint + (Integer)params.getValue("lineRadius");
		
		//Get the ball
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball has a player, check if the ball has touched the tryline
		if(ball.getPlayer()!=null){
			double ballEdge = ball.getPositionPoint().getX()-(Integer)params.getValue("ball_radius");
				if(ballEdge<=tryEdge){
					
					makeCall("Try!");
				}
		}
	}
	
	/**
	 * Checks if the player with the ball was touched by a defender
	 */
	void checkTouch(){
		
		//Get the ball object
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the balls player is not null, get the player
		if(ball.getPlayer()!=null){
				Player ballHandler = ball.getPlayer();
				
				//Iterate through the defenders
				Iterator<Object> iter = context.getObjects(Defender.class).iterator();
				while(iter.hasNext()){
					Defender defender = (Defender)iter.next();	
					
					//If the distance between the ball handler and the defender is less than 2 times body radius, the player is touched
					if (space.getDistance(defender.getPositionPoint(), ballHandler.getPositionPoint())<(Integer)params.getValue("body_radius")*2){
						
						makeCall("Touched!");
					}
				}
		}
	}
	
	/**
	 * Sets the gameOn variable to true and sends a message
	 */
	void startGame(){
		gameOn = true;
		messageBoard.addMessage(this, "Begin!");
	}
	
	/**
	 * Ends the current simulation
	 */
	void endGame(){
		RunEnvironment.getInstance().endRun();
	}
	
	/**
	 * Send a message, turn the gameOn variable off and begin a countdown to end the game
	 * @param call - the call and message to be sent
	 */
	void makeCall(String call){
		messageBoard.addMessage(this, call);
		gameOn = false;
		countDown = standardCountDown;
	}
}
