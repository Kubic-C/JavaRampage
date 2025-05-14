package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The PlayerComponent class handles the player's input and movement in the game.
 * It listens for key events to move the player and manage actions like switching slots.
 */
public class PlayerComponent extends Component implements KeyListener {
    public boolean goLeft, goRight, goUp, goDown = false;
    public float moveSpeed = 1000.0f;
    public int slot = 0;
    public int points;

    /**
     * Called when a key is typed (but not used in this implementation).
     * 
     * @param e The KeyEvent triggered by the key press.
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Called when a key is pressed. Updates the player's movement flags
     * and switches slots based on the pressed key.
     *
     * @param e The KeyEvent triggered by the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A)
            goLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_D)
            goRight = true;
        if (e.getKeyCode() == KeyEvent.VK_W)
            goUp = true;
        if (e.getKeyCode() == KeyEvent.VK_S)
            goDown = true;

        if (e.getKeyCode() == KeyEvent.VK_1)
            slot = 0;
        if (e.getKeyCode() == KeyEvent.VK_2)
            slot = 1;
        if (e.getKeyCode() == KeyEvent.VK_3)
            slot = 2;
        if (e.getKeyCode() == KeyEvent.VK_4)
            slot = 3;
        if (e.getKeyCode() == KeyEvent.VK_5)
            slot = 4;
        if (e.getKeyCode() == KeyEvent.VK_6)
            slot = 5;
    }

    /**
     * Called when a key is released. Updates the player's movement flags
     * to stop the player from moving in the corresponding direction.
     *
     * @param e The KeyEvent triggered by the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A)
            goLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_D)
            goRight = false;
        if (e.getKeyCode() == KeyEvent.VK_W)
            goUp = false;
        if (e.getKeyCode() == KeyEvent.VK_S)
            goDown = false;
    }

    /**
     * Returns the type of this component, which is the PlayerComponent class.
     *
     * @return The PlayerComponent class type.
     */
    @Override
    public Class<?> getType() {
        return PlayerComponent.class;
    }

    /**
     * Clones the current PlayerComponent and returns a copy with the same moveSpeed.
     *
     * @return A new instance of PlayerComponent with the same moveSpeed.
     */
    @Override
    public Component clone() {
        PlayerComponent clone = new PlayerComponent();
        clone.moveSpeed = moveSpeed;
        return clone;
    }
}