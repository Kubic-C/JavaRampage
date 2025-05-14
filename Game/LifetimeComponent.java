package Game;

/**
 * The LifetimeComponent class represents a component that tracks the remaining lifetime of an entity.
 */
public class LifetimeComponent extends Component {
    public float timeLeft = 1.0f;

    /**
     * Returns the type of the component (LifetimeComponent).
     * 
     * @return The class type of this component.
     */
    @Override
    public Class<?> getType() {
        return LifetimeComponent.class;
    }

    /**
     * Creates and returns a copy of this LifetimeComponent.
     * 
     * @return A new LifetimeComponent object that is a clone of this one.
     */
    @Override
    public Component clone() {
        LifetimeComponent clone = new LifetimeComponent();
        clone.timeLeft = timeLeft;
        return clone;
    }
}