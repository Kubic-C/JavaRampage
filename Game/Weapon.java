package Game;

import javax.swing.Icon;

/**
 * Represents a weapon item in the game, extending from the Item class.
 * This class manages properties such as fire rate, bullet radius, maximum hits,
 * and muzzle velocity, along with functionality to check if the weapon is ready to fire.
 */
public class Weapon extends Item {
    private float m_fireRate = 1.0f; // Fire rate in shots per second
    private float m_timeTillNextShot = 0.0f; // Time remaining until the next shot can be fired
    private float m_bulletRadius = 0.1f; // Radius of the bullet
    private int m_maxHits; // Maximum number of hits before the weapon's effectiveness drops
    private float m_muzzleVelocity; // Muzzle velocity of the bullet (how fast it travels)

    /**
     * Constructor to create a weapon item with specified properties.
     *
     * @param icon The icon representing the weapon in the user interface.
     * @param defaultUses The default number of uses the weapon has.
     * @param fireRate The fire rate of the weapon (shots per second).
     * @param bulletRadius The radius of the bullet fired by the weapon.
     * @param maxHits The maximum number of hits the projectile eject can sustain before being destroyed
     * @param muzzleVelocity The speed at which the bullet is fired.
     */
    public Weapon(Icon icon, int defaultUses, float fireRate, float bulletRadius, int maxHits, float muzzleVelocity) {
        super(icon, Item.HoldableType, null, defaultUses);
        m_fireRate = fireRate;
        m_bulletRadius = bulletRadius;
        m_maxHits = maxHits;
        m_muzzleVelocity = muzzleVelocity;
    }

    /**
     * Gets the radius of the bullet fired by the weapon.
     *
     * @return The radius of the bullet.
     */
    float getBulletRadius() {
        return m_bulletRadius;
    }

    /**
     * Checks if the weapon is ready to fire based on the time passed.
     *
     * @param dt The delta time (time elapsed since the last check) in seconds.
     * @return true if the weapon is ready to fire, false otherwise.
     */
    boolean isReadyToFire(float dt) {
        m_timeTillNextShot -= dt;
        if(m_timeTillNextShot < 0) {
            m_timeTillNextShot = m_fireRate;
            return true;
        }

        return false;
    }

    /**
     * Gets the maximum number of hits the projectile eject can sustain before being destroyed
     *
     * @return The maximum number of hits.
     */
    public int getMaxHits() {
        return m_maxHits;
    }

    /**
     * Gets the muzzle velocity of the weapon (how fast the bullet travels).
     *
     * @return The muzzle velocity in units per second.
     */
    public float getMuzzleVelocity() {
        return m_muzzleVelocity;
    }
}