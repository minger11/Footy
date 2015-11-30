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

public final class Referee {
	
	private static  Context<Object> context;
	private static  ContinuousSpace<Object> space;
	private static  Grid<Object> grid;
	private static  Parameters params;
	private static  Integer countDown;
	private static  boolean gameOn = false;
	private static  int standardCountDown = 500;
	
	private Referee(){
		
	}
	
	public static void init(Context<Object> c){
		context = c;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		params = RunEnvironment.getInstance().getParameters();
		countDown = null;;
		startGame();
	}

	public static void step(){
		checkGame();
		checkGameOver();
	}

	/**
	 * Checks the countdown variable and reduces it or ends the game based on its value
	 */
	private static void checkGameOver(){
		if(countDown!=null){
			if(countDown==0){
				endGame();
			} else countDown--;
		}
	}
	
	/**
	 * If the gameOn bool is true, performs the various gameplay checks
	 */
	private static void checkGame(){
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
	private static void checkForward(){
		
		//Get the ball
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball does not have a player
		if(ball.getPlayer()!=null){
		} else {
			if(ball.getLastPlayer() instanceof Easterner){
				//If the balls velocity is negative on the xaxis, call forward
				if(ball.getVelocity().getX()<0){
					makeCall("Forward by eastern team");
				}
			} else {
				//If the balls velocity is positive on the xaxis, call forward
				if(ball.getVelocity().getX()>0){
					makeCall("Forward by western team");
				}
			}
		}
	}
	
	/**
	 * Checks if the ball carrier or the ball has gone out
	 */
	private static void checkOut(){
		
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
					//Define the players upper and lower reaches
					Player easterner = (Player)ball.getPlayer();
					double easternerUpperEdge = easterner.getPositionPoint().getY()+(Integer)params.getValue("body_radius");
					double easternerLowerEdge = easterner.getPositionPoint().getY()-(Integer)params.getValue("body_radius");
					
					//If the easterner crosses the sideline, ball is out
					if(easternerUpperEdge>=upperEdge||easternerLowerEdge<=lowerEdge){
						if(easterner instanceof Easterner){
							makeCall("Out by eastern team");
						} else {
							makeCall("Out by western team");
						}
					}
				}
				
				//If the ball has no player
				if(!(ball.getPlayer()!=null)){
					//Define the balls upper and lower reaches
					double ballUpperEdge = ball.getPositionPoint().getY()+(Integer)params.getValue("ball_radius");
					double ballLowerEdge = ball.getPositionPoint().getY()-(Integer)params.getValue("ball_radius");
						
					//If the ball crosses the sideline, ball is out
					if(ballUpperEdge>=upperEdge||ballLowerEdge<=lowerEdge){
						Player easterner = ball.getLastPlayer();
						if(easterner instanceof Easterner){
							makeCall("Out by eastern team");
						} else {
							makeCall("Out by western team");
						}
					}
				}
		}
	}
	
	/**
	 * Checks if the player with the ball has reached the tryline
	 */
	private static void checkTry(){
		
		//Find the x value of the tryline
		int westTryPoint = (Integer)params.getValue("fieldInset") + (Integer)params.getValue("fieldIncrement");
		int westTryEdge = westTryPoint + (Integer)params.getValue("lineRadius");
		
		//Find the x value of the tryline
			int eastTryPoint = (Integer)params.getValue("display_width") - (Integer)params.getValue("fieldInset") - (Integer)params.getValue("fieldIncrement");
			int eastTryEdge = eastTryPoint + (Integer)params.getValue("lineRadius");
		
		//Get the ball
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball has a player, check if the ball has touched the tryline
		if(ball.getPlayer()!=null){
			if(ball.getPlayer() instanceof Easterner){
			double ballEdge = ball.getPositionPoint().getX()-(Integer)params.getValue("ball_radius");
				if(ballEdge<=westTryEdge){
					
					makeCall("Try to the easterners!");
				}
			} else {
				double ballEdge = ball.getPositionPoint().getX()+(Integer)params.getValue("ball_radius");
				if(ballEdge<=eastTryEdge){
					
					makeCall("Try to the westerners!");
				}
			}
		}
	}
	
	/**
	 * Checks if the player with the ball was touched by a westerner
	 */
	private static void checkTouch(){
		
		//Get the ball object
		Iterator<Object> it = context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the balls player is not null, get the player
		if(ball.getPlayer()!=null){
				Player ballHandler = ball.getPlayer();
				
				if(ballHandler instanceof Easterner){
					//Iterate through the westerners
					Iterator<Object> iter = context.getObjects(Westerner.class).iterator();
					while(iter.hasNext()){
						Westerner westerner = (Westerner)iter.next();	
						
						//If the distance between the ball handler and the westerner is less than 2 times body radius, the player is touched
						if (space.getDistance(westerner.getPositionPoint(), ballHandler.getPositionPoint())<(Integer)params.getValue("body_radius")*2){
							
							makeCall("The easterner was touched by the westerner!");
						}
					}
				} else {
					//Iterate through the westerners
					Iterator<Object> iter = context.getObjects(Easterner.class).iterator();
					while(iter.hasNext()){
						Easterner westerner = (Easterner)iter.next();	
						
						//If the distance between the ball handler and the westerner is less than 2 times body radius, the player is touched
						if (space.getDistance(westerner.getPositionPoint(), ballHandler.getPositionPoint())<(Integer)params.getValue("body_radius")*2){
							
							makeCall("The westerner was touched by the easterner!");
						}
					}
				}
		}
	}
	
	/**
	 * Sets the gameOn variable to true and sends a message
	 */
	private static void startGame(){
		gameOn = true;
		MessageBoard.addMessage("Begin!", gameOn);
	}
	
	/**
	 * Ends the current simulation
	 */
	private static void endGame(){
		RunEnvironment.getInstance().endRun();
	}
	
	/**
	 * Send a message, turn the gameOn variable off and begin a countdown to end the game
	 * @param call - the call and message to be sent
	 */
	private static void makeCall(String call){
		gameOn = false;
		MessageBoard.addMessage(call, gameOn);
		System.out.println("Referee: "+call);
		countDown = standardCountDown;
	}
}
