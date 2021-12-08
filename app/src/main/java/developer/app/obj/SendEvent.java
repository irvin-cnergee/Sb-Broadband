package developer.app.obj;

import java.util.HashMap;

public class SendEvent {

	String event_name;
	HashMap<String, String> hm_event_data;

	public SendEvent(String event_name, HashMap<String, String> hm_event_data) {
		this.event_name = event_name;
		this.hm_event_data = hm_event_data;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public HashMap<String, String> getHm_event_data() {
		return hm_event_data;
	}

	public void setHm_event_data(HashMap<String, String> hm_event_data) {
		this.hm_event_data = hm_event_data;
	}


}
