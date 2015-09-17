package info.androidhive.slidingmenu;


import java.util.HashMap;
import info.androidhive.slidingmenu.library.DatabaseHandler;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	
		public ProfileFragment(){}
		
		TextView txtFirstName;
		TextView txtLastName;
		TextView txtEmail;
		TextView txtUsername;
		TextView txtAddress1;
		TextView txtAddress2;
		TextView txtCity;
		TextView txtState;
		TextView txtPostal;
		TextView txtCountry;
		TextView txtPhone;
		Button btnEditProfile;
		Button btnProfileCancel;
		

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
     
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            
            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            
            btnEditProfile = (Button) rootView.findViewById(R.id.btnEditProfile);
            btnProfileCancel = (Button) rootView.findViewById(R.id.btnProfileCancel);
            txtFirstName = (TextView) rootView.findViewById(R.id.inputFirstName);
            txtLastName = (TextView) rootView.findViewById(R.id.inputLastName);
            txtUsername = (TextView) rootView.findViewById(R.id.inputUsername);
            txtEmail = (TextView) rootView.findViewById(R.id.inputEmail);              
            txtAddress1 = (TextView) rootView.findViewById(R.id.inputAddress1);
            txtAddress2 = (TextView) rootView.findViewById(R.id.inputAddress2);
            txtCity = (TextView) rootView.findViewById(R.id.inputCity);
            txtState = (TextView) rootView.findViewById(R.id.inputState);
            txtPostal = (TextView) rootView.findViewById(R.id.inputPostal);
            txtCountry = (TextView) rootView.findViewById(R.id.inputCountry);
            txtPhone = (TextView) rootView.findViewById(R.id.inputPhone);
            
            // display Profile in TextView
            txtFirstName.setText((CharSequence) user.get("fname"));
            txtLastName.setText( (CharSequence) user.get("lname"));
            txtEmail.setText( (CharSequence) user.get("email"));
            txtUsername.setText((CharSequence) user.get("uname"));
            txtAddress1.setText((CharSequence) user.get("address1"));
            txtAddress2.setText((CharSequence)user.get("address2"));
            txtCity.setText((CharSequence)user.get("city"));
            txtState.setText((CharSequence)user.get("state"));
            txtPostal.setText((CharSequence)user.get("postal"));
            txtCountry.setText((CharSequence)user.get("country"));
            txtPhone.setText((CharSequence)user.get("phone"));
            
            // Add to cart button click event
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // starting background task to update product
                    //new SaveProductDetails().execute();
                	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
            		//Product arraylist size
                    FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                    mFragmentTransaction.replace(R.id.frame_container, new UpdateProfileFragment(), "Update Profile");
                    mFragmentTransaction.commit();
                	
                }
            });
            
            // Cancel button click event
            btnProfileCancel.setOnClickListener(new View.OnClickListener() {
     
                @Override
                public void onClick(View arg0) {
                    //scan product again
                    FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                    mFragmentTransaction.replace(R.id.frame_container, new HomeFragment(), "Home Fragment");
                    mFragmentTransaction.commit();
                }
            });
            return rootView;
        }
    }
