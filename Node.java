public class Node implements Comparable {
	private Node rightChild;
	private Node leftChild;
	private Node parent;
	private char c = (char) 200;
	private int value;


	public Node(char c, int value) {
		this.c = c;
		rightChild = null;
		leftChild = null;
		this.value = value;
	}

	public Node(char c, int value, Node parent) {
		this(c, value);
		this.parent = parent;
	}

	public Node(Node child1, Node child2) {
		if (child1.compareTo(child2) < 0) {
			leftChild = child1;
			rightChild = child2;
		} else {
			rightChild = child1;
			leftChild = child2;
		}

		value = rightChild.getValue() + leftChild.getValue();
	}

	public Node getRightChild() {
		return rightChild;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public Node getParent() {
		return parent;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public char getChar() {
		return c;
	}

	public void setChar(char c) {
		this.c = c;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// Returns true if this node is a leaf
	public boolean isLeaf() {
		// YOU CODE THIS
		return rightChild == null && leftChild == null;
	}

	// Returns the number of children of this node
	public int degree() {
		// YOU CODE THIS
		if (rightChild == null && leftChild == null) {
			return 0;
		} else {
			return 2;
		}
	}

	public int sumOfValues() {
		if (isLeaf()) {
			return value;
		}
		int left = 0;
		int right = 0;
		if (leftChild != null) {
			left = leftChild.sumOfValues();
		}
		if (rightChild != null) {
			right = rightChild.sumOfValues();
		}
		return left + right + value;
	}

	// Returns the greatest degree of any node in the subtree
	// rooted at this node

	// Returns the number of nodes in the subtree rooted at this node

	// Returns the depth of the subtree rooted at this node,
	// i.e. the longest path from this node to any leaf
	// A tree with a single node with no children has depth 0

	// Returns the number of species in the subtree rooted at this node
	// You may assume that all leaf nodes in the tree are species

	// Returns whether otherTaxon is contained in the subtree
	// rooted at this node

	// Adds newTaxon as a child to the parent contained in this subtree
	// Returns false if either newTaxon is contained in the subtree already
	// or if parent is NOT contained in the subtree
	public boolean addChildRight(Node newRight) {
		if (rightChild != null) {
			return false;
		}
		rightChild = newRight;
		rightChild.setParent(this);
		return true;
	}

	public boolean addChildLeft(Node newLeft) {
		if (leftChild != null) {
			return false;
		}
		leftChild = newLeft;
		leftChild.setParent(this);
		return true;
	}

	public String toString() {
		return c + " : " + value + " \n";
	}

	@Override
	public int compareTo(Object o) {
		Node obj = (Node) o;
		return value - obj.getValue();
	}

	// Returns a String containing all of the taxons in the
	// subtree rooted at this node, other than this node's
	// taxon itself
}
