package Game;

/**
 * The DeathSystem is responsible for managing entities that die due to their lifetime.
 * It checks if the lifetime of an entity has expired and destroys the entity if it has.
 */
public class DeathSystem extends EntitySystem {

    /**
     * Constructs a DeathSystem that listens for entities with a LifetimeComponent.
     *
     * @param world The world in which the system operates.
     */
    DeathSystem(EntityWorld world) {
        super(world, new ComponentSet().add(world.component(LifetimeComponent.class)));
    }

    /**
     * Iterates through all entities with a LifetimeComponent and checks if their time has expired.
     * If an entity's lifetime has expired, it is destroyed.
     *
     * @param tick Unused tick value. It could be used for managing different time intervals or events, but it is not utilized here.
     * @param deltaTime The amount of time that has passed, used to decrement the entity's lifetime.
     */
    @Override
    public void run(int tick, float deltaTime) {
        each(new Runnable() {
            @Override
            public void each(Entity e) {
                LifetimeComponent lifetime = e.get(LifetimeComponent.class);
                lifetime.timeLeft -= deltaTime;
                if (lifetime.timeLeft < 0) {
                    getWorld().destroy(e.id());
                }
            }
        });
    }
}