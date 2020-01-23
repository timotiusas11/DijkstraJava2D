
public class Tile {
	
	int x, y, cost;
	boolean isVisited;
	String map;
	Tile parent;

	public Tile(int x, int y, Tile parent, int cost, boolean isVisited, String map) {
		super();
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.cost = cost;
		this.isVisited = isVisited;
		this.map = map;
	}
	
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public Tile() {
		// TODO Auto-generated constructor stub
	}

}
