package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.LoginFragment;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.DatabaseHandler;
import info.androidhive.slidingmenu.library.UserFunctions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {
	
	public RegisterFragment(){}
	
	/**
     *  JSON Response node names.
     **/
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_ADDRESS1 = "address1";
    private static String KEY_ADDRESS2 = "address2";
    private static String KEY_CITY = "city";
    private static String KEY_STATE = "state";
    private static String KEY_POSTAL = "postal";
    private static String KEY_COUNTRY = "country";
    private static String KEY_PHONE = "phone";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";
    
    /**
     * Defining layout items.
     **/
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputAddress1;
    EditText inputAddress2;
    EditText inputCity;
    EditText inputState;
    EditText inputpostal;
    EditText inputCountry;
    EditText inputPhone;
    Button btnRegister;
    TextView registerErrorMsg;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        /**
         * Defining all layout items
         **/
        inputFirstName = (EditText) rootView.findViewById(R.id.fname);
        inputLastName = (EditText) rootView.findViewById(R.id.lname);
        inputUsername = (EditText) rootView.findViewById(R.id.uname);
        inputEmail = (EditText) rootView.findViewById(R.id.email);
        inputPassword = (EditText) rootView.findViewById(R.id.pword);
        inputAddress1 =(EditText) rootView.findViewById(R.id.regAddress1);
        inputAddress2 = (EditText) rootView.findViewById(R.id.regAddress2);
        inputCity = (EditText) rootView.findViewById(R.id.regCity);
        inputState = (EditText) rootView.findViewById(R.id.regState);
        inputpostal = (EditText) rootView.findViewById(R.id.regPostal);
        inputCountry = (EditText) rootView.findViewById(R.id.regCountry);
        inputPhone = (EditText) rootView.findViewById(R.id.regPhone);
        btnRegister = (Button) rootView.findViewById(R.id.register);
        registerErrorMsg = (TextView) rootView.findViewById(R.id.register_error);
        
        /**
         * Button which Switches back to the login screen on clicked
         **/

                Button login = (Button) rootView.findViewById(R.id.bktologin);
                login.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
   
                        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                        mFragmentTransaction.replace(R.id.frame_container, new LoginFragment(), "Back To Login");
                        mFragmentTransaction.commit();
                    }

                });
                
           /**
           * Register Button click event.
           * A Toast is set to alert when the fields are empty.
           * Another toast is set to alert Username must be 6 characters.
           **/

                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (  	( !inputUsername.getText().toString().equals("")) && 
                        		( !inputPassword.getText().toString().equals("")) && 
                        		( !inputFirstName.getText().toString().equals("")) && 
                        		( !inputLastName.getText().toString().equals("")) && 
                        		( !inputEmail.getText().toString().equals("")) &&
                        		( !inputAddress1.getText().toString().equals(""))&& 
                        		( !inputAddress2.getText().toString().equals(""))&& 
                        		( !inputCity.getText().toString().equals(""))&&
                        	    ( !inputState.getText().toString().equals(""))&&
                        	    ( !inputpostal.getText().toString().equals(""))&&
                        	    ( !inputCountry.getText().toString().equals(""))&&
                        	    ( !inputPhone.getText().toString().equals("")))
                        {
                            if ( (inputUsername.getText().toString().length() > 5 ) &&
                            	 (inputPassword.getText().toString().length() > 5 )){
                            	NetAsync(view);
                            }
                            
                            else {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Username and password should be minimum of 5 characters", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "One or more fields are empty", Toast.LENGTH_SHORT).show();
                        }
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
            nDialog = new ProgressDialog(RegisterFragment.this.getActivity());
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
                    URL url = new URL("http://www.youtube.com");
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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                //registerErrorMsg.setText("Error in Network Connection");
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Error in Network Connection";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

    	/**
    	 * Defining Process dialog
    	 **/
        private ProgressDialog pDialog;

        String email,password,fname,lname,uname, address1, address2,
		city, state, postal, country, phone;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            address1 = inputAddress1.getText().toString();
            address2 = inputAddress2.getText().toString();
            city = inputCity.getText().toString();
            state = inputState.getText().toString();
            postal = inputpostal.getText().toString();
            country = inputCountry.getText().toString();
            phone = inputPhone.getText().toString();
            
            pDialog = new ProgressDialog(RegisterFragment.this.getActivity());
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
        	JSONObject json = userFunction.registerUser(fname, lname, email, uname, password, address1, address2,
        			city, state, postal, country, phone);
            return json;
        }
        
       @Override
        protected void onPostExecute(JSONObject json) {
    	/**
        * Checks for success message.
        **/
    	   
                try {
                	Log.e("JSON", json.toString());
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        String red = json.getString(KEY_ERROR);

                        if(Integer.parseInt(res) == 1){
                            pDialog.setTitle("Getting Data");
                            pDialog.setMessage("Loading Info");
                            
                            registerErrorMsg.setText("Successfully Registered");
                      
                            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");

                            /**
                             * Removes all the previous data in the SQlite database
                             **/
                            
                            UserFunctions logout = new UserFunctions();
                            logout.logoutUser(getActivity().getApplicationContext());
                            db.addUser(
                            		json_user.getString(KEY_FIRSTNAME),
                            		json_user.getString(KEY_LASTNAME),
                            		json_user.getString(KEY_EMAIL),
                            		json_user.getString(KEY_USERNAME),
                            		json_user.getString(KEY_ADDRESS1),
                            		json_user.getString(KEY_ADDRESS2),
                            		json_user.getString(KEY_CITY),
                            		json_user.getString(KEY_STATE),
                            		json_user.getString(KEY_POSTAL),
                            		json_user.getString(KEY_COUNTRY),
                            		json_user.getString(KEY_PHONE),
                            		json_user.getString(KEY_UID),
                            		json_user.getString(KEY_CREATED_AT));
                            
                            /**
                             * Stores registered data in SQlite Database
                             * Launch Login Fragment
                             **/ 
                           
                            FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                            mFragmentTransaction.replace(R.id.frame_container, new LoginFragment(), "Login Fragment");
                            mFragmentTransaction.commit();
                           
                            pDialog.dismiss();
                            //getActivity().finish();
                        }

                        else if (Integer.parseInt(red) ==2){
                            pDialog.dismiss();
                            registerErrorMsg.setText("User already exists");
                        }
                        else if (Integer.parseInt(red) ==3){
                            pDialog.dismiss();
                            registerErrorMsg.setText("Invalid Email id");
                        }

                    }

                    else{
                        pDialog.dismiss();
                            registerErrorMsg.setText("Error occured in registration");
                    }

                } catch (JSONException e) {
                	Log.d("ProcessRegister", "JSONException", e);
                	//e.printStackTrace();
                    
                }
            }
       }
    
        public void NetAsync(View view){
            new NetCheck().execute();
        }
}
