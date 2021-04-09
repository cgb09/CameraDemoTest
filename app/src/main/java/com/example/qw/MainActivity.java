package com.example.qw;


import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity implements CameraController.CameraControllerInterFaceCallback{

    private AutoFitTextureView mPreviewTexture;
    private Button mTakeVideo;
    private Button mTakePicture;
    private Button mFour_third;
    private Button mSixteen_nine;
    private CameraController mCameraController;
    private boolean mIsRecordingVideo;
    private ImageView chg_btn;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreenActivity();
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mPreviewTexture = findViewById(R.id.preview_texture);
        mTakeVideo = findViewById(R.id.take_video);
        mTakePicture = findViewById(R.id.take_picture);
        chg_btn = findViewById(R.id.change_camera_id);
        mFour_third = findViewById(R.id.four_third);
        mSixteen_nine = findViewById(R.id.sixteen_nine);

    //绑定点击事件 录像
        mTakeVideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mIsRecordingVideo){
                    mTakeVideo.setText("record");
                    mCameraController.stopRecordingVideo();
                }else{
                    mFile = new File(Environment.getExternalStorageDirectory(),"DCIM/Camera/"+System.currentTimeMillis()+"test.mp4");
                    mCameraController.setVideoPath(mFile);
                    mIsRecordingVideo = true;
                    mCameraController.startRecordingVideo();
                }
            }
        });
        //绑定事件  拍照
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.beginTakePicture();
            }
        });

        mCameraController = new CameraController(this,mPreviewTexture);
        mCameraController.setCameraControllerInterFaceCallback(this);

        //绑定事件  切换
        chg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.changeCameraBtn();
            }
        });

        mFour_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.changeToFourRatioThird();
            }
        });

        mSixteen_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.changToSixTeenRatioNine();
            }
        });

    }

    //请求全屏
    private void requestFullScreenActivity() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraController.startBackgroundThread();
        if(mPreviewTexture.isAvailable()){
        }else{
            mPreviewTexture.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
           = new TextureView.SurfaceTextureListener() {
             @Override
             public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mCameraController.openCamera();
             }

             @Override
             public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

             }

             @Override
             public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                 return true;
             }

             @Override
             public void onSurfaceTextureUpdated(SurfaceTexture surface) {
             }
         };

    @Override
    protected void onPause() {
        super.onPause();
        mCameraController.closeCamera();
    }

    @Override
    public void startRecordVideo() {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                mTakeVideo.setText("stop");
            }
        });
    }

}
