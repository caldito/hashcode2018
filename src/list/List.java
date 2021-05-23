package src.list;

public class List {

	// Attributes
	public SNode first;
	int size;
	int ride_index = -1;
	
	
	// Methods
	public boolean isEmpty() {
		return (first == null);
	}
	
	public int getSize() {
		return size;
	}

	
	public void addFirst(int [] newRide, int newRide_index) {
		
		SNode newNode = new SNode(newRide , newRide_index);
		newNode.next = first;
		first = newNode;
		size++;
		ride_index++;
	}

	public void removeFirst() {
		if (!isEmpty()) {
			first = first.next;
			size--;
		}
	}
	
	public void addLast(int [] newRide ) {
		if (isEmpty()) {
			addFirst(newRide,ride_index);
		}else {
			SNode node = new SNode(newRide,ride_index);
			for (SNode nodeIt = first; nodeIt != null; nodeIt = nodeIt.next) {
				if (nodeIt.next == null) {
					nodeIt.next = node;
					size++;
					ride_index++;
					break;
				}		
			}
		}	
	}
	
	
	public SNode getAt(int index) {
		if (index<0 || index>=size) {
			System.out.println("SList: getAt index out of bounds");
			return null;
		}else {
			int i = 0;
			SNode result=null;
			for (SNode nodeIt = first; nodeIt != null && result==null; nodeIt = nodeIt.next,i++) {
				if (i == index) {
					result= nodeIt;
				}
			}
			return result;
		}
	}

	
	public void removeLast() {
		if(size == 1) {
			removeFirst();
		}else {
			for (SNode nodeIt = first; nodeIt != null; nodeIt = nodeIt.next) {
				if (nodeIt.next.next == null) {
					nodeIt.next = null;
					
				}
			}
			size--;
		}
		
	}


	public void removeAt(int index) {
		if (index<0 || index>=size) {
			System.out.println("SList: removeAt index out of bounds");
		}else if(index == 0){
			removeFirst();
		}else if (index == size-1){
			removeLast();
		}else {
			int i = 0;
			String result=null;
			for (SNode nodeIt = first; nodeIt != null && result==null; nodeIt = nodeIt.next,i++) {
				if (i == index - 1) {
					nodeIt.next = nodeIt.next.next;
					size--;
					break;
				}
			}
		}
	}

	
	public SNode getFirst() {
		return first;
	}

	
	
	//Main
	public static void main(String[] args) {
		List list = new List();
		int[]arr = {1,2,3};
		list.addLast(arr);
		list.addLast(arr);
		list.addLast(arr);
		list.removeFirst();
		list.removeAt(0);
		
		System.out.println(list.getAt(0).ride_index);
		
	
	}

}
