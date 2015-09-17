package info.androidhive.slidingmenu.library;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;
public class UserFunctions {
    private JSONParser jsonParser;
    private JSONParser1 jsonParser1;
    
    //URL of the PHP API
    private static String loginURL = "http://www.zulanawi.com/learn2crack_login_api/";
    private static String registerURL = "http://www.zulanawi.com/learn2crack_login_api/";
    private static String forpassURL = "http://www.zulanawi.com/learn2crack_login_api/";
    private static String chgpassURL = "http://www.zulanawi.com/learn2crack_login_api/";
    private static String orderURL = "http://www.zulanawi.com/learn2crack_login_api/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String billing_tag = "billing";
    private static String order_tag = "order";
    
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
        jsonParser1 = new JSONParser1();
    }
    
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    /**
     * Function to update profile
     **/
    public JSONObject chgPass(String newpas,String newFirst, String newLast, String email, String address1,
    		String address2, String city, String state, String postal, String country, String phone){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("newFirst", newFirst));
        params.add(new BasicNameValuePair("newLast", newLast));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("address1", address1));
        params.add(new BasicNameValuePair("address2", address2));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("state", state));
        params.add(new BasicNameValuePair("postal", postal));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("phone", phone));
        
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }
    
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
    
     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password,
    		String address1, String address2, String city, String state, String postal, String country,
    		String phone){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("address1", address1));
        params.add(new BasicNameValuePair("address2", address2));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("state", state));
        params.add(new BasicNameValuePair("postal", postal));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("phone", phone));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    } 
         
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
   
    /**
     * Function store order details
     **/
    public JSONObject orderDetails(String username, String[] pid, String[] quantity, String[] totalprice, String finalprice) {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", order_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("finalprice", finalprice));
        for (int i = 0; i < pid.length; i++) {
        	params.add(new BasicNameValuePair("pid[]", pid[i]));
        }	
        /*for (int j = 0; j < products.length; j++) {
        	params.add(new BasicNameValuePair("products[]", products[j]));
        }*/
        for (int k = 0; k < quantity.length; k++) {
        	params.add(new BasicNameValuePair("quantity[]", quantity[k]));
        }
        for (int l = 0; l < totalprice.length; l++) {
        	params.add(new BasicNameValuePair("totalprice[]", totalprice[l]));
        }
        
        JSONObject json = jsonParser.getJSONFromUrl(orderURL,params);
        return json;
    } 
    
    
   /**
    * Function store billing address
    **/
   public JSONObject billingAddress(String name, String address1, String address2, String city, String state,String postal, String country, String phone ){
       // Building Parameters
       List params = new ArrayList();
       params.add(new BasicNameValuePair("tag", billing_tag));
       params.add(new BasicNameValuePair("name", name));
       params.add(new BasicNameValuePair("address1", address1));
       params.add(new BasicNameValuePair("address2", address2));
       params.add(new BasicNameValuePair("city", city));
       params.add(new BasicNameValuePair("state", state));
       params.add(new BasicNameValuePair("postal", postal));
       params.add(new BasicNameValuePair("country", country));
       params.add(new BasicNameValuePair("phone", phone));
       JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
       return json;
   } 
}