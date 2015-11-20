package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.SimpleCartesianAdder;

/**
 * Senses provide all the information about the model to the brain
 * Operates all the setters on the brain class
 * @author user
 *
 */

public class Senses {

	private Player player;
	private List<SensesObject> players;
	private List<SensesObject> tryline;
	
	Senses(Player player){
		this.player = player; 
	}
	
	protected void init(){
		player.brain.setSpace(makeSpace());
		player.brain.setPlayers(getPlayers());
		player.brain.setTryline(getTryline());
		player.brain.setPosition(getPosition());
		player.brain.setPlayer(player);
		player.brain.setMaxSpeed(getMaxSpeed());
		player.brain.setHasBall(getHasBall());
	}
	
	protected void step(){
		player.brain.setPlayers(getPlayers());
		player.brain.setPosition(getPosition());
		player.brain.setHasBall(getHasBall());
	}
	
	/**
	 * Returns a list that represents the players in view and their present(static) positions
	 * sets the list as an internal variable
	 * At this stage returns all points in context, needs work
	 * @return
	 */
	public List<SensesObject> getPlayers(){
		List<SensesObject> fresh = new ArrayList<>();
		Iterator<Object> it = player.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			if(!this.player.equals(player)){
				fresh.add(new SensesObject(player,player.currentPosition));
			}
		}
		players = fresh;
		return fresh;
	}
	
	public boolean getHasBall(){
		Iterator<Object> it = player.context.getObjects(Ball.class).iterator();
		while(it.hasNext()){
			Ball ball = (Ball)it.next();
			Vector3d vectorToBall = new Vector3d();
			vectorToBall.sub(ball.movement.currentPosition, player.movement.currentPosition);
			if(Math.abs(vectorToBall.length())<=(Integer)player.params.getValue("body_radius")+(Integer)player.params.getValue("ball_radius")){
				if(ball.player==null||ball.player.equals(player)){
					return true;
				}
			}
			//if(ball.currentPosition.equals(player.currentPosition))return true;

		}
		return false;
	}
	
	/**
	 * Returns a list that represents the trypoints in view and their present(static) positions
	 * sets the list as an internal variable
	 * At this stage returns all points in context, needs work
	 * @return
	 */
	public List<SensesObject> getTryline(){
		List<SensesObject> fresh = new ArrayList<>();
		Iterator<Object> it = player.context.getObjects(TryPoint.class).iterator();
		while(it.hasNext()){
			TryPoint tryPoint = (TryPoint)it.next();
			fresh.add(new SensesObject(tryPoint,tryPoint.currentPosition));
		}
		tryline = fresh;
		return fresh;
	}
	
	/**
	 * Creates the internal projection space to be used by the brain
	 * @return
	 */
	public ContinuousSpace<Object> makeSpace(){
		String random = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace(random, player.context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(),
						(Integer)player.params.getValue("display_width"), (Integer)player.params.getValue("display_height"));
		return space;
	}

	/**
	 * Returns the position of a simple agent object only if it is in the list variable
	 * @param o
	 * @return
	 */
	public NdPoint getPosition(SimpleAgent o){
		if(o instanceof Player){
			if(players.contains((Player)o)){
				return o.getPosition();
			}
		}
		if(o instanceof TryPoint){
			if(tryline.contains((TryPoint)o)){
				return o.getPosition();
			}
		}
			return null;
	}
	
	public NdPoint getPosition(){
		return player.getPosition();
	}

	public int getMaxSpeed(){
		if(player instanceof Attacker){
			return (Integer)player.params.getValue("attacker_speed");
		}
		if(player instanceof Defender){
			return (Integer)player.params.getValue("defender_speed");
		}
		return 1;		
	}
	
}
