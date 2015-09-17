package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectPaymentFragment extends Fragment {
	
	TextView textPayment;
	TextView  textGuide;
	ImageView maybank2u;
	ImageView cimbclicks;
	ImageView paypal;
	
	public SelectPaymentFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        
        textPayment = (TextView) rootView.findViewById(R.id.textPayment);
        textGuide = (TextView) rootView.findViewById(R.id.textGuide);
        maybank2u =  (ImageView) rootView.findViewById(R.id.imageView1);
        cimbclicks =  (ImageView) rootView.findViewById(R.id.imageView2);
        paypal =  (ImageView) rootView.findViewById(R.id.imageView3);
        
        return rootView;
    }
}
