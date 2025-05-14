package Game;

/**
 * Entity represents an object in the game world that holds a list of components.
 * It provides methods to add, remove, and retrieve components, and interacts with the
 * EntityWorld to manage the entity's state and behavior.
 */
public class Entity {
    protected Integer m_id;
    protected EntityWorld m_world;

    /**
     * Creates an entity with a given ID from the provided EntityWorld.
     * 
     * @param id    The unique ID of the entity.
     * @param world The EntityWorld where the entity's data is sourced.
     */
    public Entity(Integer id, EntityWorld world) {
        m_id = id;
        m_world = world;
    }

    /**
     * Adds a component to the entity.
     * 
     * @param obj The component to be added to the entity.
     */
    public <T> void add(Component obj) {
        m_world.getEntityData(m_id).add(m_id, obj);
    }

    /**
     * Removes a component from the entity based on its class type.
     * 
     * @param type The class type of the component to remove.
     */
    public void remove(Class<?> type) {
        m_world.getEntityData(m_id).remove(m_id, type);
    }

    /**
     * Retrieves a component of the given type from the entity.
     * 
     * @param type The class type of the component to retrieve.
     * @return The component of the specified type, or null if it does not exist.
     */
    public <T> T get(Class<T> type) {
        return m_world.getEntityData(m_id).get(type);
    }

    /**
     * Checks if the entity has a component of the given type.
     * 
     * @param type The class type of the component to check for.
     * @return True if the entity has a component of the specified type, otherwise false.
     */
    public <T> boolean has(Class<T> type) {
        return m_world.getEntityData(m_id).has(type);
    }

    /**
     * Checks if the entity is alive (exists) in the world.
     * 
     * @return True if the entity is alive, otherwise false.
     */
    public boolean isAlive() {
        return m_world.isAlive(m_id);
    }

    /**
     * Enables the entity by adding an 'Enabled' component.
     */
    public void enable() {
        add(new EntityWorld.Enabled());
    }

    /**
     * Disables the entity by removing the 'Enabled' component.
     */
    public void disable() {
        remove(EntityWorld.Enabled.class);
    }

    /**
     * Creates a clone of the entity.
     * 
     * @return A new entity instance with the same ID and associated data.
     */
    public Entity clone() {
        return m_world.clone(m_id);
    }

    /**
     * Gets the ID of the entity.
     * 
     * @return The unique ID of the entity.
     */
    public Integer id() {
        return m_id;
    }

    /**
     * Gets the EntityWorld that the entity belongs to.
     * 
     * @return The EntityWorld where the entity exists.
     */
    public EntityWorld world() {
        return m_world;
    }
}