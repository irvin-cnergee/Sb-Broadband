package developer.app.obj;

/**
 * Created by Sandip on 8/3/2016.
 */
public class CommonWebResponse {

    String Message;
    Object proceed_object;
    String proceed_status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public Object getProceed_object() {
        return proceed_object;
    }

    public void setProceed_object(Object proceed_object) {
        this.proceed_object = proceed_object;
    }

    public String getProceed_status() {
        return proceed_status;
    }

    public void setProceed_status(String proceed_status) {
        this.proceed_status = proceed_status;
    }
}
