package Game;

import java.util.Set;

/**
 * The PhysicsSystem class is responsible for managing the physics simulation for all entities
 * with a RigidBody component in the game world. It synchronizes entity transforms with their
 * corresponding physics bodies and progresses the physics world.
 */
public class PhysicsSystem extends EntitySystem {
	private PhysicsWorld m_physicsWorld;

    /**
     * Constructs a PhysicsSystem to simulate physics for entities with RigidBody components.
     *
     * @param world The entity world that contains all entities in the game.
     * @param physicsWorld The PhysicsWorld that handles the physics simulation.
     */
	public PhysicsSystem(EntityWorld world, PhysicsWorld physicsWorld) {
		super(world, new ComponentSet().add(world.component(RigidBody.class)));
		m_physicsWorld = physicsWorld;
	}

	/**
	 * Run one physics step over time period of deltaTime
	 * 
	 * @param tick Unused
	 * @param delaTime how much time to progress forward*/
	@Override
	public void run(int tick, float deltaTime) {		
		each(new Runnable() {
			@Override
			public void each(Entity e) {
				RigidBodyComponent bodyId = e.get(RigidBodyComponent.class);
				RigidBody rigidBody = bodyId.body;
				TransformComponent transform = e.get(TransformComponent.class);

				if(!(transform.pos.equals(rigidBody.getPos()) && transform.rot == rigidBody.getRot())) {
					rigidBody.setPos(transform.pos);
					rigidBody.setRot(transform.rot);
				}
			}
		});
		
		m_physicsWorld.progress(deltaTime);
		
		each(new Runnable() {
			@Override
			public void each(Entity e) {
				RigidBodyComponent bodyId = e.get(RigidBodyComponent.class);
				RigidBody rigidBody = bodyId.body;
				TransformComponent transform = e.get(TransformComponent.class);

				if(!(transform.pos.equals(rigidBody.getPos()) && transform.rot == rigidBody.getRot())) {
					transform.pos = rigidBody.getPos();
					transform.rot = rigidBody.getRot();
				}
			}
		});
	}
}