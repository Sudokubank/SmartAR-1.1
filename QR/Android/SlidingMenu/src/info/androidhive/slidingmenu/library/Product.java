package info.androidhive.slidingmenu.library;


public class Product {
	public String id;
	public String title;
	public String description;
	public double price;
	public int quantity;
	public boolean selected;

	public Product(String id, String title, double price ,
			String description, int quantity) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
	}

}

