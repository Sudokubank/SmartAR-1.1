package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import info.androidhive.slidingmenu.library.*;

public class LoginFragment extends Fragment {
	
	private Button btnLogin;
    private Button Btnregister;
    private TextView passreset;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginErrorMsg;
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_ADDRESS1 = "address1";
    private static String KEY_ADDRESS2 = "address2";
    private static String KEY_CITY = "city";
    private static String KEY_STATE = "state";
    private static String KEY_POSTAL = "postal";
    private static String KEY_COUNTRY = "country";
    private static String KEY_PHONE = "phone";
    private static String KEY_CREATED_AT = "created_at";
    SessionManagement session;
    
	public LoginFragment(){}
	
	public void onCreate(Bundle state){
		super.onCreate(state);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        session = new SessionManagement(getActivity().getApplicationContext());
        
        inputEmail = (EditText) rootView.findViewById(R.id.email);
        inputPassword = (EditText) rootView.findViewById(R.id.pword);
        Btnregister = (Button) rootView.findViewById(R.id.registerbtn);
        btnLogin = (Button) rootView.findViewById(R.id.login);
        passreset = (TextView) rootView.findViewById(R.id.passres);
        loginErrorMsg = (TextView) rootView.findViewById(R.id.loginErrorMsg);

        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        
        	FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        	mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
        	mFragmentTransaction.replace(R.id.frame_container, new PasswordResetFragment(), "Reset Password");
        	mFragmentTransaction.commit();
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	 FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                 mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                 mFragmentTransaction.replace(R.id.frame_container, new RegisterFragment(), "Register");
                 mFragmentTransaction.commit();
            	
             }});

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                    NetAsync(view);
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                } 
            }
        }); 
        return rootView;
    }
	/**
	 * Async Task to check whether internet connection is working.
	 **/

	    private class NetCheck extends AsyncTask<String,String,Boolean>
	    {
	        private ProgressDialog nDialog;

	        @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            nDialog = new ProgressDialog(LoginFragment.this.getActivity());
	            nDialog.setTitle("Checking Network");
	            nDialog.setMessage("Loading..");
	            nDialog.setIndeterminate(false);
	            nDialog.setCancelable(true);
	            nDialog.show();
	        }
	        /**
	         * Gets current device state and checks for working internet connection by trying Google.
	        **/
	        @Override
	        protected Boolean doInBackground(String... args){


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
	                new ProcessLogin().execute();
	            }
	            else{
	                nDialog.dismiss();
	                loginErrorMsg.setText("Error in Network Connection");
	            }
	        }
	    }

	    /**
	     * Async Task to get and send data to My Sql database through JSON respone.
	     **/
	    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


	        private ProgressDialog pDialog;

	        String email,password;

	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            inputEmail = (EditText) getView().findViewById(R.id.email);
	            inputPassword = (EditText) getView().findViewById(R.id.pword);
	            email = inputEmail.getText().toString();
	            password = inputPassword.getText().toString();
	            
	            pDialog = new ProgressDialog(LoginFragment.this.getActivity());
	            pDialog.setTitle("Contacting Servers");
	            pDialog.setMessage("Logging in ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

	        @Override
	        protected JSONObject doInBackground(String... args) {

	            UserFunctions userFunction = new UserFunctions();
	            JSONObject json = userFunction.loginUser(email, password);
	            // Creating user login session
	            // Use user real data
	            session.createLoginSession(KEY_USERNAME, KEY_EMAIL);
	            return json;
	        }

	        @Override
	        protected void onPostExecute(JSONObject json) {
	            try {
	               if (json.getString(KEY_SUCCESS) != null) {

	                    String res = json.getString(KEY_SUCCESS);

	                    if(Integer.parseInt(res) == 1){
	                        pDialog.setMessage("Loading User Space");
	                        pDialog.setTitle("Getting Data");
	                        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
	                        
	                        JSONObject json_user = json.getJSONObject("user");
	                        /**
	                         * Clear all previous data in SQlite database.
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
	                        *If JSON array details are stored in SQlite it launches the User Panel.
	                        **/
	                        Intent upanel = new Intent(getActivity().getApplicationContext(), MainActivity.class);
	                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                        pDialog.dismiss();
	                        startActivity(upanel);
	                        /**
	                         * Close Login Screen
	                         **/
	                        getActivity().finish();
	                    }else{
	                        pDialog.dismiss();
	                        //loginErrorMsg.setText("Incorrect username/password");
	                        Context context = getActivity().getApplicationContext();
	                        CharSequence text = "Incorrect username/password!";
	                        int duration = Toast.LENGTH_SHORT;

	                        Toast toast = Toast.makeText(context, text, duration);
	                        toast.setGravity(Gravity.CENTER, 0, 0);
	                        toast.show();
	                    }
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	       }
	    }
	    public void NetAsync(View view){
	        new NetCheck().execute();
	    }
}
