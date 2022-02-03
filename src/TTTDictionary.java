public class TTTDictionary implements TTTDictionaryADT {	
	private int tableSize;
	private Node hashTable[];
	private int num = 0;

	class Node {
		private TTTRecord data;
		private Node next;
		
		public Node(TTTRecord rec) {
			data = rec;
		}
		
		public TTTRecord getData() {
			return data;
		}

		public void setData(TTTRecord data) {
			this.data = data;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}		
	}

	public int numElements() {
		return num;
	}
	
	private int hashFunction(String input) {
		int key = 0;
		int length = input.charAt(0);
		
		for (int i = 0; i < input.length(); i++) {
			length = (length*51 + input.charAt(i)) % tableSize;
		}
		
		key = length % tableSize;
		return key;
	}

	public TTTDictionary(int size) {
		tableSize = size;
		hashTable = new Node[size];
	}

	public int put (TTTRecord record) throws DuplicatedKeyException { 
		int index = hashFunction(record.getConfiguration());
		Node current = hashTable[index];
		
		if (hashTable[index] == null) {
			hashTable[index] = new Node (record);
			hashTable[index].setNext(new Node(null));
			num++;
			return 0;
		} 

		else {
			while(current.getData() != null) {
				if (current.getData().getConfiguration().equals(record.getConfiguration())) {
					throw new DuplicatedKeyException(record.getConfiguration());
				}
				current = current.getNext();
			}
			
			current.setData(record);
			current.setNext(new Node(null));
			num++;
			return 1;
		}
	}

	public TTTRecord get (String config) {	
		int index = hashFunction(config);
		Node current = hashTable[index];

		if (hashTable[index] == null) {
			return null;
		}
		
		else {
			while(current.getData() != null) {
				if(current.getData().getConfiguration().equals(config)) {
					return current.getData();
				}
				
				current = current.getNext();
			}

			return null;
		}
	}

	public void remove (String config) throws InexistentKeyException {
		int index = hashFunction(config);
		Node current = hashTable[index];
		
		if (hashTable[index] == null) {
			throw new InexistentKeyException(config);
		}
		
		else {
			if(hashTable[index].getData().getConfiguration().equals(config)) {
				hashTable[index] = hashTable[index].getNext();
				num--;
				return;
			}
			
			while(current.getNext() != null && current.getNext().getData() != null) {
				if (current.getNext().getData().getConfiguration().equals(config)) {
					current.setNext(current.getNext().getNext());
					num--;
					return;
				}
				
				current = current.getNext();
			}
			
			if (current.getNext() == null && current.getData() != null) {
				throw new InexistentKeyException(config);
			}
		}
	}
}
