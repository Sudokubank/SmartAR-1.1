package info.androidhive.slidingmenu.library;

public class ModelProducts {
    
	private String productId;
    private String productName;
    private String productDesc;
    private double productPrice;
    private int productQuantity;
     
    public ModelProducts(String productId, String productName,String productDesc,double productPrice
    		, int productQuantity)
    {
        this.productId = productId;
    	this.productName  = productName;
        this.productDesc  = productDesc;
        this.productPrice = productPrice;
        this.setProductQuantity(productQuantity);
    }
    
    public String getProductId() {
    	return productId;
    }
     
    public String getProductName() {
         
        return productName;
    }
    
    public String getProductDesc() {
         
        return productDesc;
    }
     
    public double getProductPrice() {
         
        return productPrice;
    }

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
         
}