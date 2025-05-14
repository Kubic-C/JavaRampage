package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The PhysicsWorld class manages the physics simulation for all rigid bodies in the game.
 * It handles the creation, destruction, and updating of rigid bodies, and performs collision detection and resolution.
 */
public class PhysicsWorld {
    private Integer m_idCounter = 0;
    private Map<Integer, RigidBody> m_bodies;
    private QuadTree m_quadTree;
    private CollisionSolver m_solver;
    private ArrayList<CollisionSolver.CollisionManifold> m_manifolds;
    private ArrayList<CollisionEvent> m_collisionEvents;

    /**
     * Constructs a PhysicsWorld with a given width and height for the QuadTree.
     *
     * @param w The width of the world (for QuadTree).
     * @param h The height of the world (for QuadTree).
     */
    public PhysicsWorld(int w, int h) {
        m_quadTree = new QuadTree(w, h);
        m_solver = new CollisionSolver();
        m_bodies = new HashMap<Integer, RigidBody>();
        m_manifolds = new ArrayList<CollisionSolver.CollisionManifold>();
        m_collisionEvents = new ArrayList<CollisionEvent>();
    }

    /**
     * Returns the map of all rigid bodies in the world.
     *
     * @return A map where the key is the rigid body's ID, and the value is the RigidBody object.
     */
    public Map<Integer, RigidBody> getBodyList() {
        return m_bodies;
    }

    /**
     * Creates a new rigid body with the specified position, rotation, and static status.
     *
     * @param pos The initial position of the rigid body.
     * @param rot The initial rotation of the rigid body.
     * @param isStatic Whether the rigid body is static or dynamic.
     * @return The created RigidBody object.
     */
    public RigidBody create(Vec2D pos, float rot, boolean isStatic) {
        Integer newId = ++m_idCounter;

        m_bodies.put(newId, new RigidBody(this, newId, pos, rot, isStatic));

        return m_bodies.get(newId);
    }

    /**
     * Retrieves a rigid body by its ID.
     *
     * @param id The ID of the rigid body to retrieve.
     * @return The RigidBody object with the specified ID, or null if not found.
     */
    public RigidBody get(Integer id) {
        return m_bodies.get(id);
    }

    /**
     * Destroys a rigid body by its ID.
     *
     * @param id The ID of the rigid body to destroy.
     */
    public void destroy(Integer id) {
        m_bodies.remove(id);
    }

    /**
     * Destroys a given rigid body.
     *
     * @param body The RigidBody object to destroy.
     */
    public void destroy(RigidBody body) {
        destroy(body.id());
    }

    /**
     * Progresses the physics simulation by a time step (deltaTime).
     * This includes updating rigid body positions, checking for collisions, and resolving them.
     *
     * @param deltaTime The time step for progressing the simulation.
     */
    public void progress(float deltaTime) {
        m_collisionEvents.clear();
        m_quadTree.clear();
        m_manifolds.clear();

        ArrayList<RigidBody> markedForDeletion = new ArrayList<>();
        Set<Integer> keySet = m_bodies.keySet();
        for (Integer key : keySet) {
            RigidBody rA = m_bodies.get(key);
            if (rA.isMarkedForDeletion()) {
                markedForDeletion.add(rA);
                continue;
            }

            rA.inegrate(deltaTime);
            rA.setCollisionTested(false);
            m_quadTree.add(rA);
        }

        for (RigidBody body : markedForDeletion)
            destroy(body);

        for (Integer key : keySet) {
            RigidBody rA = m_bodies.get(key);
            rA.setCollisionTested(true);

            List<TreeElement> intersecting = m_quadTree.intersecting(rA);
            for (int i = 0; i < intersecting.size(); i++) {
                RigidBody rB = (RigidBody) intersecting.get(i);

                if (rA == rB)
                    continue;

                if (rB.hasCollisionTested())
                    continue;

                if (!((rA.getCollMask() & rB.getSelfMask()) != 0 && (rB.getCollMask() & rA.getSelfMask()) != 0)) {
                    continue;
                }
                
                CollisionSolver.CollisionManifold manifold = new CollisionSolver.CollisionManifold();
                if (m_solver.resolveCollisionIIE(manifold, rA, rB)) {
                    m_manifolds.add(manifold);

                    CollisionEvent event = new CollisionEvent();
                    event.bodyA = rA;
                    event.bodyB = rB;
                    m_collisionEvents.add(event);
                }
            }
        }
    }

    /**
     * Retrieves a list of collision events that have occurred during the current simulation step.
     *
     * @return A list of CollisionEvent objects.
     */
    public ArrayList<CollisionEvent> getCollisionEvents() {
        return m_collisionEvents;
    }

    /**
     * Retrieves the QuadTree used for spatial partitioning in the physics world.
     *
     * @return The QuadTree object.
     */
    public QuadTree getTree() {
        return m_quadTree;
    }

    /**
     * Draws the physics world, including the QuadTree and all rigid bodies, on the provided Graphics2D object.
     *
     * @param gfx The Graphics2D object to draw on.
     */
    public void draw(Graphics2D gfx) {
        m_quadTree.draw(gfx);

        gfx.setColor(Color.YELLOW);
        Set<Integer> keySet = m_bodies.keySet();
        for (Integer key : keySet) {
            RigidBody rA = m_bodies.get(key);

            rA.draw(gfx);
        }
    }
}