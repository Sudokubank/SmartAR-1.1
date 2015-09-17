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

public class BillingAddressOriFragment extends Fragment {
	
		public BillingAddressOriFragment(){}
		
		TextView txtFirstName;
		TextView txtLastName;
		TextView txtAddress1;
		TextView txtAddress2;
		TextView txtCity;
		TextView txtState;
		TextView txtPostal;
		TextView txtCountry;
		TextView txtPhone;
		Button btnPayNow;
		Button btnEditShipping;
		

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
     
            View rootView = inflater.inflate(R.layout.fragment_billingori, container, false);
            
            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
            HashMap user = new HashMap();
            user = db.getUserDetails();
            
            btnPayNow = (Button) rootView.findViewById(R.id.btnProceedToPayment);
            btnEditShipping = (Button) rootView.findViewById(R.id.btnUpdateShipping);
            txtFirstName = (TextView) rootView.findViewById(R.id.billOriFirstName);
            txtLastName = (TextView) rootView.findViewById(R.id.billOriLastName);                         
            txtAddress1 = (TextView) rootView.findViewById(R.id.billOriAddress1);
            txtAddress2 = (TextView) rootView.findViewById(R.id.billOriAddress2);
            txtCity = (TextView) rootView.findViewById(R.id.billOriCity);
            txtState = (TextView) rootView.findViewById(R.id.billOriState);
            txtPostal = (TextView) rootView.findViewById(R.id.billOriPostal);
            txtCountry = (TextView) rootView.findViewById(R.id.billOriCountry);
            txtPhone = (TextView) rootView.findViewById(R.id.billOriPhone);
            
            // display Profile in TextView
            txtFirstName.setText((CharSequence) user.get("fname"));
            txtLastName.setText( (CharSequence) user.get("lname"));
            txtAddress1.setText((CharSequence) user.get("address1"));
            txtAddress2.setText((CharSequence)user.get("address2"));
            txtCity.setText((CharSequence)user.get("city"));
            txtState.setText((CharSequence)user.get("state"));
            txtPostal.setText((CharSequence)user.get("postal"));
            txtCountry.setText((CharSequence)user.get("country"));
            txtPhone.setText((CharSequence)user.get("phone"));
            
            // Add to cart button click event
            btnPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // starting background task to update product
                    //new SaveProductDetails().execute();
                	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
            		//Product arraylist size
                    FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                    mFragmentTransaction.replace(R.id.frame_container, new SelectPaymentFragment(), "First Screen");
                    mFragmentTransaction.commit();
                	
                }
            });
            
            // Cancel button click event
            btnEditShipping.setOnClickListener(new View.OnClickListener() {
     
                @Override
                public void onClick(View arg0) {
                    //scan product again
                    FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                    mFragmentTransaction.replace(R.id.frame_container, new BillingAddressFragment(), "Home Fragment");
                    mFragmentTransaction.commit();
                }
            });
            return rootView;
        }
    }
