package Game;

import java.util.*;

/**
 * The EntityWorld class represents the collection of all entities and their components
 * in the game. It provides functionality for creating, managing, and manipulating entities
 * and their associated components in an efficient manner. The class also handles component
 * supersets and deferred operations such as entity creation, movement, and destruction.
 */
public class EntityWorld {
    private Integer m_idCounter = 0;
    private Map<Integer, EntityData> m_entities;
    private int m_compIdCounter;
    
    private Map<Class<?>, Integer> m_comps;
    private Map<ComponentSet, Set<Integer>> m_sets;
    private Map<ComponentSet, List<ComponentSet>> m_superSets;
    
    private boolean m_defer;
    private ArrayList<ComponentSet> m_deferredCreateSuperset;
    protected Map<Integer, ComponentSet> m_deferredMoves;
    private HashSet<Integer> m_deferredDelete;
    
    public static class Enabled extends Component {
        @Override
        public Class<?> getType() {
            return Enabled.class;
        }

        @Override
        public Component clone() {
            return new Enabled();
        }
    }
    
    /**
     * Constructs an EntityWorld. Initializes necessary data structures for entities and components.
     */
    public EntityWorld() {
        m_entities = new HashMap<>();
        m_comps = new HashMap<>();
        m_sets = new HashMap<>();
        m_superSets = new HashMap<>();
        
        m_deferredCreateSuperset = new ArrayList<>();
        m_deferredMoves = new HashMap<>();
        m_deferredDelete = new HashSet<>();
        
        findOrCreateSet(new ComponentSet());
        findOrCreateSet(new ComponentSet().add(component(Enabled.class)));    
    }
    
    /**
     * Starts deferring operations such as entity creation, movement, and deletion.
     */
    public synchronized void beginDefer() {
        m_defer = true;
    }
    
    /**
     * Returns whether the EntityWorld is currently deferring operations.
     * 
     * @return True if operations are being deferred, otherwise false.
     */
    public synchronized boolean isDefer() {
        return m_defer;
    }
    
    /**
     * Ends deferring operations and applies any deferred changes such as entity creation,
     * movement, and deletion.
     */
    public synchronized void endDefer() {
        m_defer = false;
        
        // Apply deferred supersets creation
        for (ComponentSet set : m_deferredCreateSuperset) {
            createSupersets(set);
        }
        
        // Apply deferred entity movements
        Set<Integer> movedEntities = m_deferredMoves.keySet();
        for (Integer id : movedEntities) {
            moveSets(id, m_deferredMoves.get(id));
        }
        
        // Apply deferred deletions
        for (Integer id : m_deferredDelete) {
            destroy(id);
        }
        
        // Clear deferred collections
        m_deferredCreateSuperset.clear();
        m_deferredMoves.clear();
        m_deferredDelete.clear();
    }

    /**
     * Returns a unique component ID for the given component type. If the component ID does not
     * exist, it is created and added to the component registry.
     * 
     * @param type The class type of the component.
     * @return The unique ID for the component.
     */
    public synchronized int component(Class<?> type) {
        if (m_comps.containsKey(type))
            return m_comps.get(type);
        
        int newId = ++m_compIdCounter;
        m_comps.put(type, newId);
        return newId;
    }
    
    /**
     * Ensures that supersets for the provided component set are created.
     * 
     * @param set The component set to warm supersets for.
     */
    public synchronized void warmSuperSets(ComponentSet set) {
        if (!m_superSets.containsKey(set))
            createSupersets(set);
        if (!m_superSets.containsKey(set.add(component(Enabled.class))))
            createSupersets(set);
    }
    
    /**
     * Returns a list of supersets for the given component set.
     * 
     * @param set The component set to retrieve supersets for.
     * @return A list of supersets for the component set.
     */
    public synchronized List<ComponentSet> getSuperSets(ComponentSet set) {
        if (!m_superSets.containsKey(set))
            createSupersets(set);
        
        return m_superSets.get(set);
    }
    
    /**
     * Returns the set of entities that have the given component set.
     * 
     * @param set The component set to check.
     * @return A set of entity IDs that have the given component set.
     */
    public synchronized Set<Integer> getEntities(ComponentSet set) {
        return findOrCreateSet(set);
    }
    
    /**
     * Creates a new entity with an assigned unique ID and enables it.
     * 
     * @return The newly created entity.
     */
    public synchronized Entity create() {
        Integer newId = ++m_idCounter;
        m_entities.put(newId, new EntityData());
        
        m_sets.get(new ComponentSet()).add(newId);
        Entity e = new Entity(m_idCounter, this);
        e.enable();
        
        return e;
    }
    
    /**
     * Creates a clone of the entity with the given ID, copying over its components.
     * 
     * @param id The ID of the entity to clone.
     * @return The cloned entity.
     */
    public synchronized Entity clone(Integer id) {
        Entity eClone = create();
        
        EntityData data = getEntityData(id);
        Set<Integer> compIds = data.comps.keySet();
        for (Integer compId : compIds) {
            Component comp = data.comps.get(compId);
            eClone.add(comp.clone());
        }
        
        return eClone;
    }
    
