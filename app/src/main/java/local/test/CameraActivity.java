package local.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;


public class CameraActivity extends AppCompatActivity {

    private static final String  TAG = "CameraActivity";
    private static final String  DATA_PATH = Environment.getExternalStorageDirectory().toString()+"/Tess";
    private static final int     REQUEST_PERMISSION = 1024;

    private CameraSource         mCameraSource;
    private SurfaceView          mCameraView;
    private TextView             mTextView;
    private Button               mButton;
    private TextView             mCameraInstruct;
    private Uri                  outputDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        mCameraView = findViewById(R.id.cameraView);

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePicture();
            }
        });
        mButton.setEnabled(true);

        mCameraInstruct = findViewById(R.id.cameraInstruct);
        mCameraInstruct.setAllCaps(true);
        mCameraInstruct.setText(getString(R.string.instruction));

        startCameraSource();
    }

    private void takePicture(){
        try{
/*            String imagePath = DATA_PATH + "/imgs";
            File dir = new File(imagePath);
            if(!dir.exists()) dir.mkdir();
            String imageFilePath = imagePath+"/ocr.jpg";
            outputDir = Uri.fromFile((new File(imageFilePath)));
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputDir);

            if(pictureIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
*/
            Intent process = new Intent(this,LoadingActivity.class);
            startActivity(process);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCameraSource(){
        final TextRecognizer textRec = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRec.isOperational()){
            Toast.makeText(this, "Detector dependencies not loaded yet",Toast.LENGTH_SHORT).show();
        } else {

            //Init camera source to high res and set auto-focus
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRec)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .build();

            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
                        } else {
                            mCameraSource.start(mCameraView.getHolder());
                        }

                    } catch (IOException e){
                        Toast.makeText(CameraActivity.this,"Surface not working", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            // Set TextRecognizer processor
            textRec.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0){
                        mTextView.post(new Runnable(){
                            @Override
                            public void run(){
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0; i<items.size(); i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                mTextView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }
}
