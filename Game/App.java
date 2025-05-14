package Game;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The app contains the core components to the game, where everything
 * at a top level is called. */
public class App extends JComponent {
	private boolean m_exit;
	private boolean m_shouldRecreate;
	private JFrame m_window;
	private ReentrantLock m_drawLock;
	private EntityWorld m_world;
	private PhysicsWorld m_physicsWorld;

	private PhysicsSystem m_physicsSystem;
	private CircleShapeSystem m_circleRenderSystem;
	private TilemapRenderSystem m_tilemapRenderSystem;
	private PlayerSystem m_playerSystem;
	private PathfindingSystem m_pathfindingSystem;
	private DeathSystem m_deathSystem;
	private HealthSystem m_healthSystem;
	private int m_targetTicks;
	
	private int m_prevState;
	private int m_currentState;
	private static final int MainMenuState = 0;
	private static final int PauseState = 1;
	private static final int PlayState = 2;
	private static final int HelpState = 3;
	private static final int CoreShopState = 4;
	private static final int LoseState = 5;
	private static final int MaxStateId = 6;
	private JPanel m_stateGUI[] = new JPanel[MaxStateId];
	private Slot m_hotbar[] = new Slot[6];
	private int m_curSlot = 0;
	
	private Vec2D m_mousePos = new Vec2D();
	private boolean m_isMousePressed;
	
	private Entity m_player;
	private Entity m_core;
	private IVec2D m_coreCoords;
	private Entity m_tilemap;
	
	private AtomicInteger m_score = new AtomicInteger();
	private JLabel m_scoreLabel;
	private AtomicInteger m_health = new AtomicInteger();
	private JLabel m_healthLabel;
	private JLabel m_lastScoreLabel;
	
	public static final int BulletMask = 1 << 1;
	public static final int EnemyMask = 1 << 2;
	public static final int PlayerMask = 1 << 3;
	
	private EnemySpawner m_spawner;
	
	private class BulletComponent extends Component {
		public int hitsLeft = 5;
		
		@Override
		public Class<?> getType() {
			// TODO Auto-generated method stub
			return BulletComponent.class;
		}

		@Override
		public Component clone() {
			// TODO Auto-generated method stub
			BulletComponent clone = new BulletComponent();
			clone.hitsLeft = hitsLeft;
			return clone;
		}}
	
	private class UnRemovableTag extends Component {
		@Override
		public Class<?> getType() {
			// TODO Auto-generated method stub
			return UnRemovableTag.class;
		}

		@Override
		public Component clone() {
			return new UnRemovableTag();
		}}
	
	private class MainHealthTag extends Component {
		@Override
		public Class<?> getType() {
			// TODO Auto-generated method stub
			return MainHealthTag.class;
		}

		@Override
		public Component clone() {
			return new MainHealthTag();
		}}
	
