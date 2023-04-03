package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        Product p = new Product(id, name, stock, day, demand);
        int sectorIndex = id % 10;

        sectors[sectorIndex].add(p);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int indexAdded = sectors[sectorIndex].getSize();

        sectors[sectorIndex].swim(indexAdded);
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD
       int sectorIndex = id % 10;
       int sectorSize = sectors[sectorIndex].getSize();

       if (sectorSize == 5) {
           sectors[sectorIndex].swap(1, sectorSize);
           sectors[sectorIndex].deleteLast();
           sectors[sectorIndex].sink(1);
       }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int sectorSize = sectors[sectorIndex].getSize();
        Product productToUpdate = null;

        for (int i = 1; i <= sectorSize; i++) {
            Product currentProduct = sectors[sectorIndex].get(i);
            if (id == currentProduct.getId()) {
                productToUpdate = currentProduct;
                break;
            }
        }

        if (productToUpdate != null) {
            productToUpdate.updateStock(amount);
        }
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int sectorSize = sectors[sectorIndex].getSize();
        Product productToDelete = null;
        int productToDeleteIndex = 0;

        for (int i = 1; i <= sectorSize; i++) {
            Product currentProduct = sectors[sectorIndex].get(i);
            if (id == currentProduct.getId()) {
                productToDelete = currentProduct;
                productToDeleteIndex = i;
                break;
            }
        }

        if (productToDelete != null) {
            sectors[sectorIndex].swap(productToDeleteIndex, sectorSize);
            sectors[sectorIndex].deleteLast();
            sectorSize = sectors[sectorIndex].getSize();
            if (productToDeleteIndex <= sectorSize) {
                sectors[sectorIndex].swim(productToDeleteIndex);
                sectors[sectorIndex].sink(productToDeleteIndex);
            }
        }
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int sectorSize = sectors[sectorIndex].getSize();
        Product productPurchased = null;
        int productPurchasedIndex = 0;

        for (int i = 1; i <= sectorSize; i++) {
            Product currentProduct = sectors[sectorIndex].get(i);
            if (id == currentProduct.getId()) {
                productPurchased = currentProduct;
                productPurchasedIndex = i;
                break;
            }
        }

        if (productPurchased != null) {
            if (productPurchased.getStock() >= amount) {
                productPurchased.setLastPurchaseDay(day);
                productPurchased.updateStock(-amount);
                productPurchased.updateDemand(amount);

                sectors[sectorIndex].sink(productPurchasedIndex);
            }
        }
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        int sectorIndex = id % 10;
        int sectorSize = sectors[sectorIndex].getSize();

        if (sectorSize != 5) {
            addProduct(id, name, stock, day, demand);
        } else {
            Sector freeSector = null;
            int freeSectorIndex = sectorIndex + 1;
            for (int i = 0; i <= 9; i++) {
                if (freeSectorIndex == 10) {
                    freeSectorIndex = 0;
                }

                sectorSize = sectors[freeSectorIndex].getSize();
                if (sectorSize != 5) {
                    freeSector = sectors[freeSectorIndex];
                    break;
                }

                freeSectorIndex++;
            }

            if (freeSector == null) {
                addProduct(id, name, stock, day, demand);
            } else {
                int newID = freeSectorIndex;
                addProduct(newID, name, stock, day, demand);
            }
        }
        
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
