package cnergee.sbbroadband;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_myapp,btn_collection,btn_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls() {

        btn_collection = (Button)findViewById(R.id.btn_collection);
        btn_myapp = (Button)findViewById(R.id.btn_myapp);
        btn_service = (Button)findViewById(R.id.btn_service);

        btn_service.setOnClickListener(listener);
        btn_myapp.setOnClickListener(listener);
        btn_collection.setOnClickListener(listener);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;

            switch (view.getId()){
                case R.id.btn_collection:
                    intent = new Intent(MainActivity.this,CollectionDashboradActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.btn_myapp:
                    intent = new Intent(MainActivity.this,DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.btn_service:
                    intent = new Intent(MainActivity.this,ServiceDashboardActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
}
