package warehouse;

/*
 * Use this class to test to addProduct method.
 */
public class AddProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	// Use this file to test addProduct
        Warehouse w = new Warehouse();
        int numProducts = StdIn.readInt();

        for (int i = 1; i <= numProducts; i++) {
            int currentDay = StdIn.readInt();
            int productID = StdIn.readInt();
            String productName = StdIn.readString();
            int initialStock = StdIn.readInt();
            int demand = StdIn.readInt();

            w.addProduct(productID, productName, initialStock, currentDay, demand);
        }
        StdOut.println(w);
    }
}
