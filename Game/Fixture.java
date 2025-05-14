package Game;

import java.awt.Graphics2D;

/**
 * Represents a generic fixture used in physics simulations.
 * Subclasses define the shape and behavior of the fixture.
 */
public abstract class Fixture {	
	public static final int CIRCLE_FIXTURE = 1;
	public static final int RECTANGLE_FIXTURE = 2;	
	
	protected float m_mass;

	/**
	 * Constructs a fixture with the specified mass.
	 *
	 * @param mass the mass of the fixture
	 */
	public Fixture(float mass) {
		m_mass = mass;
	}

	/**
	 * Returns the mass of the fixture.
	 *
	 * @return the mass
	 */
	public float getMass() {
		return m_mass;
	}

	/**
	 * Returns the axis-aligned bounding box (AABB) of the fixture at a given rotation.
	 *
	 * @param rotation the rotation angle of the fixture
	 * @return the AABB of the fixture
	 */
	public abstract AABB getAABB(float rotation);

	/**
	 * Returns the fixture type identifier.
	 *
	 * @return the fixture type ID
	 */
	public abstract int getType();

	/**
	 * Draws the fixture using the provided graphics context.
	 *
	 * @param gfx the graphics context to draw with
	 * @param pos the position of the fixture
	 * @param angle the rotation angle of the fixture
	 */
	public abstract void draw(Graphics2D gfx, Vec2D pos, float angle);
}