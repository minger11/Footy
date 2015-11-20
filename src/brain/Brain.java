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
import environment.Player;
import environment.SimpleAgent;
import environment.SensesObject;
import environment.TryPoint;

/**
 * Hub of all actions of the player
 * Brain delegates actions across various classes
 * Consider delays between delegations to imitate reaction times etc
 * @author user
 *
 */

public class Brain {

	private SimpleAgent target;
	private Vector3d desiredPosition;
	private Player player;
	private double speed;
	private double desiredBodyAngle;
	private double desiredHeadAngle;
	private NdPoint currentPosition;
	private NdPoint targetPosition;
	private ContinuousSpace<Object> space;
	private List<SensesObject> tryline;
	private List<SensesObject> players;
	private int maxSpeed;
	private boolean hasBall;
	private Vector3d desiredBallPosition;
	
	public void init(){
		mapSurroundings();
		setTarget();
	}
	
	public void step(){
		mapSurroundings();
		moveBody();
		if(hasBall) {
			moveBall();
		}
		else desiredBallPosition=null;
	}
	
	public Brain(){
		tryline = new ArrayList<SensesObject>();
		players = new ArrayList<SensesObject>();
		desiredPosition = new Vector3d();
		desiredBallPosition = new Vector3d();
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(player instanceof Attacker) {
			int randomNumber = RandomHelper.nextIntFromTo(0, tryline.size()-1);
			SensesObject x = tryline.get(randomNumber);
			target = (TryPoint)x.getSimpleAgent();
			targetPosition = x.getPosition();
			}
		else if(player instanceof Defender) {
			Iterator<SensesObject> it = players.iterator();
			while(it.hasNext()){
				if(it.next().getSimpleAgent() instanceof Attacker){
					SensesObject y = players.iterator().next();
					target = (Player)y.getSimpleAgent();
					targetPosition = y.getPosition();
				}
			}
		}
		desiredHeadAngle = SpatialMath.calcAngleFor2DMovement(space, currentPosition, targetPosition);
		desiredBodyAngle = desiredHeadAngle;
	}
	
	/**
	 * Sets both the speed and angle based on a given target
	*/
	public void moveBody(){
		desiredPosition.set(targetPosition.getX(),targetPosition.getY(),0.0);
		desiredHeadAngle = SpatialMath.calcAngleFor2DMovement(space, currentPosition, targetPosition);
		desiredBodyAngle = desiredHeadAngle;
		//Random spin mode
		//desiredPosition = new Vector3d(currentPosition.getX(), currentPosition.getY(), 0.0);
		//desiredHeadAngle = desiredHeadAngle +0.1;
		//desiredBodyAngle = desiredHeadAngle;
	}
	
	public void moveBall(){
		//if(currentPosition.getX()>=450){
			desiredBallPosition.set(currentPosition.getX(),currentPosition.getY(),0.0);
		//} else {
			//pass ball
		//	desiredBallPosition.set(currentPosition.getX()+80, currentPosition.getY()+50, 0.0);
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
			SensesObject tryobject = it.next();
			space.moveTo(tryobject.getSimpleAgent(),tryobject.getPosition().getX(),tryobject.getPosition().getY());
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
			if(player.getSimpleAgent().equals(target)){
				targetPosition = player.getPosition();
			}
			space.moveTo(player.getSimpleAgent(),player.getPosition().getX(),player.getPosition().getY());
		}
	}
		
	
	/**
	 * -----------------BEGIN SETTERS AND GETTERS--------------------
	 * 
	 */
	
	//Simple getters and setters
	public void setMaxSpeed(int x){
		maxSpeed = x;
	}
	public void setPlayer(Player x){
		player = x;
	}
	public void setPlayers(List<SensesObject> x){
		players = x;
	}
	public void setTryline(List<SensesObject> x){
		tryline = x;
	}
	public void setSpace(ContinuousSpace<Object> x){
		space = x;
	}	
	public void setPosition(NdPoint x){
		currentPosition = x;
	}	
	public SimpleAgent getTarget(){
		return target;
	}	
	public Vector3d getDesiredPosition(){
		return desiredPosition;
	}
	public double getSpeed(){
		return speed;
	}	
	public double getDesiredHeadAngle(){
		return desiredHeadAngle;
	}
	public double getDesiredBodyAngle(){
		return desiredBodyAngle;
	}
	public void setHasBall(boolean x){
		hasBall = x;
	}
	public Vector3d getDesiredBallPosition(){
		return desiredBallPosition;
	}
}