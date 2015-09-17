package info.androidhive.slidingmenu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import info.androidhive.slidingmenu.R;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import info.androidhive.slidingmenu.library.CameraPreview;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanFragment extends Fragment {
	
	public ScanFragment(){}
	
	 private Camera mCamera;
	 private CameraPreview mPreview;
	 private Handler autoFocusHandler;
	 private static final String TAG_PID = "pid";

	 TextView scanText;
	 ImageScanner scanner;
	 private boolean barcodeScanned = false;
	 private boolean previewing = true;
	 FrameLayout preview;
	 
	 static {
		 System.loadLibrary("iconv");
	 }
	 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
       
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        
        mPreview = new CameraPreview(this.getActivity(), mCamera, previewCb, autoFocusCB);
        preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
  
        scanText = (TextView) rootView.findViewById(R.id.scanText);  
        return rootView;
    }
		
		public void onPause() {
			super.onPause();
			releaseCamera();
		}

    	/** A safe way to get an instance of the Camera object. */
    	public static Camera getCameraInstance(){
    		Camera c = null;
    		try {
    			c = Camera.open();
    			} catch (Exception e){
    		}
    		return c;
    	}

    	private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        	}
    	}

    	private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

        PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        scanText.setText(/**"barcode result " + **/sym.getData());
                        barcodeScanned = true;
                    }
                    String pid = ((TextView) getView().findViewById(R.id.scanText)).getText()
                            .toString();
     
                    Bundle bundle = new Bundle();
                    bundle.putString("pid", pid);
                    
                    ProductViewFragment fragment = new ProductViewFragment();
                    fragment.setArguments(bundle);
                    
                    FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
                    mFragmentTransaction.replace(R.id.frame_container, fragment, "Product view");
                    mFragmentTransaction.addToBackStack(null);
                    mFragmentTransaction.commit();
                }
            }
        };

        // Mimic continuous auto-focusing
        AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
}


