package Game;

import java.util.Random;

/**
 * EnemySpawner is responsible for spawning enemies in the game world at regular intervals.
 * The number and difficulty of the enemies increase over time based on the difficulty level.
 */
public class EnemySpawner {
    private int m_ticksSinceLastSpawn = 0;
    private int m_spawnInterval = 100; // ticks between spawns
    private int m_difficultyLevel = 1;
    private int m_spawnCount = 1;
    private EntityWorld m_world;
    private PhysicsWorld m_physicsWorld;
    
    /**
     * Constructs an EnemySpawner to spawn enemies in the given world and physics world.
     * 
     * @param world the EntityWorld where the enemies will be spawned
     * @param physicsWorld the PhysicsWorld used for enemy movement and interaction
     */
    public EnemySpawner(EntityWorld world, PhysicsWorld physicsWorld) {
    	m_world = world;
    	m_physicsWorld = physicsWorld;
    }
    
    /**
     * Updates the spawner logic every tick, checking if it's time to spawn new enemies.
     * 
     * @param tick the current game tick (not used here, but could be useful for debugging or future logic)
     */
    public void update(int tick) {
        m_ticksSinceLastSpawn++;

        if (m_ticksSinceLastSpawn >= m_spawnInterval) {
            spawnEnemies(m_spawnCount, m_difficultyLevel);
            m_ticksSinceLastSpawn = 0;
            increaseDifficulty();
        }
    }

    private void spawnEnemies(int count, int difficulty) {
    	Random random = new Random();
    	
        for (int i = 0; i < count; i++) {
            System.out.println("Tick " + tickInfo() + ": Spawned enemy with difficulty " + difficulty);
            
			Entity e = m_world.create();
			e.add(new SeekerComponent());
			e.add(new TransformComponent());
			TransformComponent transform = e.get(TransformComponent.class);
			transform.pos = new Vec2D(210 + random.nextFloat(100) - 50.0f, 300 + random.nextFloat(300) - 150.5f);
			
			RigidBody rb = m_physicsWorld.create(transform.pos, 0, false);
			rb.attachFixture(new Circle(5.0f, 5.0f));
			rb.setUserData(e);
			rb.setSelfMask(App.EnemyMask);
			rb.setLinearDampening(5);
			e.add(new RigidBodyComponent(rb));
			
			CircleShapeComponent circleShape = new CircleShapeComponent();
			circleShape.radius = 5.0f;
			e.add(circleShape);
			
			e.add(new EnemyTag());
        }
    }

    private void increaseDifficulty() {
        m_difficultyLevel++;

        if (m_difficultyLevel % 10 == 0) {
            m_spawnCount++; // increase number of enemies
        }

        if (m_difficultyLevel % 5 == 0 && m_spawnInterval > 10) {
            m_spawnInterval -= 5; // derease interval for faster spawning
        }
    }

    private String tickInfo() {
        return String.format("d%d-c%d-i%d", m_difficultyLevel, m_spawnCount, m_spawnInterval);
    }
}