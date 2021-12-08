package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.ComplaintList;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Siddhesh on 5/30/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewholder> {

    List<ComplaintList> list = new ArrayList<>();
    Context context;
    RecyclerView rv;
    LinearLayout layout;
    String str_item = "";
    TextView tv_sel_item;
    Button btn_submit;
    String issuername = "";
    String crn = "";
    String issuerUsername = " ";
    int subscriberId ;
    int entityId ;
    int areaId ;
    AllAPIs api;
    TextView tv_fname;
    EditText met_comment;
    int productId;
    int complaintId;
    String complaintTitle = "";
    String fileName = "";
    String date = "";


    public CategoriesAdapter(FragmentActivity activity, List<ComplaintList> category_list, RecyclerView rv_category, LinearLayout ll_item, TextView tv_sel_item, Button btn_submit, AllAPIs api, int subscriberId, int entityId, int product_id) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(MyUtils.MY_PREF,Context.MODE_PRIVATE);
        issuerUsername =  sharedPreferences.getString(MyUtils.USER_NAME,"");
        issuername = sharedPreferences.getString(MyUtils.SUBSCRIBER_FULL_NAME,"");
        crn = sharedPreferences.getString(MyUtils.CRN,"");
        areaId = sharedPreferences.getInt(MyUtils.AREA_ID,0);

        context = activity;
        list = category_list;
        rv = rv_category;
        layout = ll_item;
        this.tv_sel_item = tv_sel_item;
        this.btn_submit = btn_submit;
        this.api = api;
        this.subscriberId = subscriberId;
        this.entityId = entityId;
        productId = product_id;

    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new MyViewholder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewholder holder, final int position) {

       /* holder.tv_item.setText(list.get(position));
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               rv.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                str_item = list.get(position);
//                Toast.makeText(context, str_item, Toast.LENGTH_SHORT).show();
                tv_sel_item.setText(str_item);
            }
        });
*/

            final ComplaintList cmp = list.get(position);
            holder.tv_item.setText(cmp.complaintTitle);
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
//                str_item = list.get(position);
    //                Toast.makeText(context, str_item, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context,cmp.complaintTitle+" "+ cmp.complaintId, Toast.LENGTH_SHORT).show();
                tv_sel_item.setText(cmp.complaintTitle);
                complaintId = cmp.complaintId;
                complaintTitle = cmp.complaintTitle;
            }
        });

        met_comment = (EditText)layout.findViewById(R.id.met_comment);
        final TextView tv_choose = (TextView)layout.findViewById(R.id.tv_choose);
        tv_fname = (TextView)layout.findViewById(R.id.tv_fname);

       /* tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "select image", Toast.LENGTH_SHORT).show();
                checkPermissions();

            }
        });*/

//        Button submit = (Button)layout.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,cmp.complaintTitle+" "+ cmp.complaintId, Toast.LENGTH_SHORT).show();
//                complaintId = cmp.complaintId;
//                complaintTitle = cmp.complaintTitle;
                /*String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                MyUtils.l("adapter","currentDateTimeString : "+currentDateTimeString);
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
                Date newDate = null;
                try {
                    newDate = format.parse(currentDateTimeString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                date = format.format(newDate);*/
                String attached_file = tv_fname.getText().toString();
                String[] arr = attached_file.split("/");
                if(arr.length > 0){
                    tv_fname.setText(arr[arr.length-1]);
                    fileName = arr[arr.length-1];
                }
                if(attached_file.equals(context.getString(R.string.selected_file))){
                    fileName = "";
                    ws_lanuchComplaint(complaintId,complaintTitle,issuername,issuerUsername,areaId,subscriberId,entityId, met_comment.getText().toString(),fileName,date);
                }else{
                    uploadFile(attached_file);
                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return  list.size();

    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView tv_item;

        public MyViewholder(View itemView) {
            super(itemView);

            tv_item = (TextView)itemView.findViewById(R.id.tv_item);

        }
    }

    int adminId = 0;

    private void ws_lanuchComplaint(Integer complaintId, String complaintTitle, String issuername, String issuerUsername, int areaId, int subscriberId, int entityId, String comment, String file, String date) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Please wait....");
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setConfirmText("Ok");

        if(!file.equals("")){
            file = "assets/uploads/customerCare/ticketAttachments/"+file;
        }

        Call<ResponseData> resApi = api.launchResponse(subscriberId,entityId,issuername,issuerUsername,areaId,complaintId,comment,file,date,adminId);
//        Call<ResponseData> resApi = api.launchResponse(2,2,"Shamal","testshamal12",2,complaintId,comment,file,date,adminId);
        resApi.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                ResponseData responseData = response.body();
                String msg = responseData.message;
                int status = responseData.status;

                MyUtils.l("ADAPTER","status : "+status+" msg : "+msg);
                if(status == 1){
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("Thank You");
                    sweetAlertDialog.setContentText(msg);
                }else{
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Failed");
                    sweetAlertDialog.setContentText(context.getString(R.string.went_wrong));
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Failed");
                sweetAlertDialog.setContentText(context.getString(R.string.went_wrong));
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
            }
        });

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                rv.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv_fname.setText(context.getString(R.string.selected_file));
                met_comment.setText("");
            }
        }).show();

    }

    private void uploadFile(String filePath) {

        try {
            // create upload service client
            AllAPIs service = APIClient.getClient(context).create(AllAPIs.class);
            File file = new File(filePath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MyUtils.l("tag",file.getName());
            MyUtils.l("tag",requestFile.contentLength()+"");

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            MyUtils.l("tag",body.toString());

            Call<ResponseBody> req = service.upload(body,requestFile);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Do Something
                    MyUtils.l("tag",response.body().contentLength()+"");
                    MyUtils.l("tag","Success");
                    int length = (int) response.body().contentLength();
                    if(length > 0){
                        ws_lanuchComplaint(complaintId,complaintTitle,issuername,issuerUsername,areaId,subscriberId,entityId, met_comment.getText().toString(),fileName,date);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();

                    MyUtils.l("onFailure ",t.toString());
                }
            });

        }catch (Exception r){
            MyUtils.l("error ",r.toString());
        }
    }

   }
