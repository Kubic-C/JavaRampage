package Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision detection and resolution between rigid bodies.
 * Supports different fixture types (circle, rectangle) and uses
 * Separating Axis Theorem (SAT) for collision detection.
 */
public class CollisionSolver {
	public static class CollisionManifold {
		float depth;
		Vec2D normal;
		Vec2D points[];
	}
	
	class MinMax {
		float min;
		float max;
	};
	
	/**
	 * Projects the points onto the given normal and returns the minimum and maximum values.
	 *
	 * @param normal The normal vector to project the points onto.
	 * @param points The points to be projected onto the normal.
	 * @return A MinMax object containing the minimum and maximum projection values.
	 */
	public MinMax project(Vec2D normal, Vec2D[] points) {
		MinMax minMax = new MinMax();
		minMax.min = minMax.max = normal.dot(points[0]);
		
		for(int i = 1; i < points.length; i++) {
			float val = normal.dot(points[i]);
			
			if(val < minMax.min) {
				minMax.min = val;
			}
			if(val > minMax.max) {
				minMax.max = val;
			}
		}
		
		return minMax; 
	}
	
	/**
	 * Checks for a collision using the Separating Axis Theorem (SAT).
	 * 
	 * @param manifold The collision manifold to store the results of the collision.
	 * @param normals The array of normals to test.
	 * @param aPoints The points of body A.
	 * @param bPoints The points of body B.
	 * @return True if there is a collision, false otherwise.
	 */
	public boolean separatingAxisTheoremTest(CollisionManifold manifold, Vec2D normals[], Vec2D aPoints[], Vec2D bPoints[]) {
		manifold.depth = Float.MAX_VALUE;
		for(int i = 0; i < normals.length; i++) {
			Vec2D normal = normals[i];
			
			MinMax minMax1 = project(normal, aPoints);
			MinMax minMax2 = project(normal, bPoints);
			
			if(minMax1.max >= minMax2.min && minMax2.max >= minMax1.min) {
				float maxSet = Math.min(minMax1.max, minMax2.max);
				float minSet = Math.max(minMax1.min, minMax2.min);
				float o = Math.max(0, maxSet - minSet);
				
				if(o <= manifold.depth) {
					manifold.depth = o;
					manifold.normal = normal;
				
					float dir = minMax1.max - minMax2.max;
					if(dir < 0) {
						manifold.normal.scale(-1);
					}
				}
				
			} else
				return false;
		}
		
		return true;
	}
	
	
	class PointDistance {
		Vec2D vertex;
		Vec2D point;
		float dist;
		
		PointDistance(Vec2D vertex, Vec2D point, float dist) {
			this.vertex = vertex;
			this.point = point;
			this.dist = dist;
		}
	}
	
	/**
	 * Calculates the closest point on a line segment defined by two points (a and b) to a vertex.
	 * 
	 * @param vertex The vertex to project onto the segment.
	 * @param a The first point of the segment.
	 * @param b The second point of the segment.
	 * @return A PointDistance object containing the closest point and distance.
	 */
	PointDistance pointSegmentDistance(Vec2D vertex, Vec2D a, Vec2D b) {
		Vec2D v_to_a = vertex.clone().sub(a);
		Vec2D a_to_b = b.clone().sub(a);
		float p = v_to_a.dot(a_to_b);
		float l2 = a_to_b.length2();
		float d = p / l2;
		
		Vec2D cp = null;
		
		if(d <= 0)
			cp = a.clone();
		else if(d >= 1)
			cp = b.clone();
		else
			cp = a.clone().add(a_to_b.scale(d));
		
		float dist = vertex.clone().sub(cp).length();
		return new PointDistance(vertex, cp, dist);
	}
	
	/**
	 * Checks if two floating-point values are nearly equal within a specified maximum difference.
	 * 
	 * @param a The first value.
	 * @param b The second value.
	 * @param maxima The maximum allowable difference between the two values.
	 * @return True if the values are nearly equal, false otherwise.
	 */
	public boolean nearlyEqual(float a, float b, float maxima) {
		return Math.abs(a - b) <= maxima;
	}
	
	/**
	 * Checks if two Vec2D objects are nearly equal within a specified maximum difference.
	 * 
	 * @param a The first vector.
	 * @param b The second vector.
	 * @param maxima The maximum allowable difference between the two vectors.
	 * @return True if the vectors are nearly equal, false otherwise.
	 */
	public boolean nearlyEqual(Vec2D a, Vec2D b, float maxima) {
		return nearlyEqual(a.x, b.x, maxima) && nearlyEqual(a.y, b.y, maxima);
	}
	
