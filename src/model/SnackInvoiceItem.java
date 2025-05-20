
package model;

/**
 *
 * @author Heshan Nawarathna
 */
public class SnackInvoiceItem {

    private String StockID;
    private String ProductName;
    private String ProductQty;
    private String SellingPrice;

    public String getStockID() {
        return StockID;
    }

    public void setStockID(String StockID) {
        this.StockID = StockID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getProductQty() {
        return ProductQty;
    }

    public void setProductQty(String ProductQty) {
        this.ProductQty = ProductQty;
    }

    public String getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(String SellingPrice) {
        this.SellingPrice = SellingPrice;
    }

    public void setStockID(int StockID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
