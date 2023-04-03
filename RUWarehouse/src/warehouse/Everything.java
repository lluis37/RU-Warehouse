package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	// Use this file to test all methods
        int numQueries = StdIn.readInt();
        Warehouse w = new Warehouse();

        for (int i = 1; i <= numQueries; i++) {
            String queryType = StdIn.readString();
            if (queryType.compareTo("add") == 0) {
                int currentDay = StdIn.readInt();
                int productID = StdIn.readInt();
                String productName = StdIn.readString();
                int initialStock = StdIn.readInt();
                int initialDemand = StdIn.readInt();

                w.addProduct(productID, productName, initialStock, currentDay, initialDemand);
            } else if (queryType.compareTo("restock") == 0) {
                int productID = StdIn.readInt();
                int restockAmount = StdIn.readInt();

                w.restockProduct(productID, restockAmount);
            } else if (queryType.compareTo("purchase") == 0) {
                int currentDay = StdIn.readInt();
                int productID = StdIn.readInt();
                int amountPurchased = StdIn.readInt();

                w.purchaseProduct(productID, currentDay, amountPurchased);
            } else {
                int productID = StdIn.readInt();

                w.deleteProduct(productID);
            }
        }

        StdOut.println(w);
    }
}
