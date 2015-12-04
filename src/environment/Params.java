package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;

public class Params {

	//----------Time----------Time----------Time----------Time----------Time----------Time----------Time----------Time----------Time----------//
	
	/**
	 * The delay (in milliseconds) used by the scheduler before performing each step. 
	 */
	static int 		schedulerDelay	=	10;
	/**
	 * The scale of each time step (in seconds).
	 */
	static double 	timeScale 		= 	(double)1/(1000/schedulerDelay);
	
	
	//---------Distance---------Distance---------Distance---------Distance---------Distance---------Distance---------Distance---------Distance---------//
	
	/**
	 * The amount of measurement units (in pixels) per meter.
	 */
	static double 	distanceScale 	= 	10;
	
	
	//----------Field----------Field----------Field----------Field----------Field----------Field----------Field----------Field----------Field----------//
	
	/**
	 * The vertical height (in pixels) of the display - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	displayHeight	=	78 
			* distanceScale;
	/**
	 * The horizontal width (in pixels) of the display - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	displayWidth	=	130 
			* distanceScale;
	/**
	 * The inset distance (in pixels) of the field from the boundary of the display - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	fieldInset		=	5 
			* distanceScale;	
	/**
	 * The increment of each ten meter line (in pixels) of the field - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	fieldIncrement	=	10 
			* distanceScale;	
	/**
	 * The radius (in pixels) of the line - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	lineRadius		=	.1 
			* distanceScale;	
	/**
	 * The field's size relative to the size of the png image - field.png is 1300*780 pixels.
	 */
	static float 	fieldScale 		= 	(float)displayWidth/1300;
	/**
	 * The y value of the southern sideline
	 */
	static double southernSideline = fieldInset;
	/**
	 * The y value of the northern sideline
	 */
	static double northernSideline = displayHeight-fieldInset;
	/**
	 * The x value of the western sideline
	 */
	static double westernTryline = fieldInset+fieldIncrement;
	/**
	 * the x value of the eastern tryline
	 */
	static double easternTryline = displayWidth-fieldInset-fieldIncrement;
	
	//----------Player----------Player----------Player----------Player----------Player----------Player----------Player----------Player----------//
	
	/**
	 * The maximum displacement (in pixels) a player can travel forward in one time step - reflects true distance in meters (m/s) before being multiplied by the distance scale and time scale.
	 */
	static double 	maxForwardSpeed = 	10 
			* distanceScale * timeScale;
	/**
	 * The maximum displacement (in pixels) a player can travel backwards in one time step - reflects true distance in meters (m/s) before being multiplied by the distance scale and time scale.
	 */
	static double 	maxBackwardSpeed = 	5 
			* distanceScale * timeScale;
	/**
	 * The maximum displacement (in pixels) a player can travel sideways in one time step - reflects true distance in meters (m/s) before being multiplied by the distance scale and time scale.
	 */
	static double 	maxSideWaySpeed = 	4 
			* distanceScale * timeScale;
	/**
	 * The radius (in pixels) a message may be heard within - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double 	hearingRadius 	= 	30 
			* distanceScale;
	/**
	 * The radius (in pixels) of the player - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	playerRadius	=	.5 
			* distanceScale;
	/**
	 * The player's size relative to the size of the png image - player.png is 100*100 pixels.
	 */
	static float 	playerScale 	= 	(float)(2*playerRadius)/100;
	/**
	 * The head's size relative to the size of the png image - head.png is 100*100 pixels.
	 */
	static float 	headScale 		= 	(float)(2*playerRadius)/100;
	/**
	 * The arm's size relative to the size of the png image - arms.png is 100*100 pixels.
	 */
	static float 	armsScale 		= 	(float)(2*playerRadius)/100;
	/**
	 * The maximum acceleration (pixels) a player can apply in one time step - reflects true distance in meters (m/s^2) before being multiplied by the distance scale and the time scale squared.
	 */
	static double 	playerAcceleration = 10 
			* distanceScale * (Params.timeScale*Params.timeScale);
	/**
	 * The scaling applied to each player's velocity vector at each time step. Simulates natural energy loss, through forces such as friction and air resistance. 
	 */
	static double 	playerVelocityDecay = 0.99;
	/**
	 * The maximum amount of energy (per time step) that may be applied to a player move - reflects amount of energy that may be applied in one second before being multiplied by the time scale..
	 */
	static double 	maxMoveEnergy = 100
			* Params.timeScale;
	/**
	 * The maximum angle (in radians) that the head can be turned relative to the body. Ranges from negative to positive of this number.
	 */
	static double 	maxHeadToBodyTurn = Math.PI/2;
	/**
	 * The maximum angle (in radians) that the arms can be turned relative to the body. Ranges from negative to positive of this number.
	 */
	static double 	maxArmsToBodyTurn = 2*Math.PI/3;
	/**
	 * The maximum rotation (in radians) that the head can rotate in one timestep - reflects true radian angle per second before being multiplied by the time scale.
	 */
	static double	maxHeadRotationSpeed = 6.67*Math.PI
			*timeScale;
	/**
	 * The maximum rotation (in radians) that the arms can rotate in one timestep - reflects true radian angle per second before being multiplied by the time scale.
	 */
	static double 	maxArmsRotationSpeed = 6.67*Math.PI
			*timeScale;
	/**
	 * The maximum rotation (in radians) that a player can rotate in one timestep while at rest - reflects true radian angle per second before being multiplied by the time scale.
	 */
	static double 	maxRotationSpeedAtRest = 4*Math.PI
			*timeScale;
	/**
	 * The maximum rotation (in radians) that a player can rotate in one timestep while travelling at maximum speed - reflects true radian angle per second before being multiplied by the time scale.
	 */
	static double 	maxRotationSpeedAtMaxSpeed = 2*Math.PI
			*timeScale;
	/**
	 * The angle (in degrees) of the players arms. 
	 */
	static double 	armsAngle = 30;
	/**
	 * The angle (in degrees) of the players full vision (including both side and depth vision).
	 */
	static double 	fullVision = 190.00;
	/**
	 * The angle (in degrees) of the players depth perceiving vision. That is, the area that can perceive depth.
	 */
	static double 	depthVision = 114.00;
	/**
	 * The angle (in degrees) of each of the players side visions. That is, the area that cannot perceive depth, but can perceive objects.
	 */
	static double 	sideVision = (fullVision - depthVision)/2;
	/**
	 * The weight of a player (in grams).
	 */
	static double	playerWeight	=	8000.00;
		
	
	//----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------//
	
