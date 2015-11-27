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
	
	//Derived
	private SensesObject targetObject;
	
	//Sent
	private String newMessage;
	//private Vector3d passEffort;
	//private Vector3d effort;
	private double turn;
	private double headTurn;
	private double armsTurn;
	
	private double moveDirection;
	private double moveEnergy;
	private double passDirection;
	private double passEnergy;
	
	public Brain(){
		tryline = new ArrayList<SensesObject>();
		players = new ArrayList<SensesObject>();
		//effort = new Vector3d();
		//passEffort = new Vector3d();
	}
	
	public void init(){
		mapSurroundings();
		setTarget();
		if(player instanceof Attacker){
			headTurn = turn = Math.PI;
		}
	}
	
	public void step(){
		mapSurroundings();
		moveBody();
		if(hasBall) {
			moveBall();
		}
		else passEnergy=0;
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(player instanceof Attacker) {
			int randomNumber = RandomHelper.nextIntFromTo(0, tryline.size()-1);
		try {
				targetObject = tryline.get(randomNumber);
				moveEnergy = targetObject.getDistance();
				moveDirection = targetObject.getRelativeAngle();
				//effort = targetObject.getRelativeVector();
			} catch (Exception e){
			}
		Vector3d vec = new Vector3d();
		}
		else 
		if(player instanceof Defender) {
			int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
			Iterator<SensesObject> it = players.iterator();
			while(it.hasNext()){
				if(it.next().getSimpleAgent() instanceof Attacker){
					//SensesObject y = players.iterator().next();
					targetObject = players.iterator().next();
					//effort = targetObject.getRelativeVector();
					moveEnergy = targetObject.getDistance();
					moveDirection = targetObject.getRelativeAngle();
				}
			}
		}
		try{
			if(targetObject.isWithinDepth()){
				headTurn = targetObject.getRelativeAngle();
			} else {
				headTurn = targetObject.getRelativeAngle();
			}
			turn = headTurn;
		} catch (Exception e) {
		}
	}

	public void run(double speed){
		
		//if(speed>maxSpeed){
		//	speed = maxSpeed;
		//}
		//if (bodyVelocity.length() > speed ){
			//effort.normalize();
			//effort.scale(speed);
			moveEnergy = speed;
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
		
		//Run right
		//bodyVelocity = new Vector3d(10000,0,0);
		
		if(targetObject!=null){
			if(targetObject.isWithinDepth()){
				//effort = targetObject.getRelativeVector();
				moveEnergy = targetObject.getDistance();
				moveDirection = targetObject.getRelativeAngle();

				headTurn = targetObject.getRelativeAngle();
				turn = headTurn;
			}
			else {
				headTurn = targetObject.getRelativeAngle();
				turn = headTurn;
			}
		}
		if(targetObject==null){
			setTarget();
		}
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
			//passEffort = effort;
			passEnergy = moveEnergy;
			passDirection = moveDirection;
		//} else {
			//pass ball
			//ballVelocity.set(50.0, 2000.0, 0.0);
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
	
	public double getHeadTurn(){
		return headTurn;
	}
	
	//-------------------------------ARMS---------------------------------------------//
	
	//public Vector3d getPassEffort(){
		//return passEffort;
	//}
	
	public double getPassDirection(){
		return passDirection;
	}
	
	public double getPassEnergy(){
		return passEnergy;
	}
	
	public double getArmsTurn(){
		return armsTurn;
	}
	
	//--------------------------------BODY-------------------------------------------//
	
	public double getTurn(){
		return turn;
	}
	
	//--------------------------------LEGS--------------------------------------------//
	
	//public Vector3d getEffort(){
	//	return effort;
	//}
	
	public double getMoveEnergy(){
		return moveEnergy;
	}
	
	public double getMoveDirection(){
		return moveDirection;
	}
}