package Game;

class Square extends TreeElement {
	public Vec2D pos;
	Vec2D size;
	
	// TL oriented AABB
	public Square(float x, float y, float w, float h) {
		this(new Vec2D(x, y), new Vec2D(w, h));
	}
	
	public Square(Vec2D p, Vec2D s) {
		pos = p;
		size = s;
	}
	
	@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return new AABB(pos.x, pos.y, size.x, size.y);
	}	
}