	/**
	 * The radius (in pixels) of the ball - reflects true distance in meters before being multiplied by the distance scale.
	 */
	static double	ballRadius		=	.3 
			* distanceScale;
	/**
	 * The ball's size relative to the size of the png image - ball.png is 100*100 pixels.
	 */
	static float 	ballScale = (float)(2*ballRadius)/100;
	/**
	 * The weight of the ball (in grams).
	 */
	static double	ballWeight	=	500.00;	
	/**
	 * The maximum displacement (in pixels) a ball can travel in one time step - reflects true distance in meters (m/s) before being multiplied by the distance scale and time scale.
	 */
	static double 	ballMaxSpeed 	= 	20 
			* distanceScale * timeScale;
	/**
	 * The scaling applied to each ball's velocity vector at each time step after a collision. Simulates natural energy loss, through forces such as friction and air resistance. 
	 */
	static double 	ballVelocityDecay = 0.99;
	

	//----------Boundary---------Boundary---------Boundary---------Boundary---------Boundary---------Boundary---------Boundary---------Boundary---------//
	
	/**
	 * The frequency of boundary points (every x pixels) - true value reflects meters before being multiplied by the distance scale
	 */
	static double boundaryFrequency = 5
			* distanceScale;
	
	//----------Physics----------Physics----------Physics----------Physics----------Physics----------Physics----------Physics----------Physics----------//

	/**
	 * The scaling applied to a velocity vector immediately proceeding a collision. Simulates energy loss during impact.
	 */
	static double 	collisionEnergy = .2;
		
	
	//----------MessageBoard----------MessageBoard----------MessageBoard----------MessageBoard----------MessageBoard----------MessageBoard----------//
	
	/**
	 * The delay for all unofficial messages (time steps) - the true value (in seconds) is divided by the timeScale.
	 */
	static int 		fixedDelay = (int)(.1
			/timeScale);
	/**
	 * The addition delay per char (in time steps) for all unofficial messages - the true value (in seconds) is divided by the timeScale.	
	 */
	static int 		delayPerChar = (int)(.01
			/timeScale);
	
	
	//----------Referee-----------Referee-----------Referee-----------Referee-----------Referee-----------Referee-----------Referee-----------//
	
	/**
	 * The amount of time (in steps) before the simulation stops after a referee has made a final decision - true value (in seconds) is divided by the timeScale.
	 */
	static int 		standardCountDown = (int)(2 
			/timeScale);
	
	
	//----------Environment----------Environment----------Environment----------Environment----------Environment----------Environment----------//
	
	/**
	 * The projection used to place agents on a space continuum. Each agent occupies a point in space and moving agents will be moved about this space at each time step.
	 */
	static ContinuousSpace<Object> space;
	/**
	 * The context the simulation is performed in. All agents are added to this context at the start of the simulation.
	 */
	static Context<Object> context;
	
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------//
	
	private Params(){
	}
	
	/**
	 * Sets the final requirements for the params class, the context and the space.
	 * @param c - the context of the simulation
	 * @param s - the space being used for the simulation
	 */
	static void makeParams(Context<Object> c, ContinuousSpace<Object> s){
		context = c;
		space = s;
	}
	
	//----------Teams----------Teams----------Teams----------Teams----------Teams----------Teams----------Teams----------Teams----------Teams----------//
	
	/**
	* The amount of westerners in the simulation.
	*/
	public static int getWesternerCount(){
		return (Integer)RunEnvironment.getInstance().getParameters().getValue("westernerCount");
	}
	
	/**
	 * The amount of easterners in the simulation.
	 */
	public static int getEasternerCount(){
		return (Integer)RunEnvironment.getInstance().getParameters().getValue("easternerCount");
	}
	
	/**
	 * The desired starting x position of the westerners team - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getWesternerStartX(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("westernerStartX")*distanceScale)+fieldInset+fieldIncrement;	
	}
	
	/**
	 * The desired starting y position of the westerners team - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getWesternerStartY(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("westernerStartY")*distanceScale)+fieldInset;
	}
	
	/**
	 * The desired starting x position of the easterners team - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getEasternerStartX(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("easternerStartX")*distanceScale)+fieldInset+fieldIncrement;
	}
	
	/**
	 * The desired starting y position of the easterners team - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getEasternerStartY(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("easternerStartY")*distanceScale)+fieldInset;
	}
	
	//----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------Ball----------//
	
	/**
	 * The desired starting x position of the ball - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getBallStartX(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("ballStartX")*distanceScale)+fieldInset+fieldIncrement;
	}
	
	/**
	 * The desired starting y position of the ball - reflects true distance in meters before being multiplied by the distance scale.
	 */
	public static double getBallStartY(){
		return ((double)RunEnvironment.getInstance().getParameters().getValue("ballStartY")*distanceScale)+fieldInset;
	}
	
	/**
	 * The amount of balls in the simulation.
	 */
	public static int getBallCount(){
		return (Integer)RunEnvironment.getInstance().getParameters().getValue("ballCount");
	}
}
