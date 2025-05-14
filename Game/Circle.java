package Game;

import java.awt.Graphics2D;

/**
 * Represents a circular fixture used in physics simulations.
 * Stores the radius and provides calculations specific to a circle shape.
 */
public class Circle extends Fixture {
	private float m_radius;

	/**
	 * Constructs a new Circle fixture with the specified mass and radius.
	 *
	 * @param mass the mass of the circle
	 * @param radius the radius of the circle
	 */
	public Circle(float mass, float radius) {
		super(mass);
		m_radius = radius;
	}

	/**
	 * Returns the radius of the circle.
	 *
	 * @return the radius
	 */
	public float getRadius() {
		return m_radius;
	}

	/**
	 * Computes and returns the axis-aligned bounding box (AABB) of the circle,
	 * ignoring rotation since a circle is rotationally symmetric.
	 *
	 * @param rotation the rotation of the object (ignored for circles)
	 * @return the AABB of the circle
	 */
	@Override
	public AABB getAABB(float rotation) {
		float sl = m_radius * 2;
		return new AABB(-sl + m_radius, -sl + m_radius, sl, sl);
	}

	/**
	 * Returns the fixture type constant for a circle.
	 *
	 * @return the fixture type ID
	 */
	@Override
	public int getType() {
		return CIRCLE_FIXTURE;
	}

	/**
	 * Draws the circle on the screen using the provided graphics context.
	 *
	 * @param gfx the graphics context to draw with
	 * @param pos the position of the circle's center
	 * @param angle the rotation angle of the circle (not used in drawing)
	 */
	@Override
	public void draw(Graphics2D gfx, Vec2D pos, float angle) {
		float d = m_radius * 2;
		gfx.drawOval((int)(pos.x - m_radius), (int)(pos.y - m_radius), (int)d, (int)d);
	}
}