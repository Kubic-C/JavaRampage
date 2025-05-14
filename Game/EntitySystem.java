package Game;

import java.util.*;

/**
 * A system operates over all ENABLED entities that contain a superset of the queried set.
 * */
public abstract class EntitySystem {
	private EntityWorld m_world;
	private ComponentSet m_set;
	
	protected abstract class Runnable {
		public abstract void each(Entity e);
	}
	
	/**
	 * Creates an entity system 
	 * 
	 * @param world The EntityWorld to get entity info from
	 * @param set the set to query all entities against */
	EntitySystem(EntityWorld world, ComponentSet set) {
		m_world = world;
		m_set = set.add(world.component(EntityWorld.Enabled.class));
		
		m_world.warmSuperSets(m_set);
	}
	
	public void each(Runnable runnable) {
		List<ComponentSet> set = m_world.getSuperSets(m_set);
		
		m_world.beginDefer();
		for(ComponentSet superSet : set) {
			for(Integer id : m_world.getEntities(superSet)) {
				Entity entity = new Entity(id, m_world);
				
				runnable.each(entity);
			}
		}
		m_world.endDefer();
	}
	
	/**
	 * Returns entity world of this system
	 * @return The Entity World */
	public EntityWorld getWorld() {
		return m_world;
	}

	/**
	 * The method to call every tick
	 * 
	 * @param tick The current tick of this frame
	 * @param deltaTime time since last tick */
	public abstract void run(int tick, float deltaTime);
}