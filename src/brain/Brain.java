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
import environment.SimpleAgent;
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
	private SensesObject targetObject;
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
	private List<SensesObject> sidelines;
	private List<SensesObject> balls;
	private int maxSpeed;
	private boolean hasBall;
	private Vector3d desiredBallPosition;
	private Message lastMessage;
	private String newMessage;
	
	
	public Brain(){
		tryline = new ArrayList<SensesObject>();
		players = new ArrayList<SensesObject>();
		desiredBallPosition = new Vector3d();
		desiredPosition = new Vector3d();
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
		else desiredBallPosition=null;
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(player instanceof Attacker) {
			int randomNumber = RandomHelper.nextIntFromTo(0, tryline.size()-1);
		try {
				SensesObject x = tryline.get(randomNumber);
				target = (TryPoint)x.getSimpleAgent();
				targetObject = x;
			} catch (Exception e){
			}
		}
		else 
		if(player instanceof Defender) {
			int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
			Iterator<SensesObject> it = players.iterator();
			while(it.hasNext()){
				if(it.next().getSimpleAgent() instanceof Attacker){
					SensesObject y = players.iterator().next();
					target = (Player)y.getSimpleAgent();
					targetObject = y;
				}
			}
		}
		try{
			if(targetObject.isWithinDepth()){
				desiredHeadAngle = SpatialMath.calcAngleFor2DMovement(space, currentPosition, targetPosition);
			} else {
				desiredHeadAngle = targetObject.getAgentAngle();
			}
			desiredBodyAngle = desiredHeadAngle;
		} catch (Exception e) {
		}
	}

	/**
	 * Sets both the speed and angle based on a given target
	*/
	public void moveBody(){
		/**
		if(currentPosition.getX()<=650&&currentPosition.getX()>=200){
			//setTarget();
		}*/
		if(target!=null){
			if(targetObject.isWithinDepth()){
				desiredPosition.set(targetObject.getX(),targetObject.getY(),0.0);
				desiredHeadAngle = SpatialMath.calcAngleFor2DMovement(space, currentPosition, targetObject.getPosition());
				desiredBodyAngle = desiredHeadAngle;
			}
			else {
				desiredHeadAngle = targetObject.getAgentAngle();
				desiredBodyAngle = desiredHeadAngle;
			}
		}
		if(target==null){
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
			if(tryobject.getPosition()!=null){
				space.moveTo(tryobject.getSimpleAgent(),tryobject.getPosition().getX(),tryobject.getPosition().getY());
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
			if(player.getSimpleAgent().equals(target)){
				targetObject = player;
			}
			if(player.isWithinDepth()){
				space.moveTo(player.getSimpleAgent(),player.getPosition().getX(),player.getPosition().getY());
			}
			if(player.getSimpleAgent() instanceof Attacker){
				/**if(player.isWithinDepth()){
					System.out.println("depth");
				} else System.out.println("side");*/
			}
		}
	}
		
	
	/**
	 * -----------------BEGIN SETTERS AND GETTERS--------------------
	 * 
	 */
	
	//Simple getters and setters
	public void setLastMessage(Message message){
		lastMessage = message;
		if(message.isOfficial()){
			System.out.println("Official: "+message.getMessage());
		} else {
			System.out.println(player.getClass()+": "+message.getAngle()+": "+message.getMessage());
		}
	}
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
	public void setBalls(List<SensesObject> x){
		balls = x;
	}
	public void setSidelines(List<SensesObject> x){
		sidelines = x;
	}	
	public void setSpace(ContinuousSpace<Object> x){
		space = x;
	}	
	public String getNewMessage(){
		String text = newMessage;
		newMessage = null;
		return text;
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