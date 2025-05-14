package Game;

/**
 * Represents the health component of an entity.
 */
public class HealthComponent extends Component {
    public int health = 50;

    /**
     * Gets the type of the component.
     * 
     * @return the class type of the HealthComponent
     */
    @Override
    public Class<?> getType() {
        return HealthComponent.class;
    }

    /**
     * Clones the current HealthComponent.
     * 
     * This method creates a new instance of the HealthComponent, 
     * copying the health value from the current component to the new one.
     * 
     * @return a new instance of HealthComponent with the same health value
     */
    @Override
    public Component clone() {
        HealthComponent clone = new HealthComponent();
        clone.health = health;
        return clone;
    }
}