package Game;

/**
 * Used to track entities that want to be moved by the pathfinding system.
 */
public class SeekerComponent extends Component {

    /**
     * Returns the class type of this component.
     * 
     * @return The class type of SeekerComponent.
     */
    @Override
    public Class<?> getType() {
        return SeekerComponent.class;
    }

    /**
     * Clones this component by creating a new instance of SeekerComponent.
     * 
     * @return A new instance of SeekerComponent.
     */
    @Override
    public Component clone() {
        return new SeekerComponent();
    }

}