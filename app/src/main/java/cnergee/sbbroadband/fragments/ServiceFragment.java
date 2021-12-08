package cnergee.sbbroadband.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.SelfResolutionActivity;
import cnergee.sbbroadband.adapters.CategoriesAdapter;
import cnergee.sbbroadband.retroObj.ComplaintData;
import cnergee.sbbroadband.retroObj.ComplaintList;
import cnergee.sbbroadband.retroObj.CustomerCareData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 5/30/2017.
 */

public class ServiceFragment extends Fragment {

    private static final String TAG =  "ServiceFragment";

    RecyclerView rv_category;
    CategoriesAdapter categoriesAdapter;
    ArrayList<String> category_list;
    LinearLayout ll_item;
    TextView tv_sel_item,tv_choose,tv_fname;
    Button btn_submit;
    AllAPIs api;
    int product_id ;

    String imgPath = "";

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    SharedPreferences sharedPreferences;

    int entityId = 0;
    int subscriberId = 0;

   /* private static final String PERMISSIONS_REQUIRED[] = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };*/

    String[] arr ;
    private String[] PERMISSIONS_REQUIRED;

    ArrayList<String> list = new ArrayList<>();

    private static final int REQUEST_PERMISSIONS = 100;

    ImageView img_call,img_self,img_error;
    String from = "";

    ToggleButton toggleButton;

