
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * EX 17 for CS 4050
 *
 * @author Casey Monson
 */
public class Ex17 {

    /**
     * This is the best node
     */
    public static node best = new node();

    /**
     * Capacity of the knapsack
     */
    public static int cap = 0;

    /**
     * Number of items
     */
    public static int nItems = 0;

    /**
     * Node counter 
     */
    public static int nodeC = 1;

    /**
     * This will solve and show a solution to a 0-1 knapsack problem from a data
     * file
     *
     * @param args needed for java to tun no args are used
     */
    public static void main(String[] args) {
        String line = "";
        List<item> itemList = new ArrayList<>();
        node startNode = new node();
        //load the data file
        JButton open = new JButton();
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pick the File ");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION);
        String fn = fc.getSelectedFile().getAbsolutePath();
        //Reads file
        try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
            int lC = 1;
            line = br.readLine();
            cap = Integer.parseInt(line);
            line = br.readLine();
            nItems = Integer.parseInt(line);
            System.out.println("Capacity of knapsack is " + cap);
            System.out.println("Items Are: ");
            while ((line = br.readLine()) != null) {
                String[] splitArray = line.split("\\s+");
                //Puts items in itemList item( number, weight, profit)
                itemList.add(new item(lC, Integer.parseInt(splitArray[1]),
                        Integer.parseInt(splitArray[0])));
                lC++;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        double iBound = 0;
        iBound = getBound(itemList, cap);
        System.out.println("\nBegin exploration of the Possibilities tree\n");
        startNode.bound = iBound;
        startNode.number = nodeC++;
        start(startNode, itemList); //start branch and bound with start node
        System.out.println("\nBest Node: <" + best + ">");
    }

    /**
     * takes startNode and the itemList and start b-b also creates start node
     * and prints out the first 2
     *
     * @param startNode The start node
     * @param list the list of items
     */
    public static void start(node startNode, List<item> list) {
        System.out.println("Exploring  <" + startNode + ">");
        List<item> leftTemp = new ArrayList(list);
        leftTemp.set(startNode.level, (new item()));
        node leftChild = new node(nodeC++, 1, 0, 0, getBound(leftTemp,
                cap));
        System.out.println("\tLeft Child: <" + leftChild + ">");
        List<item> rightTemp = new ArrayList(list);
        node rightChild = new node(nodeC++, 1, list.get(0).weight,
                list.get(0).profit, startNode.bound);
        if (list.get(0).weight <= cap) { //explore becasue it is not too heavy
            rightChild.items.add(1);
            System.out.println("\tRight Child: <" + rightChild + ">");
            best = rightChild;
            System.out.println("\t\tNote profit of " + best.profit
                    + " will explore further if possible");
            branch(rightChild, rightTemp);
            branch(leftChild, leftTemp);
        } else {
            System.out.println("Too heavy won't explore right child");
            branch(leftChild, leftTemp);
        }
    }

    /**
     * This does the work and prints the work
     *
     * @param current The node
     * @param list of items
     */
    public static void branch(node current, List<item> list) {
        //If the current nodes bound is less our best prune it
        if (current.bound < best.profit) {
            System.out.println("Exploring Node: <" + current + ">");
            System.out.println("\t\tPruned, don't explore children because "
                    + "bound " + current.bound
                    + " is smaller than known profit " + best.profit);
            return;
        }
        if (current.level == (nItems - 1)) {
            //dont do anything as this is the end of the list and it has been 
            //noted from parent node
            return;
        }
        System.out.println("Exploring Node: <" + current + ">");
        Boolean leftPrune = false;
        List<item> lTemp = new ArrayList(list);
        lTemp.set(current.level, (new item()));
        node leftChild = new node(nodeC++, (current.level + 1),
                current.weight, current.profit, getBound(lTemp, cap));
        //get parents items and add them to our node
        for (int i = 0; i < current.items.size(); i++) {
            leftChild.items.add(current.items.get(i));
        }
        System.out.println("\tLeft Child: <" + leftChild + ">");
        //Check weight
        if (cap < leftChild.weight) {
            System.out.println("\t\tPruned because of weight");
            leftPrune = true;
        } //see if it is better than the best
        else if (best.profit < leftChild.profit) {
            System.out.println("\t\tNote profit of " + leftChild.profit
                    + " is greater than previous profit of " + best.profit
                    + " will explore further if possible");
            best = leftChild;
        } else {
            System.out.println("\t\t Will explore further");
        }
        Boolean rightPrune = false;
        List<item> rTemp = new ArrayList(list);
        int rProfit = 0;
        int rWeight = 0;
        for (int i = 0; i < current.level + 1; i++) {
            rProfit += rTemp.get(i).profit;
            rWeight += rTemp.get(i).weight;
        }
        node rightChild = new node(nodeC++, (current.level + 1),
                rWeight, rProfit, getBound(rTemp, cap));
        //get parents items and add current item
        for (int i = 0; i < current.items.size(); i++) {
            rightChild.items.add(current.items.get(i));
        }
        rightChild.items.add(current.level + 1);
        System.out.println("\tRight Child: <" + rightChild + ">");
        // check weight
        if (cap < rightChild.weight) {
            System.out.println("\t\tPruned because of weight");
            rightPrune = true;
        } //see if it is better than the best
        else if (best.profit < rightChild.profit) {
            System.out.println("\t\tNote profit of " + rightChild.profit
                    + " is greater than previous profit of " + best.profit
                    + " will explore further if possible");
            best = rightChild;
        } else {
            System.out.println("\t\tWill explore Further");
        }
        if (!rightPrune) {
            branch(rightChild, rTemp);
        }
        if (!leftPrune) {
            branch(leftChild, lTemp);
        }
    }

    /**
     * Calculates bounds
     *
     * @param list list of items
     * @param cap the capacity of the knapsack
     * @return b the bound
     */
    public static double getBound(List<item> list, int cap) {
        int w = 0;
        double b = 0;
        int rem = 0; //Weight remaining
        List<item> temp = new ArrayList(list);
        for (int i = 0; i < temp.size(); i++) {
            if ((w + temp.get(i).weight) <= cap) {
                w = w + temp.get(i).weight;
                b = b + temp.get(i).profit;
            } else {
                rem = cap - w;
                b = b + (rem * (temp.get(i).profit / temp.get(i).weight));
                return b;
            }
        }
        return b;
    }
}
