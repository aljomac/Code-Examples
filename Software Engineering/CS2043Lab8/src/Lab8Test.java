import java.util.ArrayList;

public class Lab8Test {

	public static void main(String[] args) {
		
		DataManager dm = new DataManager();
		OrderBookControl obc = new OrderBookControl(dm);
		ArrayList<BookObj> books = obc.getBookList();
		for (int i = 0; i < books.size(); i++) {
			System.out.println("Id: " + books.get(i).id);
			System.out.println("Title: " + books.get(i).title);
			System.out.println("Description: " + books.get(i).description);
			System.out.println("Author: " + books.get(i).author);
			System.out.println("ISBN: " + books.get(i).isbn);
			System.out.println("Publisher: " + books.get(i).publisher);
			System.out.println("Year: " + books.get(i).year);
			System.out.println("Inventory: " + books.get(i).inventory);
			System.out.println();
		}
		CustomerObj co = new CustomerObj();
		co.id = "008";
		co.firstName = "Weichang";
		co.lastName = "Du";
		co.address = "550 Windsor St.";
		co.contactInfo = "(506 451-6972";
		dm.addCustomerInfo(co);
		String[] bookIds = {"001", "002"};
		dm.addOrder(bookIds, "Abc Street", "001");
		obc.makeOrder(co, "123 Windsor St.", bookIds);
		
	}

}