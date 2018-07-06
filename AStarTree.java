import java.util.*;

public class AStarTree {
	private int hNum;
	Comparator<Node> comparator = new NodeComparator();
	private PriorityQueue<Node> frontier = new PriorityQueue<Node>(comparator);
	private Hashtable<String, Node> explored = new Hashtable<String, Node>();
	private Stack<Node> solutionPath = new Stack<Node>();
	private LinkedList<Node> depthList = new LinkedList<Node>();
	
	public AStarTree(String state, int hNum) {
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
		while(!isGoalState(getHead().getState())) {
			explore(getHead());
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
		frontier.add(getHead());
		while(getHead() != null) {
			explore(getHead(), depth);
		}
		return depthList;
	}
	
	private void explore(Node n) {
		String state = n.getState();
		int depth = n.getDepth();
		int emptyTile = state.indexOf("0");
		int x = emptyTile % 3;
		int y = emptyTile / 3;
		
		if(!isGoalState(state)) {
			if(x != 0) {
				String newState = expandNode(state, emptyTile, emptyTile - 1);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(x != 2) {
				String newState = expandNode(state, emptyTile, emptyTile + 1);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(y != 0) {
				String newState = expandNode(state, emptyTile, emptyTile - 3);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(y != 2) {
				String newState = expandNode(state, emptyTile, emptyTile + 3);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
		}
		explored.put(state, n);
		frontier.remove(n);
	}
	
	private void explore(Node n, int depthGoal) {
		String state = n.getState();
		int depth = n.getDepth();
		int emptyTile = state.indexOf("0");
		int x = emptyTile % 3;
		int y = emptyTile / 3;
		
		if(n.getDepth() < depthGoal) {
			if(x != 0) {
				String newState = expandNode(state, emptyTile, emptyTile - 1);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(x != 2) {
				String newState = expandNode(state, emptyTile, emptyTile + 1);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(y != 0) {
				String newState = expandNode(state, emptyTile, emptyTile - 3);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
			if(y != 2) {
				String newState = expandNode(state, emptyTile, emptyTile + 3);
				if(!explored.containsKey(newState)) {
					int hCost = hCost(newState, hNum);
					frontier.add(new Node(newState, state, depth + 1, hCost, depth + 1 + hCost));
				}
			}
		}
		if(n.getDepth() == depthGoal && !explored.containsKey(n.getState())) {
			depthList.add(n);
		}
		explored.put(state, n);
		frontier.remove(n);
	}
	
	private String expandNode(String state, int emptyTile, int swapTile) {
		char temp = state.charAt(swapTile);
		StringBuilder newState = new StringBuilder(state);
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
