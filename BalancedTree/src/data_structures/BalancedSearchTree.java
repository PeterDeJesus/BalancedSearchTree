package data_structures;



/**
 * 
 * @author Peter De Jesus
 * 		   CS310 - Data Structures
 * 		   Assignment 3
 *
 * This code is a Balanced Search Tree that will create a
 * node with three pointers and hold a generic data.
 * This is somewhat like a linked list but the structure is
 * not formed linearly but rather like a "tree"
 * This code includes: add, delete, and get method
 *
 * @param <E> is the object or element of type E
 */

public class BalancedSearchTree<E> {
	
	/**
	 * This constructs the node for the tree
	 *
	 * @param <E> the element type generic
	 */
	@SuppressWarnings("hiding")
	class Node<E> {
		E data;
		Node<E> leftChild;
		Node<E> rightChild;
		Node<E> parent;
		public Node(E obj) {
			data = obj;
			parent = leftChild = rightChild = null;
		}
	}
	
	private Node<E> rootNode;
	private int currentSize;
	@SuppressWarnings("unused")
	private int treeHeight;
	
	/**
	 * Constructor of the Balanced Search Tree
	 */
	public BalancedSearchTree() {
		rootNode = null;
		currentSize = 0;
		treeHeight = 0;
	}
	
	/**
	 * This method will add an object to the tree
	 * @param obj the object that is being added
	 * @return true if adding is successful else false
	 */
	public boolean add(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if (rootNode==null) {
			rootNode = newNode;
			currentSize++;
			return true;
		}
		return addNode(rootNode, newNode);
	}
	
	// This is the addNode method helper for the add method
	@SuppressWarnings("unchecked")
	public boolean addNode(Node<E> parent, Node<E> newNode) {
		if (((Comparable<E>)newNode.data).compareTo(parent.data) < 0) {
			if (parent.leftChild==null) {
				parent.leftChild = newNode;
				newNode.parent = parent;
				currentSize++;
				return true;
			}
			else
				addNode(parent.leftChild, newNode);
		}
		else { //duplicates go to the right child
			if (parent.rightChild==null) {
				parent.rightChild = newNode;
				newNode.parent = parent;
				currentSize++;
				return true;
			}
			else 
				addNode(parent.rightChild, newNode);
		}
		checkBalance(newNode);
		return true;
	}
	
	/**
	 * This method check the tree if it is balance
	 * @param node selected node in the tree
	 */
	public void checkBalance(Node<E> node) {
		if (node==null) return;
		
		if ( Math.abs(getHeight(node.leftChild) - getHeight(node.rightChild)) > 1)
			rebalance(node);
		
		checkBalance(node.parent);
	}
	
	/**
	 * This method balance the tree
	 * @param node where the rebalance occur
	 */
	public void rebalance(Node<E> node) {
		//left subtree is bigger by more than 1
		if (getHeight(node.leftChild) - getHeight(node.rightChild) > 1) {
			if (getHeight(node.leftChild.leftChild) > getHeight(node.leftChild.rightChild)) {
				if (node==rootNode)
					node = rightRotate(node);
				else
					node.parent.leftChild = rightRotate(node);
			}	
			else {
				if (node==rootNode)
					node = leftRightRotate(node);
				else
					node.parent.leftChild = leftRightRotate(node);
				
			}
		}
		else
			if (getHeight(node.rightChild.rightChild) > getHeight(node.rightChild.leftChild)) {
				if (node==rootNode)
					node = leftRotate(node);
				else
					node.parent.rightChild = leftRotate(node);
			}
			else {
				if (node==rootNode)
					node = rightLeftRotate(node);
				else
					node.parent.rightChild = rightLeftRotate(node);
			}
	}
	
	/**
	 * ROTATIONS OF NODES
	 */
	/**
	 * This method does a left rotation of some path of the tree
	 * @param node where the rotation occurs
	 * @return the result of the rotation
	 */
	public Node<E> leftRotate(Node<E> node) {
		Node<E> tempNode = node.rightChild;		//create a temporary node
		node.rightChild = tempNode.leftChild;
		tempNode.leftChild = node;
		
		//update the parent pointer
		tempNode.parent = node.parent;
		node.parent = tempNode;
		
		//if node to be rotated is the root, set the new root node
		if (node==rootNode)
			rootNode = tempNode;
		return tempNode;
	}
	
	/**
	 * This method does a right rotation of some path of the tree
	 * @param node where the rotation occurs
	 * @return the result of the rotation
	 */
	public Node<E> rightRotate(Node<E> node) {
		Node<E> tempNode = node.leftChild;
		node.leftChild = tempNode.rightChild;
		tempNode.rightChild = node;
		
		//update parent pointer
		tempNode.parent = node.parent;
		node.parent = tempNode;
		
		//if node to be rotated is the root, set the new root node
		if (node==rootNode)
			rootNode = tempNode;
		return tempNode;
	}
	
	/**
	 * The method for the left right rotation
	 * @param node where rotation occurs
	 * @return the result of the rotation
	 */
	public Node<E> leftRightRotate(Node<E> node) {
		node.leftChild = leftRotate(node.leftChild);
		return rightRotate(node);
	}
	
