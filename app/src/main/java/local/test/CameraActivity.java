package local.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CameraActivity extends AppCompatActivity {

    public static final String  TAG = "CameraActivity";
    public static final String  DATA_PATH = Environment.getExternalStorageDirectory().toString();
    public static final int     REQUEST_PERMISSION = 1024;
    public static final int     REQUEST_IMAGE_CAPTURE = 1;
    public static final int     RESULT_LOAD_IMG = 1;

    private TextRecognizer      textRec;

    private CameraSource        mCameraSource;
    private SurfaceView         mCameraView;
    private Button              mButton;
    private TextView            mCameraInstruct;
    private Uri                 outputDir;
    private Bitmap              readImage;

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
                Frame image = takePicture();
                extractDetails(image);
                Intent intent = new Intent(CameraActivity.this, VisitorsActivity.class);
                startActivity(intent);
            }
        });
        mButton.setEnabled(true);

        mCameraInstruct = findViewById(R.id.cameraInstruct);
        mCameraInstruct.setAllCaps(true);
        mCameraInstruct.setText(getString(R.string.instruction));

        startCameraSource();

    }

    private Frame takePicture(){
        Frame outputFrame = null;

        try{

            String imagePath = DATA_PATH + "/id";
            File dir = new File(imagePath);

            if(!dir.exists()) dir.mkdir();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            LocalDateTime now = LocalDateTime.now();

            String imageFilePath = imagePath+"/"+ dateTimeFormatter.format(now);

            outputDir = Uri.fromFile((new File(imageFilePath)));

            //Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputDir);

            mCameraSource.takePicture(null,null);

            //if(pictureIntent.resolveActivity(getPackageManager()) != null)
            //    startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);

            Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(),outputDir);

            outputFrame = new Frame.Builder().setBitmap(image).build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFrame;
    }

    private void startCameraSource(){
        final TextRecognizer textRec = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRec.isOperational()){
            Toast.makeText(this, "Detector dependencies not loaded yet",Toast.LENGTH_SHORT).show();
        } else {

            //Init camera source to high res and set auto-focus
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRec)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1440,2560)
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
        }
    }

    /**
     * After taking picture, process picture and extract relevant text.
     * @param image
     */
    private void extractDetails(Frame image){
        final SparseArray<TextBlock> items = textRec.detect(image);
        if(items.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            int j = 0;
            for(int i=0; i<items.size(); i++) {
                TextBlock item = items.valueAt(i);
                if(item.equals("FULL NAMES") || item.equals("DATE OF BIRTH") || item.equals("SEX") || item.equals("ID NUMBER")){
                    stringBuilder.append(item.getValue());
                    stringBuilder.append("\n");
                    j = i+1;
                }
                if(j==i){
                    stringBuilder.append(item.getValue());
                    stringBuilder.append("\n");
                }
            }
            // set data in room db

        }
    }
}


