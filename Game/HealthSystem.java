package Game;

/**
 * The HealthSystem is a part of the Entity Component System (ECS) that handles health-based logic for entities.
 * It checks the health status of all entities with a HealthComponent, and if the health is zero or below,
 * it destroys the entity.
 */
public class HealthSystem extends EntitySystem {

    /**
     * Constructor that initializes the HealthSystem with the provided world.
     * The system listens for entities that have a HealthComponent.
     * 
     * @param world The entity world that this system operates within.
     */
    HealthSystem(EntityWorld world) {
        super(world, new ComponentSet()
                .add(world.component(HealthComponent.class)));  // Listen for entities with HealthComponent
    }

    /**
     * The run method is called every tick of the game. It iterates through all entities with a HealthComponent
     * and checks if their health is less than or equal to zero. If so, the entity is destroyed.
     * 
     * @param tick The current tick or frame count. (Unused in this method)
     * @param deltaTime The time difference between ticks (Unused in this method)
     */
    @Override
    public void run(int tick, float deltaTime) {
        // Iterate over each entity with a HealthComponent
        each(new Runnable() {
            @Override
            public void each(Entity e) {
                if (e.get(HealthComponent.class).health <= 0) {
                    e.world().destroy(e.id());
                }
            }
        });
    }
}