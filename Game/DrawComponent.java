package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.locks.Lock;

import javax.swing.JComponent;

public class DrawComponent extends JComponent {
	private PhysicsWorld m_world;
	private Lock m_drawLock;
	
	public DrawComponent(PhysicsWorld tree, Lock drawLock) {
		m_world = tree;
		m_drawLock = drawLock;
	}
	
	public void setDebugDrawTree(PhysicsWorld tree) {
		m_world = tree;
	}
	
	@Override
	public void paint(Graphics gfx) {
		Graphics2D gfx2d = (Graphics2D)gfx;
		
		gfx2d.setBackground(Color.BLACK);
		gfx2d.clearRect(0, 0, 800, 600);
		
		m_drawLock.lock();
		if(m_world != null) {
			m_world.draw(gfx2d);
		}
		m_drawLock.unlock();
	}
}