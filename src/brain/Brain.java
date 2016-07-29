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
	private Boolean hadBall;
	
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
		setTargets();	
	}
	
	public void step(){
		mapSurroundings();
		if(gameOn){
			//moveBody();
			//possessionChange();
			//if(hasBall) {
				//inPosition();
				//moveBall();
			//}
			//else {
				//catchBall();
			//}
				//find();
				//passEnergy=0;
			//}
			//turnHeadSideToSide();
			//turnBody();
			//if(player instanceof Easterner) {
				//strafeSideToSide();
				if(player instanceof Easterner){
					adjustTargetAngle();
					turnAndRun(100);
				} else {
					
					//PHASE 1
						setBallTarget();
						adjustTargetAngle();
						turnAndRun(100);
					
					
					
					//PHASE 2
					/**
					defenderSetTarget();
					strafeMark();
					*/
					
					//PHASE 3
					/**
					createAttackerAngleList();
					setMark();
					strafeMark();
					*/
					
					//PHASE 4
					/**
					if(startChaseCounter>0){
					createAttackerAngleList();
					setMark();
					strafeMark();
					interceptDetection();
					} else {
						adjustTargetAngle();
						parabolicChase();
					}
					**/
				}
				
			//runSquare();
			//}
			//if(player instanceof Westerner){
				//find();
				//watchTarget();
				//turnAndRun(100);
				//markAttacker();
			//}
			//moveArmsSideToSide();
			//}
			//runUpAndBack();
			//strafeBox();
			//runForward();
			//longCircle();
			//runDiagLines();
			//runBackAndForward();
			//runForward();
			//}
		} 
		//else {
			//passEnergy = 0;
			//moveEnergy = 0;
		//}
	}
	
	//----------------------------------------------------------------------------------------------------------
		//-----------------------------------------IMPORTANT----------------------------------------------------
		//----------------------------------------------------------------------------------------------------------
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
	 * Sets the initial targets of both attackers and defenders
	 */
	private void setTargets(){
		if(player instanceof Easterner) {
			attackerSetTarget();
		} else {
			defenderSetTarget();
		}
	}
	
	/**
	 * Sets a random piece of Western tryline as the target for Easterner attackers
	 */
	private void attackerSetTarget(){
				int randomNumber = RandomHelper.nextIntFromTo(0, westTryline.size()-1);
			try {
					targetObject = westTryline.get(randomNumber);
					targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
					turnAndRun(100);
				} catch (Exception e){
				}
	}
	
	/**
	 * Sets the opposing Easterner and the Westerners target
	 */
	private void defenderSetTarget(){
		try {
		Iterator<SensesObject> it = players.iterator();
		while(it.hasNext()){
			SensesObject o = it.next();
			if(o.getNumber()==me.getNumber()){
				targetObject = o;
				targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
				turnAndRun(100);
			}
		} 
		} catch (Exception e){
		}
	}
	
	/**
	 * Sets both the SimleAgent target and NdPoint of the target
	 */
	public void setBallTarget() {
		try{
			if(player instanceof Westerner) {
				Iterator<SensesObject> it = players.iterator();
				while(it.hasNext()){
					SensesObject o = it.next();
					if(o.getObject() instanceof Easterner){
						//SensesObject y = players.iterator().next();
						if(o.getNumber()==1){
							targetObject = o;
						//effort = targetObject.getRelativeVector();
						//moveEnergy = targetObject.getDistance();
						//moveDirection = targetObject.getRelativeAngle();
						}
					}
				}
			}
		} catch(Exception e){
			
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
	
	/**
	 * Adjusts the angle to the target
	 */
	private void adjustTargetAngle(){
		try {
		targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
		} catch (Exception e){
		}
	}
	
	private void parabolicChase(){
		//targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
		try{
		double targetVelocityAngle = targetObject.getMoveDirection();
		double myDirection = me.getMoveDirection();
		double targetAngle = targetObject.getRelativeAngle();
		double distanceToTarget = Math.abs(targetObject.getDistance());
		
		//System.out.println("TargetDir"+targetVelocityAngle+", "+"MyDir"+myDirection+", Angle2Target"+targetAngle);
		
		double simon = Math.abs(180 - Math.abs(targetVelocityAngle)*57.2958);
		double bob = Math.abs(180 - (180 - simon - Math.abs(targetAngle)*57.2958));
		
		double c = Math.abs(distanceToTarget);
		
		double opp = Math.sin(Math.toRadians(bob))*c;
		double angle = Math.asin(opp/c);
		double runAngle = ((((angle)*(targetVelocityAngle/Math.abs(targetVelocityAngle)))*.6)+(.4*targetObject.getRelativeAngle()));
		targetAbsAngle = runAngle;
		turnAndRun(100);
		} catch(Exception e){
			
		}
	}
	
	private double targetAbsAngle;
	
	/**
	 * Turns the players head, body and arms and accelerates in the direction of the target angle
	 * @param velocity
	 */
	public void turnAndRun(double velocity){
		double angle = 0.0;
		if(noseHeading!=targetAbsAngle){
			angle = Utils.absoluteToRelative(targetAbsAngle, noseHeading);
		}
		turn = angle;
		moveDirection = angle;
		moveEnergy = velocity;
	}
	
	public void strafeMark(){
		if(targetAbsAngle<1){
			strafeLeft();
		} else {
			strafeRight();
		}
	}
	
	public class playerAngle{
		SensesObject player;
		Double angle;
	}
	
	private ArrayList<SensesObject> list;
	
	public void createAttackerAngleList(){
		try {
		Iterator<SensesObject> it = players.iterator();
		//Double RelativeAngle = 9999999.00;
		list = new ArrayList<SensesObject>();
		int x = 0;
		while(it.hasNext()){
			tempObject = it.next();
			if(tempObject.getObject() instanceof Easterner){
					list.add(x, tempObject);
					x++;
			}
		}
	} catch (Exception e){
	}
	} 
	
	
	public void setMark(){
		try{
		if(list.size()>0){
			ArrayList<SensesObject> attackerCountoff = new ArrayList<SensesObject>();
			double floor = 0;
			SensesObject current = null;
			for(int w=0; w<list.size(); w++){
				double ceiling = Math.PI;
				double g = 0;
				for(int z=0; z<list.size(); z++){
					g = Utils.RelativeToAbsolute(list.get(z).getRelativeAngle(), noseHeading);
					if(g>Math.PI){
						g=g-Math.PI;
					}
					if(g<Math.PI/2){
						g=g+(Math.PI)/2;
					} else {
						g=g-Math.PI/2;
					}
					if(g<ceiling&&g>floor){
						ceiling = g;
						current = list.get(z);
					}
				}
				attackerCountoff.add(w, current);
				//current = null;
				floor = ceiling;
			}
			targetObject = attackerCountoff.get(me.getNumber()-1);
			targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
		}
		} catch(Exception e){
			}
		}
	
	public int startChaseCounter = 100;
	
	public void interceptDetection(){
		try{
		double targetVelocityAngle = targetObject.getMoveDirection();
		double targetVelocity = targetObject.getMoveVelocity();
		
		double mySpeed = me.getMoveVelocity();
		double myDirection = me.getMoveDirection();
		
		double targetAngle = targetObject.getRelativeAngle();
		double distanceToTarget = Math.abs(targetObject.getDistance());
		
		//System.out.println("TargetDir"+targetVelocityAngle+", "+"MyDir"+myDirection+", Angle2Target"+targetAngle);
		
		
		double jim = (Math.abs(myDirection)-Math.abs(targetAngle))*57.2958;
		double simon = Math.abs(180 - Math.abs(targetVelocityAngle)*57.2958);
		double bob = Math.abs(180 - (180 - simon - Math.abs(targetAngle)*57.2958));
		double roger = Math.abs(180 - jim - bob);
		double c = Math.abs(distanceToTarget);
		
		double opp = Math.sin(Math.toRadians(bob))*c;
		double adj = Math.cos(Math.toRadians(bob))*c;
		double myLine = opp/Math.sin(Math.toRadians(roger));
		double enemyLine = adj+(Math.cos(Math.toRadians(roger))*myLine);
		
		//System.out.println("Attack:"+bob+", FromMe"+jim+", ToInterSect"+roger+","+c);
		//System.out.println("Opp;"+opp+", Adj: "+adj);
		//System.out.println("MyLine:"+myLine+",EnemyLine:"+enemyLine);
		//System.out.println(mySpeed+","+targetVelocity);
		
		double timeTargetToIntersection = enemyLine/targetVelocity;
		double timeMeToIntersection = myLine/mySpeed;
		
		//System.out.println(timeTargetToIntersection+","+timeMeToIntersection);
		
		if(timeTargetToIntersection<timeMeToIntersection){
			startChaseCounter--;
			if(startChaseCounter<=0){
				targetAbsAngle = Utils.RelativeToAbsolute(myDirection, noseHeading);
				turnAndRun(100);
				}} 
		else {
			startChaseCounter=100;
		}
		} catch(Exception e){
			
		}
		}		
	
	//----------------------------------------------------------------------------------------------------------
	//-----------------------------------------RANDOM FUNTIONS----------------------------------------------------
	//----------------------------------------------------------------------------------------------------------
	
	private SensesObject tempObject;
	
	private void markAttacker(){
			Iterator<SensesObject> it = players.iterator();
			Double RelativeAngle = 9999999.00;
			while(it.hasNext()){
				tempObject = players.iterator().next();
				if(tempObject.getObject() instanceof Easterner){
					if(Utils.RelativeToAbsolute(tempObject.getRelativeAngle(), noseHeading)<RelativeAngle){
						targetObject = tempObject;
						targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
					}
				}
				turnAndRun(100);
			}
	}
	
	private SensesObject headAndArmsTarget;
	
	private void catchBall(){
		if(balls.size()!=0){
			headAndArmsTarget = balls.get(0);
			double targetAngle = headAndArmsTarget.getRelativeAngle();
			double targetAbsTAngle = Utils.RelativeToAbsolute(targetAngle, noseHeading);
			if(noseHeading!=targetAbsTAngle){
				headTurn = targetAngle;
				armsTurn = targetAngle;
			}
		}
	}
	
	private int newCount = 200;
	public void moveArmsSideToSide(){
		if(0<newCount&&newCount<=100){
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
		} else if(newCount>100){
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
			newCount=200;
		}
	}
	public void runDiagLines(){
		if(counter==400){
			targetAbsAngle = Utils.RelativeToAbsolute(-Math.PI/2, noseHeading);
			turnAndRun(100);
			counter--;
		} else if(counter==800){
			targetAbsAngle = Utils.RelativeToAbsolute(Math.PI/4, noseHeading);
			turnAndRun(50);
			counter--;
		} else if(0<counter&&counter<400){
			counter--;
			turnAndRun(100);
		} else if(counter>400){
			counter--;
			turnAndRun(50);
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
	
	public void strafeSideToSide(){
		if(400<=counter&&counter<600){
			counter--;
			strafeLeft();
		} else if(200<=counter&&counter<400){
			counter--;
			strafeLeft();
		} else if(0<counter&&counter<200){
			counter--;
			strafeRight();
		} else if(counter>=600){
			counter--;
			strafeRight();
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
	
	private int newcounter = 100;
	public void turnHeadSideToSide(){
		if(-100<newcounter&&newcounter<0){
			newcounter--;
			turnHeadRight();
		} else if(newcounter>=0){
			newcounter--;
			turnHeadLeft();
		} else if(newcounter==-100){
			newcounter=100;
		}
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
			targetAbsAngle = Utils.RelativeToAbsolute(targetObject.getRelativeAngle(), noseHeading);
			turnAndRun(100);
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
	
	private void possessionChange(){
		try{
		//just received ball
		if(hasBall==true&&(hadBall==null||hadBall==false)){
			hadBall=true;
			temporaryTurnAngle = Math.PI;
		}
		//just lost ball
		if(hasBall==false&&(hadBall==true)){
			hadBall=false;
			ballPassCounter = 100;
		}
		}catch(Exception e){
			
		}
	}
	
	private Double temporaryTurnAngle;
	private void inPosition(){
		if(temporaryTurnAngle!=null){
			if(noseHeading!=temporaryTurnAngle){
				double angle = Utils.absoluteToRelative(temporaryTurnAngle, noseHeading);
				headTurn = angle;
				armsTurn = angle;
			} else {
				temporaryTurnAngle = null;
			}
		}
	}
	
	private int ballPassCounter = 100;
	private double moveBallAngle;
	public void moveBall(){
		//if(currentPosition.getX()>=640||currentPosition.getX()<=630){
			//passEffort = effort;
		//wait 
		if(ballPassCounter <= 0){
			SensesObject mate = getMate();
			if(mate!=null&&mate.isWithinDepth()){
				double targetHeadAngle = mate.getRelativeAngle();
				if(targetHeadAngle!=0){
					headTurn = targetHeadAngle;
				}
				double targetArmsAngle = -me.getRelativeArmsRotation()+targetHeadAngle;
				if(targetArmsAngle!=0){
					armsTurn = targetArmsAngle;
				}
				passEnergy = 100;
				passDirection = mate.getRelativeAngle();
			}
		}
		ballPassCounter--;
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
		armsTurn = 0.0;
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