package com.example.naveen.recyclerview.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

import static android.content.ContentValues.TAG;


public class PaintView extends View {


    private Path path = new Path();
    private Paint brush = new Paint();
    private ArrayList<Path> path_list = new ArrayList<Path>();
    private ArrayList<float[]> pathPoints = new ArrayList<>();
    private ArrayList<ArrayList<float[]>> pathPoints_list = new ArrayList<>();


    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        brush.setAntiAlias(true);
        brush.setColor(Color.RED);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(4f);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        float[] points = new float[2];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                pathPoints.clear();
                points[0]=pointX;
                points[1]=pointY;
                path.moveTo(pointX, pointY);
                pathPoints.add(points);
                return true;
            case MotionEvent.ACTION_MOVE:
                points[0]=pointX;
                points[1]=pointY;
                path.lineTo(pointX, pointY);
                pathPoints.add(points);
                break;
            case MotionEvent.ACTION_UP:
                points[0]=pointX;
                points[1]=pointY;
                path.lineTo(pointX,pointY);
                pathPoints.add(points);
                path_list.add(path);
                pathPoints_list.add(pathPoints);
                path = new Path();
                break;

            default:
                return false;

        }
        postInvalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Path p : path_list){
            canvas.drawPath(p,brush);
        }
        canvas.drawPath(path,brush);
    }


    public void saveView(Context context){
        /*
        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        File file = new File(context.getExternalFilesDir(null).getAbsolutePath()+"imglolepic.png");

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;*/
        Gson gson = new Gson();
        String json = gson.toJson(pathPoints_list);
        File path = context.getFilesDir();
        File file = new File(path,"save.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();
            Toast.makeText(getContext(),"Saved!",Toast.LENGTH_LONG).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"File not saved!",Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"File not saved!",Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, "saveView: "+pathPoints_list.toString());
       }
    public void onClickUndo(){
        if(path_list.size()>0){
            path_list.remove(path_list.size()-1);
            invalidate();
        }else {
            Toast.makeText(getContext(),"Draw smth first!!",Toast.LENGTH_SHORT).show();
        }
    }
}

