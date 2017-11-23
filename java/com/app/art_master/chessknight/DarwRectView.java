package com.app.art_master.chessknight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Прорисовывает шахматную доску, а так же выполняет отрисовку других элементов
 * Created by Art-_-master.
 */

public class DarwRectView extends View implements  View.OnTouchListener{

    /** Объект Paint. Устанавливает параметры отрисовки */
    private Paint mPaint;

    /** Отрисовывает поля для хода на шахматной доске */
    private Rect mRect;

    /** Отрисовывает метки на полях для хода */
    //private Ova mCircle;

    /** Количество столбцов в доске */
    private int mNumColumn;

    /** Количество линий в доске */
    private int mNumCell;

    /** Координаты полей для хода */
    public static int [][][] mRectsCoordinates;

    /** Объект Picture. Для сохранения шахматной доски в доп. слое */
    private Picture mBoardLayer;

    /** Объект Picture. Для сохранения шахматной доски в доп. слое */
    private Picture [][] mRectLayer;

    /** Высота текущего View */
    private  int mHeight;

    /** Ширина текущего View */
    private  int mWidth;

    /** Разрешает перерисокву Canvas */
    private boolean mReDraw=false;

    private int rectWidth;
    private int rectHeight;

    public DarwRectView(Context context, AttributeSet atr, int column, int cell, int height, int width) {
        super(context);

        this.setFocusableInTouchMode(true);
        this.setFocusable(true);
        this.setClickable(true);

        mNumCell =cell;
        mNumColumn =column;
        mHeight=height;
        mWidth=width;
        
        if(mNumColumn<=mNumCell){
            rectHeight= height/ mNumCell;   
            rectWidth=rectHeight;
        }else{
            rectWidth=height/ mNumColumn;    
            rectHeight= rectWidth;      
        }       
       
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int textSize=50;
        if(mNumColumn<=mNumCell){
            mPaint.setTextSize(textSize/rectHeight);
        }else{
            mPaint.setTextSize(textSize/rectHeight);
        }  
        

        mBoardLayer = new Picture();

        mRectLayer= new Picture[mNumCell][mNumColumn];

        mRect = new Rect();

        mRectsCoordinates = new int[cell][column][2];

        mRectsCoordinates[0][0][0]=0;

        drawBoard(width, height);
       /* this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch entered");
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "ACTION_UP");
                    return true;
                } else {
                    return false;
                }
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                i++;
            }
        }); */
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPicture(mBoardLayer);

        if(mReDraw){
            for(int i = 0; i< mNumCell; i++) {
                for (int i1 = 0; i1 < mNumColumn; i1++) {
                    if(mRectLayer[i][i1]!=null){
                        canvas.drawPicture(mRectLayer[i][i1]);
                    }
                }
            }
        }
    }

    /**
     * Отрисовывает шахматную доску в зависимости от введенных данных пользователем
     * и сохраняет ее в отдельном слое
     *
     * @param width  - ширина текущего View
     * @param height - высота текущего View
     */
    private void drawBoard(int width, int height){
            Canvas canvas = mBoardLayer.beginRecording(width, height);

            int alternation;

            for(int i = 0; i< mNumCell; i++) {
                for (int i1 = 0; i1 < mNumColumn; i1++) {
                    mRectsCoordinates[i][i1][0]= mRectsCoordinates[i][i1][0]+rectWidth*i1;
                    mRectsCoordinates[i][i1][1]= mRectsCoordinates[i][i1][1]+rectHeight*i;
                    mRect.set(mRectsCoordinates[i][i1][0],
                            mRectsCoordinates[i][i1][1] + rectHeight,
                            mRectsCoordinates[i][i1][0] + rectWidth,
                            mRectsCoordinates[i][i1][1]);

                    if (i%2==0) {
                        alternation=1;
                    }else{
                        alternation=0;
                    }
                    switch (alternation)
                    {
                        case 0:
                            if (i1%2!=0) {
                                mPaint.setColor(Color.WHITE);
                            }else{
                                mPaint.setColor(Color.BLACK);
                            }
                            break;
                        case 1:
                            if (i1%2!=0) {
                                mPaint.setColor(Color.BLACK);
                            }
                            else{
                                mPaint.setColor(Color.WHITE);
                            }
                            break;
                    }

                    // рисуем прямоугольник из объекта mRect
                    canvas.drawRect(mRect, mPaint);
                }
            }
        mBoardLayer.endRecording();
        }

    public void drawCircle(int arrayIndex1, int arrayIndex2, int color){
        mReDraw=true;
        mRectLayer[arrayIndex1][arrayIndex2] = new Picture();
        Canvas canvas = mRectLayer[arrayIndex1][arrayIndex2].beginRecording(mWidth, mHeight);
        mRect.set(mRectsCoordinates[arrayIndex1][arrayIndex2][0],
                mRectsCoordinates[arrayIndex1][arrayIndex2][1] + rectHeight,
                mRectsCoordinates[arrayIndex1][arrayIndex2][0] + rectWidth,
                mRectsCoordinates[arrayIndex1][arrayIndex2][1]);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(text, mRectsCoordinates[arrayIndex1][arrayIndex2][0],
                        mRectsCoordinates[arrayIndex1][arrayIndex2][1]  mPaint);
        mPaint.setColor(color);
        RectF rectF= new RectF(mRect);
        canvas.drawOval(rectF, mPaint);
        mRectLayer[arrayIndex1][arrayIndex2].endRecording();

        invalidate();
    }
    public void clearCircle(int arrayIndex1, int arrayIndex2, int color){
        if(mRectLayer[arrayIndex1][arrayIndex2]!=null){
            mRectLayer[arrayIndex1][arrayIndex2]=null;
        }
    }
    

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch entered");
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "ACTION_UP");
            //Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            return true;
        } else {
            return false;
        }
    }
}
