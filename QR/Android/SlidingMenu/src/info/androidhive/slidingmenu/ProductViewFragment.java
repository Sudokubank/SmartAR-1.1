package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.ScanFragment;
import info.androidhive.slidingmenu.library.Controller;
import info.androidhive.slidingmenu.library.ModelProducts;
import info.androidhive.slidingmenu.library.ImageLoader;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.JSONParser1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class ProductViewFragment extends Fragment {
	
	public ProductViewFragment(){}
	
	TextView txtName;
	TextView txtPrice;
	TextView txtDesc;
	TextView productErrorMsg;
	EditText editTxtQuantity;
	ImageView image;
    Button btnAddtocart;
    Button btnCancel;
    public static String pid;
    public static String productName;
    public static double productPrice;
    public static String productDesc;
    public static int productQuantity;
 
    // JSON parser class
    JSONParser1 jsonParser = new JSONParser1();
 
    // single product url
    private static final String url_product_details = "http://www.zulanawi.com/learn2crack_login_api/";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    //private static final String TAG_pid = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
    //private static final String TAG_QUANTITY = "quantity";
	
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
   	@SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        
        // Loader image - will be shown before loading image
        int loader = R.drawable.loader;
        
        editTxtQuantity = (EditText)rootView.findViewById(R.id.inputQuantity);
        
        btnAddtocart = (Button) rootView.findViewById(R.id.btnAddtocart);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        productErrorMsg = (TextView) rootView.findViewById(R.id.product_error);
        
        txtName = (TextView) rootView.findViewById(R.id.inputName);
        txtPrice = (TextView) rootView.findViewById(R.id.inputPrice);
        txtDesc = (TextView) rootView.findViewById(R.id.inputDesc);
         
        // Imageview to show
        image = (ImageView) rootView.findViewById(R.id.image);
        
        // Getting productid from ScanFragment
        pid = getArguments().getString("pid");
        
        // Image url
        String image_url = "http://smartqr.droid-addict.com/upload/products/" + pid +".png";
         
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());
        
        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView 
        imgLoader.DisplayImage(image_url, loader, image);
        
        // Getting complete product details in background thread
        //new NetCheck().execute();
        new GetProductDetails().execute();
        
        if( editTxtQuantity.getText().toString().length() == 0 ){       	
        	editTxtQuantity.setText("1");
        }
        	 // Add to cart button click event
            btnAddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // starting background task to update product
                    //new SaveProductDetails().execute();
                	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
            		//Product arraylist size
                	 if( editTxtQuantity.getText().toString().length() == 0 ){       	
                		 Context context = getActivity().getApplicationContext();
                         CharSequence text = "Please enter at least 1 quantity";
                         int duration = Toast.LENGTH_SHORT;

                         Toast toast = Toast.makeText(context, text, duration);
                         toast.setGravity(Gravity.CENTER, 0, 0);
                         toast.show();
                     } else {
                    	 FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                         mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                         mFragmentTransaction.replace(R.id.frame_container, new ScreenFirstFragment(), "First Screen");
                         productQuantity =  new Integer(editTxtQuantity.getText().toString()).intValue();  
                         final Controller aController = (Controller) getActivity().getApplicationContext();
                         ModelProducts productObject;                                                         
                         // Create product model class object
                 		productObject = new ModelProducts(pid, productName,productDesc,productPrice, productQuantity);	
                 		//store product object to arraylist in controller
                 		aController.setProducts(productObject);
                    
                         mFragmentTransaction.commit();       
                     }                                        	
                }
            });
       
       
        
        // Cancel button click event
        btnCancel.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                //scan product again
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                mFragmentTransaction.replace(R.id.frame_container, new ScanFragment(), "Scan Fragment");
                mFragmentTransaction.commit();
            }
        });
        return rootView;
    }

	private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        /**@Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(productview.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }**/

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                //new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                //productErrorMsg.setText("Error in Network Connection");
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Error in Network Connection";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    } 
 	
    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {
    	
    	// Progress Dialog
        private ProgressDialog pDialog; 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductViewFragment.this.getActivity());
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));
 
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_details, "GET", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                      
                            String hub="RM ";
                            // display product data in TextView
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(hub + (product.getString(TAG_PRICE)));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                          
                            productName = product.getString(TAG_NAME);
                            productPrice = Double.valueOf(product.getString(TAG_PRICE)).doubleValue();
                            productDesc = product.getString(TAG_DESCRIPTION);
                           
                        } else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
}