    /**
     * Destroys the entity with the given ID.
     * 
     * @param id The ID of the entity to destroy.
     */
    public synchronized void destroy(Integer id) {
        if (!m_defer) {
            EntityData data = getEntityData(id);
            m_sets.get(getEntityData(id).set).remove(id);
            
            Set<Integer> comps = data.comps.keySet();
            for (Integer compId : comps) {
                data.comps.get(compId).onRemove();
            }
            m_entities.remove(id);
        } else {
            m_deferredDelete.add(id);
        }
    }
    
    /**
     * Checks if the entity with the given ID is alive in the world.
     * 
     * @param id The ID of the entity to check.
     * @return True if the entity is alive, otherwise false.
     */
    public boolean isAlive(Integer id) {
        return m_entities.containsKey(id) && !m_deferredDelete.contains(id);
    }
    
    /**
     * Moves an entity with the given ID to a new component set.
     * 
     * @param id  The ID of the entity to move.
     * @param to  The component set to move the entity to.
     */
    protected synchronized void moveSets(Integer id, ComponentSet to) {
        if (!m_defer) {
            EntityData data = getEntityData(id);
            findOrCreateSet(data.set).remove(id);
            findOrCreateSet(to).add(id);
            data.set = to;
        } else {
            m_deferredMoves.put(id, to);
        }
    }
    
    /**
     * Creates the supersets for a given component set.
     * 
     * @param set The component set to create supersets for.
     */
    private synchronized void createSupersets(ComponentSet set) {
        if (!m_defer) {
            m_superSets.put(set, new LinkedList<>());
            m_superSets.get(set).add(set);
            
            ArrayList<ComponentSet> supersetsOf = new ArrayList<>();
            ArrayList<ComponentSet> subsetsOf = new ArrayList<>();
            Set<ComponentSet> allSets = m_sets.keySet();
            for (ComponentSet otherSet : allSets) {
                if (set.supersetOf(otherSet))
                    supersetsOf.add(otherSet);
                else if (set.subsetOf(otherSet))
                    subsetsOf.add(otherSet);
            }
            
            for (ComponentSet supersetOf : supersetsOf)
                m_superSets.get(supersetOf).add(set);
            for (ComponentSet subsetOf : subsetsOf)
                m_superSets.get(set).add(subsetOf);
        } else {
            m_deferredCreateSuperset.add(set);
        }
    }
    
    /**
     * Finds or creates a component set for a given set of components.
     * 
     * @param set The component set to find or create.
     * @return The set of entities that belong to the component set.
     */
    protected synchronized Set<Integer> findOrCreateSet(ComponentSet set) {
        if (!m_sets.containsKey(set)) {
            m_sets.put(set, new HashSet<>());
            createSupersets(set);
        } 
        
        return m_sets.get(set);
    }

    /**
     * Retrieves the entity data for a given entity ID.
     * 
     * @param id The ID of the entity.
     * @return The entity data associated with the ID.
     */
    protected synchronized EntityData getEntityData(int id) {
        return m_entities.get(id);
    }
    
    /**
     * Private inner class that holds the components and set of an entity.
     */
    public class EntityData {
        public ComponentSet set;
        public Map<Integer, Component> comps;
        
        EntityData() {
            set = new ComponentSet();
            comps = new HashMap<>();
        }
        
        /**
         * Gets the current set of components for an entity.
         * 
         * @param id The ID of the entity.
         * @return The current component set for the entity.
         */
        protected synchronized ComponentSet getCurrentSet(int id) {
            ComponentSet set = m_deferredMoves.get(id);
            
            if (set != null)
                return set;
            
            return getEntityData(id).set;
        }
        
        /**
         * Adds a component to the entity.
         * 
         * @param id  The ID of the entity to add the component to.
         * @param obj The component to add.
         */
        public synchronized <T> void add(int id, Component obj) {
            ComponentSet curSet = getCurrentSet(id);
            int compId = component(obj.getType());
            
            if (curSet.contains(compId)) {
                comps.replace(compId, obj);
                return;
            }
            
            comps.put(compId, obj);
            moveSets(id, curSet.add(compId));
        }
		
        /**
         * Removes a component of the specified type from the entity identified by the given ID.
         * 
         * @param id the ID of the entity from which the component will be removed
         * @param type the class type of the component to be removed
         */
		public synchronized void remove(int id, Class<?> type) {
			ComponentSet curSet = getCurrentSet(id);
			int compId = component(type);
			
			if(!curSet.contains(compId))
				return;
			
			Component comp = comps.remove(compId);
			comp.onRemove();
			
			moveSets(id, curSet.remove(compId));
		}
		
	    /**
	     * Retrieves the component of the specified type from the entity identified by the given ID.
	     * 
	     * @param type the class type of the component to be retrieved
	     * @return the component of the specified type, or null if the component is not found
	     * @param <T> the type of the component
	     */
		@SuppressWarnings("unchecked")
		public synchronized <T> T get(Class<T> type) {
			return (T)comps.get(component(type));
		}
		
	    /**
	     * Checks if the entity identified by the given ID has a component of the specified type.
	     * 
	     * @param type the class type of the component to check for
	     * @return true if the entity has the component, false otherwise
	     * @param <T> the type of the component
	     */
		public synchronized <T> boolean has(Class<T> type) {
			return comps.containsKey(component(type));
		}
	}
}