package developer.app;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import cnergee.sbbroadband.utils.MyUtils;


public class BaseApplication extends Application {


	static Context context;

	 public static Bus mEventBus;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyUtils.l("EventBus", "Initialised");
		mEventBus = new Bus(ThreadEnforcer.ANY);
		context=this;
	}

	    public static Bus getEventBus() {
	        return mEventBus;
	    }
	

}
