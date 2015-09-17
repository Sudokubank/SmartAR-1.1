package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.HomeFragment;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.DatabaseHandler;
import info.androidhive.slidingmenu.library.UserFunctions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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

public class UpdateProfileFragment extends Fragment {
	
	public UpdateProfileFragment(){}
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
   
    EditText oldpass;
    EditText newpass;
    EditText newfn;
    EditText newln;
    EditText newAddress1;
    EditText newAddress2;
    EditText newCity;
    EditText newState;
    EditText newPostal;
    EditText newCountry;
    EditText newPhone;
    TextView alert;
    Button changepass;
    Button cancel;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_updateprofile, container, false);
        
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        HashMap user = new HashMap();
        user = db.getUserDetails();
        
        cancel = (Button) rootView.findViewById(R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View arg0){

        	FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        	mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
        	mFragmentTransaction.replace(R.id.frame_container, new HomeFragment(), "Home");
        	mFragmentTransaction.commit();
            }

        });
        
        newfn = (EditText) rootView.findViewById(R.id.newfn);
        newln = (EditText) rootView.findViewById(R.id.newln);
        newpass = (EditText) rootView.findViewById(R.id.newpass);
        newAddress1 = (EditText) rootView.findViewById(R.id.newAddress1);
        newAddress2 = (EditText) rootView.findViewById(R.id.newAddress2);
        newCity = (EditText) rootView.findViewById(R.id.newCity);
        newState = (EditText) rootView.findViewById(R.id.newState);
        newPostal = (EditText) rootView.findViewById(R.id.newPostal);
        newCountry = (EditText) rootView.findViewById(R.id.newCountry);
        newPhone = (EditText) rootView.findViewById(R.id.newPhone);
        alert = (TextView) rootView.findViewById(R.id.alertpass);
        changepass = (Button) rootView.findViewById(R.id.btchangepass);

        //Retrive details
        newfn.setText((CharSequence) user.get("fname"));
        newln.setText( (CharSequence) user.get("lname"));
        newAddress1.setText((CharSequence) user.get("address1"));
        newAddress2.setText((CharSequence)user.get("address2"));
        newCity.setText((CharSequence)user.get("city"));
        newState.setText((CharSequence)user.get("state"));
        newPostal.setText((CharSequence)user.get("postal"));
        newCountry.setText((CharSequence)user.get("country"));
        newPhone.setText((CharSequence)user.get("phone"));
        
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (  	( !newfn.getText().toString().equals("")) && 
                		( !newln.getText().toString().equals("")) && 
                		( !newpass.getText().toString().equals("")) && 
                		( !newAddress1.getText().toString().equals("")) && 
                		( !newAddress2.getText().toString().equals("")) &&
                		( !newCity.getText().toString().equals(""))&& 
                		( !newState.getText().toString().equals(""))&& 
                		( !newPostal.getText().toString().equals(""))&&
                	    ( !newCountry.getText().toString().equals(""))&&
                	    ( !newPhone.getText().toString().equals("")))
                {
                    if ( newpass.getText().toString().length() > 5 ){
                    	NetAsync(view);
                    }
                    
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "New password should be minimum of 6 characters", Toast.LENGTH_SHORT).show();
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
	
	private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(UpdateProfileFragment.this.getActivity());
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                //alert.setText("Error in Network Connection");
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


        private ProgressDialog pDialog;

        String newpas,newfirst,newlast,email, newaddress1, 
        newaddress2,newcity,newstate,newpostal,newcountry,newphone ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();
            
            newfirst = newfn.getText().toString();
            newlast = newln.getText().toString();
            newpas = newpass.getText().toString();
            newaddress1  = newAddress1.getText().toString();
            newaddress2 = newAddress2.getText().toString();
            newcity = newCity.getText().toString();
            newstate = newState.getText().toString();
            newpostal = newPostal.getText().toString();
            newcountry = newCountry.getText().toString();
            newphone  = newPhone.getText().toString();
            email = user.get("email");

            pDialog = new ProgressDialog(UpdateProfileFragment.this.getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.chgPass(newpas,newfirst,newlast, email, newaddress1,
            		newaddress2, newcity, newstate, newpostal, newcountry, newphone);
            
            Log.d("Button", "Register");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    
                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        //alert.setText("Your Profile is successfully updated.");
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Your Profile is successfully updated.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        
                        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                       
                        db.updateProfile(
                        		newfirst,
                        		newlast,
                        		newaddress1,
                        		newaddress2,
                        		newcity,
                        		newstate,
                        		newpostal,
                        		newcountry,
                        		newphone
                        		);
                        
                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        alert.setText("Invalid old Password.");
                    } else {
                        pDialog.dismiss();
                        //alert.setText("Error occured in changing Password.");
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Error occured in changing Password.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
