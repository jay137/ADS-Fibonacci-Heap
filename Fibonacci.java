import java.util.*;

class Fibonacci {

    Node max_ptr; // ptr to the max node of the heap
    int size; // Size of the top-level list

    Fibonacci() {
        this.max_ptr = null;
        this.size = 0;
    }

    public Node insert(int frequency_input, String keyword) {
        Node new_node = new Node(frequency_input, keyword);

        new_node.prev = new_node;
        new_node.next = new_node;

        if (max_ptr == null) {
            // Heap is empty so just set the max_ptr
            max_ptr = new_node;
            size++;
        } else {
            // Meld the newly spawned node with the existing heap
            meld(new_node, 1);
        }

        return new_node;
    }

    public void meld(Node new_insert, int size_increment) {

        if (new_insert == null)
            return;

        if (max_ptr != null) {
            new_insert.prev.next = max_ptr.next;
            max_ptr.next.prev = new_insert.prev;
            new_insert.prev = max_ptr;
            max_ptr.next = new_insert;

            // Update max_ptr if needed
            if (new_insert.frequency > max_ptr.frequency) {
                max_ptr = new_insert;
            }
        } else {
            max_ptr = new_insert;
        }

        this.size += size_increment;
    }

    public void cut(Node cut_node) {
        // Update parent's child ptr, if it was pointing to the cut_node
        // System.out.println("Cut_node: " + cut_node.frequency + "Cut_node's parent" +
        // cut_node.parent.frequency);
        if (cut_node.parent != null && cut_node.parent.child == cut_node) {
            if (cut_node.next == cut_node) {
                // There is only one node in the sibling list. The cut_node is a single child
                cut_node.parent.child = null;
            } else {
                cut_node.parent.child = cut_node.next;
            }
        }

        // Update the references in the doubly circular sibling LL
        if (cut_node.next != cut_node) {
            cut_node.prev.next = cut_node.next;
            cut_node.next.prev = cut_node.prev;
            // cut_node.prev = null;
            // cut_node.next = null;
            cut_node.prev = cut_node;
            cut_node.next = cut_node;
        }

        // For Cascading Cut
        // Check if current cutnode's parent is root or not by checking if cutnode's
        // parent has a parent or not.
        // No parent means it is a root node.
        // This is checked by the second comparison in this if (condition1 &&
        // condition2).
        // If current cutnode's parent is root we're required to halt Cascading cut.

        // if (cut_node.parent != null && cut_node.parent.parent != null) {
        // if (cut_node.parent.child_cut == false) {
        // // Set the child_cut of the parent to true
        // cut_node.parent.child_cut = true;
        // } else {
        // // Cut the node from the heap, meld it to the top-level and set its child_cut
        // to
        // // false
        // cut(cut_node.parent);
        // meld(cut_node.parent, 1);
        // cut_node.parent.child_cut = false;
        // }
        // }

        // Nullify the parent ptr
        cut_node.parent = null;

    }

    public void increase_key(Node increase_key_node, int increment) {
        // Check if increment leads to a value higher than the parent
        increase_key_node.frequency = increase_key_node.frequency + increment;

        if (increase_key_node.parent != null && increase_key_node.frequency > increase_key_node.parent.frequency) {
            // Cut the subtree
            cut(increase_key_node);

            // Meld the subtree with top level LL
            meld(increase_key_node, 1);
        }

        if (increase_key_node.frequency > max_ptr.frequency) {
            max_ptr = increase_key_node;
        }
    }

    public void remove(Node remove_node) {
        if (remove_node == max_ptr) {
            remove_max();
            System.out.println("Call remove max");
        } else {
            int remove_node_degree = remove_node.degree;

            // Cut the subtree out
            System.out.println("Before cut");
            cut(remove_node);
            System.out.println("After cut");

            Node head = remove_node.child;
            if (head != null) {
                // Set the parent ptr of the children of the node to be removed to null
                head.parent = null;
                Node next_node = head.next;
                while (next_node != head) {
                    next_node.parent = null;
                    next_node = next_node.next;
                }

                // Meld the children list of the node to be removed with the top-level list
                meld(head, remove_node_degree);
            }

            // Free the node to be removed
            remove_node = null;

        }
    }

