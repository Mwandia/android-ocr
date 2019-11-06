package local.test;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import local.test.models.Visitor;
import local.test.ui.views.SectionsPagerAdapter;
import local.test.viewmodels.VisitorViewModel;

public class VisitorsActivity extends AppCompatActivity {

    private static final String TAG = "VisitorsActivity";
    public static final int     ADD_VISITOR_REQUEST = 1;

    private long                backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        final FloatingActionButton toCamera = findViewById(R.id.fab);

        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()!=0) {
                    toCamera.hide();
                } else {
                    toCamera.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_VISITOR_REQUEST && resultCode == RESULT_OK) {
            String id = data.getStringExtra(CameraActivity.NEW_ID);
            String name = data.getStringExtra(CameraActivity.NEW_NAME);
            String dob = data.getStringExtra(CameraActivity.NEW_DOB);
            String sex = data.getStringExtra(CameraActivity.NEW_SEX);

            VisitorViewModel visitorViewModel = ViewModelProviders.of(this).get(VisitorViewModel.class);
            Visitor visitor = new Visitor(id, name, dob, sex);
            visitorViewModel.insert(visitor);

            Toast.makeText(VisitorsActivity.this,"Added Visitor",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeScreenIntent);
            return;
        } else {
            Toast.makeText(getApplicationContext(),"Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void openCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}