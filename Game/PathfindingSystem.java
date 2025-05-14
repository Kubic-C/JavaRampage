package Game;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The PathfindingSystem class is responsible for managing the pathfinding logic within the game world,
 * updating the arrows and movement of entities on the map.
 */
public class PathfindingSystem extends EntitySystem {
	private Entity m_map;
	private Entity m_player;
	private int m_curGen;
	

    /**
     * Constructs a PathfindingSystem to manage pathfinding logic.
     *
     * @param world The entity world that contains all entities in the game.
     * @param map   The map entity that represents the terrain.
     * @param player The player entity that is the focus of the pathfinding.
     */
	public PathfindingSystem(EntityWorld world, Entity map, Entity player) {
		super(world, new ComponentSet()
				.add(world.component(TransformComponent.class))
				.add(world.component(RigidBodyComponent.class))
				.add(world.component(SeekerComponent.class)));
		
		m_map = map;
		m_player = player;
	}
	
	private ArrowComponent getTopArrow(EntityWorld world, TilemapComponent tilemapLayers, IVec2D pos) {
	    for (int i = tilemapLayers.getTilemapCount(); i != 0; i--) {
	        Tilemap tilemap = tilemapLayers.getTilemap(i - 1);

	        if (!tilemap.contains(pos)) {
	            continue;
	        }

	        Entity entity = tilemap.find(pos).entity;
	        if (entity == null || !entity.has(ArrowComponent.class)) {
	            continue;
	        }

	        return entity.get(ArrowComponent.class);
	    }

	    return null;
	}

	private void addTarget(Queue<IVec2D> queue, IVec2D target) {
		TilemapComponent tilemapLayers = m_map.get(TilemapComponent.class);
		
		ArrowComponent topArrow = getTopArrow(m_player.world(), tilemapLayers, target);
		if(topArrow == null)
			return;
		topArrow.gen = m_curGen;
		topArrow.cost = 0;
		queue.add(target);
	}
	
    /**
     * Runs the pathfinding system, updating the arrows and entities based on the current state.
     *
     * @param tick The current game tick.
     * @param deltaTime The time elapsed since the last tick.
     */
	@Override
	public void run(int tick, float deltaTime) {
		TransformComponent transform = m_map.get(TransformComponent.class);
		TilemapComponent tilemapLayers = m_map.get(TilemapComponent.class);
		Queue<IVec2D> openQueue = new LinkedList<IVec2D>();
		
		m_curGen++;
		addTarget(openQueue, new IVec2D(0, 0));
		addTarget(openQueue, Tilemap.getNearestTile(transform.getLocalPoint(m_player.get(TransformComponent.class).pos.clone())));
		
		while(!openQueue.isEmpty()) {
			IVec2D curPos = openQueue.poll();
			ArrowComponent curArrow = getTopArrow(m_player.world(), tilemapLayers, curPos);
			
			for(int i = 0; i < dirs.length; i++) {
				IVec2D otherPos = curPos.clone().add(dirs[i]);
				
				ArrowComponent otherArrow = getTopArrow(m_player.world(), tilemapLayers, otherPos);
				if(otherArrow == null)
					continue;
				float newCost = curArrow.cost + costs[i] + otherArrow.terrainCost;
				if(otherArrow.gen == m_curGen && otherArrow.cost <= newCost)
					continue;
				
				otherArrow.cost = newCost;
				otherArrow.dir = normDirs[i].clone().scale(-1);
				otherArrow.gen = m_curGen;
				openQueue.add(otherPos);
			}
		}
		
		each(new Runnable() {
			@Override
			public void each(Entity e) {
				RigidBodyComponent rbId = e.get(RigidBodyComponent.class);
				TransformComponent seekerTransform = e.get(TransformComponent.class);
				RigidBody rb = rbId.body;
				
				Vec2D pos = seekerTransform.pos;
				IVec2D seekerTilePos = Tilemap.getNearestTile(transform.getLocalPoint(pos));
				ArrowComponent otherArrow = getTopArrow(m_player.world(), tilemapLayers, seekerTilePos);
				if(otherArrow == null)
					return;
	
				seekerTransform.rot = otherArrow.dir.angle();
				rb.setLinearVelocity(otherArrow.dir.clone().scale(2));
			}
		});
	}
	
	private IVec2D[] dirs = {
			new IVec2D(0, 1),
			new IVec2D(0, -1),
			new IVec2D(1, 0),
			new IVec2D(-1, 0),
			new IVec2D(-1, -1),
			new IVec2D(1, -1),
			new IVec2D(1, 1),
			new IVec2D(-1, 1)
	};
	
	private Vec2D[] normDirs = {
			new Vec2D(dirs[0].x, dirs[0].y).normal(),
			new Vec2D(dirs[1].x, dirs[1].y).normal(),
			new Vec2D(dirs[2].x, dirs[2].y).normal(),
			new Vec2D(dirs[3].x, dirs[3].y).normal(),
			new Vec2D(dirs[4].x, dirs[4].y).normal(),
			new Vec2D(dirs[5].x, dirs[5].y).normal(),
			new Vec2D(dirs[6].x, dirs[6].y).normal(),
			new Vec2D(dirs[7].x, dirs[7].y).normal(),
	};
	
	private float costs[] = {
		1,
		1,
		1,
		1,
		(float)Math.sqrt(2),
		(float)Math.sqrt(2),
		(float)Math.sqrt(2),
		(float)Math.sqrt(2)
	};
}