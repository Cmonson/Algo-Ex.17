class item {

    int number;
    int weight;
    int profit;

    item() {
        number = 0;
        profit = 0;
        weight = 0;
    }

    item(int n, int w, int p) {
        number = n;
        profit = p;
        weight = w;
        System.out.println( number + ": " +  "Weight:" + weight + 
        "  Profit:" + profit);
    }
}