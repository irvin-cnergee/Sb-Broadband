package developer.app.webservices;

import android.net.ParseException;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;


public class WebServiceCall {
	
	public ArrayList<String> postDataWithParam(String url ,JSONObject json_obj) throws ClientProtocolException,Exception  {
	    // Create a new HttpClient and Post Header
		ArrayList<String>al_response=new ArrayList<String>();
		String return_msg="OK";
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    httppost.setEntity(new StringEntity(json_obj.toString()));
//	    httppost.setHeader("Accept", "application/json");
	    httppost.setHeader("Content-type", "application/json");
		httppost.setHeader("platform", "android");

	    MyUtils.l("Request", "is:" + json_obj.toString());
	    try {
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity responseEntity = response.getEntity();
        	/* char[] buffer = new char[(int) responseEntity.getContentLength()];
             InputStream stream = responseEntity.getContent();
             InputStreamReader reader = new InputStreamReader(stream);
             reader.read(buffer);
             stream.close();
             String json_response = new String(buffer);*/
        	 
        	String json_response=getResponseBody(responseEntity);
	        if (response.getStatusLine().getStatusCode() == 200)
	        {
	            al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add(json_response);
	            al_response.add("");
	        }
	        else   if (String.valueOf(response.getStatusLine().getStatusCode()).charAt(0) == '4'){
	        	return_msg="We are unable to process your request";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add(json_response);
	        }
	        
	        else   if (String.valueOf(response.getStatusLine().getStatusCode()).charAt(0) == '5'){
	        	return_msg="Sorry we are facing technical problem!!";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add(json_response);
	        }
	        else
	        {
	        	return_msg="We are back on track in a minute!!";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add(json_response);
	        }
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	 Log.d("Error","is:"+e.toString());
	    	 al_response.add(return_msg);
	         al_response.add(String.valueOf("0"));
	         al_response.add(e.toString());
	    	
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	return_msg="error";
			Log.d("Error","is:"+e.toString());
	    	al_response.add(return_msg);
	    	al_response.add(String.valueOf("0"));
	    	al_response.add(e.toString());
	    }
	    
	    catch (Exception e) {
	        // TODO Auto-generated catch block
	    	return_msg="error";
	    	al_response.add(return_msg);
	    	al_response.add(String.valueOf("0"));
	    	al_response.add(e.toString());
	    	 
	    }
	    MyUtils.l("Result", "is: "+al_response.get(0));
	    MyUtils.l("Status Code", "is: "+al_response.get(1));
	    MyUtils.l("Response", "is: "+al_response.get(2));
	    
		return al_response;
	} 

	public ArrayList<String> get_data(String url ) throws ClientProtocolException,Exception  {
	    // Create a new HttpClient and Post Header
		ArrayList<String>al_response=new ArrayList<String>();
		String return_msg="OK";
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(url);
	    
	    httpget.setHeader("Accept", "application/json");
	    httpget.setHeader("Content-type", "application/json");

	    try {
	    
	        // Execute HTTP Get Request
	        HttpResponse response = httpclient.execute(httpget);
	        
	        if (response.getStatusLine().getStatusCode() == 200)
	        {
	        	HttpEntity responseEntity = response.getEntity();
	        	
	        	String json_response = getResponseBody(responseEntity);
	        	 
	            al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add(json_response);
	            al_response.add("");
	        }
	        else  if (String.valueOf(response.getStatusLine().getStatusCode()).charAt(0) == '4'){
	        	return_msg="We are unable to process your request";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add("Client side error");
	        }
	        
	        else   if (String.valueOf(response.getStatusLine().getStatusCode()).charAt(0) == '5'){
	        	return_msg="Sorry we are facing technical problem!!";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add("Server side error");
	        }
	        else{
	        	 return_msg="We are back on track in a minute!!";
	        	al_response.add(return_msg);
	            al_response.add(String.valueOf(response.getStatusLine().getStatusCode()));
	            al_response.add("");
	        }
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	 Log.d("Error","is:"+e.toString());
	    	 al_response.add(return_msg);
	         al_response.add(String.valueOf("0"));
	         al_response.add(e.toString());
	    	
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	return_msg="error";
	    	al_response.add(return_msg);
	    	al_response.add(String.valueOf("0"));
	    	al_response.add(e.toString());
	    	 
	    }
	    
	    catch (Exception e) {
	        // TODO Auto-generated catch block
	    	return_msg="error";
	    	al_response.add(return_msg);
	    	al_response.add(String.valueOf("0"));
	    	al_response.add(e.toString());
	    	 
	    }
	    MyUtils.l("Result", "is: "+al_response.get(0));
	    MyUtils.l("Status Code", "is: "+al_response.get(1));
	    MyUtils.l("Response", "is: "+al_response.get(2));
	    
		return al_response;
	} 
	
	
	public String getResponseBody(final HttpEntity entity) throws IOException, ParseException {


        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        InputStream instream = entity.getContent();

        if (instream == null) {
            return "";
        }

        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(

            "HTTP entity too large to be buffered in memory");
        }


        StringBuilder buffer = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(instream, HTTP.UTF_8));

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } finally {
            instream.close();
            reader.close();
        }

        return buffer.toString();

    }
}
