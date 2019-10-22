package local.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String  TAG                   = "CameraActivity";
    private static final String  DATA_PATH             = Environment.getExternalStorageDirectory().toString()+"/Tess";
    private static final int     RECT_WIDTH            = 2271;
    private static final int     RECT_HEIGHT           = 1465;
    private static final int     REQUEST_IMAGE_CAPTURE = 1;

    private BaseLoaderCallback   baseLoaderCallback;
    private JavaCameraView       cameraView;
    private Mat                  mat1;
    private Rect                 bound;
    private Button               button;
    private TextView             cameraInstruct;
    //private TessBaseAPI        tessBaseAPI;
    private Uri                  outputDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"OpenCV loaded",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"OpenCV could not be loaded",Toast.LENGTH_SHORT).show();
        }

        cameraView = findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(CameraActivity.this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                if(status == BaseLoaderCallback.SUCCESS) {
                    cameraView.enableView();
                } else {
                    super.onManagerConnected(status);
                }
            }
        };

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takePicture();
            }
        });
        button.setEnabled(true);

        cameraInstruct = findViewById(R.id.cameraInstruct);
        cameraInstruct.setAllCaps(true);
        cameraInstruct.setText(getString(R.string.instruction));
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

    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat(width,height,CvType.CV_8UC4);
        int x = (width-RECT_WIDTH)/2;
        int y = (height-RECT_HEIGHT)/2;
        bound = new Rect(x,y,RECT_WIDTH,RECT_HEIGHT);
    }

    @Override
    public void onCameraViewStopped() {
        mat1.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat1 = inputFrame.rgba();
        Mat gray = inputFrame.gray();
        drawOverlay();
        checkFrame(mat1,gray);
        return mat1;
    }

    @Override
    protected void onResume(){
        super.onResume();
        baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(cameraView!=null) cameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraView!=null) cameraView.disableView();
    }

    protected void drawOverlay(){
        // blue overlay and empty output
        Mat overlay = new Mat();
        Mat output = new Mat();

        mat1.copyTo(overlay);
        mat1.copyTo(output);

        Imgproc.rectangle(overlay,new Point(0,0),new Point(overlay.width(),overlay.height()),new Scalar(19,42,64),-1);

        // mask
        Mat mask = Mat.zeros(mat1.size(),CvType.CV_8UC4);
        Imgproc.rectangle(mask,bound.tl(),bound.br(),new Scalar(255,255,255),-1);
        Core.bitwise_not(mask,mask);

        // apply mask to output from blue overlay
        overlay.copyTo(output,mask);

        // place overlay on camera view
        Core.addWeighted(output,0.3, mat1,0.7,0,mat1);
    }

    protected void checkFrame(Mat mat, Mat gray){
        //drawOverlay();

        // Init matrices of images and list of contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat bw = new Mat(gray.size(),CvType.CV_8UC1);
        Mat canny = new Mat();
        Mat hierarchy = new Mat();
        Mat kernel = Mat.ones(new Size(3,4),CvType.CV_8UC1);

        // Threshold, erode, detect edges and find contours
        Imgproc.threshold(gray, bw, 160, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.erode(bw, bw, kernel);
        Imgproc.Canny(bw, canny, 50, 254);
        Imgproc.findContours(bw, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Draw rectangle overlay

        Imgproc.rectangle(mat,bound.tl(),bound.br(),new Scalar(50,0,0),10);

    }
}