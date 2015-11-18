package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.context.Context;
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

public class Senses extends PlayerInterface{

	private List<SimpleObject> players;
	private List<SimpleObject> tryline;
	
	Senses(Player player, Context context){
		super(player, context);
	}
	
	protected void init(){
		brain.setSpace(makeSpace());
		brain.setPlayers(getPlayers());
		brain.setTryline(getTryline());
		brain.setPosition(getPosition());
		brain.setPlayer(player);
		brain.setMaxSpeed(getMaxSpeed());
	}
	
	protected void step(){
		brain.setPlayers(getPlayers());
		brain.setPosition(getPosition());
	}
	
	/**
	 * Returns a list that represents the players in view and their present(static) positions
	 * sets the list as an internal variable
	 * At this stage returns all points in context, needs work
	 * @return
	 */
	public List<SimpleObject> getPlayers(){
		List<SimpleObject> fresh = new ArrayList<>();
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			if(!this.player.equals(player)){
				fresh.add(new SimpleObject(player,player.currentPosition));
			}
		}
		players = fresh;
		return fresh;
	}
	
	/**
	 * Returns a list that represents the trypoints in view and their present(static) positions
	 * sets the list as an internal variable
	 * At this stage returns all points in context, needs work
	 * @return
	 */
	public List<SimpleObject> getTryline(){
		List<SimpleObject> fresh = new ArrayList<>();
		Iterator<Object> it = context.getObjects(TryPoint.class).iterator();
		while(it.hasNext()){
			TryPoint tryPoint = (TryPoint)it.next();
			fresh.add(new SimpleObject(tryPoint,tryPoint.currentPosition));
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
		space = 
				spaceFactory.createContinuousSpace(random, context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.StrictBorders(),
						(Integer)params.getValue("display_width"), (Integer)params.getValue("display_height"));
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
			return (Integer)params.getValue("attacker_speed");
		}
		if(player instanceof Defender){
			return (Integer)params.getValue("defender_speed");
		}
		return 1;		
	}
	
}
