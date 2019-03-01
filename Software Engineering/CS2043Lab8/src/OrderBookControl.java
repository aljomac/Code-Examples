import java.util.ArrayList;

public class OrderBookControl {
	private DataManager dm;
	
	public OrderBookControl(DataManager dm) {this.dm = dm;}
	
	public ArrayList<BookObj> getBookList() {
		return dm.getAllBooks();
	}
	
	public boolean makeOrder(CustomerObj customerInfo, String shippingAddress, String[] bookIds){
		dm.addCustomerInfo(customerInfo);
		dm.addOrder(bookIds, shippingAddress, customerInfo.id);
		return true;
	}
}