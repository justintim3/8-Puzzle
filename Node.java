public class Node {
	private String state;
	private String parent;
	private int depth;
	private int hCost;
	private int totalCost;
	
	public Node(String state, String parent, int depth, int hCost, int totalCost) {
		this.state = state;
		this.parent = parent;
		this.depth = depth;
		this.hCost = hCost;
		this.totalCost = totalCost;
	}
	
	public String getState() {
		return state;
	}
	
	public String getParent() {
		return parent;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getHCost() {
		return hCost;
	}
	
	public int getTotalCost() {
		return totalCost;
	}
}