	/**
	 * The method for the right left rotation
	 * @param node where rotation occurs
	 * @return the result of the rotation
	 */
	public Node<E> rightLeftRotate(Node<E> node) {
		node.rightChild = rightRotate(node.rightChild);
		return leftRotate(node);
	}
	//END OF ROTATION
	
	
	/**
	 * This method is the delete method for a selected node
	 * in the tree
	 * @param obj the node that we want to delete
	 * @return true if node is deleted else false
	 */
	public boolean delete(E obj) {
		Node<E> node = new Node<E>(obj);
		return delete(node, rootNode);
	}
	
	/**
	 * Delete helper method
	 * @param toDelete node to be deleted
	 * @param node current node
	 * @return true if deleted, else false
	 */
	@SuppressWarnings("unchecked")
	public boolean delete(Node<E> toDelete, Node<E> node) {
		if (node==null) return false;
		if(((Comparable<E>)toDelete.data).compareTo(node.data)==0) {
			//do some stuff
			deleteHelper(node);
			return true;
		}
		if(((Comparable<E>)toDelete.data).compareTo(node.data) < 0)
			return delete(toDelete, node.leftChild);
		return delete(toDelete, node.rightChild);
	}
	
	/**
	 * Delete method helper where the deleted node either have zero, one
	 * or two children
	 * @param node the node to be deleted
	 */
	public void deleteHelper(Node<E> node) {
		
		//if only one node in the entire tree
		if (currentSize==1) {
			rootNode = null;
			node.parent = null;
			currentSize--;
			return;
		}
		
		//if no children and not the root node
		if (node.leftChild==null && node.rightChild==null) {
			if (node.parent.rightChild==node)	//if the node is the right child of the parent
				node.parent.rightChild = null;
			else								//if the node is the left child of the parent
				node.parent.leftChild = null;
			checkBalance(node.parent);
			node.parent = null;
			currentSize--;
			return;
		}
		
		//ONE CHILD
		//if node has a left child and no right child
		if (node.leftChild!=null && node.rightChild==null) {
			
			if (node==rootNode) {
				rootNode = node.leftChild;
				node.leftChild.parent = null;
				node.leftChild = null;
				currentSize--;
				checkBalance(rootNode);
				return;
			}
			
			if (node.parent.leftChild==node)
				node.parent.leftChild = node.leftChild;
			else
				node.parent.rightChild = node.leftChild;
			
			node.leftChild.parent = node.parent;
			node.parent = null;
			checkBalance(node.leftChild);
			node.leftChild = null;
			currentSize--;
			return;
			
		}
		//if node has a right child and no left child
		if (node.leftChild==null && node.rightChild!=null) {
			
			if (node==rootNode) {
				rootNode = node.rightChild;
				node.rightChild.parent = null;
				node.rightChild = null;
				currentSize--;
				checkBalance(rootNode);
				return;
			}
			
			if (node.parent.leftChild==node)
				node.parent.leftChild = node.rightChild;
			else
				node.parent.rightChild = node.rightChild;
			
			node.rightChild.parent = node.parent;
			node.parent = null;
			checkBalance(node.rightChild);
			node.rightChild = null;
			currentSize--;
			return;
		}
		
		//if deleted node has two children
		if (node.leftChild!=null && node.rightChild!=null) {
			Node<E> succ = successor(node.rightChild);
			
			//if root node is to be deleted
			if (node==rootNode) {
				
				if (succ.parent.leftChild==succ) {
					succ.parent.leftChild = succ.rightChild;
					if (succ.rightChild!=null)
						succ.rightChild.parent = succ.parent;
					succ.rightChild = node.rightChild;
					succ.rightChild.parent = succ;
				}
				
				succ.leftChild = node.leftChild;
				node.leftChild = null;
				succ.leftChild.parent = succ;
				succ.parent = null;
				rootNode = succ;
				node.rightChild = null;
				node.parent = null;
				checkBalance(succ);
				currentSize--;
				return;
			}
			
			//replace deleted node with the successor using inorder traversal
			if (succ.parent.leftChild==succ) {
				succ.parent.leftChild = succ.rightChild;
				
				if (succ.rightChild!=null)
					succ.rightChild.parent = succ.parent;
				
				succ.parent = null;
				
				if (node.parent.rightChild==node) {
					node.parent.rightChild = succ;
					succ.parent = node.parent;
				}
				if (node.parent.leftChild==node) {
					node.parent.leftChild = succ;
					succ.parent = node.parent;
				}
				
				node.parent = null;
				succ.rightChild = node.rightChild;
				node.rightChild.parent = succ;
				node.rightChild = null;
				succ.leftChild = node.leftChild;
				node.leftChild.parent = succ;
				node.leftChild = null;
				
				checkBalance(succ.rightChild);
				currentSize--;
				return;
			}
			
			//if successor has no left child
			if (succ.parent.rightChild==succ) {
				
				if (node.parent.rightChild==node) {
					node.parent.rightChild = succ;
					succ.parent = node.parent;
				}
				if (node.parent.leftChild==node) {
					node.parent.leftChild = succ;
					succ.parent = node.parent;
				}
				
				node.parent = null;
				succ.leftChild = node.leftChild;
				node.leftChild.parent = succ;
				node.leftChild = null;
				node.rightChild = null;
				checkBalance(succ);
				currentSize--;
				return;
			}
			
			
		}
	}//end of delete helper
	
