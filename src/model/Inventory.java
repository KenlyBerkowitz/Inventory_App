package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory DB
 * @author Kenly Berkowitz
 */
public class Inventory {

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int autoGenPartId = 1;
    private static int autoGenProductId = 2;


    /**
     * @return tempPartId new generated part id
     */
    public int getGenPartID() {
        int tempPartId = autoGenPartId;
        autoGenPartId += 2;

        return tempPartId;
    }

    /**
     * @return tempProductId new generated product id
     */
    public int getGenProductID() {
        int tempProductId = autoGenProductId;
        autoGenProductId += 2;

        return tempProductId;
    }

    /**
     * @param newPart adds new part to list
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct adds new product to list
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);

    }

    /**
     * @param partId id to search for in allParts
     * @return part if found in allProducts or null
     */
    public Part lookupPart(int partId) {

        for (Part allPart : allParts) {
            if (allPart.getId() == partId)
                return allPart;
        }
        return null;
    }

    /**
     * @param productId id to search for in allProducts
     * @return product if found in allProducts or null
     */
    public Product lookupProduct(int productId) {


        for (int i = 0; i < allProducts.size(); i++ ) {
            if (allProducts.get(i).getId() == productId)
                return allProducts.get(i);
        }
        return null;
    }

    /**
     * @param partName the part name to find
     * @return observable list called tempListToReturn or return null
     */
    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempListToReturn = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().startsWith(partName))
                tempListToReturn.add(part);

        }
        if (tempListToReturn.isEmpty())
            return null;
        else
            return tempListToReturn;
    }

    /**
     * @param productName the product name to find
     * @return observable list called tempListToReturn or return null
     */
    public ObservableList<Product> lookupProduct (String productName) {
        ObservableList<Product> tempListToReturn = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if (product.getName().startsWith(productName))
                tempListToReturn.add(product);
        }
        if (tempListToReturn.isEmpty())
            return null;
        else
            return tempListToReturn;
    }

    /**
     * @param index use to replace a part with new part
     * @param selectedPart the new part object to be updated with
     */
    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index use to replace a product with new product
     * @param newProduct the new product object to be updated with
     */
    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart part object to be removed
     */
    public boolean deletePart(Part selectedPart) {

        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct product object to be removed
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return allParts an observable list of all parts in the inventory object
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return allProducts an observable list of all parts in the inventory object
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }


}
