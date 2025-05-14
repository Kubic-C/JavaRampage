package Game;

import java.awt.*;

/**
 * A component that defines a circle's visual representation using color and radius.
 */
public class CircleShapeComponent extends Component {
	public Color color = Color.red;
	public float radius = 1.0f;

	/**
	 * Returns the type of this component.
	 *
	 * @return the class type of this component
	 */
	@Override
	public Class<?> getType() {
		return CircleShapeComponent.class;
	}

	/**
	 * Creates a copy of this component.
	 *
	 * @return a cloned instance of CircleShapeComponent
	 */
	@Override
	public Component clone() {
		CircleShapeComponent clone = new CircleShapeComponent();
		clone.color = new Color(color.getRGB());
		clone.radius = radius;
		return clone;
	}
}