package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

/**
 * Senses provide all the information about the model to the brain
 * Operates all the setters on the brain class
 * @author user
 *
 */

public final class Senses {

	/**
	 * The current player
	 */
	private static  Player player;
	/**
	 * The central heading of the left side vision angle
	 */
	private static  Double leftSideVisionHeading;
	/**
	 * The central heading of the right side vision angle
	 */
	private static  Double rightSideVisionHeading;

	private Senses(){
	}
	
	public static void sense(Player p){
		player = p;
		info();
		eyes();
		ears();
		touch();
	}
	
	//-----------INFO---------------------------------------------------------------------------------//
	
		/**
	 * Everything to do with inherent knowledge of the game being played
	 */
	private static void info(){
		player.getBrain().setPlayer(player);
		player.getBrain().setMaxSpeed(Params.maxForwardSpeed);
		player.getBrain().setEasternersCount(Params.getEasternerCount());
		player.getBrain().setWesternersCount(Params.getWesternerCount());
	}
	
	//---------------EYES-----------------------------------------------------------------//
	
	/**
	 * Everything to do with vision
	 */
	private static void eyes(){
		setHeadings();
		sendView();
		sendPlayerObservations();
	}
	
	/**
	 * Sets the respective vision heading variables. Based on the current heading, full vision angle and depth vision angle
	 */
	private static void setHeadings(){
		//The central headings of the respective visions
		leftSideVisionHeading = Utils.checkRadian(player.getHead().getRotation()+((Params.depthVision*0.0174533)/2)+((Params.sideVision*0.0174533)/2));
		rightSideVisionHeading = Utils.checkRadian(player.getHead().getRotation()-((Params.depthVision*0.0174533)/2)-((Params.sideVision*0.0174533)/2));
	}
	
	/**
	 * Sends the various object lists to the brain
	 */
	private static void sendView(){
		player.getBrain().setWestTryline(getObjectsInView(WestTryPoint.class));
		player.getBrain().setEastTryline(getObjectsInView(EastTryPoint.class));
		player.getBrain().setPlayers(getObjectsInView(Player.class));
		player.getBrain().setSidelines(getObjectsInView(SidePoint.class));
		player.getBrain().setBalls(getObjectsInView(Ball.class));
	}
	
	/**
	 * Sends the players senses object and the absolute heading of the players head
	 */
	private static void sendPlayerObservations(){
		player.getBrain().setMe(new SensesObject(player, new Vector3d(0.0,0.0,0.0), player, true));
		player.getBrain().setNoseHeading(player.getHead().getRotation());
	}
	
	/**
	 * Returns a list of all the objects of the given class currently in the players view
	 * @param a - is the class of agent. All agents of this class within view will be returned
	 * @return arraylist of senses objects.
	 */
	private static List<SensesObject> getObjectsInView(Class<?> a){
		List<SensesObject> fresh = new ArrayList<>();
		
		//Iterate through each object
		Iterator<Object> it = Params.context.getObjects(a).iterator();
		while(it.hasNext()){
			Object agent = it.next();	
			
			//Pass the agent and the list to the createView method
			createView(agent, fresh);
		}
		return fresh;
	}
	
