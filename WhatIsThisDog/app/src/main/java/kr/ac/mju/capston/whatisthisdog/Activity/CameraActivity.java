package kr.ac.mju.capston.whatisthisdog.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;
import kr.ac.mju.capston.whatisthisdog.Util.ModelConnector;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{

    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;

    private Camera.PictureCallback takePicture;

    private Button button;
    private FileManager fm;
    private ModelConnector model;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        initActionBar(false);
        init();

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCamera != null) {
                    mCamera.takePicture(null, null, takePicture);
                }
            }
        });

        /*
        * 화면 캡쳐
        * */
        takePicture = new Camera.PictureCallback(){
            public void onPictureTaken(byte[] data, Camera camera){
                Log.i("CameraActivity", "capture");
                if(data != null){
                    try {
                        Object[] transfer = new Object[2];
                        transfer[0] = data;
                        transfer[1] = camera;
                        DistDogTask ddt = new DistDogTask();
                        ddt.execute(transfer);

                    }catch(Exception e){
                        Log.e("save image", "fail");
                    }
                }
            }
        };
    }

    private void init(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        mCameraView = findViewById(R.id.surfaceView);

        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.setDisplayOrientation(0);


        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        model = new ModelConnector(this);


        Button capMsg = findViewById(R.id.capture_msg);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        capMsg.startAnimation(anim);
    }

    //생성 시 호출
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {

                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
        }
    }

    //surfaceView 크기 바뀔 때 호출
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // View 가 존재하지 않을 때
        if (mCameraHolder.getSurface() == null) {
            return;
        }

        // 작업하기 전 Preview 멈춤
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }

        // 카메라 다시 세팅
        Camera.Parameters parameters = mCamera.getParameters();

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Camera.Size size = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), point.x ,point.y);
        parameters.setPreviewSize(size.width, size.height);


        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);

        // View 재생성
        try {
            mCamera.setPreviewDisplay(mCameraHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    //Surface 종료 시
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public DogInfo saveImage(byte[] data, Camera camera){

        //이미지의 너비와 높이 결정
        int w = camera.getParameters().getPictureSize().width;
        int h = camera.getParameters().getPictureSize().height;

        //byte array 를 bitmap 으로 변환
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);


        bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true);

        //bitmap 을  byte array 로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] currentData = stream.toByteArray();

        //파일로 저장
        FileOutputStream outStream = null;
        String fileName = null;

        try {
            fileName = String.format("%s.jpg", System.currentTimeMillis());
            File outputFile = new File(FileManager.getPath(), fileName);

            outStream = new FileOutputStream(outputFile);
            outStream.write(currentData);
            outStream.flush();
            outStream.close();

            // 갤러리에 반영
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(outputFile));
            sendBroadcast(mediaScanIntent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //강아지 판별
        String dog_name = model.distDog(bitmap);

        // 텍스트 파일에 정보 저장
        fm = new FileManager(this,"album.txt");

        int resId = getResources().getIdentifier(dog_name, "string", getPackageName());
        String strData = getResources().getString(resId);
        DogInfo item = new DogInfo(strData);
        item.setDogImage(fileName);

        Log.d("DogName", dog_name +"/" + fileName);

        fm.saveItemsToFile(item);

        return item;
    }

    private class DistDogTask extends AsyncTask<Object, Void, DogInfo> {

        ProgressDialog asyncDialog = new ProgressDialog(CameraActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("강아지 분석 중입니다..");
            asyncDialog.setCancelable(false);

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected DogInfo doInBackground(Object... arg) {
            byte[] data = (byte[]) arg[0];
            Camera camera = (Camera) arg[1];

            return saveImage(data, camera);
        }

        @Override
        protected void onPostExecute(DogInfo result) {
            asyncDialog.dismiss();

            Toast.makeText(CameraActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();

            //정보보기로 이동
            Intent intent = new Intent(CameraActivity.this, DogInfoActivity.class);
            intent.putExtra("dogitem", result);
            intent.putExtra("call", "camera");
            startActivity(intent);
            finish();

            super.onPostExecute(result);
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }
}
