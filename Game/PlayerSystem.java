package Game;

import java.util.Set;

/**
 * The PlayerSystem class is responsible for updating the player's movement based on input.
 * It processes each entity with a PlayerComponent and applies linear velocity to the associated RigidBody.
 */
public class PlayerSystem extends EntitySystem {
    /**
     * Constructs a PlayerSystem.
     *
     * @param world The EntityWorld where the entities with PlayerComponent exist.
     */
    PlayerSystem(EntityWorld world) {
        super(world, new ComponentSet().add(world.component(PlayerComponent.class)));
    }

    /**
     * Runs the system logic for one update step. It checks player movement input and updates
     * the linear velocity of the player's RigidBody accordingly.
     *
     * @param tick Unused parameter (tick count).
     * @param deltaTime The time step by which to update the player movement.
     */
    @Override
    public void run(int tick, float deltaTime) {
        each(new Runnable() {
            @Override
            public void each(Entity e) {
                PlayerComponent playerComp = e.get(PlayerComponent.class);
                RigidBody rb = e.get(RigidBodyComponent.class).body;
                
                Vec2D movement = new Vec2D(0);
                if (playerComp.goLeft)
                    movement.add(new Vec2D(-playerComp.moveSpeed, 0.0f));
                if (playerComp.goRight)
                    movement.add(new Vec2D(playerComp.moveSpeed, 0.0f));
                if (playerComp.goUp)
                    movement.add(new Vec2D(0.0f, -playerComp.moveSpeed));
                if (playerComp.goDown)
                    movement.add(new Vec2D(0.0f, playerComp.moveSpeed));    
                
                rb.setLinearVelocity(movement);
            }
        });
    }
}