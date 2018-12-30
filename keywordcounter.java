import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.io.*;

public class keywordcounter {

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            String file_name = args[0];
            // System.out.println("Args received: " + file_name);
            File file = new File(file_name);
            Scanner sc = new Scanner(file);

            PrintStream o = new PrintStream(new File("output_file.txt"));
            PrintStream console = System.out;
            System.setOut(o);

            Fibonacci fib_obj = new Fibonacci();
            Dictionary kw_dict = new Hashtable();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // System.out.println("\n" + line + " ");
                char first = line.charAt(0);

                if (first == '$') {
                    // System.out.println("Input is a Keyword: frequency pair");
                    String[] words = line.split("\\s");
                    String kw = words[0].substring(1);
                    int frequency = Integer.parseInt(words[1]);
                    // System.out.println("KW:" + kw + " Freq:" + frequency);

                    // Fibonacci.Node node_in_hash = (Fibonacci.Node) kw_dict.get(kw);
                    Node node_in_hash = (Node) kw_dict.get(kw);
                    if (node_in_hash != null) {
                        fib_obj.increase_key(node_in_hash, frequency);
                    } else {
                        // Insert the node with frequency into the heap
                        // Fibonacci.Node inserted_node = fib_obj.insert(frequency, kw);
                        Node inserted_node = fib_obj.insert(frequency, kw);

                        // Insert the kw: Node reference pair into the Hashtable
                        kw_dict.put(kw, inserted_node);
                    }
                } else if (Character.isDigit(first)) {

                    int query_for = Integer.parseInt(line);
                    // System.out.println("Input is a Query " + query_for);

                    // Perform remove max operation number of times

                    // Fibonacci.Node[] removed_nodes = new Fibonacci.Node[query_for];
                    Node[] removed_nodes = new Node[query_for];
                    // String str = "";
                    boolean comma_check = false;

                    for (int iter = 0; iter < query_for; iter++) {
                        removed_nodes[iter] = (Node) fib_obj.remove_max();
                        // kw_dict.remove(removed_nodes[iter].kw);

                        if (removed_nodes[iter] != null) {
                            if (comma_check)
                                System.out.print(",");
                            System.out.print(removed_nodes[iter].kw);
                            comma_check = true;
                        }

                        // System.out.print(removed_nodes[iter].kw + ",");
                    }
                    System.out.print("\n");
                    // str = str.substring(0, str.length() - 1);
                    // System.out.print(str + "\n");

                    // TODO: Reinsert the nodes back into the heap
                    for (int iter = 0; iter < query_for; iter++) {
                        if (removed_nodes[iter] != null) {
                            int frequency = removed_nodes[iter].frequency;
                            String kw = removed_nodes[iter].kw;

                            fib_obj.meld(removed_nodes[iter], 0);

                        } // System.out.print(" " + max.kw);

                    }
                } else if (line == "stop") {
                    // System.out.println("!Execution ends here");
                    return;
                } else {
                    // System.out.println("Unknown input");
                }

            }
            System.setOut(console);
            sc.close();

            // Fibonacci fib_main_obj = new Fibonacci();
            // fib_main_obj.fibonacci_main();
        } else {
            System.err.println("Error: Either no input arguments or one too many provided");
        }

    }
}