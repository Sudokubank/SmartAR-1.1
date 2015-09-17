package info.androidhive.slidingmenu;

//import info.androidhive.slidingmenu.library.Controller;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.UserFunctions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BillingAddressFragment extends Fragment {

    /**
     * Defining layout items.
     **/
    TextView textBillingAddress;
	TextView textBillName;
	EditText etName;
	TextView textAddress;
	EditText etAddress1;
	EditText etAddress2;
	TextView textCity;
	EditText etCity;
	TextView textState;
	EditText etState;
	TextView textPostalCode;
	EditText etPostal;
	TextView textCountry;
	EditText etCountry;
	TextView textPhone;
	EditText etPhone;
	Button btnNext;
	
	public BillingAddressFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_billing, container, false);
        
        textBillingAddress = (TextView) rootView.findViewById(R.id.textBillingAddress);
        textBillName = (TextView) rootView.findViewById(R.id.textBillName);
        textAddress = (TextView) rootView.findViewById(R.id.textAddress);
        textCity = (TextView) rootView.findViewById(R.id.textCity);
        textState = (TextView) rootView.findViewById(R.id.textState);
        textPostalCode = (TextView) rootView.findViewById(R.id.textPostal);
        textCountry = (TextView) rootView.findViewById(R.id.textCountry);
        textPhone = (TextView) rootView.findViewById(R.id.textPhone);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        
        etName =(EditText) rootView.findViewById(R.id.billName);
        etAddress1 =(EditText) rootView.findViewById(R.id.billAddress1);
        etAddress2 = (EditText) rootView.findViewById(R.id.billAddress2);
        etCity = (EditText) rootView.findViewById(R.id.billCity);
        etState = (EditText) rootView.findViewById(R.id.billState);
        etPostal = (EditText) rootView.findViewById(R.id.billPostal);
        etCountry = (EditText) rootView.findViewById(R.id.billCountry);
        etPhone = (EditText) rootView.findViewById(R.id.billPhone);
        
        // Go to select payment button click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                mFragmentTransaction.replace(R.id.frame_container, new SelectPaymentFragment(), "First Screen");
                mFragmentTransaction.commit();
            	
            }
        });
         
        return rootView;
    }
	
	/**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(BillingAddressFragment.this.getActivity());
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

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
                new ProcessBilling().execute(); 
            }
            else{
                nDialog.dismiss();
                //registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }
    
    private class ProcessBilling extends AsyncTask<String, String, JSONObject> {

    	/**
    	 * Defining Process dialog
    	 **/
        private ProgressDialog pDialog;

        String name, address1, address2, city, state, postal, country, phone;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           
            name = etName.getText().toString();
            address1 = etAddress1.getText().toString();
            address2 = etAddress2.getText().toString();
            city = etCity.getText().toString();
            state = etState.getText().toString();
            postal = etPostal.getText().toString();
            country = etCountry.getText().toString();
            phone = etPhone.getText().toString();
            
            pDialog = new ProgressDialog(BillingAddressFragment.this.getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	// Register user on the database
        	UserFunctions userFunction = new UserFunctions();
        	JSONObject json = userFunction.billingAddress(name, address1, address2, city, state, postal, country, phone);
            return json;
        }
        
       @Override
        protected void onPostExecute(JSONObject json) {
       /**
        * Checks for success message.
        **/
        }	
    }
    
        public void NetAsync(View view){
            new NetCheck().execute();
        }
}
