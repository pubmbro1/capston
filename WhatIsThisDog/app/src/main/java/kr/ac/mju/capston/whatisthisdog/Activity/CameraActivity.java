package kr.ac.mju.capston.whatisthisdog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;

import org.tensorflow.lite.Interpreter;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{

    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;

    private Camera.PictureCallback takePicture;

    private Button button;

    private FileManager fm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(false);
        setContentView(R.layout.activity_camera);

        mCameraView = (SurfaceView) findViewById(R.id.surfaceView);
        init();

        //임시 캡쳐 버튼
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCamera != null)
                mCamera.takePicture(null,null,takePicture);
            }
        });


        /*
        * 화면 캡쳐
        * Bitmap 이미지 저장 부분 느림
        * Bufferstream 이용 부분 추가?
        * */
        takePicture = new Camera.PictureCallback(){
            public void onPictureTaken(byte[] data, Camera camera){
                Log.i("CameraActivity", "capture");
                if(data != null){
                    try {
                        saveImage(data, camera);
                        Toast.makeText(CameraActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();

                    }catch(Exception e){
                        Log.e("save image", "fail");
                    }
                }
            }
        };
    }

    private void init(){
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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

    public void saveImage(byte[] data, Camera camera){

        //이미지의 너비와 높이 결정
        //int w = camera.getParameters().getPictureSize().width;
        //int h = camera.getParameters().getPictureSize().height;

        int w = camera.getParameters().getPictureSize().width;
        int h = camera.getParameters().getPictureSize().height;

        //byte array 를 bitmap 으로 변환
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);

        //matrix.postRotate(orientation);
        //bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true);
        bitmap =  Bitmap.createBitmap(bitmap, 0, 0, 224, 224, null, true);


        //모델 이용 계산
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        toGrayscale(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] inputDataByte = stream1.toByteArray();
        String dog_name = calRate(convert(inputDataByte));


        //bitmap 을  byte array 로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] currentData = stream.toByteArray();

        //파일로 저장
        FileOutputStream outStream = null;
        String fileName = null;

        try {
            int nameIndex = 0;
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




        // 텍스트 파일에 정보 저장
        fm = new FileManager(this,"album.txt");
        DogInfo saveItem = new DogInfo(DogInfo.getRandomData(fileName));
        saveItem.setName(dog_name);
        fm.saveItemsToFile(saveItem) ;


        //정보보기로 이동
        Intent intent = new Intent(CameraActivity.this, DogInfoActivity.class);
        intent.putExtra("dogitem", saveItem);
        intent.putExtra("call", "camera");
        startActivity(intent);
        finish();
    }
    public int[] convert(byte buf[]) {
        int intArr[] = new int[buf.length / 4];
        int offset = 0;
        for(int i = 0; i < intArr.length; i++) {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
                    ((buf[1 + offset] & 0xFF) << 16) | ((buf[0 + offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public String calRate(int[] input){
        Interpreter tflite = getTfliteInterpreter("xception_to_lite.tflite");
        int[] output = new int[120];

        tflite.run(input, output);
        if(output == null) Log.d("모델 아웃풋" , "null");

        double max = 0;
        int index = 0;
        for(int i=0;i<120;i++){
            if(output[i] > index){
                max = output[i];
                index = i;
            }
        }

        return "Dog index : " + String.valueOf(index) + "\n";

    }
    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(CameraActivity.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