	/**
	 * Detects a collision between two rigid bodies based on their fixtures.
	 * 
	 * @param manifold The collision manifold to store the collision result.
	 * @param A The first rigid body.
	 * @param B The second rigid body.
	 * @return True if a collision exists, false otherwise.
	 */
	public boolean doesCollisionExist(CollisionManifold manifold, 
			RigidBody A, Circle fA, 
			RigidBody B, Circle fB) {
		Vec2D aPos = A.getPos();
		Vec2D bPos = B.getPos();
		float dist = aPos.clone().sub(bPos).length();
		float radii = (fA.getRadius() + fB.getRadius());
		
		if(dist <= radii) {
			manifold.depth = radii - dist;
			manifold.normal = aPos.clone().sub(bPos).normal();
			if(aPos.x == bPos.x && 
			   aPos.y == bPos.y) {
				manifold.normal = new Vec2D(1.0f, 0.0f);
				manifold.depth = radii;
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Detects a collision between two rigid bodies based on their fixtures.
	 * 
	 * @param manifold The collision manifold to store the collision result.
	 * @param A The first rigid body.
	 * @param B The second rigid body.
	 * @return True if a collision exists, false otherwise.
	 */
	public boolean doesCollisionExist(CollisionManifold manifold, 
			RigidBody A, Rectangle fA, 
			RigidBody B, Circle fB) {
		Vec2D aPos = A.getPos();
		Vec2D bPos = B.getPos();
		float bR = fB.getRadius();
			
		Vec2D normals[] = new Vec2D[1];
		normals[0] = bPos.clone().sub(aPos).normal();

		Vec2D aPoints[] = fA.getPoints(A.getPos(), A.getRot());
		Vec2D bPoints[] = {
			new Vec2D(-bR, bR),
			new Vec2D( bR, bR)
		};
		
		for(int i = 0; i < 2; i++)
			bPoints[i].rotate(normals[0].angle()).add(bPos);

		if(!separatingAxisTheoremTest(manifold, normals, aPoints, bPoints))
			return false;
		
		return true;
	}

	/**
	 * Detects a collision between two rigid bodies based on their fixtures.
	 * 
	 * @param manifold The collision manifold to store the collision result.
	 * @param A The first rigid body.
	 * @param B The second rigid body.
	 * @return True if a collision exists, false otherwise.
	 */
	public boolean doesCollisionExist(CollisionManifold manifold, 
			RigidBody A, Rectangle fA, 
			RigidBody B, Rectangle fB) {
		Vec2D defNormals[] = {
				new Vec2D(1.0f, 0.0f),
				new Vec2D(0.0f, 1.0f)
		};		
		Vec2D normals[] = new Vec2D[4];
		Vec2D aPoints[] = fA.getPoints(A.getPos(), A.getRot());
		Vec2D bPoints[] = fB.getPoints(B.getPos(), B.getRot());
		
		for(int i = 0; i < 2; i++)
			normals[i] = defNormals[i].clone().rotate(A.getRot());
		for(int i = 2; i < 4; i++)
			normals[i] = defNormals[i - 2].clone().rotate(B.getRot());
		
		return separatingAxisTheoremTest(manifold, normals, aPoints, bPoints);
	}
	
	/**
	 * Detects a collision between two rigid bodies based on their fixtures.
	 * 
	 * @param manifold The collision manifold to store the collision result.
	 * @param A The first rigid body.
	 * @param B The second rigid body.
	 * @return True if a collision exists, false otherwise.
	 */
	public boolean detectCollision(CollisionManifold manifold, RigidBody A, RigidBody B) {
		switch(A.getFixture().getType()) {
		case Fixture.CIRCLE_FIXTURE:
			switch(B.getFixture().getType()) {
			case Fixture.CIRCLE_FIXTURE:
				return doesCollisionExist(manifold, A, (Circle)A.getFixture(), B, (Circle)B.getFixture());
			case Fixture.RECTANGLE_FIXTURE:
				boolean ret = doesCollisionExist(manifold, B, (Rectangle)B.getFixture(), A, (Circle)A.getFixture());
				if(ret)
					manifold.normal.scale(-1.0f);
				
				return ret;
			}
			break;
		case Fixture.RECTANGLE_FIXTURE:
			switch(B.getFixture().getType()) {
			case Fixture.CIRCLE_FIXTURE:
				return doesCollisionExist(manifold, A, (Rectangle)A.getFixture(), B, (Circle)B.getFixture());
			case Fixture.RECTANGLE_FIXTURE:
				return doesCollisionExist(manifold, A, (Rectangle)A.getFixture(), B, (Rectangle)B.getFixture());
			}
			break;
		}
		
		return false;
	}
	
	/**
	 * Resolves the collision between two rigid bodies by applying inverse-mass-weighted movement.
	 * Handles static and dynamic bodies.
	 * 
	 * @param manifold The collision manifold containing collision data.
	 * @param A The first rigid body.
	 * @param B The second rigid body.
	 * @return True if the collision was resolved, false otherwise.
	 */
	public boolean resolveCollisionIIE(CollisionManifold manifold, RigidBody A, RigidBody B) {
		if(A.isStatic() && B.isStatic())
			return false;
		
		if(!detectCollision(manifold, A, B))
			return false;
		
		float totalMass = A.getMass() + B.getMass();

		// Handle static cases as before
		if (A.isStatic()) {
		    B.move(manifold.normal.clone().scale(-manifold.depth));
		} else if (B.isStatic()) {
		    A.move(manifold.normal.clone().scale(manifold.depth));
		} else {
		    // Inverse-mass-weighted movement
		    float aRatio = B.getMass() / totalMass;
		    float bRatio = A.getMass() / totalMass;

		    Vec2D correction = manifold.normal.clone().scale(manifold.depth);
		    
		    A.move(correction.clone().scale(aRatio));
		    B.move(correction.clone().scale(-bRatio));
		}
		
		return true;
	}
}