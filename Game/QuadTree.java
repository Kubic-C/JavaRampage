package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * QuadTree is a spatial data structure that divides the 2D space into quadrants (nodes) 
 * to efficiently store and query objects with axis-aligned bounding boxes (AABBs).
 * It is useful for scenarios where you need to manage a large number of objects
 * and perform fast intersection queries.
 */
public class QuadTree {
	private static final int MaxElementsInNodes = 6;
	private static final int MaxLevels = 8;	
	
	private TreeNode root;
	
	private class TreeNode {
		int level;
		AABB aabb;
		List<TreeElement> contains;
		TreeNode[] children;
	
		TreeNode(int level, int x, int y, int w, int h) {
			this(level, new AABB(x, y, w, h));
		}
		
		TreeNode(int level, AABB aabb) {
			this.aabb = aabb;
			this.level = level;
			
			contains = new ArrayList<TreeElement>();
			children = new TreeNode[4];
		}
		
		public void clear() {
			contains = new ArrayList<TreeElement>();
			children = new TreeNode[4];
		}
		
		public void draw(Graphics2D gfx) {
			gfx.setColor(Color.RED);
			if(contains != null) {
				gfx.drawString("" + contains.size(), aabb.bl.x, aabb.tr.y);
				
				for(int i = 0; i < contains.size(); i++) {
					contains.get(i).getAABB().draw(gfx);
				}
			}
			
			gfx.setColor(Color.BLUE);
			aabb.draw(gfx);
			
			for(int i = 0; i < 4; i++) {
				if(children[i] == null)
					continue;
				
				children[i].draw(gfx);
			}
		}
		
		public void add(TreeElement ele) {
			if(contains == null) {
				insertInLeafNodes(ele);
			} else if(contains.size() + 1 > MaxElementsInNodes && 
			   level + 1 <= MaxLevels) {
				insertInLeafNodes(ele);

				for(int i = 0; i < contains.size(); i ++) {
					insertInLeafNodes(contains.get(i));
				}
				
				contains = null;
			} else {
				contains.add(ele);				
			}
		}
		
		public void remove(TreeElement ele) {
			if(contains == null) {
				eraseInLeafNodes(ele);
			} else {
				contains.remove(ele);
			}
		
		}
		
		public void getIntersecting(List<TreeElement> intersecting, TreeElement e) {
			if(contains != null) {
				for(int i = 0; i < contains.size(); i++) {
					TreeElement other = contains.get(i);
					
					if(!other.hasQueried() && 
						other.getAABB().intersects(e.getAABB())) {
						other.setQueriedStatus(true);
						intersecting.add(other);
					}
				}
				
				return;
			}
			
			for(int i = 0; i < 4; i++) {
				if(children[i] == null || 
				   !e.getAABB().intersects(getChildAABB(i)))
					continue;

				children[i].getIntersecting(intersecting, e);					
			}
		}
		
		private void insertInLeafNodes(TreeElement e) {
			for(int i = 0; i < 4; i++) {
				AABB childAABB = getChildAABB(i);
				
				if(!e.getAABB().intersects(childAABB))
					continue;
				if(children[i] == null)
					children[i] = new TreeNode(level + 1, childAABB);
				
				children[i].add(e);
			}
		}
		
		private void eraseInLeafNodes(TreeElement e) {
			for(int i = 0; i < 4; i++) {
				AABB childAABB = getChildAABB(i);
				
				if(!e.getAABB().intersects(childAABB))
					continue;
				if(children[i] == null)
					continue;
				
				children[i].remove(e);
			}
		}
		
		
		private AABB getChildAABB(int nodeIndex) {
			float x = aabb.bl.x;
			float y = aabb.tr.y;
			float hw = Math.abs(aabb.bl.x - aabb.tr.x) / 2;
			float hh = Math.abs(aabb.bl.y - aabb.tr.y) / 2;
			
			switch(nodeIndex) {
			case 0:
				return new AABB(x, y, hw, hh); 
			case 1:
				return new AABB(x + hw, y, hw, hh); 
			case 2:
				return new AABB(x, y + hh, hw, hh); 
			case 3:
				return new AABB(x + hw, y + hh, hw, hh); 
			default:
				assert(false);
				return new AABB(0, 0, 0, 0);
			}
		}
	}
	
	/**
     * Constructs a QuadTree with a given width and height.
     * 
     * @param w The width of the area to be managed by the QuadTree.
     * @param h The height of the area to be managed by the QuadTree.
     */
    public QuadTree(int w, int h) {
        root = new TreeNode(0, 0, 0, w, h);
    }

    /**
     * Finds all elements that intersect with a given element.
     * 
     * @param e The element for which to find intersecting elements.
     * @return A list of TreeElement objects that intersect with the given element.
     */
    public List<TreeElement> intersecting(TreeElement e) {
        List<TreeElement> arr = new ArrayList<TreeElement>();
        
        root.getIntersecting(arr, e);
        for (int i = 0; i < arr.size(); i++) {
            arr.get(i).setQueriedStatus(false);
        }
        
        return arr;
    }

    /**
     * Adds an element to the QuadTree.
     * 
     * @param e The element to be added to the QuadTree.
     */
    public void add(TreeElement e) {
        root.add(e);
    }

    /**
     * Removes an element from the QuadTree.
     * 
     * @param e The element to be removed from the QuadTree.
     */
    public void remove(TreeElement e) {
        root.remove(e);
    }

    /**
     * Draws the QuadTree structure (including nodes and contained elements) onto the 
     * provided Graphics2D object.
     * 
     * @param gfx The Graphics2D object onto which the QuadTree will be drawn.
     */
    public void draw(Graphics2D gfx) {
        root.draw(gfx);
    }

    /**
     * Clears the QuadTree, removing all elements and resetting the structure.
     */
    public void clear() {
        root.clear();
    }
}