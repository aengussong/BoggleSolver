import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

	private RTrie<Boolean> trieDictionary;

	public static void main(String[] args) {
    	In in = new In(args[0]);
    	String[] dictionary = in.readAllStrings();
    	BoggleSolver solver = new BoggleSolver(dictionary);
    	BoggleBoard board = new BoggleBoard(args[1]);
    	int score = 0;
    	for (String word : solver.getAllValidWords(board)) {
        	StdOut.println(word);
        	score += solver.scoreOf(word);
    	}
    	StdOut.println("Score = " + score);
	}

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		trieDictionary = new RTrie<Boolean>();

		//store dictionary in the 26-way trie for prefix check operation and fast word finding.
		for (String str : dictionary) {
			trieDictionary.put(str, true);
		}

	}
	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		return generateWords(board);
	}

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		Boolean isValidWord = trieDictionary.get(word);
		if (isValidWord == null) return 0;
		return calculateScore(word);
	}

	private int calculateScore(String word) {
		if (word == null) return 0;
		int score;
		switch(word.length()) {
			case 0 :
			case 1 :
			case 2 : score = 0; break;
			case 3 : 
			case 4 : score = 1; break;
			case 5 : score = 2; break;
			case 6 : score = 3; break;
			case 7 : score = 5; break;
			default : score = 11;
		}
		return score;
	}

	/**
	* Generate all possible words from the board via DFS.
	*/
	private Iterable<String> generateWords(BoggleBoard board) {
		Set<String> words = new HashSet<>();

		boolean[][] passes = new boolean[board.cols()][board.rows()];

		for(int i = 0; i < board.cols(); i++) {
			for(int j = 0; j < board.rows(); j++) {
				getWords("", i, j, passes, board, words);
			}
		}

		return words;
	}

	private Set<String> getWords(String word, int i, int j, boolean[][] passes, BoggleBoard board, Set<String> words) {
		char letter = board.getLetter(j,i);
		word += letter;
		//special case, letter Q in boggle means Qu, 'cause in the English language, the letter Q is almost always followed by the letter U
		if(letter == 'Q') {
			word += 'U';
		}
		passes[i][j] = true;

		//drop a path if dictionary doesn't contain such a prefix
		if(!trieDictionary.containsPrefix(word)) {
			passes[i][j] = false;
			return words;
		}

		if(word.length() >= 3 && trieDictionary.get(word) != null) {
			words.add(word);
		}

		List<Pair<Integer, Integer>> neighbours = getNeighbours(i, j, passes);

		for (Pair<Integer, Integer> n : neighbours) {
			getWords(word, n.first, n.second, passes, board, words);
		}

		passes[i][j] = false;
		return words;
	}

	private List<Pair<Integer, Integer>> getNeighbours(int i, int j, boolean[][] passes) {
		List<Pair<Integer, Integer>> neighbours = new ArrayList<>();

		if (i > 0 && j > 0 && !passes[i-1][j-1]) {
			neighbours.add(new Pair(i-1, j-1));
		}

		if (j > 0 && !passes[i][j-1]) {
			neighbours.add(new Pair(i, j-1));
		}

		if (j > 0 && i < passes.length - 1 && !passes[i+1][j-1]) {
			neighbours.add(new Pair(i+1, j-1));
		}

		if (i < passes.length - 1 && !passes[i+1][j]) {
			neighbours.add(new Pair(i+1, j));
		}

		if (i < passes.length - 1 && j < passes[i].length - 1 && !passes[i+1][j+1]) {
			neighbours.add(new Pair(i+1, j+1));
		}

		if (j < passes[i].length -1 && !passes[i][j+1]) {
			neighbours.add(new Pair(i, j+1));
		}

		if (i > 0 && j < passes[i].length - 1 && !passes[i-1][j+1]) {
			neighbours.add(new Pair(i-1, j+1));
		}

		if (i > 0 && !passes[i-1][j]) {
			neighbours.add(new Pair(i-1, j));
		}

		return neighbours;
	}

	private class Pair<T,V> {
		T first;
		V second;

		Pair(T first, V second) {
			this.first = first;
			this.second = second;
		}

		public String toString() {
			return "[" + first + " " + second + "]";
		}
	}
}