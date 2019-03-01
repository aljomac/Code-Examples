import javax.swing.*;

public class MainUI extends JApplet {
	private DataManager dm = new DataManager();
	private OrderBookControl orderBookControl = new OrderBookControl(dm);
	private OrderBookUI orderBookUI = new OrderBookUI(orderBookControl);
	private ViewOrderStatusControl viewOrderStatusControl = new ViewOrderStatusControl(dm);
	private ViewOrderStatusUI viewOrderStatusUI = new ViewOrderStatusUI(viewOrderStatusControl);
	
	public void init() {
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Order Book", orderBookUI);
		tabs.add("View Order Status", viewOrderStatusUI);
		this.add(tabs);
		this.setSize(600, 600);
	}
}
