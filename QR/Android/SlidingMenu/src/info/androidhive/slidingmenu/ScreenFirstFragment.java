package info.androidhive.slidingmenu;

import info.androidhive.slidingmenu.ScanFragment;
import info.androidhive.slidingmenu.library.Controller;
import info.androidhive.slidingmenu.library.ModelProducts;
import info.androidhive.slidingmenu.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.LayoutParams;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenFirstFragment extends Fragment {
	
	public ScreenFirstFragment(){}
	public double pFinalPrice;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_firstscreen, container, false);
        //final LinearLayout lm = (LinearLayout) rootView.findViewById(R.id.linearMain);
		final Button secondBtn = (Button) rootView.findViewById(R.id.second);
		final Button scanMoreBtn = (Button) rootView.findViewById(R.id.scanMore);
		
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		final Controller aController = (Controller) getActivity().getApplicationContext();
		
		/******* Create view elements dynamically and show on activity ******/
		

		// create the layout params that will be used to define how your
	    // button will be displayed
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	            //LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		/******** Dynamically create view elements - Start **********/
		 
		TableLayout ll = (TableLayout) rootView.findViewById(R.id.displayLinear);
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
        prodLabel.setTypeface(Typeface.SERIF, Typeface.NORMAL);
        prodLabel.setTextSize(13);
        prodLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(prodLabel,paramargin);
        
        // Price  column
        TextView priceLabel = new TextView(this.getActivity());
        priceLabel.setText("RM");
        priceLabel.setTypeface(Typeface.SERIF, Typeface.NORMAL);
        priceLabel.setTextSize(13);
        priceLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(priceLabel,paramargin);
        
        // Quantity column
        TextView quantityLabel = new TextView(this.getActivity());
        quantityLabel.setText("Amt");
        quantityLabel.setTypeface(Typeface.SERIF, Typeface.NORMAL);
        quantityLabel.setTextSize(13);
        quantityLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(quantityLabel,paramargin);
        
        // Total column
        TextView totalLabel = new TextView(this.getActivity());
        totalLabel.setText("Total");
        totalLabel.setTypeface(Typeface.SERIF, Typeface.NORMAL);
        totalLabel.setTextSize(13);
        totalLabel.setTextColor(android.graphics.Color.WHITE);
        rowProdLabels.addView(totalLabel,paramargin);
        
        ll.addView(rowProdLabels, params);
        
        /****** Title header end ******/
        
        
		for(int j=0;j< aController.getProductsArraylistSize();j++)
		{	
			// Get product data from product data arraylist
			String pName = aController.getProducts(j).getProductName();
			double pPrice   = aController.getProducts(j).getProductPrice();
			int pQuantity	= aController.getProducts(j).getProductQuantity();
			double pTotalPrice = pPrice * pQuantity;
			
			TableRow row= new TableRow(this.getActivity());
			row.setBackgroundColor(android.graphics.Color.WHITE);
			
			TextView product = new TextView(this.getActivity());
			product.setText(pName+"    ");
			product.setTextSize(14);
			//Add textView to LinearLayout
			row.addView(product);
			
			TextView price = new TextView(this.getActivity());
			price.setText(String.format("RM %.2f",pPrice));
			price.setTextSize(14);
			//Add textView to LinearLayout
			row.addView(price);
			
			TextView quantity = new TextView(this.getActivity());
			quantity.setText(pQuantity+"     ");
			quantity.setTextSize(14);
			//Add textView to LinearLayout
			row.addView(quantity);
			
			TextView totalprice = new TextView(this.getActivity());
			totalprice.setText(String.format("RM %.2f",pTotalPrice));
			totalprice.setTextSize(14);
			//Add textView to LinearLayout
			row.addView(totalprice);
			
			//Update final price
			pFinalPrice += pTotalPrice;
			
			final int index = j;
			Log.i("TAG", "index :" + index);
			// Get product instance for index
            
			final ModelProducts tempProductObject = aController.getProducts(index);
			
            if(!aController.getCart().checkProductInCart(tempProductObject))
            {
            	// Product not Exist in cart so add product to
            	// Cart product arraylist
            	aController.getCart().setProducts(tempProductObject);
            	
            	Toast.makeText(getActivity().getApplicationContext(), "Products in cart: "+aController.getCart().getCartSize(), 
            			Toast.LENGTH_LONG).show();
            }
              
			ll.addView(row,j+1, params);		    
		}
	
		/****** Title footer ******/
        TableRow rowFinalLabels = new TableRow(this.getActivity());
		rowFinalLabels.setBackgroundColor(android.graphics.Color.LTGRAY);
		
		// Price  column
        TextView finalPriceLabel = new TextView(this.getActivity());
        finalPriceLabel.setText("Final Price");
        finalPriceLabel.setTypeface(Typeface.SERIF, Typeface.NORMAL);
        finalPriceLabel.setTextSize(17);
        finalPriceLabel.setTextColor(android.graphics.Color.WHITE);
        rowFinalLabels.addView(finalPriceLabel, param);
        
		
		TextView finalprice = new TextView(this.getActivity());
		finalprice.setText(String.format("RM %.2f",pFinalPrice));
		finalprice.setTextColor(android.graphics.Color.WHITE);
		finalprice.setTypeface(Typeface.SERIF, Typeface.NORMAL);
		finalprice.setTextSize(17);
		//Add textView to LinearLayout
		rowFinalLabels.addView(finalprice,paramargin);
		
		ll.addView(rowFinalLabels);
		/****** Title footer end ******/
		
		/******** Dynamically create view elements - End **********/
		
		secondBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				 FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                 mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                 mFragmentTransaction.replace(R.id.frame_container, new ScreenSecondFragment(), "Cart");
                 mFragmentTransaction.commit();
				
			}
		});	
		
		scanMoreBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				 FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                 mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                 mFragmentTransaction.replace(R.id.frame_container, new ScanFragment(), "Scan Product");
                 mFragmentTransaction.commit();
			}
		});	 
        return rootView;
    }
}
