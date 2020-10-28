//26-way trie combined with ternary search trie. Key-value data storage for fast value retrieval. Works a little bit faster than
//Ternary Search Trie, but uses more memory. Root node contains 26 TST-s for each letter of English alphabet.
public class RTrie<Value> {

	//English alphabet
	private TST<Value>[] root = new TST[26];

	public RTrie() {
		for (int i = 0; i < 26; i++) {
			root[i] = new TST<Value>();
		}
	}

	public void put(String key, Value val) {
		if(key == null || key.isEmpty()) return;
		int index = key.charAt(0) - 'A';
		root[index].put(key, val);
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	public Value get(String key) {
		if (key == null || key.isEmpty()) return null;
		int index = key.charAt(0) - 'A';
		return root[index].get(key);
	}

	public boolean containsPrefix(String key) {
		if (key == null || key.isEmpty()) return false;
		int index = key.charAt(0) - 'A';
		return root[index].containsPrefix(key);
	}
}