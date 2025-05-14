package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Set;

public class CircleShapeSystem extends EntitySystem {
	/**
	 * Constructs a CircleShapeSystem that processes entities with
	 * TransformComponent and CircleShapeComponent.
	 *
	 * @param world the entity world containing components
	 */
	public CircleShapeSystem(EntityWorld world) {
		super(world,
				new ComponentSet()
				.add(world.component(TransformComponent.class))
				.add(world.component(CircleShapeComponent.class)));
	}

	/**
	 * Unused
	 *
	 * @param tick      the current tick (unused)
	 * @param deltaTime how much time to progress forward
	 */
	@Override
	public void run(int tick, float deltaTime) {}

	/**
	 * Renders all entities with CircleShapeComponent and TransformComponent.
	 *
	 * @param gfx the graphics context to draw on
	 */
	public void draw(Graphics2D gfx) {
		each(new Runnable() {
			@Override
			public void each(Entity e) {
				CircleShapeComponent circle = e.get(CircleShapeComponent.class);
				TransformComponent transform = e.get(TransformComponent.class);
				float hr = circle.radius / 2;

				gfx.setColor(circle.color);
				gfx.fillOval((int)(transform.pos.x - hr), (int)(transform.pos.y - hr),
						(int)circle.radius, (int)circle.radius);
			}
		});
	}
}