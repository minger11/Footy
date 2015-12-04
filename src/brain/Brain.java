package brain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import environment.Easterner;
import environment.Message;
import environment.Player;
import environment.SensesObject;
import environment.Utils;
import environment.Westerner;

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
	private int easternersCount;
	private int westernersCount;
	
	private boolean gameOn;
	
	//Received
	private SensesObject message;
	private List<SensesObject> westTryline;
	private List<SensesObject> eastTryline;
	private List<SensesObject> players;
	private List<SensesObject> balls;
	private SensesObject me;
	private boolean hasBall;
	private double noseHeading;
	
	
	//Derived
	private SensesObject targetObject;
	
	//Sent
	private String newMessage;
	//private Vector3d passEffort;
	//private Vector3d effort;
	/**
	 * relative angle to turn body by
	 */
	private double turn;
	private double headTurn;
	private double armsTurn;
	
	private double moveDirection;
	private double moveEnergy;
	private double passDirection;
	private double passEnergy;
	
	public Brain(){
		westTryline = new ArrayList<SensesObject>();
		eastTryline = new ArrayList<SensesObject>();
		players = new ArrayList<SensesObject>();
	}
	
	public void init(){
		mapSurroundings();
		//setTarget();
		//if(player instanceof Easterner){
		//	turn = headTurn = Math.PI;
		//}
		
	}
	
	public void step(){
		mapSurroundings();
		if(gameOn){
			//moveBody();
			//if(hasBall) {
				//moveBall();
			//}
			//else {
				//find();
				//passEnergy=0;
			//}
			//turnHeadSideToSide();
			//turnBody();
			//runSquare();
			moveArmsSideToSide();
			//}
			//runUpAndBack();
			//strafeBox();
			//runForward();
			//longCircle();
			//runDiagLines();
			//runBackAndForward();
			//runForward();
		} 
		//else {
			//passEnergy = 0;
			//moveEnergy = 0;
		//}
	}
	
	private int newCount = 800;
	public void moveArmsSideToSide(){
		if(0<newCount&&newCount<=400){
			newCount--;
			//where are my arms relative to my body
			//abs
			double bodyAngle = Utils.RelativeToAbsolute(me.getRelativeBodyRotation(), noseHeading);
			//abs
			double armsAngle = Utils.RelativeToAbsolute(me.getRelativeArmsRotation(), noseHeading);
			//rel
			double armsToBody = Utils.absoluteToRelative(armsAngle, bodyAngle);
			//rel
			double target = Math.PI/2;	
			if(armsToBody>target){
				//we know its a negative number
				armsTurn = -armsToBody-target;
			} else if(armsToBody<target){
				armsTurn = target-armsToBody;
			} else if(armsToBody==target){
				armsTurn =0;
			}
			passEnergy = 100;
			passDirection = target;
			//System.out.println("bodyAngle: "+bodyAngle+", armsAngle: "+armsAngle+", armsToBody: "+armsToBody+", target: "+target+", armsTurn: "+armsTurn);
		} else if(newCount>400){
			newCount--;
			//where are my arms relative to my body
			double bodyAngle = Utils.RelativeToAbsolute(me.getRelativeBodyRotation(), noseHeading);
			double armsAngle = Utils.RelativeToAbsolute(me.getRelativeArmsRotation(), noseHeading);
			double armsToBody = Utils.absoluteToRelative(armsAngle, bodyAngle);
			double target = -Math.PI/2;
			if(armsToBody>target){
				armsTurn = target-armsToBody;
			} else if(armsToBody<target){
				armsTurn = target-armsToBody;
			} else if(armsToBody==target){
				armsTurn =0;
			}
		} else if(newCount==0){
			newCount=800;
		}
	}
	public void runDiagLines(){
		if(counter==400){
			targetAbsAngle = Utils.RelativeToAbsolute(-Math.PI/2, noseHeading);
			turnAndRun(100);
			counter--;
		} else if(counter==800){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI/4, noseHeading);
			turnAndRun(20);
			counter--;
		} else if(0<counter&&counter<400){
			counter--;
			turnAndRun(100);
		} else if(counter>400){
			counter--;
			turnAndRun(20);
		} else if(counter==0){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI/2, noseHeading);
			turnAndRun(100);
			counter=799;
		}
	}
	
	public void runForward(){
		moveEnergy=100;
		moveDirection=0;
	}
	
	public void runBackward(){
		moveEnergy=100;
		moveDirection=Math.PI;
	}
	public void strafeLeft(){
		moveEnergy=100;
		moveDirection=Math.PI/2;
	}
	public void strafeRight(){
		moveEnergy=100;
		moveDirection=-Math.PI/2;
	}
	
	public void runBackAndForward(){
		if(000<counter&&counter<450){
			counter--;
			runBackward();
		} else if(counter>=450){
			counter--;
			runForward();
		} else if(counter==0){
			counter=800;
		}
	}
	
	public void strafeBox(){
		if(400<=counter&&counter<600){
			counter--;
			strafeLeft();
		} else if(200<=counter&&counter<400){
			counter--;
			runBackward();
		} else if(0<counter&&counter<200){
			counter--;
			strafeRight();
		} else if(counter>=600){
			counter--;
			runForward();
		} else if(counter==0){
			counter=800;
		}
	}
	
	public void longCircle(){
		if(counter%2==0){
			counter--;
			turnEveryThing(.4);
			run(100);
		} else {
			run(100);
			counter--;
		}
	}
	
	public void turnHeadLeft(){
		headTurn = 2;
	}
	
	public void turnHeadRight(){
		headTurn = -2;
	}
	
	public void turnBody(){
		turn = .2;
	}
	
	public void turnEveryThing(double x){
		turn = x;
		moveDirection = x;
	}
	
	private int counter = 800;
	
	private int newcounter = 300;
	public void turnHeadSideToSide(){
		if(-300<newcounter&&newcounter<0){
			newcounter--;
			turnHeadRight();
		} else if(newcounter>=0){
			newcounter--;
			turnHeadLeft();
		} else if(newcounter==-300){
			newcounter=300;
		}
	}
	
	private double targetAbsAngle;
	
	public void turnAndRun(double velocity){
		double angle = 0.0;
		if(noseHeading!=targetAbsAngle){
			angle = Utils.absoluteToRelative(targetAbsAngle, noseHeading);
		}
		turn = angle;
		moveDirection = angle;
		moveEnergy = velocity;
	}
	
	public void runUpAndBack(){
		if(counter==400){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI, noseHeading);
			turnAndRun(100);
			counter--;
		} else if(0<counter&&counter<400){
			counter--;
			turnAndRun(100);
		} else if(counter>400){
			counter--;
			turnAndRun(100);
		} else if(counter==0){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI, noseHeading);
			turnAndRun(100);
			counter=800;
		}
	}
	
	public void runSquare(){
		if(counter==600||counter==400||counter==200){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI/2, noseHeading);
			turnAndRun(100);
			counter--;
		}
		else if(400<counter&&counter<600){
			counter--;
			turnAndRun(100);
		} else if(200<counter&&counter<400){
			counter--;
			turnAndRun(100);
		} else if(0<counter&&counter<200){
			counter--;
			turnAndRun(100);
		} else if(counter>600){
			counter--;
			turnAndRun(100);
		} else if(counter==0){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI/2, noseHeading);
			turnAndRun(100);
			counter=800;
		}
	}
	
	public SensesObject ballTarget;
	
	public void find(){
		Iterator<SensesObject> it = balls.iterator();
		while(it.hasNext()){
			SensesObject o = it.next();
			if(o.isWithinDepth()){
				//SensesObject y = players.iterator().next();
				ballTarget = o;
				headTurn = o.getRelativeAngle();
				turn = o.getRelativeAngle();
				return;
				//effort = targetObject.getRelativeVector();
				//moveEnergy = targetObject.getDistance();
				//moveDirection = targetObject.getRelativeAngle();
				
			} else {
				headTurn = o.getRelativeAngle();
				turn = o.getRelativeAngle();
			}
		}
		headTurn = 2;
		turn = 2;
	}

	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setTarget() {
		if(hasBall){
			if(player instanceof Easterner) {
				int randomNumber = RandomHelper.nextIntFromTo(0, westTryline.size()-1);
			try {
					targetObject = westTryline.get(randomNumber);
					//moveEnergy = targetObject.getDistance();
					//moveDirection = targetObject.getRelativeAngle();
					//effort = targetObject.getRelativeVector();
				} catch (Exception e){
				}
			}
			else 
				if(player instanceof Westerner) {
					int randomNumber = RandomHelper.nextIntFromTo(0, eastTryline.size()-1);
				try {
						targetObject = eastTryline.get(randomNumber);
						//moveEnergy = targetObject.getDistance();
						//moveDirection = targetObject.getRelativeAngle();
						//effort = targetObject.getRelativeVector();
					} catch (Exception e){
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
		} else {
			if(player instanceof Westerner) {
				int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
				Iterator<SensesObject> it = players.iterator();
				while(it.hasNext()){
					if(it.next().getObject() instanceof Easterner){
						//SensesObject y = players.iterator().next();
						targetObject = players.iterator().next();
						//effort = targetObject.getRelativeVector();
						//moveEnergy = targetObject.getDistance();
						//moveDirection = targetObject.getRelativeAngle();
					}
				}
			}
			else 
			if(player instanceof Easterner) {
				int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
				Iterator<SensesObject> it = players.iterator();
				while(it.hasNext()){
					if(it.next().getObject() instanceof Westerner){
						//SensesObject y = players.iterator().next();
						targetObject = players.iterator().next();
						//effort = targetObject.getRelativeVector();
						//moveEnergy = targetObject.getDistance();
						//moveDirection = targetObject.getRelativeAngle();
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
				//moveEnergy = targetObject.getDistance();
				//run(1000);
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
	
	private int count = 100;
	
	public void moveBall(){
		//if(currentPosition.getX()>=640||currentPosition.getX()<=630){
			//passEffort = effort;
		//wait 
		if(count <= 0){
			SensesObject mate = getMate();
			if(mate!=null&&mate.isWithinDepth()){
				
				passEnergy = 100;
				passDirection = mate.getRelativeAngle();
				System.out.println("Passing!");
			}
		} else {
			passEnergy = moveEnergy;
			passDirection = moveDirection;
		}
		count--;
		//} else {
			//pass ball
			//ballVelocity.set(50.0, 2000.0, 0.0);
		//}
	}
	
	public SensesObject getMate(){
		SensesObject mate = findDepthMate();
		if(mate!=null){
			return mate;
		} else {
			mate=findMate();
			turn = mate.getRelativeAngle();
			headTurn = mate.getRelativeAngle();
			return mate;
		}
	}
	
	public SensesObject findDepthMate(){
		int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
		Iterator<SensesObject> it = players.iterator();
		while(it.hasNext()){
			SensesObject p = it.next();
			if(p.getObject().getClass().equals(player.getClass())&&p.isWithinDepth()){
				//SensesObject y = players.iterator().next();
				return p;
				//effort = targetObject.getRelativeVector();
				//moveEnergy = targetObject.getDistance();
				//moveDirection = targetObject.getRelativeAngle();
			
			}
		}
		return null;
	}
	
	public SensesObject findMate(){
			int randomNumber = RandomHelper.nextIntFromTo(0, players.size()-1);
			Iterator<SensesObject> it = players.iterator();
			while(it.hasNext()){
				SensesObject p = it.next();
				if(p.getObject().getClass().equals(player.getClass())){
					//SensesObject y = players.iterator().next();
					return p;
					//effort = targetObject.getRelativeVector();
					//moveEnergy = targetObject.getDistance();
					//moveDirection = targetObject.getRelativeAngle();
				
				}
			}
			return null;
	}
	
	/**
	 * Puts the contents of SimpleObject lists onto the internal projection space
	 */
	public void mapSurroundings(){
		mapPlayers();
		mapTryline();
		mapBalls();
	}
	
	/**
	 * Iterates through the tryline and moves each trypoint onto the internal projection space
	 */
	public void mapBalls(){
		Iterator<SensesObject> it;
		it = balls.iterator();
		while(it.hasNext()){
			SensesObject ball = it.next();
			try{
				if(ball.getObject().equals(ballTarget.getObject())){
					ballTarget = ball;
				}
			}catch(Exception e){
				
			}
		}
	}
	
	/**
	 * Iterates through the tryline and moves each trypoint onto the internal projection space
	 */
	public void mapTryline(){
		Iterator<SensesObject> it;
		if(player instanceof Easterner){
			it = westTryline.iterator();
		} else {
			it = eastTryline.iterator();
		}
		while(it.hasNext()){
			SensesObject tryPoint = it.next();
			try{
				if(tryPoint.getObject().equals(targetObject.getObject())){
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
				if(player.getObject().equals(targetObject.getObject())){
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
	
	public void setMaxSpeed(double x){
		maxSpeed = x;
	}
	
	public void setPlayer(Player x){
		player = x;
	}	
	
	public void setEasternersCount(int x){
		easternersCount = x;
	}
	
	public void setWesternersCount(int x){
		westernersCount = x;
	}
	//---------------------------------------EYES-----------------------------------//
	//------------------Received every timestep------------------------------------//
			
	public void setPlayers(List<SensesObject> x){
		players = x;
	}
	public void setWestTryline(List<SensesObject> x){
		westTryline = x;
	}
	public void setEastTryline(List<SensesObject> x){
		eastTryline = x;
	}
	public void setBalls(List<SensesObject> x){
		balls = x;
	}
	public void setSidelines(List<SensesObject> x){
		//sidelines = x;
	}	
	public void setNoseHeading(double x){
		noseHeading = x;
	}
	
	public void setMe(SensesObject x){
		me = x;
	}
	
	//--------------------------------------EARS--------------------------------------//
	//---------------------Only received when heard-----------------------------------//
	
	public void setMessage(SensesObject message){
		this.message = message;
		Message mess = (Message)message.getObject();
		if(mess.getOfficial()){
			gameOn = mess.getGameOn();
		}
	}
	
	//----------------------------------TOUCH--------------------------------------//
	//--------------------------Each time step-------------------------------------//
		
	public void setHasBall(boolean x){
		hasBall = x;
	}
		
	
	
	
	//-----------------------------REFLEXES---------------------------------------------------------------------------------//
	//---------------Drawn from the model each time step---------------------------------------------------------------------//
	
	//-------------------------------MOUTH--------------------------------------//
	
	/**
	 * returns newMessage and sets the value of newMessage to be null
	 * @return the string the player would like to pass out in a new message
	 */
	public String getNewMessage(){
		String text = newMessage;
		newMessage = null;
		return text;
	}
	
	//--------------------------------NECK----------------------------------------//
	
	/**
	 * returns headTurn and sets headTurn to 0.0
	 * @return the angle (relative to the head)(in radians) the brain would like to rotate the head by
	 */
	public double getHeadTurn(){
		double angle = headTurn;
		headTurn = 0.0;
		return angle;
	}
	
	//-------------------------------ARMS---------------------------------------------//
	
	/**
	 * returns passDirection ans sets pass direction to 0.0
	 * @return the angle (in radians) (relative to the head) with which the brain would like to move the ball by
	 */
	public double getPassDirection(){
		double angle = passDirection;
		passDirection = 0.0;
		return angle;
	}
	/**
	 * returns passEnergy and sets pass energy to 0.0
	 * @return the energy with which the brain would like to move the ball by
	 */
	public double getPassEnergy(){
		double x = passEnergy;
		passEnergy = 0.0;
		return x;
	}
	
	/**
	 * return armsTurn and sets armsTurn to 0.0
	 * @return the angle (relative to the arms) (in radians) the brain wishes to rotate the arms by
	 */
	public double getArmsTurn(){
		double angle = armsTurn;
		turn = 0.0;
		return angle;
	}
	
	//--------------------------------BODY-------------------------------------------//
	
	/**
	 * returns turn and sets turn to 0.0
	 * @return the angle (relative to the body) (in radians) the brain wishes to rotate the player by
	 */
	public double getTurn(){
		double angle = turn;
		turn = 0.0;
		return angle;
	}
	
	//--------------------------------LEGS--------------------------------------------//
	
	/**
	 * returns moveEnergy and sets moveEnergy to 0.0
	 * @return the energy with which the brain would like to move the body by
	 */
	public double getMoveEnergy(){
		double x = moveEnergy;
		moveEnergy = 0.0;
		return x;
	}
	/**
	 * returns moveDirection and sets moveDirection to 0.0
	 * @return the angle (in radians) (relative to the head) with which the brain would like to move the body by
	 */
	public double getMoveDirection(){
		double angle = moveDirection;
		moveDirection = 0;
		return angle;
	}
}