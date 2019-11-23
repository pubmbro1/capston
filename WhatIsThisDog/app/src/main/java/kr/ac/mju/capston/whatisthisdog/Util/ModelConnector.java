package kr.ac.mju.capston.whatisthisdog.Util;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ModelConnector {

    Activity activity;
    Interpreter model;

    float[][][][] input_data;

    public ModelConnector(Activity activity) {
        this.activity = activity;

        model = getTfliteInterpreter("xception_to_lite.tflite");
    }

    public String distDog(Bitmap bitmap){
        getImageTo3dArr(bitmap);

        float[][] output = new float[1][120];

        model.run(input_data, output);

        float max = 0;
        int index = 0;
        for(int i=0;i<120;i++){
            if(output[0][i] > max){
                max = output[0][i];
                index = i;
            }
        }

        return "dog" + index;
    }


    public void getImageTo3dArr(Bitmap bitmap){
        Bitmap image = Bitmap.createScaledBitmap(bitmap, 224,224, false);

        float[][][][] data = new float[1][224][224][3];

        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int px = image.getPixel(x,y);


                float red = ((px >> 16) & 0xFF) / (float) 255.0;
                float green = ((px >> 8) & 0xFF) / (float)255;
                float blue = (px & 0xFF) / (float)255;

                data[0][x][y][0] = red;
                data[0][x][y][1] = green;
                data[0][x][y][2] = blue;
            }
        }
        input_data = data;
    }

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(activity, modelPath));
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
