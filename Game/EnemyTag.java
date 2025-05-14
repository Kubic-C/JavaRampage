package Game;

/**
 * EnemyTag is a marker component used to tag entities as enemies in the game world.
 * This component doesn't hold any data itself but can be used to identify enemy entities.
 */
public class EnemyTag extends Component {
    
    /**
     * Returns the type of this component.
     * 
     * @return the class type of this component, which is EnemyTag.class
     */
    @Override
    public Class<?> getType() {
        return EnemyTag.class;
    }

    /**
     * Creates a new instance of the EnemyTag component.
     * 
     * @return a new EnemyTag instance
     */
    @Override
    public Component clone() {
        return new EnemyTag();
    }
}