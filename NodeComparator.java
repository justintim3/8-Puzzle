import java.util.*;

public class NodeComparator implements Comparator<Node>{
	public int compare(Node a, Node b) {
		if(a.getTotalCost() == b.getTotalCost()) {
			return Integer.compare(b.getDepth(), a.getDepth());
		}
		return Integer.compare(a.getTotalCost(), b.getTotalCost());
	}
} 