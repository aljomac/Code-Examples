import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.*;

public class OrderBookUI extends JPanel{
	OrderBookControl control;
	JTextArea textArea = new JTextArea();
	
	public OrderBookUI(OrderBookControl control) {
		this.control = control;
		this.add(textArea);
		displayBookList();
	}
	
	public void displayBookList() {
		ArrayList<BookObj> books = control.getBookList();
		for (int i = 0; i < books.size(); i++) {
			textArea.append("Id: " + books.get(i).id + "\n");
			textArea.append("Title: " + books.get(i).title + "\n");
			textArea.append("Description: " + books.get(i).description + "\n");
			textArea.append("Author: " + books.get(i).author + "\n");
			textArea.append("ISBN: " + books.get(i).isbn + "\n");
			textArea.append("Publisher: " + books.get(i).publisher + "\n");
			textArea.append("Year: " + books.get(i).year + "\n");
			textArea.append("Inventory: " + books.get(i).inventory + "\n\n");
		}
	}
	
}