package environment;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;

/**
 * Referee makes decisions and declarations about the simulation
 * @author user
 *
 */

public final class Referee {
	
	/**
	 * Set and updated by the referee in the event of an end of game to countdown to the termination of the simulation
	 */
	private static  Integer countDown;
	/**
	 * Changed by the referee to indicate whether the game is currently in play or not
	 */
	private static  boolean gameOn = false;
	
	private Referee(){
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
	static void checkGame(){
		checkGameOver();
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
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball does not have a player
		if(ball.getPlayer()!=null){
		} else {
			if(ball.getLastPlayer() instanceof Easterner){
				//If the balls velocity is negative on the xaxis, call forward
				if(ball.getVelocity().getX()<0){
					makeCall("Forward by eastern team");
				}
			} else if (ball.getLastPlayer() instanceof Westerner){
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
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		while(it.hasNext()){
			Ball ball = (Ball)it.next();
				
				//Define the sidelines
				double upperSideline = Params.displayHeight-Params.fieldInset;
				double upperEdge = upperSideline - Params.lineRadius;
				double lowerSideline = Params.fieldInset;
				double lowerEdge = lowerSideline + Params.lineRadius;
				
				
				//If the ball has a player
				if(ball.getPlayer()!=null){
					//Define the players upper and lower reaches
					Player easterner = (Player)ball.getPlayer();
					double easternerUpperEdge = easterner.getPositionPoint().getY()+Params.playerRadius;
					double easternerLowerEdge = easterner.getPositionPoint().getY()-Params.playerRadius;
					
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
					double ballUpperEdge = ball.getPositionPoint().getY()+Params.ballRadius;
					double ballLowerEdge = ball.getPositionPoint().getY()-Params.ballRadius;
						
					//If the ball crosses the sideline, ball is out
					if(ballUpperEdge>=upperEdge||ballLowerEdge<=lowerEdge){
						Player player = ball.getLastPlayer();
						if(player instanceof Easterner){
							makeCall("Out by eastern team");
						} else if (player instanceof Westerner){
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
		double westTryPoint = Params.fieldInset + Params.fieldIncrement;
		double westTryEdge = westTryPoint + Params.lineRadius;
		
		//Find the x value of the tryline
			double eastTryPoint = Params.displayWidth - Params.fieldInset - Params.fieldIncrement;
			double eastTryEdge = eastTryPoint + Params.lineRadius;
		
		//Get the ball
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the ball has a player, check if the ball has touched the tryline
		if(ball.getPlayer()!=null){
			if(ball.getPlayer() instanceof Easterner){
			double ballEdge = ball.getPositionPoint().getX()-Params.ballRadius;
				if(ballEdge<=westTryEdge){
					
					makeCall("Try to the easterners!");
				}
			} else {
				double ballEdge = ball.getPositionPoint().getX()+Params.ballRadius;
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
		Iterator<Object> it = Params.context.getObjects(Ball.class).iterator();
		Ball ball = (Ball)it.next();
		
		//If the balls player is not null, get the player
		if(ball.getPlayer()!=null){
				Player ballHandler = ball.getPlayer();
				
				if(ballHandler instanceof Easterner){
					//Iterate through the westerners
					Iterator<Object> iter = Params.context.getObjects(Westerner.class).iterator();
					while(iter.hasNext()){
						Westerner westerner = (Westerner)iter.next();	
						
						//If the distance between the ball handler and the westerner is less than 2 times body radius, the player is touched
						if (Params.space.getDistance(westerner.getPositionPoint(), ballHandler.getPositionPoint())<Params.playerRadius*2){
							
							makeCall("The easterner was touched by the westerner!");
						}
					}
				} else {
					//Iterate through the westerners
					Iterator<Object> iter = Params.context.getObjects(Easterner.class).iterator();
					while(iter.hasNext()){
						Easterner westerner = (Easterner)iter.next();	
						
						//If the distance between the ball handler and the westerner is less than 2 times body radius, the player is touched
						if (Params.space.getDistance(westerner.getPositionPoint(), ballHandler.getPositionPoint())<Params.playerRadius*2){
							
							makeCall("The westerner was touched by the easterner!");
						}
					}
				}
		}
	}
	
	/**
	 * Sets the gameOn variable to true and sends a message
	 */
	static void startGame(){
		countDown = null;
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
		countDown = Params.standardCountDown;
	}
}
