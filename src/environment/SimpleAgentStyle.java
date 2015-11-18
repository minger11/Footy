package environment;

import java.io.IOException;
import java.net.URL;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class SimpleAgentStyle extends DefaultStyleOGL2D{

	protected ShapeFactory2D factory;
	protected VSpatial attackerShape;
	protected VSpatial defenderShape;
	protected VSpatial fieldShape;
	
	@Override
	public void init(ShapeFactory2D x){
		factory = x;
		try{
			URL url = getClass().getResource("/maroon.png");
			attackerShape = factory.createImage(url.getPath());
			url = getClass().getResource("/baby_blue.png");
			defenderShape = factory.createImage(url.getPath());
			}
		catch (IOException e) {
		    System.err.println("Couldnt get player images!");
		}
		try{
			URL	url = getClass().getResource("/field.png");
			fieldShape = factory.createImage(url.getPath());
			}
		catch (IOException e) {
		    System.err.println("Couldnt get field image!");
		}
		super.init(factory);
	}
	
	@Override
	public VSpatial getVSpatial(Object o, saf.v3d.scene.VSpatial spatial) {
		if (o instanceof Attacker)
			return attackerShape;
		if (o instanceof Defender)
			return defenderShape;
		if (o instanceof Field)
			return fieldShape;
		return null;
	}
	
	@Override
	public float getRotation(Object o) {
		if (o instanceof Player){
			double angle = ((Player) o).motion.getAngle();
			float heading = 360-(float)(angle*57.2958);
			return heading;
		}
		return 0;
	  }

	@Override
	public float getScale(Object o) {
		if (o instanceof Player)
			return 0.3f;
		if (o instanceof Field)
			return 1f;
		return 1f;
	}
}


