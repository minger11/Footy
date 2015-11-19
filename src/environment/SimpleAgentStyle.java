package environment;

import java.io.IOException;
import java.net.URL;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class SimpleAgentStyle extends DefaultStyleOGL2D{

	protected ShapeFactory2D factory;
	
	@Override
	public void init(ShapeFactory2D x){
		factory = x;
		super.init(factory);
	}
	
	@Override
	public VSpatial getVSpatial(Object o, saf.v3d.scene.VSpatial spatial) {
		if(spatial == null){
			if (o instanceof Attacker){
				try{
					URL url = getClass().getResource("/maroon.png");
					return(factory.createImage(url.getPath()));
					}
				catch (IOException e) {
				    System.err.println("Couldnt get player images!");
				}
			}
			if (o instanceof Defender){
				try{
					URL url = getClass().getResource("/baby_blue.png");
					return factory.createImage(url.getPath());
					}
				catch (IOException e) {
				    System.err.println("Couldnt get player images!");
				}
			}
			if (o instanceof Field){
				try{
					URL	url = getClass().getResource("/field.png");
					return factory.createImage(url.getPath());
					}
				catch (IOException e) {
				    System.err.println("Couldnt get field image!");
				}
			}
			if (o instanceof Head){
				try{
					URL	url = getClass().getResource("/head.png");
					return factory.createImage(url.getPath());
				}
				catch (IOException e) {
				    System.err.println("Couldnt get head image!");
				}
			}
			return null;
		}
		return spatial;
	}
	
	@Override
	public float getRotation(Object o) {
		if (o instanceof Player){
			double angle = ((Player) o).movement.currentBodyAngle;
			float heading = 360-(float)(angle*57.2958);
			return heading;
		}
		if (o instanceof Head){
			double angle = ((Head) o).player.movement.currentHeadAngle;
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
		if (o instanceof Head)
			return 0.3f;
		return 1f;
	}
}

