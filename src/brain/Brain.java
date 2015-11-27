package brain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import environment.Attacker;
import environment.Defender;
import environment.Message;
import environment.Player;
import environment.SensesObject;

/**
 * Hub of all actions of the player
 * Brain delegates actions across various classes
 * Consider delays between delegations to imitate reaction times etc
 * @author user
 *
 */

public class Brain {

	private Player player;
	
	private double maxSpeed;
	
	//Received
	private SensesObject message;
	private List<SensesObject> tryline;
	private List<SensesObject> players;
	private boolean hasBall;
	
	//Processed
	private SensesObject targetObject;
	private String newMessage;
	private Vector3d ballVelocity;
	private Vector3d bodyVelocity;
	private double desiredBodyAngle;
	private double desiredHeadAngle;
	
	public Brain(){
		tryline = new ArrayList<SensesObject>();
		players = new ArrayList<SensesObject>();
		bodyVelocity = new Vector3d();
		ballVelocity = new Vector3d();
	}
	
	public void init(){
		mapSurroundings();
		setTarget();
		if(player instanceof Attacker){
			desiredHeadAngle = desiredBodyAngle = Math.PI;
		}
	}
	
	public void step(){
		mapSurroundings();
		moveBody();
		if(hasBall) {
			moveBall();
		}
		else ballVelocity=null;
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(player instanceof Attacker) {
			int randomNumber = RandomHelper.nextIntFromTo(0, tryline.size()-1);
		try {
				targetObject = tryline.get(randomNumber);
				bodyVelocity = targetObject.getRelativeVector();
			} catch (Exception e){
			}
		}
		else 
		if(player instanceof Defender) {
			int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
			Iterator<SensesObject> it = players.iterator();
			while(it.hasNext()){
				if(it.next().getSimpleAgent() instanceof Attacker){
					//SensesObject y = players.iterator().next();
					targetObject = players.iterator().next();
					bodyVelocity = targetObject.getRelativeVector();
				}
			}
		}
		try{
			if(targetObject.isWithinDepth()){
				desiredHeadAngle = targetObject.getRelativeAngle();
			} else {
				desiredHeadAngle = targetObject.getRelativeAngle();
			}
			desiredBodyAngle = desiredHeadAngle;
		} catch (Exception e) {
		}
	}

	public void run(double speed){
		
		//if(speed>maxSpeed){
		//	speed = maxSpeed;
		//}
		//if (bodyVelocity.length() > speed ){
			bodyVelocity.normalize();
			bodyVelocity.scale(speed);
		//}
	}
	
	/**
	 * Sets both the speed and angle based on a given target
	*/
	public void moveBody(){
		/**
		if(currentPosition.getX()<=650&&currentPosition.getX()>=200){
			//setTarget();
		}*/
		if(targetObject!=null){
			if(targetObject.isWithinDepth()){
				bodyVelocity = targetObject.getRelativeVector();

				desiredHeadAngle = targetObject.getRelativeAngle();
				desiredBodyAngle = desiredHeadAngle;
			}
			else {
				desiredHeadAngle = targetObject.getRelativeAngle();
				desiredBodyAngle = desiredHeadAngle;
			}
		}
		if(targetObject==null){
			setTarget();
		}
		if(targetObject.getRelativeVector().length()>200){
			run(1000);
		}else run(100);
		/**
		if(target==null){
		//Random spin mode
		setTarget();
		desiredPosition = new Vector3d(currentPosition.getX(), currentPosition.getY(), 0.0);
		desiredHeadAngle = desiredHeadAngle +0.01;
		}
		
		while(desiredHeadAngle>=2*Math.PI){
			desiredHeadAngle -= 2*Math.PI;
		}
		desiredBodyAngle = desiredHeadAngle;
		
		//}*/
	}
	
	public void moveBall(){
		//if(currentPosition.getX()>=640||currentPosition.getX()<=630){
			ballVelocity = bodyVelocity;
		//} else {
			//pass ball
		//	desiredBallPosition.set(currentPosition.getX()-50, currentPosition.getY()+200, 0.0);
		//}
	}
	
	/**
	 * Puts the contents of SimpleObject lists onto the internal projection space
	 */
	public void mapSurroundings(){
		mapPlayers();
		mapTryline();
	}
	
	/**
	 * Iterates through the tryline and moves each trypoint onto the internal projection space
	 */
	public void mapTryline(){
		Iterator<SensesObject> it = tryline.iterator();
		while(it.hasNext()){
			SensesObject tryPoint = it.next();
			try{
				if(tryPoint.getSimpleAgent().equals(targetObject.getSimpleAgent())){
					targetObject = tryPoint;
				}
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * Iterates through the list of players and moves them on the internal projection space
	 * Sets the point of the target
	 */
	public void mapPlayers(){
		Iterator<SensesObject> it = players.iterator();
		while(it.hasNext()){
			SensesObject player = it.next();
			try{
				if(player.getSimpleAgent().equals(targetObject.getSimpleAgent())){
					targetObject = player;
				}
			}catch(Exception e){
				
			}
		}
	}
		
	
	/**
	 * -----------------BEGIN SETTERS AND GETTERS--------------------
	 * These are the only way the model will interact with the brain
	 * 
	 */
	
	//--------------------------------SENSES-----------------------------------------------------------------------------------//
	//----------The following are incoming messages to the brain---------------------------------------------------------------//
	
	
	//-----------------------------------INFO--------------------------------------//
	//---------------------Sent once, at the start of the game---------------------//
	
		public void setMaxSpeed(int x){
		
		}
		
		public void setSpace(ContinuousSpace<Object> x){
			
		}	
		
		public void setPlayer(Player x){
			player = x;
		}	
	
	//---------------------------------------EYES-----------------------------------//
	//------------------Received every timestep------------------------------------//
			
	public void setPlayers(List<SensesObject> x){
		players = x;
	}
	public void setTryline(List<SensesObject> x){
		tryline = x;
	}
	public void setBalls(List<SensesObject> x){
		//balls = x;
	}
	public void setSidelines(List<SensesObject> x){
		//sidelines = x;
	}	
	
	//--------------------------------------EARS--------------------------------------//
	//---------------------Only received when heard-----------------------------------//
	
	public void setMessage(SensesObject message){
		this.message = message;
	}
	
	//----------------------------------TOUCH--------------------------------------//
	//--------------------------Each time step-------------------------------------//
		
	public void setHasBall(boolean x){
		hasBall = x;
	}
		
	
	
	
	//-----------------------------REFLEXES---------------------------------------------------------------------------------//
	//---------------Drawn from the model each time step---------------------------------------------------------------------//
	
	//-------------------------------MOUTH--------------------------------------//
	
	public String getNewMessage(){
		String text = newMessage;
		newMessage = null;
		return text;
	}
	
	//--------------------------------NECK----------------------------------------//
	
	public double getDesiredHeadAngle(){
		return desiredHeadAngle;
	}
	
	//-------------------------------ARMS---------------------------------------------//
	
	public Vector3d getBallVelocity(){
		return ballVelocity;
	}
	
	//--------------------------------BODY-------------------------------------------//
	
	public double getDesiredBodyAngle(){
		return desiredBodyAngle;
	}
	
	//--------------------------------LEGS--------------------------------------------//
	
	public Vector3d getBodyVelocity(){
		return bodyVelocity;
	}
}