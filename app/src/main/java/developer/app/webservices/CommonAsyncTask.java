package developer.app.webservices;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;


public class CommonAsyncTask extends AsyncTask<Object, Void, ArrayList<String>>{
int method;
String url;
	public CommonAsyncTask(int method, String url) {
		// TODO Auto-generated constructor stub
		this.method=method;
		this.url=url;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<String> doInBackground(Object... params) {
		// TODO Auto-generated method stub
		WebServiceCall call=new WebServiceCall();
		ArrayList<String> al_responses = null;
		if(method==0){
			MyUtils.l("Method", "Type: GET");
			MyUtils.l("Url", "is: "+url);
			try {
				al_responses=call.get_data(url);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	if(method==1){
		JSONObject jsonObject=(JSONObject)params[0];
		try {
			MyUtils.l("Method", "Type: POST");
			MyUtils.l("Url", "is: "+url);
			al_responses=call.postDataWithParam(url, jsonObject);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		return al_responses;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