	private class OnPlay implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	changeState(PlayState);
	    }
	}
	
	private class OnHelp implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	changeState(HelpState);
	    }
	}
	
	private class OnPrevious implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	changeState(m_prevState);
	    }
	}
	
	private class OnExit implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	m_exit = true;
	    }
	}
	
	private class OnPause implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	changeState(PauseState);
	    }
	}
	
	private class OnRestart implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	m_shouldRecreate = true;
	    	m_exit = true;
	    }
	}
	
	private void changeState(int nextStateId) {
		System.out.println("Changed to state: " + nextStateId);
		m_prevState = m_currentState;
    	m_currentState = nextStateId;
    	updateGUI();
	}
	
	/**
	 * Creates an APP Window, with a title and size of (w, h) along with the game data.
	 * @param title name of the game.
	 * @param w the width of the window
	 * @param h the height of the window
	 * @param targetTicks how many ticks a second to run the game at */
	public App(String title, int w, int h, int targetTicks) {	
		System.out.println("App launching");
		
		m_exit = false;
		m_currentState = MainMenuState;
		m_targetTicks = targetTicks;
		
		m_drawLock = new ReentrantLock();
		m_world = new EntityWorld();
		m_physicsWorld = new PhysicsWorld(w, h);
		/* ALL SYSTEMS SHALL BE ADDED HERE: -|*/
		m_physicsSystem = new PhysicsSystem(m_world, m_physicsWorld);
		m_circleRenderSystem = new CircleShapeSystem(m_world);
		m_tilemapRenderSystem = new TilemapRenderSystem(m_world);
		m_playerSystem = new PlayerSystem(m_world);
		m_deathSystem = new DeathSystem(m_world);
		m_healthSystem = new HealthSystem(m_world);
		
		/* GUI */
		m_window = new JFrame();
		m_window.setFocusable(true);
		m_window.setTitle(title);
		m_window.setLayout(new BoxLayout(m_window.getContentPane(), BoxLayout.Y_AXIS));
		m_window.setLocationRelativeTo(null);
		m_window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		m_window.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            m_exit = true;
	        }
	    }); 
		
		JLayeredPane overlayPane = new JLayeredPane();
		setBounds(0, 0, w, h);
		overlayPane.setPreferredSize(new Dimension(w, h));
		overlayPane.add(this, JLayeredPane.DEFAULT_LAYER);
		
		overlayPane.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
            public void mouseDragged(MouseEvent e) {
        		m_mousePos.x = e.getX();
        		m_mousePos.y = e.getY();
            }
        });
		
		
		overlayPane.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
            	m_isMousePressed = true;
        		m_mousePos.x = e.getX();
        		m_mousePos.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	m_isMousePressed  = false;
        		m_mousePos.x = e.getX();
        		m_mousePos.y = e.getY();
            }

			@Override
			public void mouseClicked(MouseEvent e) {
				 IVec2D coords = Tilemap.getNearestTile(m_tilemap.get(TransformComponent.class).getLocalPoint(m_mousePos));
				 System.out.println(coords);
				 if(coords.equals(m_coreCoords)) {
					 m_core.get(CoreComponent.class).updatePanel(m_stateGUI[CoreShopState], 4, m_hotbar, m_score);
					 changeState(CoreShopState);
				 }
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
        });
		
		{ // ============== MAIN MENU ============== 
			m_stateGUI[MainMenuState] = new JPanel();
			m_stateGUI[MainMenuState].setAlignmentY(CENTER_ALIGNMENT);
			m_stateGUI[MainMenuState].setLayout(new GridBagLayout());
			m_stateGUI[MainMenuState].setBackground(Color.RED);
			m_stateGUI[MainMenuState].setPreferredSize(new Dimension(w / 2, h / 2));
			m_stateGUI[MainMenuState].setAlignmentX(CENTER_ALIGNMENT);
			m_stateGUI[MainMenuState].setBounds(0, 0, w, h);
			overlayPane.add(m_stateGUI[MainMenuState], JLayeredPane.MODAL_LAYER);
			
			JLabel rampageText = new JLabel("RAMPAGE!");
			rampageText.setFont(rampageText.getFont().deriveFont(48.0f));
			rampageText.setAlignmentX(CENTER_ALIGNMENT);
			
			JButton mainPlayButton = new JButton("Play");
			mainPlayButton.addActionListener(new OnPlay());
			mainPlayButton.setPreferredSize(new Dimension(100, 50));
			mainPlayButton.setAlignmentX(CENTER_ALIGNMENT);
			
			JButton helpButton = new JButton("Help");
			helpButton.addActionListener(new OnHelp());
			helpButton.setPreferredSize(new Dimension(100, 50));
			helpButton.setAlignmentX(CENTER_ALIGNMENT);
			
			JButton mainExitButton = new JButton("Exit");
			mainExitButton.addActionListener(new OnExit());
			mainExitButton.setPreferredSize(new Dimension(100, 50));
			mainExitButton.setAlignmentX(CENTER_ALIGNMENT);
			
			GridBagConstraints constraint = new GridBagConstraints();
			constraint.gridy = 0;
			m_stateGUI[MainMenuState].add(rampageText, constraint);
			constraint.gridy = 1;
			m_stateGUI[MainMenuState].add(mainPlayButton, constraint);
			constraint.gridy = 2;
			m_stateGUI[MainMenuState].add(helpButton, constraint);
			constraint.gridy = 4;
			m_stateGUI[MainMenuState].add(mainExitButton, constraint);
		}
		
		{// ============== PAUSE MENU ============== 
			GridBagConstraints constraint = new GridBagConstraints();
			m_stateGUI[PauseState] = new JPanel();
			m_stateGUI[PauseState].setPreferredSize(new Dimension(w / 2, h / 2));
			m_stateGUI[PauseState].setBackground(Color.RED);
			m_stateGUI[PauseState].setVisible(false);
			m_stateGUI[PauseState].setBounds(0, 0, w, h);
			m_stateGUI[PauseState].setLayout(new GridBagLayout());
			overlayPane.add(m_stateGUI[PauseState], JLayeredPane.MODAL_LAYER);	
			
			JLabel pauseTitle = new JLabel("You are paused!");
			pauseTitle.setFont(pauseTitle.getFont().deriveFont(32.0f));
			constraint.gridy = 0;
			m_stateGUI[PauseState].add(pauseTitle);
			
			JButton pauseResumeButton = new JButton("Resume");
			pauseResumeButton.addActionListener(new OnPlay());
			pauseResumeButton.setPreferredSize(new Dimension(100, 50));
			pauseResumeButton.setAlignmentX(CENTER_ALIGNMENT);
			constraint.gridy = 1;
			m_stateGUI[PauseState].add(pauseResumeButton, constraint);
			
			JButton helpButton = new JButton("Help");
			helpButton.addActionListener(new OnHelp());
			helpButton.setPreferredSize(new Dimension(100, 50));
			helpButton.setAlignmentX(CENTER_ALIGNMENT);
			constraint.gridy = 2;
			m_stateGUI[PauseState].add(helpButton, constraint);
			
			JButton mainExitButton = new JButton("Exit");
			mainExitButton.addActionListener(new OnExit());
			mainExitButton.setPreferredSize(new Dimension(100, 50));
			mainExitButton.setAlignmentX(CENTER_ALIGNMENT);
			constraint.gridy = 3;
			m_stateGUI[PauseState].add(mainExitButton, constraint);
		}
		
		{ // ============== PLAY MENU ============== 
			GridBagConstraints constraint = new GridBagConstraints();
			m_stateGUI[PlayState] = new JPanel();
			m_stateGUI[PlayState].setPreferredSize(new Dimension(w / 2, h / 2));
			m_stateGUI[PlayState].setBackground(Color.YELLOW);
			m_stateGUI[PlayState].setOpaque(false);
			m_stateGUI[PlayState].setVisible(false);		
			m_stateGUI[PlayState].setBounds(0, (h / 3) + 50, w, h);
			m_stateGUI[PlayState].setLayout(new GridBagLayout());
			m_stateGUI[PlayState].setAlignmentY(BOTTOM_ALIGNMENT);
			overlayPane.add(m_stateGUI[PlayState], JLayeredPane.MODAL_LAYER);
			
			JButton playPauseButton = new JButton("Pause");
			playPauseButton.addActionListener(new OnPause());
			playPauseButton.setPreferredSize(new Dimension(100, 50));
			playPauseButton.setAlignmentY(BOTTOM_ALIGNMENT);
			constraint.gridy = 4;
			m_stateGUI[PlayState].add(playPauseButton , constraint);
			for(int i = 0; i < m_hotbar.length; i++) {
				JButton slotBtn = new JButton("" + i);
				slotBtn.setPreferredSize(new Dimension(100, 50));
				slotBtn.setAlignmentY(BOTTOM_ALIGNMENT);
				constraint.gridy = 4;
				m_stateGUI[PlayState].add(slotBtn, constraint);
				
				m_hotbar[i] = new Slot(slotBtn, i);
			}
			m_healthLabel = new JLabel("Health");
			m_healthLabel.setPreferredSize(new Dimension(100, 25));
			m_healthLabel.setAlignmentY(BOTTOM_ALIGNMENT);
			m_healthLabel.setAlignmentX(CENTER_ALIGNMENT);
			m_healthLabel.setForeground(Color.WHITE);
			constraint.gridy = 4;
			m_stateGUI[PlayState].add(m_healthLabel, constraint);
			
			m_scoreLabel = new JLabel("Score");
			m_scoreLabel.setPreferredSize(new Dimension(100, 25));
			m_scoreLabel.setAlignmentY(BOTTOM_ALIGNMENT);
			m_scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
			m_scoreLabel.setForeground(Color.WHITE);
			constraint.gridy = 4;
			m_stateGUI[PlayState].add(m_scoreLabel, constraint);
		}
		
		{ // ============== HELP MENU ============== 
			GridBagConstraints constraint = new GridBagConstraints();
			m_stateGUI[HelpState] = new JPanel();
			m_stateGUI[HelpState].setPreferredSize(new Dimension(w / 2, h / 2));
			m_stateGUI[HelpState].setBackground(Color.YELLOW);
			m_stateGUI[HelpState].setVisible(false);		
			m_stateGUI[HelpState].setBounds(0, 0, w, h);
			m_stateGUI[HelpState].setLayout(new GridBagLayout());
			overlayPane.add(m_stateGUI[HelpState], JLayeredPane.MODAL_LAYER);
			
			String text = 
					"""
					Welcome to Rampage!
					Fend off the zombies to protect you and your core.
					
					Use [AWSD] to move
					Use [Left click] to interact
					Use [1-6] to select items on your hotbar.
					Click the core to access the core store!
					
					Kill zombies, earn points, buy items and fortifications from the core store.
					""";
			
			JTextArea helpText = new JTextArea(text);
			helpText.setEditable(false);
			helpText.setFont(helpText.getFont().deriveFont(12.0f));
			helpText.setWrapStyleWord(true);
			helpText.setPreferredSize(new Dimension(300, 300));
			helpText.setLineWrap(true);
			constraint.gridy = 0;
			m_stateGUI[HelpState].add(helpText, constraint);
			
			JButton goBackButton = new JButton("Go back");
			goBackButton.addActionListener(new OnPrevious());
			goBackButton.setPreferredSize(new Dimension(100, 50));
			goBackButton.setAlignmentX(CENTER_ALIGNMENT);
			constraint.gridy = 1;
			m_stateGUI[HelpState].add(goBackButton, constraint);
		}
		
		{ // ============== CORE MENU ============== 
			GridBagConstraints constraint = new GridBagConstraints();
			m_stateGUI[CoreShopState] = new JPanel();
			JPanel panel = m_stateGUI[CoreShopState];
			
			panel.setPreferredSize(new Dimension(w / 2, h / 2));
			panel.setBackground(Color.YELLOW);
			panel.setVisible(false);		
			panel.setBounds(0, 0, w, h);
			panel.setLayout(new GridBagLayout());
			overlayPane.add(panel, JLayeredPane.MODAL_LAYER);
			
			JButton goBackButton = new JButton("Go back");
			goBackButton.addActionListener(new OnPlay());
			goBackButton.setPreferredSize(new Dimension(100, 50));
			goBackButton.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(goBackButton, constraint);
		}
		
		{ // ============== LOSE MENU ============== 
			GridBagConstraints constraint = new GridBagConstraints();
			m_stateGUI[LoseState] = new JPanel();
			JPanel panel = m_stateGUI[LoseState];
			
			panel.setPreferredSize(new Dimension(w / 2, h / 2));
			panel.setBackground(Color.YELLOW);
			panel.setVisible(false);		
			panel.setBounds(0, 0, w, h);
			panel.setLayout(new GridBagLayout());
			overlayPane.add(panel, JLayeredPane.MODAL_LAYER);
			
			m_lastScoreLabel = new JLabel("You Lost");
			m_lastScoreLabel.setPreferredSize(new Dimension(500, 50));
			m_lastScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(m_lastScoreLabel, constraint);
			
			JButton restartButton = new JButton("Restart?");
			restartButton.addActionListener(new OnRestart());
			restartButton.setPreferredSize(new Dimension(100, 50));
			restartButton.setAlignmentX(CENTER_ALIGNMENT);
			constraint.gridy = 1;
			panel.add(restartButton, constraint);
		}
		
		m_drawLock.lock();
		m_tilemap = m_world.create();
		{
			Random random = new Random();
			TransformComponent transform = new TransformComponent();
			transform.pos = new Vec2D(1100.0f, 325.0f);
			m_tilemap.add(transform);
			TilemapComponent tm = new TilemapComponent();
			for(int i = -55; i <= 15; i += 1)
				for(int j = -17; j <= 17; j += 1) {
					if(i == 0 && j == 0)
						continue;
					else if((i >= 14) || (j <= -14 || j >= 14)) {
						Entity e = m_world.create();
						SpriteComponent sprite = new SpriteComponent();
						sprite.image = ImageLoader.createImage("images/stone.png");
						e.add(sprite);
						e.add(new UnRemovableTag());
						
						tm.getTopTilemap().insert(m_physicsWorld, transform, new IVec2D(i, j), new IVec2D(1, 1), e, true);
					} else {
						Entity e = m_world.create();
						e.add(new ArrowComponent());
						SpriteComponent sprite = new SpriteComponent();
						sprite.image = ImageLoader.createImage("images/terrain" + (random.nextInt(4) + 1) + ".png");
						e.add(sprite);
						
						tm.getBottomTilemap().insert(m_physicsWorld, transform, new IVec2D(i, j), new IVec2D(1, 1), e, false);
					}
						
				}
			m_tilemap.add(tm);
			
			m_core = m_world.create();
			m_core.add(new CoreComponent());
			SpriteComponent sprite = new SpriteComponent();
			sprite.image = ImageLoader.createImage("images/core.png");
			m_core.add(sprite);
			m_core.add(new ArrowComponent());
			m_core.add(new MainHealthTag());
			m_core.add(new UnRemovableTag());
			
			CoreComponent store = m_core.get(CoreComponent.class);
			store.addItem(200, new BigBoom());
			store.addItem(75, new AK16());
			store.addItem(10, new StonePlacable(m_world));
			store.addItem(5, new WoodPlacable(m_world));
			store.addItem(50, new HeavyMetalPlacable(m_world));
			m_coreCoords = new IVec2D(0, 0);
			tm.getTopTilemap().insert(m_physicsWorld, transform, m_coreCoords, new IVec2D(1, 1), m_core, true);
		}
		
		{
			m_player = m_world.create();
			m_player.add(new MainHealthTag());
			TransformComponent transform = new TransformComponent();
			transform.pos = new Vec2D(400.0f, 300.0f);
			m_player.add(transform);
			PlayerComponent playerComp = new PlayerComponent();
			playerComp.moveSpeed = 5.0f;
			m_player.add(playerComp);
			m_window.addKeyListener(playerComp);
			CircleShapeComponent circleShape = new CircleShapeComponent();
			circleShape.color = Color.GREEN;
			circleShape.radius = 10.0f;
			m_player.add(circleShape);
			RigidBody rb = m_physicsWorld.create(transform.pos, 0, false);
			rb.setLinearDampening(10);
			rb.setSelfMask(PlayerMask);
			rb.setCollMask(~(PlayerMask | BulletMask));
			rb.attachFixture(new Circle(5.0f, 10.0f));
			rb.setUserData(m_player);
			m_player.add(new RigidBodyComponent(rb));
		}
		
		m_pathfindingSystem = new PathfindingSystem(m_world, m_tilemap, m_player);
		
		m_window.add(overlayPane);
		m_window.setSize(w, h);
		m_window.setVisible(true);
		setSize(new Dimension(w, h));
		
		m_hotbar[0].setItem(new LilGun());
		
		m_spawner = new EnemySpawner(m_world, m_physicsWorld);
		
		m_health.set(300);
		
		m_drawLock.unlock();
	}
	
	private void updateGUI() {
		for(int i = 0; i < m_stateGUI.length; i++) {
			boolean isEnabled = false;
			if(m_currentState == i)
				isEnabled = true;
			
			m_stateGUI[i].setVisible(isEnabled);
			m_stateGUI[i].setEnabled(isEnabled);
		}
		m_window.repaint();
	}
	
	private boolean trySpawnBullet(Weapon weapon, float deltaTime) {		
		if(!weapon.isReadyToFire(deltaTime))
			return false;
		
        Vec2D dir = m_mousePos.clone().sub(m_player.get(TransformComponent.class).pos).normal();
		Entity e = m_world.create();
		e.add(new TransformComponent());
		TransformComponent transform = e.get(TransformComponent.class);
		transform.pos = m_player.get(TransformComponent.class).pos.clone().add(dir.scale(5.0f));
		
		
		BulletComponent bullet = new BulletComponent();
		bullet.hitsLeft = weapon.getMaxHits();
		e.add(bullet);
		e.add(new LifetimeComponent());
		
		RigidBody rb = m_physicsWorld.create(transform.pos, 0, false);
		rb.attachFixture(new Circle(5.0f, 5.0f));
		rb.applyLinearVelocity(new Vec2D(weapon.getMuzzleVelocity(), 0.0f).rotate(dir.angle()));
		rb.setSelfMask(BulletMask);
		rb.setCollMask(~BulletMask);
		rb.setUserData(e);
		e.add(new RigidBodyComponent(rb));
		
		CircleShapeComponent circleShape = new CircleShapeComponent();
		circleShape.radius = weapon.getBulletRadius();
		circleShape.color = Color.MAGENTA;
		e.add(circleShape);
		
		return true;
	}
	
	private boolean tryPlace(Placable place) {					
		Tilemap tilemap = m_tilemap.get(TilemapComponent.class).getTopTilemap();
		TransformComponent transform = m_tilemap.get(TransformComponent.class);
		IVec2D pos = Tilemap.getNearestTile(m_tilemap.get(TransformComponent.class).getLocalPoint(m_mousePos.clone()));
		
		if(tilemap.canPlace(pos, place.getDim()) || 
		   tilemap.contains(pos) && tilemap.find(pos).entity != null && !tilemap.find(pos).entity.has(UnRemovableTag.class)) {
			tilemap.eraseAll(pos, place.getDim());
			Entity e = ((Entity)place.getPrefab()).clone();
			e.add(new UnRemovableTag());
			
			tilemap.insert(m_physicsWorld, transform, pos, place.getDim(), e, place.isCollidable());
			return true;
		}
		
		return false;
	}
	
	private void playTick(int tick, float deltaTime) {
		int playerSlot = m_player.get(PlayerComponent.class).slot;
		if(playerSlot != m_curSlot) {
			m_hotbar[m_curSlot].getButton().setBackground(Color.WHITE);
			m_curSlot = playerSlot;
			m_hotbar[m_curSlot].getButton().setBackground(Color.YELLOW);
		}
		
		Slot slot = m_hotbar[m_curSlot];
		Item item = slot.getItem();
		if(item != null && m_isMousePressed) {
			boolean itemUsed = false;
			if(item instanceof Weapon)
				itemUsed = trySpawnBullet((Weapon)item, deltaTime);
			else if(item instanceof Placable)
				itemUsed = tryPlace((Placable)item);
				
			
			if(itemUsed) {
				slot.used();
				
				if(slot.shouldRemove())
					slot.setItem(null);
			}
		}
		
		m_world.beginDefer();
		for(CollisionEvent event : m_physicsWorld.getCollisionEvents()) {
			Entity eA = (Entity)event.bodyA.getUserData();
			Entity eB = (Entity)event.bodyB.getUserData();
			if((eA == null || !eA.isAlive()) || (eB == null || !eB.isAlive()))
				continue;
			
			if(eA.has(BulletComponent.class) && eB.has(EnemyTag.class) || 
			   eB.has(BulletComponent.class) && eA.has(EnemyTag.class)) {
				if(eA.has(BulletComponent.class)) {
					BulletComponent bullet = eA.get(BulletComponent.class);
					bullet.hitsLeft--;
					if(bullet.hitsLeft < 0)
						m_world.destroy(eA.id());
				} else if(eA.has(EnemyTag.class)) {
					m_world.destroy(eA.id());
					m_score.addAndGet(1);
				}
				
				if(eB.has(BulletComponent.class)) {
					BulletComponent bullet = eB.get(BulletComponent.class);
					bullet.hitsLeft--;
					if(bullet.hitsLeft < 0)
						m_world.destroy(eB.id());
				} else if(eB.has(EnemyTag.class)) {
					m_world.destroy(eB.id());
					m_score.addAndGet(1);
				}
			}
			
			if(eA.has(MainHealthTag.class) && eB.has(EnemyTag.class) || 
			   eB.has(MainHealthTag.class) && eA.has(EnemyTag.class)) {
				m_health.getAndAdd(-1);
			}
			
			if(eA.has(HealthComponent.class) && eB.has(EnemyTag.class) || 
			   eB.has(HealthComponent.class) && eA.has(EnemyTag.class)) {
				if(eA.has(HealthComponent.class)) {
					HealthComponent health = eA.get(HealthComponent.class);
					health.health--;
					
				}
				
				if(eB.has(HealthComponent.class)) {
					HealthComponent health = eB.get(HealthComponent.class);
					health.health--;
				}
			}
		}
		m_world.endDefer();
		
		m_scoreLabel.setText("Score " + m_score);
		m_healthLabel.setText("Health " + m_health);
		m_lastScoreLabel.setText("Final Score " + m_score);
		
		m_spawner.update(tick);
	}
	
	private void tick(int tick, float deltaTime) {
		switch(m_currentState) {
		case PlayState:
			m_physicsSystem.run(tick, deltaTime);
			m_tilemapRenderSystem.run(tick, deltaTime);
			m_playerSystem.run(tick, deltaTime);
			m_pathfindingSystem.run(tick, deltaTime);
			m_deathSystem.run(tick, deltaTime);
			m_healthSystem.run(tick, deltaTime);
			playTick(tick, deltaTime);
			break;
		}
	}
	
	private void update(float deltaTime) {
		repaint();
		
		if(m_health.get() < 0 && m_currentState != LoseState) {
			changeState(LoseState);
		}
	}
	
	/**
	 * Runs the game, running at the the targetTicksPer second.
	 * @return an error code, 0 meaning no error. */
	public int run() {
		float deltaTime = 0;
		float last = timeNowSeconds();
		float ticksToGo = 0;
		
		float tickDeltaTime = 0;
		float tickLast = timeNowSeconds();
		int tickNum = 0;
		
		while(!m_exit && m_window.isActive()) {
			float now = timeNowSeconds();
			deltaTime = now - last;
			ticksToGo += deltaTime * m_targetTicks;
			last = now;
			
			for(; ticksToGo > 1; ticksToGo--) {
				float tickNow = timeNowSeconds();
				tickDeltaTime = tickNow - tickLast;
				tickLast = tickNow;
				
				m_drawLock.lock();
				tick(tickNum, tickDeltaTime);
				m_drawLock.unlock();
				
				tickNum++;
			}
			
			update(deltaTime);
		}
		
		m_window.dispose();
		
		return 0;
	}
	
	/**
	 * Paints the window and game data.
	 * CALLED ASYNC, DO NOT INVOKE DIRECTLY 
	 * @param gfx the target to draw to. */
	@Override
	public void paintComponent(Graphics gfx) {
		Graphics2D gfx2d = (Graphics2D)gfx;
		
		Dimension winDim = m_window.getSize();
		gfx2d.setBackground(Color.BLACK);
		gfx2d.clearRect(0, 0, (int)winDim.getWidth(), (int)winDim.getHeight());
		
		m_drawLock.lock();
		if(m_currentState == PlayState && m_world != null) {
			//m_physicsWorld.draw(gfx2d);
			m_tilemapRenderSystem.draw(gfx2d);
			m_circleRenderSystem.draw(gfx2d);
		}
		
		gfx.setColor(Color.WHITE);
		gfx.fillOval((int)m_mousePos.x - 2, (int)m_mousePos.y - 2, 4, 4);
		m_drawLock.unlock();
	}
	
	/**
	 * @return When the run() method is returned, should the app be recreated and re ran? */
	public boolean shouldRecreate() {
		return m_shouldRecreate;
	}
	
	private static float timeNowSeconds() {
		return (float)System.nanoTime() / 1_000_000_000.0f;
	}
}