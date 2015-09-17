package info.androidhive.slidingmenu;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.library.Controller;
import info.androidhive.slidingmenu.library.DatabaseHandler;
import info.androidhive.slidingmenu.library.ModelProducts;
import info.androidhive.slidingmenu.library.UserFunctions;
import info.androidhive.slidingmenu.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenSecondFragment extends Fragment {
	
	public ScreenSecondFragment(){}
	private static String KEY_SUCCESS = "success";
	
	public double pFinalPrice;
	public double[] pTotalPrice;
	public String[] sPID;
	public String[] sProduct;
	public String[] sQuantity;
	public String[] sTotalPrice;
	public String sFinalPrice;
	
	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_secondscreen, container, false);
        //TextView showCartContent    = (TextView)rootView.findViewById(R.id.showCart);
		final Button thirdBtn = (Button) rootView.findViewById(R.id.third);
		
		//Get Global Controller Class obiect (see application tag in AndroidManifest.xml)
		final Controller aController = (Controller) getActivity().getApplicationContext();
		
		// Get Cart Size
		final int cartSize = aController.getCart().getCartSize();
		
		/******** Dynamically create view elements - Start **********/
		 
		TableLayout ll = (TableLayout) rootView.findViewById(R.id.displayTable);
		ll.setStretchAllColumns(true);
		ll.setShrinkAllColumns(true);
		
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(1, 1, 1, 1);
		
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.span = 3;
		param.setMargins(10, 0, 10, 0);
		
		TableRow.LayoutParams paramargin = new TableRow.LayoutParams();
		paramargin.setMargins(10, 0, 10, 0);
		
		/****** Title header ******/
		TableRow rowProdLabels = new TableRow(this.getActivity());
		rowProdLabels.setBackgroundColor(android.graphics.Color.GRAY);
	
		// Product column
        TextView prodLabel = new TextView(this.getActivity());
        prodLabel.setText("Product");
        prodLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
        prodLabel.setTextSize(12);
        prodLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(prodLabel,paramargin);
        
        // Price  column
        TextView priceLabel = new TextView(this.getActivity());
        priceLabel.setText("Price");
        priceLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
        priceLabel.setTextSize(12);
        priceLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(priceLabel,paramargin);
        
        // Quantity column
        TextView quantityLabel = new TextView(this.getActivity());
        quantityLabel.setText("Amt");
        quantityLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
        quantityLabel.setTextSize(12);
        quantityLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(quantityLabel,paramargin);
        
        // Total column
        TextView totalLabel = new TextView(this.getActivity());
        totalLabel.setText("Total Price");
        totalLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
        totalLabel.setTextSize(12);
        totalLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(totalLabel,paramargin);
        
        // Delete
        TextView Delete = new TextView(this.getActivity());
        Delete.setText("DEL");
        Delete.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Delete.setTextSize(12);
        Delete.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(Delete,paramargin);
        
        ll.addView(rowProdLabels, params);
        
        /****** Title header end ******/
        // to initialize finalprice textview of ROW
        final TextView finalprice = new TextView(this.getActivity());
        
        pTotalPrice=new double[cartSize]; // store array of totalprice 
        
        /** To be sent to DB **/
        sPID = new String[cartSize];
        sProduct = new String[cartSize];
        sQuantity = new String[cartSize];
        sTotalPrice = new String[cartSize];
        
        
        if(cartSize >0)
        {
			for(int i=0;i<cartSize;i++)
			{	
				final int counter = i;
				// Get probuct data from product data arraylist
				String pID = aController.getProducts(i).getProductId();
				sPID[i] = pID;
				String pName = aController.getProducts(i).getProductName();
				double pPrice   = aController.getProducts(i).getProductPrice();
				int pQuantity	= aController.getProducts(i).getProductQuantity();
				sQuantity[i] = Integer.toString(pQuantity);
				pTotalPrice[i] = pPrice * pQuantity;
				sTotalPrice[i] = Double.toString(pTotalPrice[i]); 
				
				TableRow row= new TableRow(this.getActivity());
				row.setBackgroundColor(android.graphics.Color.WHITE);
				
				TextView product = new TextView(this.getActivity());
				product.setText(pName+"    ");
				product.setTextSize(12);
				//Add textView to LinearLayout
				row.addView(product,paramargin);
				
				TextView price = new TextView(this.getActivity());
				price.setText(String.format("RM %.2f",pPrice));
				price.setTextSize(12);
				//Add textView to LinearLayout
				row.addView(price,paramargin);
				
				TextView quantity = new TextView(this.getActivity());
				quantity.setText(pQuantity+"     ");
				quantity.setTextSize(12);
				//Add textView to LinearLayout
				row.addView(quantity,paramargin);
				
				TextView totalprice = new TextView(this.getActivity());
				totalprice.setText(String.format("RM %.2f",pTotalPrice[i]));
				totalprice.setTextSize(12);
				//Add textView to LinearLayout
				row.addView(totalprice,paramargin);
				
				//Update final price
				pFinalPrice += pTotalPrice[i];
				
				final int index = i;
				Log.i("TAG", "index :" + index);
				// Get product instance for index
	            
				final ModelProducts tempProductObiect = aController.getProducts(index);
				
				int buttonNormalResId = R.drawable.button_delete;
				int buttonPressedResId = R.drawable.button_delete;
				Resources resources = getActivity().getApplicationContext().getResources();
				
				StateListDrawable states = new StateListDrawable();
				states.addState(new int[] {android.R.attr.state_pressed}, resources.getDrawable(buttonPressedResId));
				states.addState(new int[] {android.R.attr.state_focused}, resources.getDrawable(buttonPressedResId));
				states.addState(new int[] { }, resources.getDrawable(buttonNormalResId));
				//((Button) rootView.findViewById(R.id.button_delete)).setBackgroundDrawable(states);
				
				
				
	            final Button btnRemove = new Button(this.getActivity());
	            btnRemove.setId(i+1);
	            btnRemove.setBackgroundDrawable(states);
	            btnRemove.setMinimumWidth(0);
	            btnRemove.setMinimumHeight(0);
	            btnRemove.setWidth(1);
	            btnRemove.setHeight(1);
	            btnRemove.setOnClickListener(new OnClickListener()
	            {
	                @Override public void onClick(View v)
	                {
	 
	                	if(aController.getCart().checkProductInCart(tempProductObiect))
			            {
	                		// Product not Exist in cart so add product to
	    	            	// Cart product arraylist
	    	            	aController.getCart().removeProducts(tempProductObiect);
	    	            	
	    	            	pFinalPrice -= pTotalPrice[counter];
	    	            	
	    	            	finalprice.setText(String.format("RM %.2f",pFinalPrice));
	    	            	Toast.makeText(getActivity().getApplicationContext(), "Now Cart size: "+aController.getCart().getCartSize(), 
	    	            			Toast.LENGTH_LONG).show();
			            }
	                	// row is your row, the parent of the clicked button
	                    View row = (View) v.getParent();
	                    // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
	                    ViewGroup container = ((ViewGroup)row.getParent());
	                    // delete the row and invalidate your view so it gets redrawn
	                    container.removeView(row);
	                    container.invalidate();
	                  
	                }
	            });
	           
				row.addView(btnRemove);
				   
				ll.addView(row,i+1, params);		    
			}
        }
		
        /****** Title footer ******/
        TableRow rowFinalLabels = new TableRow(this.getActivity());
		rowFinalLabels.setBackgroundColor(android.graphics.Color.LTGRAY);
		
		// Price  column
        TextView finalPriceLabel = new TextView(this.getActivity());
        finalPriceLabel.setText("FINAL PRICE");
        finalPriceLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
        finalPriceLabel.setTextSize(12);
        finalPriceLabel.setTextColor(android.graphics.Color.WHITE);
        rowFinalLabels.addView(finalPriceLabel,param);
        
		
		//final TextView finalprice = new TextView(this.getActivity());
		finalprice.setText(String.format("RM %.2f",pFinalPrice));
		sFinalPrice = Double.toString(pFinalPrice);
		finalprice.setTextColor(android.graphics.Color.WHITE);
		finalprice.setTypeface(Typeface.SERIF, Typeface.BOLD);
		finalprice.setTextSize(12);
		//Add textView to LinearLayout
		rowFinalLabels.addView(finalprice,paramargin);
		
		ll.addView(rowFinalLabels);
		/****** Title footer end ******/
		
		thirdBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(cartSize > 0)
				{
					//Intent i = new Intent(getBaseContext(), ThirdScreen.class);
					//startActivity(i);
					FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
	                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
	                mFragmentTransaction.replace(R.id.frame_container, new BillingAddressOriFragment(), "Cart");
	                NetAsync(v);
	                mFragmentTransaction.commit();
				}
				else
					Toast.makeText(getActivity().getApplicationContext(), 
							"Shopping cart is empty.", 
	            			Toast.LENGTH_LONG).show();
			}
		}); 
        return rootView;
    }
	
	 private class ProcessOrder extends AsyncTask<String, String, JSONObject> {
		 
	        private ProgressDialog pDialog;

	        String username;
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

	            HashMap user = new HashMap();
	            user = db.getUserDetails();
	          
	            username = (String) user.get("uname");

	            pDialog = new ProgressDialog(ScreenSecondFragment.this.getActivity());
	            pDialog.setTitle("Contacting Servers");
	            pDialog.setMessage("Getting Data ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

	        @Override
	        protected JSONObject doInBackground(String... args) {

	            UserFunctions userFunction = new UserFunctions();
	            JSONObject json = userFunction.orderDetails(username, sPID, sQuantity, sTotalPrice, sFinalPrice);
	            
	            Log.d("Button", "Order");
	            return json;

	        }
	        @Override
	        protected void onPostExecute(JSONObject json) {
	            try {
	               if (json.getString(KEY_SUCCESS) != null) {
	            	   pDialog.dismiss();	         
	               }
	                   else{
	                        pDialog.dismiss();	                 
	                        Context context = getActivity().getApplicationContext();
	                        CharSequence text = "Error!";
	                        int duration = Toast.LENGTH_SHORT;

	                        Toast toast = Toast.makeText(context, text, duration);
	                        toast.setGravity(Gravity.CENTER, 0, 0);
	                        toast.show();
	                    }
	                
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	       }
	  }
	    
	 public void NetAsync(View view){
		 new ProcessOrder().execute();
	 }
}
