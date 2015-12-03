package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;

public class Sim {

	static double 	timeScale = 0.010;
	static double 	acceleration = 1.000;
	static double 	maxForwardSpeed = 100;
	static double 	maxBackwardSpeed = 50;
	static double 	maxSideWaySpeed = 40;
	static double 	ballMaxSpeed = 200;
	static double 	maxHeadToBodyTurn = Math.PI/2;
	static double 	maxArmsToBodyTurn = 2*Math.PI/3;
	static double 	playerVelocityDecay = 0.99;
	static double	maxHeadRotationSpeed = Math.PI/15;
	static double 	maxArmsRotationSpeed = Math.PI/15;
	static double 	maxRotationSpeedAtRest = Math.PI/25;
	static double 	maxRotationSpeedAtMaxSpeed = Math.PI/45;
	static double 	maxMoveEnergy = 100;
	static double 	armsAngle = 30;
	//The scales of the various images
	static float 	playerScale = 0.3f;
	static float 	fieldScale = 1f;
	static float 	headScale = 0.3f;
	static float 	armsScale = 0.3f;
	static float 	ballScale = 0.1f;
	static double 	collisionEnergy = .2;
	//The delay variables which determine how long a message remains pending
	static int 		delayPerChar = 50;
	static int 		fixedDelay = 100;
	static int 		standardCountDown = 500;

	static ContinuousSpace<Object> space;
	static Context<Object> context;
	static double 	hearingRadius = 300;
	//The full vision, central (depth perceiving) vision and sideVision angles (in degrees NOT radians)
	static double 	fullVision = 190.00;
	static double 	depthVision = 114.00;
	static double 	sideVision = (fullVision - depthVision)/2;
	
	
	static int 		schedulerDelayInMilliseconds=10;
	
	static double	trylineWidth	=	180.00	;	//	tryline_width
	static double	displayHeight	=	780.00	;	//	display_height
	static double	displayWidth	=	1300.00	;	//	display_width
	static double	bodyRadius	=	15.00	;	//	body_radius
	static double	fieldInset	=	50.00	;	//	fieldInset
	static double	fieldIncrement	=	100.00	;	//	fieldIncrement
	static double	lineRadius	=	1.00	;	//	lineRadius
	static double	initialHeadAngle	=	0.00	;	//	initial_head_angle
	static double	ballRadius	=	3.00	;	//	ball_radius
	static double	ballWeight	=	500.00	;	//	ball_weight
	static double	bodyWeight	=	8000.00	;	//	body_weight

	static double	westernerStartX	=	(double)RunEnvironment.getInstance().getParameters().getValue("westernerStartX");	
	static double 	westernerStartY	=	(double)RunEnvironment.getInstance().getParameters().getValue("westernerStartY");
	static double	easternerStartX	=	(double)RunEnvironment.getInstance().getParameters().getValue("easternerStartX");
	static double	easternerStartY	=	(double)RunEnvironment.getInstance().getParameters().getValue("easternerStartY");
	static int		westernerCount	=	(Integer)RunEnvironment.getInstance().getParameters().getValue("westernerCount");
	static int		easternerCount	=	(Integer)RunEnvironment.getInstance().getParameters().getValue("easternerCount");
	static double	easternerSpeed	=	(double)RunEnvironment.getInstance().getParameters().getValue("easternerSpeed");
	static double	westernerSpeed	=	(double)RunEnvironment.getInstance().getParameters().getValue("westernerSpeed");
	static double	ballStartX		=	(double)RunEnvironment.getInstance().getParameters().getValue("ballStartX");
	static double	ballStartY		=	(double)RunEnvironment.getInstance().getParameters().getValue("ballStartY");
	static int		ballCount		=	(Integer)RunEnvironment.getInstance().getParameters().getValue("ballCount");
	
	private Sim(){
	}
	
	static void makeSim(Context<Object> c, ContinuousSpace<Object> s){
		context = c;
		space = s;
	}
}
