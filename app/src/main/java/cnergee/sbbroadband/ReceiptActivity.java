package cnergee.sbbroadband;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_back;
    String data_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        data_from=getIntent().getStringExtra("data_from");

        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                Intent intent;
                if(data_from.equalsIgnoreCase("collection")){
                    intent=new Intent(ReceiptActivity.this,CollectionDashboradActivity.class);

                }else{
                    intent=new Intent(ReceiptActivity.this,DashboardActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
