package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.SessionManagement;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
    ImageView smartqr;
    ImageView uniten;
    ImageView splash0;
    TextView guide;
    Button btnShopNow;
    Button btnAR;
    SessionManagement session;
 

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        guide = (TextView) rootView.findViewById(R.id.textWelcome1);
        smartqr = (ImageView) rootView.findViewById(R.id.ivSmartQR);
        uniten = (ImageView) rootView.findViewById(R.id.ivUniten);
        splash0 = (ImageView) rootView.findViewById(R.id.ivSplash0);
        btnShopNow = (Button) rootView.findViewById(R.id.btnShopNow);
        btnAR = (Button) rootView.findViewById(R.id.btnAR);
        
        session = new SessionManagement(getActivity().getApplicationContext());
        
    	btnAR.setOnClickListener(new View.OnClickListener() { //AR scanning button			

		public void onClick(View v) {
			
			Intent intent = new Intent (v.getContext(), ARLaunch.class);
			startActivityForResult(intent, 0);
			}
		});
        
        btnShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	
            	if (!session.isLoggedIn()) {
                	FragmentTransaction mFragmentTransaction1 = getFragmentManager().beginTransaction();
                    mFragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                	mFragmentTransaction1.replace(R.id.frame_container, new LoginFragment(), "Login");
                	mFragmentTransaction1.commit();
            	}
            	else{
                	FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                	mFragmentTransaction.replace(R.id.frame_container, new ScanFragment(), "Scan Fragment");
                	mFragmentTransaction.commit(); 
            	}
            }
        });
        
        return rootView;
    }
 
 
}
