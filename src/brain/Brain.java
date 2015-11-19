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
import environment.SimpleObject;
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
	private List<SimpleObject> tryline;
	private List<SimpleObject> players;
	private int maxSpeed;
	
	public void init(){
		mapSurroundings();
		setTarget();
	}
	
	public void step(){
		mapSurroundings();
		moveBody();
	}
	
	public Brain(){
		tryline = new ArrayList<SimpleObject>();
		players = new ArrayList<SimpleObject>();
		desiredPosition = new Vector3d();
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(player instanceof Attacker) {
			int randomNumber = RandomHelper.nextIntFromTo(0, tryline.size()-1);
			SimpleObject x = tryline.get(randomNumber);
			target = (TryPoint)x.getSimpleAgent();
			targetPosition = x.getPosition();
			}
		else if(player instanceof Defender) {
			Iterator<SimpleObject> it = players.iterator();
			while(it.hasNext()){
				if(it.next().getSimpleAgent() instanceof Attacker){
					SimpleObject y = players.iterator().next();
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
		Iterator<SimpleObject> it = tryline.iterator();
		while(it.hasNext()){
			SimpleObject tryobject = it.next();
			space.moveTo(tryobject.getSimpleAgent(),tryobject.getPosition().getX(),tryobject.getPosition().getY());
		}
	}
	
	/**
	 * Iterates through the list of players and moves them on the internal projection space
	 * Sets the point of the target
	 */
	public void mapPlayers(){
		Iterator<SimpleObject> it = players.iterator();
		while(it.hasNext()){
			SimpleObject player = it.next();
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
	public void setPlayers(List<SimpleObject> x){
		players = x;
	}
	public void setTryline(List<SimpleObject> x){
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
}