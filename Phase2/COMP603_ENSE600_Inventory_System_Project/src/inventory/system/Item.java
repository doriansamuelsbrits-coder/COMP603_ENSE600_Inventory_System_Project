package inventory.system;

/*
This class focuses on aiding the Hashmap for printing to the txt file.
*/
public class Item {
    public String itemCode;
    public String itemName;
    public int qty;
    public int moq;
    public int stkMin;
    public double price;
    
    public Item(String itemCode, String itemName, int qty, int moq, int stkMin, double price) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.qty = qty;
        this.moq = moq;
        this.stkMin = stkMin;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-10s | %3d | %3d | %6d | %6.2f",
                itemCode, itemName, qty, moq, stkMin, price);
    }
}

