import java.util.*;

public class AStarTree {
	Comparator<Node> comparator;
	private PriorityQueue<Node> frontier;
	private Hashtable<String, Node> explored;
	private Stack<Node> solutionPath;
	private LinkedList<Node> depthList;
	private int hNum;
	
	public AStarTree(String state, int hNum) {
		comparator = new NodeComparator();
		frontier = new PriorityQueue<Node>(comparator);
		explored = new Hashtable<String, Node>();
		int hCost = hCost(state, hNum);
		frontier.add(new Node(state, null, 0, hCost, hCost));
		this.hNum = hNum;
	}
	
	public int getFrontierSize() {
		return frontier.size();
	}
	
	public int getExploredSize() {
		return explored.size();
	}
	
	private Node getHead() {
		return frontier.peek();
	}
	
	private boolean isGoalState(String state) {
		return state.equals("012345678");
	}
	
	public void findGoal() {
		solutionPath = new Stack<Node>();
		while(!isGoalState(getHead().getState())) {
			explore(getHead(), !isGoalState(getHead().getState()));
			if(isGoalState(getHead().getState())) {
				solutionPath.push(getHead());
			}
		}
	}
	
	public void findSolution() {
		while(!solutionPath.isEmpty() && solutionPath.peek().getParent() != null) {
			solutionPath.push(explored.get(solutionPath.peek().getParent()));
		}
	}
	
	public void printSolution() {
		if(!solutionPath.isEmpty()) {
			System.out.format("%-8s %-8s %-8s %-12s\n", "Depth", "State", "h" + hNum + " Cost", "Total Cost");
			while(!solutionPath.isEmpty()) {
				String state = solutionPath.peek().getState();
				System.out.format("%-8d %-8s %-8d %-12d\n", solutionPath.peek().getDepth(), state.charAt(0) + " " + state.charAt(1) + " " + state.charAt(2), solutionPath.peek().getHCost(), + solutionPath.peek().getTotalCost());
				System.out.format("%-8s %-8s\n", "", state.charAt(3) + " " + state.charAt(4) + " " + state.charAt(5));
				System.out.format("%-8s %-8s\n", "", state.charAt(6) + " " + state.charAt(7) + " " + state.charAt(8));
				System.out.println();
				solutionPath.pop();
			}
		}
		else {
			System.out.println("Puzzle is already at goal state!");
			System.out.println();
		}
	}
	
	public LinkedList<Node> findDepthStates(int depth) {
		depthList = new LinkedList<Node>();
		frontier.add(getHead());
		while(getHead() != null) {
			explore(getHead(), depth);
		}
		return depthList;
	}
	
	private void explore(Node n, boolean expandCondition) {
		String state = n.getState();
		int depth = n.getDepth();
		int emptyTile = state.indexOf("0");
		int x = emptyTile % 3;
		int y = emptyTile / 3;
		
		if(expandCondition) {
			int leftTile = emptyTile - 1, rightTile = emptyTile + 1, aboveTile = emptyTile - 3, belowTile = emptyTile + 3;
			if(x != 0) {
				addAction(state, depth + 1, emptyTile, leftTile);
			}
			if(x != 2) {
				addAction(state, depth + 1, emptyTile, rightTile);
			}
			if(y != 0) {
				addAction(state, depth + 1, emptyTile, aboveTile);
			}
			if(y != 2) {
				addAction(state, depth + 1, emptyTile, belowTile);
			}
		}
		explored.put(state, n);
		frontier.remove(n);
	}
	
	private void explore(Node n, int depthGoal) {
		if(n.getDepth() == depthGoal && !explored.containsKey(n.getState())) {
			depthList.add(n);
		}
		explore(n, n.getDepth() < depthGoal);
	}
	
	private void addAction(String state, int depth, int emptyTile, int swapTile) {
		String newState = moveTile(state, emptyTile, swapTile);
		if(!explored.containsKey(newState)) {
			int hCost = hCost(newState, hNum);
			frontier.add(new Node(newState, state, depth, hCost, depth + hCost));
		}
	}
	
	private String moveTile(String state, int emptyTile, int swapTile) {
		StringBuilder newState = new StringBuilder(state);
		char temp = state.charAt(swapTile);
		newState.setCharAt(swapTile, '0');
		newState.setCharAt(emptyTile, temp);
		return newState.toString();
	}
	
	private int hCost(String state, int hNum) {
		if(hNum == 1) {
			return h1(state);
		}
		else {
			return h2(state);
		}
	}
	
	private int h1(String state) {
		int sum = 0;
		for(int i = 0; i < state.length(); i++) {
			int num = Character.getNumericValue(state.charAt(i));
			if (num != i && num != 0) {
				sum++;
			}
		}
		return sum;
	}
	
	private int h2(String state) {
		int sum = 0;
		for(int i = 0; i < state.length(); i++) {
			int num = Character.getNumericValue(state.charAt(i));
			if (num != 0) {
				sum += Math.abs((i % 3) - (num % 3));
				sum += Math.abs((i / 3) - (num / 3));
			}
		}
		return sum;
	}
}
