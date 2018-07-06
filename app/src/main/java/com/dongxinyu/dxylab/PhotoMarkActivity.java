package com.dongxinyu.dxylab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

                for (int i = 0; i < count; i++) {
                    final int finalI = i;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Bitmap bm = null;
                            try {
                                bm = FileUtil.getInstance().getImage(picPath); //获取限定宽高的bitmap，不限定则容易占用内存过大及OOM
                                if (bm == null) {
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmmss");
                                    String currentDateandTime = sdf.format(new Date());
                                    if (addWatermarkBitmap(bm, currentDateandTime, bm.getWidth(), bm.getHeight())) {
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
    }


    private boolean addWatermarkBitmap(Bitmap bitmap, String str, int w, int h) {
        int destWidth = w;   //此处的bitmap已经限定好宽高
        int destHeight = h;
        Log.v("tag", "width = " + destWidth + " height = " + destHeight);

        Canvas canvas = new Canvas(bitmap);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(destWidth / 22);//字体大小
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setAntiAlias(true);  //抗锯齿
        textPaint.setStrokeWidth(10);
        textPaint.setAlpha(15);
        textPaint.setColor(Color.WHITE);//采用的颜色
//        textPaint.setShadowLayer(3f, 1, 1,getResources().getColor(android.R.color.white));//影音的设置
        canvas.drawText(str, destWidth / 2, destHeight / 2, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return FileUtil.getInstance().saveMyBitmap(bitmap, String.valueOf(new Date().getTime())); //保存至文件
//        return true;
    }
}
