package com.dongxinyu.dxylab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoMarkActivity extends AppCompatActivity {

    private final int requestCode = 0x101;
    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_mark);
        initViews();
        checkpermission();
    }

    private void initViews() {
        imageView = (ImageView) findViewById(R.id.imageShow);
        editText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.btnPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoMarkActivity.this, PhotoPickActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    private void checkpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                                                  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                  1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == this.requestCode) {
                final String picPath;
                picPath = data.getStringExtra(PhotoPickActivity.KEY_PHOTO_PATH);
                if (picPath == null || picPath.equals("")) {
                    return;
                }
                Toast.makeText(this, "pic = " + picPath, Toast.LENGTH_SHORT).show();

                Editable text = editText.getText();
                int count = Integer.parseInt(text.toString());


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Bitmap bm = null;
                        try {
                            bm = FileUtil.getInstance().getImage(picPath); //获取限定宽高的bitmap，不限定则容易占用内存过大及OOM
                            if (bm == null) {
                            } else {
                                for (int i = 0; i < count; i++) {
                                    Bitmap tmp = Bitmap.createScaledBitmap(bm, bm.getWidth() / 4, bm.getHeight() / 4, false);
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
                                    String currentDateandTime = sdf.format(new Date());
                                    String label = currentDateandTime + "_" + i;
                                    boolean result = addWatermarkBitmap(tmp, label, tmp.getWidth(), tmp.getHeight());
                                    Log.v("addWatermarkBitmap", "no." + i + " result:" + result);
                                }
                            }
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            if (bm != null) {
                                bm.recycle();
                            }
                            System.gc();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(PhotoMarkActivity.this, "水印生成成功，文件已保存在 " + FileUtil.getInstance().IMAGE_PATH, Toast.LENGTH_SHORT).show();

                    }
                }.execute();


            }
        }

    }


    private boolean addWatermarkBitmap(Bitmap bitmap, String str, int w, int h) {
        int destWidth = w;   //此处的bitmap已经限定好宽高
        int destHeight = h;

        Canvas canvas = new Canvas(bitmap);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(destWidth / 8);//字体大小
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setStrokeWidth(10);
        textPaint.setAlpha(15);
        textPaint.setColor(Color.WHITE);//采用的颜色
//        textPaint.setShadowLayer(3f, 1, 1,getResources().getColor(android.R.color.white));//影音的设置
        canvas.drawText(str, destWidth / 2, destHeight / 2, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save();
        canvas.restore();
        return FileUtil.getInstance().saveMyBitmap(bitmap, str); //保存至文件
//        return true;
    }
}
