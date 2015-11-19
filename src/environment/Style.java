package environment;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.Shape3D;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;
import repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition;

public class Style implements Style3D<SimpleAgent> {
	public TaggedBranchGroup getBranchGroup(SimpleAgent agent, TaggedBranchGroup taggedGroup) {

		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
			Shape3D sphere = ShapeFactory.createSphere(.03f, "DEFAULT");
			sphere.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			taggedGroup.getBranchGroup().addChild(sphere);

			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(SimpleAgent agent) {
		return null;
	}

	public String getLabel(SimpleAgent agent, String currentLabel) {
		return null; 
	}

	public Color getLabelColor(SimpleAgent agent, Color currentColor) {
		return Color.YELLOW;
	}

	public Font getLabelFont(SimpleAgent agent, Font currentFont) {
		return null;
	}

	public LabelPosition getLabelPosition(SimpleAgent agent, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float getLabelOffset(SimpleAgent agent) {
		return .035f;
	}

	public TaggedAppearance getAppearance(SimpleAgent agent, TaggedAppearance taggedAppearance, Object shapeID) {
			taggedAppearance = new TaggedAppearance();
		  AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.red);
		
		return taggedAppearance;
	}

	public float[] getScale(SimpleAgent agent) {
		return null;
	}
}
