public class Node {
	public int frequency; // Contains the frequency of the keyword
	public int degree; // Contains the number of children of the node
	public boolean child_cut; // Child_cut field. True if previously lost a child, false otherwise

    // Pointers that make the heap
    public Node next; // ptr to the next sibling in LL
    public Node prev; // ptr to the prev sibling in LL
    public Node child; // ptr to the child in the max tree, if any
    public Node parent; // ptr to the parent in the max tree, if any

    String kw; // Keyword of the node

    Node(int frequency_input, String keyword) {
        this.frequency = frequency_input;
        this.degree = 0;
        this.child_cut = false;

        this.kw = keyword;
        // System.out.println("Next and previous in Constructor args:" + next + prev);
    } // Constructor

    @Override
    public boolean equals(Object x) {
        boolean response = false;

        if (x instanceof Node) {
            response = (((Node) x).kw).equals(this.kw);
        }
        return response;
    }
}

