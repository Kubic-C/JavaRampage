package Game;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The RigidBody class represents a rigid body in a physics world, including its position, rotation,
 * linear and angular velocities, and collision properties. It provides methods to manipulate the body, 
 * apply forces, and interact with other objects in the physics world.
 */
public class RigidBody extends TreeElement {
    private PhysicsWorld m_world;

    // Determined by fixtures
    private float m_mass = 0;

    private boolean m_isStatic;
    private Integer m_id;

    // Kinematics
    private Vec2D m_pos;
    private Vec2D m_lastPos;
    private float m_rot;
    private float m_angularVel;
    private float m_linearDampening;

    // Single fixture
    private Fixture m_fixture;

    // Has tested for collision tested
    private boolean m_collTested = false;
    private boolean m_isMarkedForDeletion = false;

    private int m_selfMask = 1;
    private int m_collMask = Integer.MAX_VALUE;

    private Object m_userData;
    
	private void setLastPos() {
		m_lastPos = m_pos.clone();
	}
	
	/**
	 * Constructs a new RigidBody with the specified parameters.
	 * 
	 * @param world The PhysicsWorld this RigidBody belongs to.
	 * @param id The unique identifier for this RigidBody.
	 * @param pos The initial position of the RigidBody.
	 * @param rot The initial rotation of the RigidBody in radians.
	 * @param isStatic Whether the RigidBody is static (non-movable).
	 */
	public RigidBody(PhysicsWorld world, Integer id, Vec2D pos, float rot, boolean isStatic) {
	    m_world = world;
	    m_id = id;
	    m_pos = pos;
	    m_rot = rot;
	    m_isStatic = isStatic;
	    m_linearDampening = 1.0f;

	    setLastPos();
	}
    
    /**
     * Returns whether this RigidBody is marked for deletion.
     * 
     * @return true if marked for deletion, false otherwise.
     */
    public boolean isMarkedForDeletion() {
        return m_isMarkedForDeletion;
    }

    /**
     * Marks this RigidBody for deletion.
     */
    public void markForDeletion() {
        m_isMarkedForDeletion = true;
    }

    /**
     * Returns the current rotation of this RigidBody in radians.
     * 
     * @return The rotation of the RigidBody.
     */
    public float getRot() {
        return m_rot;
    }

    /**
     * Returns the current position of this RigidBody.
     * 
     * @return A Vec2D representing the position of the RigidBody.
     */
    public Vec2D getPos() {
        return m_pos.clone();
    }

    /**
     * Sets the rotation of this RigidBody to the specified value.
     * 
     * @param rot The rotation to set in radians.
     */
    public void setRot(float rot) {
        m_rot = rot;
    }

    /**
     * Sets the position of this RigidBody to the specified value.
     * 
     * @param pos The position to set.
     */
    public void setPos(Vec2D pos) {
        setLastPos();
        m_pos = pos;
    }

    /**
     * Moves this RigidBody by a specified amount, updating its position.
     * 
     * @param amount The amount to move the RigidBody by.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody move(Vec2D amount) {
        setLastPos();
        m_pos.add(amount);
        return this;
    }

    /**
     * Integrates the position and rotation of the RigidBody using the provided delta time.
     * If linear dampening is set to a special value (NoVerletIntegration), no integration occurs.
     * 
     * @param deltaTime The time step to integrate over.
     * @return The updated RigidBody.
     */
    public RigidBody inegrate(float deltaTime) {
        Vec2D velocity = m_pos.clone().sub(m_lastPos);
        setLastPos();

        m_pos.add(velocity);
        m_rot += m_angularVel * deltaTime;

        return this;
    }

    /**
     * Sets whether this RigidBody has been tested for collisions.
     * 
     * @param collTested true if the RigidBody has been collision tested, false otherwise.
     */
    public void setCollisionTested(boolean collTested) {
        m_collTested = collTested;
    }

    /**
     * Returns whether this RigidBody has been tested for collisions.
     * 
     * @return true if collision testing has been done, false otherwise.
     */
    public boolean hasCollisionTested() {
        return m_collTested;
    }

