package local.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "LoadingActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        ConstraintLayout layout = findViewById(R.id.loadingLayout);
        AnimationDrawable anim = (AnimationDrawable) layout.getBackground();
        anim.setEnterFadeDuration(2500);
        anim.setExitFadeDuration(2500);
        anim.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, VisitorsActivity.class);
        startActivity(intent);
    }

    public void process(){

        // Go to visitors once processing is done
        Intent intent = new Intent(this, VisitorsActivity.class);
        startActivity(intent);
    }
}