    LinearLayout ll_service;
    ProgressWheel progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_content,container,false);

        ((DashboardActivity)getActivity()).updateTitle(getString(R.string.service_title));

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        rv_category = (RecyclerView)view.findViewById(R.id.rv_category);
        ll_item = (LinearLayout)view.findViewById(R.id.ll_item);
        tv_sel_item = (TextView)view.findViewById(R.id.tv_sel_item);
        tv_choose = (TextView)view.findViewById(R.id.tv_choose);
        tv_fname = (TextView)view.findViewById(R.id.tv_fname);
        btn_submit = (Button)view.findViewById(R.id.btn_submit);
        img_call = (ImageView)view.findViewById(R.id.img_call);
        img_self = (ImageView)view.findViewById(R.id.img_self);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggle);
        img_error = (ImageView) view.findViewById(R.id.img_error);
        ll_service = (LinearLayout) view.findViewById(R.id.ll_service);
        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                list.add( Manifest.permission.CALL_PHONE);
                PERMISSIONS_REQUIRED = new String[list.size()];
                list.toArray(PERMISSIONS_REQUIRED);

                MyUtils.l(TAG,"arr : "+PERMISSIONS_REQUIRED[0]);
                from = "call";

                checkPermissions();

            }
        });
        img_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SelfResolutionActivity.class));
            }
        });

        api = APIClient.getClient(getActivity()).create(AllAPIs.class);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_category.setLayoutManager(manager);
        rv_category.setItemAnimator(new DefaultItemAnimator());

        getComplaintService(entityId,1);


        toggleButton.setChecked(true);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                rv_category.setVisibility(View.VISIBLE);
                ll_item.setVisibility(View.GONE);
                if(isChecked){
//                    Toast.makeText(getActivity(), "Internet", Toast.LENGTH_SHORT).show();
                    getComplaintService(entityId,1);
                }else {
//                    Toast.makeText(getActivity(), "TV", Toast.LENGTH_SHORT).show();
                    getComplaintService(entityId,2);
                }

            }
        });

        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "select image", Toast.LENGTH_SHORT).show();

                list.clear();
                list.add( Manifest.permission.CAMERA);
                list.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);
                list.add( Manifest.permission.READ_EXTERNAL_STORAGE);
                PERMISSIONS_REQUIRED = new String[list.size()];
                list.toArray(PERMISSIONS_REQUIRED);
                from = "choose";

                MyUtils.l(TAG,"arr : "+PERMISSIONS_REQUIRED[0]);

                checkPermissions();

            }
        });

        return view;
    }

    private void getComplaintService(int entityId, int productId) {

        if (MyUtils.isOnline(getActivity())){
            product_id = productId;
            ws_getCompplaints(entityId,productId);
            ws_getCustomerCareNumber(entityId);
        }else {
            ll_service.setVisibility(View.GONE);
            img_error.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
//            Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

    }

    String customerCare = "";

    public void ws_getCustomerCareNumber(int entityId){

        ll_service.setVisibility(View.VISIBLE);
        img_error.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        Call<CustomerCareData> careAPI = api.getCustomerCareData(entityId);
        careAPI.enqueue(new Callback<CustomerCareData>() {
            @Override
            public void onResponse(Call<CustomerCareData> call, Response<CustomerCareData> response) {

                if(response.isSuccessful()){
                    CustomerCareData customerCareData = response.body();

                    customerCare = customerCareData.customerCare;
                }
            }

            @Override
            public void onFailure(Call<CustomerCareData> call, Throwable t) {

            }
        });

    }

    public void callCustomerCare(){
//        if(customerCare != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "8792911860"));
            startActivity(callIntent);
        /*}else {
            Toast.makeText(getActivity(), "Customer care number not available", Toast.LENGTH_SHORT).show();

        }*/
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tv_sel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_item.setVisibility(View.GONE);
                rv_category.setVisibility(View.VISIBLE);
            }
        });

       /* btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), tv_sel_item.getText(), Toast.LENGTH_SHORT).show();
                if(MyUtils.isOnline(getActivity())){
                    ws_lanuchComplaint();
                }
            }
        });*/

    }

    private void ws_getCompplaints(int entityId, int productId) {

        ll_service.setVisibility(View.VISIBLE);
        img_error.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setTitleText("Please wait....");

        Call<ComplaintData> comAPI = api.getComplaints(entityId,productId);
        comAPI.enqueue(new Callback<ComplaintData>() {
            @Override
            public void onResponse(Call<ComplaintData> call, Response<ComplaintData> response) {

               if(response.isSuccessful()){
                   ComplaintData data = response.body();
                   int status = data.status;
                   String msg = data.msg;
                   List<ComplaintList> list = data.complaintList;

                   if(status == 1){

                       sweetAlertDialog.dismiss();
//                    if(list.size() > 0){

                       try {
                           categoriesAdapter = new CategoriesAdapter(getActivity(),list, rv_category , ll_item,tv_sel_item , btn_submit,api,subscriberId, ServiceFragment.this.entityId, product_id);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                manager.getOrientation());
        rv_category.addItemDecoration(dividerItemDecoration);*/
                       rv_category.setAdapter(categoriesAdapter);

                    /*}else{
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setContentText("No Complaint data found");
                        sweetAlertDialog.setTitleText("Oops...");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();
                    }*/

                       if(list.size() == 0){
                           sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                           sweetAlertDialog.setContentText("No Complaint data found");
                           sweetAlertDialog.setTitleText("Oops...");
                           sweetAlertDialog.setConfirmText("Ok");
                           sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   sweetAlertDialog.dismiss();
                               }
                           });
                           sweetAlertDialog.show();
                       }

                   }else if(status == 0) {
                       sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                       sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                       sweetAlertDialog.setTitleText("Error");
                       sweetAlertDialog.setConfirmText("Ok");
                       sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               sweetAlertDialog.dismiss();
                           }
                       });
                   }
               }
            }

            @Override
            public void onFailure(Call<ComplaintData> call, Throwable t) {

            }
        });

        sweetAlertDialog.show();
    }

    private void checkPermissions() {
        boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED);
        if (permissionsGranted) {
//            Toast.makeText(getActivity(), "You've granted all required permissions!", Toast.LENGTH_SHORT).show();
            if(from.equalsIgnoreCase("call")){

                callCustomerCare();

            }else if(from.equalsIgnoreCase("choose")) {
                selectImage();
            }

        } else {
            boolean showRationale = true;
            for (String permission: PERMISSIONS_REQUIRED) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                if (!showRationale) {
                    break;
                }
            }

            String dialogMsg = showRationale ? "We need some permissions to capture image" : "You've declined the required permissions, please grant them from your phone settings";

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permissions Required")
                    .setMessage(dialogMsg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
                        }
                    }).create().show();
        }
    }

    private boolean checkPermission(String permissions[]) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyUtils.l("MainActivity", "requestCode: " + requestCode);
        MyUtils.l("MainActivity", "Permissions:" + Arrays.toString(permissions));

        if (requestCode == REQUEST_PERMISSIONS) {

            MyUtils.l("MainActivity", "grantResults: " + Arrays.toString(grantResults));
            boolean hasGrantedPermissions = true;
            for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
//                finish();
//                Toast.makeText(mActivity, "Declined", Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(mActivity, "Granted", Toast.LENGTH_SHORT).show();
                /*startNxtActivity();*/
                selectImage();
            }

        } else {
//            finish();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose From Gallery", "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose From Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
//                Toast.makeText(getActivity(), "FILE SELECTED", Toast.LENGTH_SHORT).show();
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        imgPath = destination.getAbsolutePath();

//        String[] arr = imgPath.split("/");
//        if(arr.length > 0){
//            tv_fname.setText(arr[arr.length-1]);
//        }else {
            tv_fname.setText(imgPath);
//        }

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        profile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);

        /*String[] arr = imgPath.split("/");
        if(arr.length > 0){
            tv_fname.setText(arr[arr.length-1]);
        }else {*/
            tv_fname.setText(imgPath);
//        }

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(imgPath, options);

//        profile.setImageBitmap(bm);
//        tv_fname
    }

}
