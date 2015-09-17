package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.LoginFragment;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordResetFragment extends Fragment {
	
	public PasswordResetFragment(){}
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	EditText email;
	TextView alert;
	Button resetpass;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_passwordreset, container, false);
        Button login = (Button) rootView.findViewById(R.id.bktolog);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	 FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                 mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                 mFragmentTransaction.replace(R.id.frame_container, new LoginFragment(), "Back To Login");
                 mFragmentTransaction.commit();
            }
        });
        
        email = (EditText) rootView.findViewById(R.id.forpas);
        alert = (TextView) rootView.findViewById(R.id.alert);
        resetpass = (Button) rootView.findViewById(R.id.respass);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetAsync(view);
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
	        nDialog = new ProgressDialog(PasswordResetFragment.this.getActivity());
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

	    String forgotpassword;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        forgotpassword = email.getText().toString();

	        pDialog = new ProgressDialog(PasswordResetFragment.this.getActivity());
	        pDialog.setTitle("Contacting Servers");
	        pDialog.setMessage("Getting Data ...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    @Override
	    protected JSONObject doInBackground(String... args) {

	        UserFunctions userFunction = new UserFunctions();
	        JSONObject json = userFunction.forPass(forgotpassword);
	        return json;
	    }


	    @Override
	    protected void onPostExecute(JSONObject json) {
	  /**
	   * Checks if the Password Change Process is sucesss
	   **/
	        try {
	            if (json.getString(KEY_SUCCESS) != null) {
	                alert.setText("");
	                String res = json.getString(KEY_SUCCESS);
	                String red = json.getString(KEY_ERROR);


	                if(Integer.parseInt(res) == 1){
	                	pDialog.dismiss();
	                    //alert.setText("A recovery email is sent to you, see it for more details.");
	                    Context context = getActivity().getApplicationContext();
                        CharSequence text = "A recovery email is sent to you, see it for more details.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
	                }
	                else if (Integer.parseInt(red) == 2)
	                {    pDialog.dismiss();
	                    //alert.setText("Your email does not exist in our database.");
	                    Context context = getActivity().getApplicationContext();
                        CharSequence text = "Your email does not exist in our database.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
	                }
	                else {
	                    pDialog.dismiss();
	                    //alert.setText("Error occured in changing Password");
	                    Context context = getActivity().getApplicationContext();
                        CharSequence text = "Error occured in changing Password";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
	                }



	            }}
	        catch (JSONException e) {
	            e.printStackTrace();
	        	}
	    	}}

		public void NetAsync(View view){
			new NetCheck().execute();
		}
		
}




