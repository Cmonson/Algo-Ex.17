
import java.util.ArrayList;
import java.util.List;

class node {

    int number;
    int level;
    int weight;
    int profit;
    double bound;
    List<Integer> items;

    node() {
        number = 0;
        level = 0;
        weight = 0;
        profit = 0;
        bound = 0.0;
        items = new ArrayList();
    }

    node(int n, int l, int w, int p, double b) {
        number = n;
        level = l;
        weight = w;
        profit = p;
        bound = b;
        items = new ArrayList();
    }

    public String toString() {
        return "Node: " + number + " Items: " + items + " Level: " + level
                + " Profit: " + profit + " Weight: " + weight + " Bound: "
                + bound;
    }
}