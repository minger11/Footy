package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

public class Ball extends SimpleAgent{
	
	protected BallMovement movement;
	protected Player player;
	
	Ball(Context context, int x, int y){
		super(context, x, y);
		movement = new BallMovement(this);
	}
	
	public void init(){
		movement.init();
	}
	
	public void step(){
		movement.step();
	}
	
	public void setPlayer(Player x){
		player = x;
	}
	
	public class BallMovement{
		
		protected Vector3d desiredPosition; 
		protected Vector3d velocity;
		protected Vector3d currentPosition;
		
		protected Ball ball;
		
		BallMovement(Ball ball){
			this.ball = ball;
			velocity = new Vector3d();
			currentPosition = new Vector3d();
		}
		
		protected void init(){
			currentPosition.set(ball.getPosition().getX(), ball.getPosition().getY(), 0.0);	
		}
		
		protected void step(){
			updateMotion();
			move();
		}
		
		public void updateMotion(){
			Physics physics = new Physics(ball, currentPosition, desiredPosition, velocity);
			if(ball.player!=null){
				velocity = physics.getUpdatedVelocity();
				currentPosition.add(velocity);
			}
			else {
				currentPosition = desiredPosition;
			}
		}
		
		protected void move(){
			//Move player
			ball.space.moveTo(ball, currentPosition.getX(), currentPosition.getY());
			ball.grid.moveTo(ball, (int)ball.currentPosition.getX(), (int)ball.currentPosition.getY());
			ball.setPosition(ball.currentPosition);
			setPosition(space.getLocation(ball));
		}
		
		protected void setDesiredPosition(Vector3d position){
			this.desiredPosition = position;
		}		
	}
}