    /**
     * Translates (moves) this RigidBody by a specified amount.
     * 
     * @param pos The position to translate the RigidBody by.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody translate(Vec2D pos) {
        m_pos.add(pos);
        return this;
    }

    /**
     * Rotates this RigidBody by the specified amount in radians.
     * 
     * @param rot The rotation to apply in radians.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody rotate(float rot) {
        m_rot += rot;
        return this;
    }

    /**
     * Sets the linear dampening of this RigidBody, which affects its velocity over time.
     * 
     * @param damp The linear dampening factor to set.
     */
    public void setLinearDampening(float damp) {
        m_linearDampening = damp;
    }

    /**
     * Applies a linear velocity change to this RigidBody by adjusting its last position.
     * 
     * @param linearVel The velocity to apply.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody applyLinearVelocity(Vec2D linearVel) {
        m_lastPos.add(linearVel.scale(-1.0f));
        return this;
    }

    /**
     * Sets the linear velocity of this RigidBody by adjusting its last position.
     * 
     * @param linearVel The velocity to set.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody setLinearVelocity(Vec2D linearVel) {
        m_lastPos = m_pos.clone().add(linearVel.scale(-1.0f));
        return this;
    }

    /**
     * Applies an angular velocity change to this RigidBody.
     * 
     * @param angularVel The angular velocity to apply.
     * @return The RigidBody itself for method chaining.
     */
    public RigidBody applyAngularVelocity(float angularVel) {
        m_angularVel += angularVel;
        return this;
    }

    /**
     * Returns whether this RigidBody is static (does not move).
     * 
     * @return true if the RigidBody is static, false otherwise.
     */
    public boolean isStatic() {
        return m_isStatic;
    }

    /**
     * Returns the unique identifier for this RigidBody.
     * 
     * @return The identifier of the RigidBody.
     */
    public int id() {
        return m_id;
    }

    /**
     * Returns the Axis-Aligned Bounding Box (AABB) of this RigidBody.
     * 
     * @return The AABB of the RigidBody.
     */
    public AABB getAABB() {
        return m_fixture.getAABB(m_rot).translate(m_pos);
    }

    /**
     * Returns the fixture attached to this RigidBody.
     * 
     * @return The fixture of the RigidBody.
     */
    public Fixture getFixture() {
        return m_fixture;
    }

    /**
     * Attaches a fixture to this RigidBody and updates its mass.
     * 
     * @param fixture The fixture to attach to this RigidBody.
     */
    public void attachFixture(Fixture fixture) {
        m_fixture = fixture;

        if (m_isStatic) {
            m_mass = 0;
        } else {
            m_mass = fixture.getMass();
        }
    }

    /**
     * Draws the RigidBody on the provided Graphics2D object, including its AABB and fixture.
     * 
     * @param gfx The Graphics2D object used for drawing.
     */
    public void draw(Graphics2D gfx) {
        gfx.setColor(Color.PINK);
        getAABB().draw(gfx);
        gfx.setColor(Color.YELLOW);
        m_fixture.draw(gfx, m_pos, m_rot);
        gfx.setColor(Color.CYAN);
        gfx.fillOval((int) m_pos.x - 2, (int) m_pos.y - 2, 4, 4);
    }

    /**
     * Returns the self mask for collision filtering.
     * 
     * @return The self mask value.
     */
    public int getSelfMask() {
        return m_selfMask;
    }

    /**
     * Returns the collision mask for collision filtering.
     * 
     * @return The collision mask value.
     */
    public int getCollMask() {
        return m_collMask;
    }

    /**
     * Sets the self mask for collision filtering.
     * 
     * @param selfMask The self mask value to set.
     */
    public void setSelfMask(int selfMask) {
        m_selfMask = selfMask;
    }

    /**
     * Sets the collision mask for collision filtering.
     * 
     * @param collMask The collision mask value to set.
     */
    public void setCollMask(int collMask) {
        m_collMask = collMask;
    }

    /**
     * Returns the user data associated with this RigidBody.
     * 
     * @return The user data.
     */
    public Object getUserData() {
        return m_userData;
    }

    /**
     * Sets the user data for this RigidBody.
     * 
     * @param userData The user data to set.
     */
    public void setUserData(Object userData) {
        m_userData = userData;
    }

    /**
     * Returns the PhysicsWorld this RigidBody belongs to.
     * 
     * @return The PhysicsWorld of the RigidBody.
     */
    public PhysicsWorld getWorld() {
        return m_world;
    }

    /**
     * Returns the mass of the RigidBody.
     * 
     * @return The mass of the RigidBody.
     */
    public float getMass() {
        return m_mass;
    }
}