	/**
	 * For a given object, if in the players view, creates a new senses object and adds to the arraylist
	 * Importantly, the senses object is made to ensure any locations passed to the brain are NOT dynamic
	 * @param o - the simpleagent being viewed
	 * @param list - the list of senses objects of the same agent class
	 */
	private static void createView(Object o, List<SensesObject> list){
		
		//Convert the simple agent to the relevant agent type
		SimpleAgent agent;
		if(o instanceof Player)agent = (Player)o;
		else if(o instanceof Ball)agent = (Ball)o;
		else if(o instanceof SidePoint)agent = (SidePoint)o;
		else if(o instanceof WestTryPoint)agent = (WestTryPoint)o;
		else if(o instanceof EastTryPoint)agent = (EastTryPoint)o;
		else agent = null;
		
		//Ensure the agent is not the curent player
		if(o!=player){
			
			//If the agent to be seen is completely unobstructed 
			if(!isObstructed(agent)){
				
				//if the agent is within the depth vision of the player
				if(Utils.inView(agent, player, Params.depthVision, player.getHead().getRotation())){
					
					//Create depth Senses object
					list.add(new SensesObject(agent,Utils.getVector(agent.getPositionPoint(), player.getPositionPoint()), player, true));
				}
				
				//if the agent to be seen is within the right side vision field of the player
				if(Utils.inView(agent, player, Params.sideVision, leftSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, player, leftSideVisionHeading));
				}
				
				//if the agent to be seen is within the left side vision field of the player
				if(Utils.inView(agent, player, Params.sideVision, rightSideVisionHeading)){
					//Create non-depth Senses object
					list.add(new SensesObject(agent, player, rightSideVisionHeading));
				}
			}
		}
	}
	
	/**
	 * Checks if an agent to be seen is obstructed from the current players view
	 * @param agent - the agent to be viewed
	 * @return true if obstructed and false if not obstructed
	 */
	private static boolean isObstructed(SimpleAgent agent){
		//Iterate through potential obstructors in the environment (the players)
		Iterator<Object> it = Params.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player obstructor = (Player)it.next();
			
			//If the player is not the current player
			if(obstructor!=player){
				double obstructorRadius = Params.playerRadius;
				double distToObstructor = Math.abs(Params.space.getDistance(player.getPositionPoint(), obstructor.getPositionPoint()));
				
				//Use the length to and radius of the obstruction to derive the obstruction angle in degrees of the object from the player
				double halfObstructionAngle = Math.atan((obstructorRadius)/distToObstructor);
				double fullObstructionAngle = halfObstructionAngle*2;
				double obstructionAngle = fullObstructionAngle * 57.2958;
				
				//Obtain the angle in radians from the player to the obstruction
				Vector3d xAxis = new Vector3d(1.0,0.0,0.0);
				Vector3d vectorToObstruction = Utils.getVector(obstructor.getPositionPoint(), player.getPositionPoint());
				double heading = xAxis.angle(vectorToObstruction);
				if(vectorToObstruction.y<0){
					heading = 2*Math.PI - heading;
				}
				
				//Pass the agent to be seen, the obstruction angle and the heading of the obstruction to the inView method
				if(Utils.inView(agent, player, obstructionAngle, heading)){
					
					//If the agent is within the obstruction zone
					//And the distance to the agent is greater than the dist to the obstruction
					//The agent is obstructed from view
					if(Math.abs(Params.space.getDistance(player.getPositionPoint(), agent.getPositionPoint()))>distToObstructor){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//-------------EARS-----------------------------------------------------------------------//
	
	/**
	 * Everything to do with hearing
	 */
	private static void ears(){
		hear();
	}
	
	/**
	 * Checks if new messages have hit the messageboard, recreates and sends the most recent message to the brain
	 */
	private static void hear(){
		
		//If the most recent messageboard message is not the same as the last one processed
		if(MessageBoard.getNewMessage()){
			
			Message message = MessageBoard.getLastMessage();		
			
			//If the message is from the referee, create an official message
			if(message.getOfficial()){
				
				//Put the message into a senses object
				SensesObject lastMess = new SensesObject(message);
				
				//Send the sensesobject to the brain
				player.getBrain().setMessage(lastMess);
				
				//Otherwise create a vector to message
			} else {
				Player sender = (Player)message.getSender();
				if(sender!=player){
					Vector3d vectorToMessage = Utils.getVector(sender.getPositionPoint(), player.getPositionPoint());
					
					//If the length of the vector is less than the hearing radius
					if(vectorToMessage.length()<=Params.hearingRadius){

						//Create a new unofficial message
						Message mess = new Message(message.getMessage());
						
						//Put the message into a new senses object
						SensesObject lastMess = new SensesObject(mess, vectorToMessage, player, false);
						
						//Send the sensesobject to the brain
						player.getBrain().setMessage(lastMess);
					}
				}
			}
		}
	}
	
	//-----------TOUCH--------------------------------------------------------------------------------//
	
	/**
	 * Everything to do with touch
	 */
	private static void touch(){
		player.getBrain().setHasBall(Utils.hasBall(player));
	}
	
}
