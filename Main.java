import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) {
		int menuChoice;
		do {
			printMainMenu();
			menuChoice = validMenuOption(1, 6, "Enter valid integer menu option: ");
			if(menuChoice == 1 || menuChoice == 2 || menuChoice == 3) {
				String state;
				if(menuChoice == 1) {
					state = randPuzzle();
				}
				else if(menuChoice == 2) {
					int depth = validMenuOption(1, 31, "Enter depth level (min 1, max 31): ");
					state = randPuzzle(depth);
				}
				else {
					state = createState();
				}
				
				AStarTree tree = new AStarTree(state, 2);
				tree.findGoal();
				tree.findSolution();
				tree.printSolution();
			}
			else if(menuChoice == 4) {
				printSearchCostTable();
			}
			else if(menuChoice == 5) {
				printDepthTable();
			}
			else if(menuChoice == 6) {
				break;
			}
		} while(true);
	}
	
	public static void printMainMenu() {
		System.out.println("1. Randomly generate and solve puzzle");
		System.out.println("2. Randomly generate puzzle by depth and solve");
		System.out.println("3. Manually enter puzzle");
		System.out.println("4. Print average search cost table");
		System.out.println("5. Print total unique states per depth table");
		System.out.println("6. Exit program");
		System.out.println();
	}
	
	public static int validMenuOption(int min, int max, String message) {
		Scanner s = new Scanner(System.in);
		int validInt;
		
		do {
			validInt = validInt(s, message);
			System.out.println();
			if(validInt >= min && validInt <= max) {
				return validInt;
			}
			else {
				System.out.println("Invalid option! Choose an integer inclusively between " + min + " and " + max + ".");
			}
		} while(true);
	}
	
	public static int validInt(Scanner s, String message) {	//Validates user input to accept integer values
		int i;
		do {
			System.out.print(message);
			try {
				i = s.nextInt();
				return i;
			}
			catch (InputMismatchException E) {
				System.out.println("Non-integer value!");
				s.nextLine();
			}
		} while(true);
	}
	
	public static String validIntToString(Scanner s, String message) {
		String str;
		int integer;
		boolean valid;
		do {
			System.out.print(message);
			try {
				valid = true;
				str = s.nextLine();
				for(int i = 0; i < str.length(); i++) {
					integer = Character.getNumericValue(str.charAt(i));
					if(integer < 0 || integer > 9) {
						valid = false;
						break;
					}
				}
				if(valid) {
					return str;
				}
				else {
					System.out.println("Non-integer value!");
				}
			}
			catch (InputMismatchException E) {
				System.out.println("Non-integer value!");
				s.nextLine();
			}
		} while(true);
	}
	
	public static String randPuzzle() {
		String initial = "012345678";
		String state = "";

		do{
			state = shuffleString(initial);
		} while(!isSolvable(state));
		
		return state;
	}
	
	public static String randPuzzle(int depth) {
		Random rand = ThreadLocalRandom.current();
		LinkedList<Node> ll = getAllStates(depth);

		return ll.get(rand.nextInt(ll.size())).getState();
	}
	
	public static LinkedList<Node> getAllStates(int depth) {
		String initial = "012345678";
		AStarTree tree = new AStarTree(initial, 2);
		return tree.findDepthStates(depth);
	}
	
	public static String createState() {
		Scanner s = new Scanner(System.in);
		String state;
		do {
			state = validIntToString(s, "Enter a valid 8-puzzle state (9 digit, left to right, top to bottom. Ex. '806547231') : ");
			state = String.valueOf(state);
		} while(!isValidState(state));
		System.out.println();
		return state;
	}
	
	public static String shuffleString(String state) {
		StringBuilder s = new StringBuilder(state);
		char temp;
		Random rand = ThreadLocalRandom.current();
		
		for(int i = s.length() - 1; i > 0; i--) {
			int index = rand.nextInt(i + 1);
			temp = s.charAt(index);
			s.setCharAt(index, s.charAt(i));
			s.setCharAt(i, temp);
		}
		return s.toString();
	}
	
	public static boolean isValidState(String state) {
		if(state.length() != 9) {
			System.out.println("State must be exactly 9 digits!");
			return false;
		}
		if(!hasAllDigits(state)) {
			System.out.println("State must have all 9 digits (0-8)!");
			return false;
		}
		if(!isSolvable(state)) {
			System.out.println("Puzzle is not solvable!");
			return false;
		}
		return true;
	}
	
	public static boolean hasAllDigits(String state) {
		boolean hasDigit[] = new boolean[9];
		Arrays.fill(hasDigit, false);
		
		for(int i = 0; i < 9; i++) {
			if(state.indexOf(i + '0') >= 0) {
				hasDigit[i] = true;
			}
		}
		for(int i = 0; i < 9; i++) {
			if(!hasDigit[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isSolvable(String state) {
		int sum = 0;
		for(int i = 0; i < 8; i++) {
			if(state.charAt(i) != '0') {
				for(int j = i + 1; j < 9; j++) {
					if(state.charAt(j) != '0' && state.charAt(i) > state.charAt(j)) {
						sum++;
					}
				}
			}
		}
		return sum % 2 == 0;
	}
	
	public static void printSearchCostTable() {
		System.out.println("Search Cost (nodes generated)");
		System.out.format("%-5s %-10s %-10s %-15s %-15s %-5s\n", "d", "A*(h1)", "A*(h2)", "Time(h1)(ns)", "Time(h2)(ns)", "Cases");
		double costAvg1 = 0, costAvg2 = 0, timeAvg1 = 0, timeAvg2 = 0;
		int iterations = 100;
		
		//Warmup for cache
		for(int d = 2; d <= 14; d += 2) {
			for(int i = 0; i < iterations; i++) {
				String state = randPuzzle(d);
				AStarTree tree1 = new AStarTree(state, 1);
				AStarTree tree2 = new AStarTree(state, 2);
				tree1.findGoal();
				tree2.findGoal();
			}
		}
		
		for(int d = 2; d <= 20; d += 2) {
			for(int i = 0; i < iterations; i++) {
				String state = randPuzzle(d);
				AStarTree tree1 = new AStarTree(state, 1);
				AStarTree tree2 = new AStarTree(state, 2);
				
				timeAvg1 += timeElapsed(tree1);
				timeAvg2 += timeElapsed(tree2);
				costAvg1 += tree1.getFrontierSize() + tree1.getExploredSize();
				costAvg2 += tree2.getFrontierSize() + tree2.getExploredSize();
			}
			costAvg1 /= iterations;
			costAvg2 /= iterations;
			timeAvg1 /= iterations;
			timeAvg2 /= iterations;
			System.out.format("%-5d %-10.2f %-10.2f %-15.2f %-15.2f %-5d\n", d, costAvg1, costAvg2, timeAvg1, timeAvg2, iterations);
			costAvg1 = 0;
			costAvg2 = 0;
			timeAvg1 = 0;
			timeAvg2 = 0;
		}
		System.out.println();
	}
	
	public static long timeElapsed(AStarTree tree) {
		long startTime, endTime;
		
		startTime = System.nanoTime();
		tree.findGoal();
		endTime = System.nanoTime();
		return endTime - startTime;
	}
	
	public static void printDepthTable() {
		int total = 0;
		System.out.println("Total Unique States Per Depth");
		System.out.format("%-5s %-20s\n", "d", "Total Unique States");
		for(int i = 0; i <= 31; i++) {
			LinkedList<Node> ll = getAllStates(i);
			System.out.format("%-5s %-20s\n", i, ll.size());
			total += ll.size();
		}
		System.out.println("Total: " + total + " unique states.");
		System.out.println();
	}
	
	public static void printLinkedList(LinkedList<Node> ll) {
		for(int i = 0; i < ll.size(); i++) {
			System.out.println(ll.get(i).getState() + "\t" + ll.get(i).getDepth());
		}
	}
}