    public void pairwise_combine(Node root) {

        // Table to store <Node.degree: Node> pair
        Dictionary tree_table = new Hashtable();

        root.prev.next = null;
        Node temp;

        while (root != null) {

            temp = root;
            root = root.next;

            temp.prev = temp;
            temp.next = temp;

            // System.out.println(temp);
            // temp = null;
            while (temp != null) {
                // int temp_degree = temp.degree;
                Node node_in_hash = null;
                int degree = temp.degree;

                node_in_hash = (Node) tree_table.get(temp.degree);

                if (node_in_hash != null) {

                    // Removing from node_in_hash from hash
                    tree_table.remove(degree);

                    // Find smaller and larger tree of the two
                    Node smaller_tree, larger_tree;

                    if (temp.frequency > node_in_hash.frequency) {
                        smaller_tree = node_in_hash;
                        larger_tree = temp;
                    } else {
                        smaller_tree = temp;
                        larger_tree = node_in_hash;
                    }

                    // Combine the two nodes, making smaller the child of larger
                    // Insert the smaller into the children LL
                    // Change parent ptr of the smaller
                    // Change degree of the larger
                    // Change the child ptr if required (comparision)
                    // Remove node_in_hash from the hash
                    smaller_tree.parent = larger_tree;

                    if (larger_tree.child == null) {
                        larger_tree.child = smaller_tree;
                    } else {
                        smaller_tree.next = larger_tree.child.next;
                        larger_tree.child.next.prev = smaller_tree.prev;
                        smaller_tree.prev = larger_tree.child;
                        larger_tree.child.next = smaller_tree;
                    }

                    // Increasing larger's degree
                    larger_tree.degree++;

                    // Now check for collision for the merged tree
                    temp = larger_tree;
                } else {
                    tree_table.put(temp.degree, temp);
                    temp = null;
                }
            }
        }

        max_ptr = null;
        Enumeration e1 = tree_table.keys();

        while (e1.hasMoreElements()) {
            Node foo = (Node) tree_table.get(e1.nextElement());
            // System.out.println("Melding node: " + foo.kw + "from tree_table");
            meld(foo, 0);
        }
    }

    public Node remove_max() {

        if (max_ptr == null)
            return null;

        // Save the node that is removed and it's immediate neighbor
        Node old_max = max_ptr;

        Node max_next;

        if (max_ptr.next == max_ptr) {
            max_next = null;
        } else {
            max_next = max_ptr.next;
        }

        // Cut out the node of its list and children
        cut(max_ptr);

        // Decrement the size of top-level list by 1
        this.size--;

        if (old_max.child != null) {

            // meld the children list to top-level
            Node max_child = old_max.child;

            if (max_next != null) {
                max_ptr = max_next;
                meld(max_child, max_ptr.degree);
            } else {
                max_ptr = max_child;
            }
        } else {
            max_ptr = max_next;
        }

        old_max.child = null;

        if (max_ptr != null) {
            pairwise_combine(max_ptr);
        }

        return old_max;
    }

    public boolean not_empty() {
        return max_ptr != null ? true : false;
    }

    public void remove_max_test(int num_nodes) {
        if (num_nodes == 0) {

        } else if (num_nodes == 1) {

        } else if (num_nodes == 2) {

        } else {
            for (int iter = 0; iter < num_nodes; iter++) {
                this.insert(iter * 100, "asd");
            }

            System.out.println("Max_ptr: " + max_ptr.frequency);
            int count = 0;
            int y = num_nodes * 200;
            while (this.not_empty()) {
                count++;
                Node x = remove_max();
                System.out.println("Remove max return: " + x + " Freq: " + x.frequency);
                if (y < x.frequency) {
                    System.err.println("y < x: This remove_max removed larger value than previous ones");
                } else {
                    y = x.frequency;
                }
            }
            if (count != num_nodes) {
                System.err.println("The number of remove_max' was not 20");
            }
        }
    }
}
