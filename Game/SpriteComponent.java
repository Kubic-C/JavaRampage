package Game;

import java.awt.Image;

/**
 * A SpriteComponent retains an image.
 * */
public class SpriteComponent extends Component {
    public Image image;

    /**
     * Retrieves the type of the component (SpriteComponent class).
     * 
     * @return The class type of the component.
     */
    @Override
    public Class<?> getType() {
        return SpriteComponent.class;
    }

    /**
     * Clones the current SpriteComponent, including a copy of the image.
     * 
     * @return A new instance of SpriteComponent with the same image.
     */
    @Override
    public Component clone() {
        SpriteComponent clone = new SpriteComponent();
        clone.image = (image != null) ? image : null;  // Cloning image if it's not null
        return clone;
    }
}