	/**
	 * This method will find and retrieve an object in the tree
	 * @param obj the object to be retrieve
	 * @return the object we retrieved
	 */
	public E get(E obj) {
		Node<E> tempNode = new Node<E>(obj);
		return find(tempNode, rootNode);
	}
	
	/**
	 * Find helper for the get method
	 * @param toFind the object to be retrieve
	 * @param node current node in the tree
	 * @return the object we retrieved
	 */
	@SuppressWarnings("unchecked")
	public E find(Node<E> toFind, Node<E> node) {
		if (node==null)
			return null;
		if (((Comparable<E>)toFind.data).compareTo(node.data)==0)
			return node.data;
		if (((Comparable<E>)toFind.data).compareTo(node.data) < 0)
			return find(toFind, node.leftChild);
		return find(toFind, node.rightChild);
	}
	
	/**
	 * This helper method is to find the successor of the node
	 * that is selected
	 * @param succ the actual node
	 * @return the successor of the actual node
	 */
	public Node<E> successor(Node<E> succ) {
		if (succ.leftChild==null)
			return succ;
		return successor(succ.leftChild);
	}
	
	/**
	 * This method return the current size of the tree
	 * @return size of the trees
	 */
	public int size() {
		return currentSize;
	}
	
	/**
	 * This method return the height of the tree
	 * @return the height of the tree
	 */
	public int height() {
		return getHeight(rootNode) - 1;
	}
	
	/**
	 * This method determines the height below the node selected
	 * @return int the height of the node below
	 */
	public int heightBelow(Node<E> node) {
		return getHeight(node) - 1;
	}
	
	/**
	 * This method determines the actual height of a selected node
	 * @param node the object that is selected
	 * @return the max height of the node + 1
	 */
	public int getHeight(Node<E> node) {
		if (node==null) return 0;
		
		int leftHeight = getHeight(node.leftChild) + 1;
		int rightHeight = getHeight(node.rightChild) + 1;
		
		return Math.max(leftHeight, rightHeight);
	}
	
	/**
	 * This method determines if the tree is empty
	 * @return true if empty else false
	 */
	public boolean isEmpty() {
		if (currentSize==0)
			return true;
		return false;
	}
	
	/**
	 * This method determines if the tree is full
	 * @return false since we can always add an object to the tree
	 */
	public boolean isFull() {
		return false;
	}
	
	/**
	 * This method is the iterator that iterates through the tree
	 */
	public void allElements() {
		inOrder();
	}
	
	/**
	 * This method return the next object in the structure
	 * @param obj the selected object
	 * @return the next object of the node selected
	 */
	public E findNext(E obj) {
		Node<E> node = new Node<E>(obj);
		Node<E> next = getNext(rootNode, node);
		
		//if node is the largest, return null
		if (next==null)
			return null;
		return next.data;
	}
	
	/**
	 * This method is a helper method for the find next method
	 * @param node the current node we are in
	 * @param find the selected node
	 * @return the next node of the selected node
	 */
	@SuppressWarnings("unchecked")
	public Node<E> getNext(Node<E> node, Node<E> find) {
		
		if (((Comparable<E>)find.data).compareTo(node.data) == 0)
			return node.rightChild;
		else
			return node.parent;
	}
	
	/**
	 * This method returns the previous object in the structure
	 * @param obj the selected node
	 * @return the previous object of the selected node
	 */
	public E findPrevious(E obj) {
		Node<E> node = new Node<E>(obj);
		
		Node<E> next = getPrev(rootNode, node);
		
		//if node is the smallest, return null
		if (next==null)
			return null;
		return next.data;
	}
	
	/**
	 * This is a helper method for the find previous method
	 * @param node the current node we are in
	 * @param find the node selected
	 * @return the previous node
	 */
	@SuppressWarnings("unchecked")
	public Node<E> getPrev(Node<E> node, Node<E> find) {
		Node<E> prev = null;
		if (((Comparable<E>)find.data).compareTo(node.data)==0)
			return prev;
		if (((Comparable<E>)find.data).compareTo(node.data) < 0)
			return getPrev(node.leftChild, find);
		return getPrev(node.rightChild, find);
		
	}
	
	/**
	 * This method returns the root node of the tree
	 * @return root node of the tree
	 */
	public Node<E> rootNode() {
		return rootNode;
	}
	
	/**
	 * This method iterates throughout the structure that becomes
	 * sorted
	 */
	public void inOrder() {
		Node<E> n = rootNode;
		inOrder(n);
	}
	
	/**
	 * This is the helper method for the inOrder
	 * @param n
	 */
	public void inOrder(Node<E> n) {
		if (n != null) {
			inOrder(n.leftChild);
			System.out.println(n.data);
			inOrder(n.rightChild);
		}
	}
	
}