package com.example.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WuziqiPanel extends View{

    private int mpanelWidth;
    private float mLineHeight;
    private int MAX_LINE=10;
    private Paint mPaint=new Paint();
    private Bitmap mWhite;
    private Bitmap mblack;
    private float ratioPeceofLineHeight =3*1.0f/4;
    //白棋先下，还是黑棋先下
    private boolean mIsWhite =false;
    private List<Point> mWhiteArray=new ArrayList<>();

    private List<Point> mBlackArray=new ArrayList<>();
    private boolean mIsGameOver;
    private  boolean ismIsWhiteWin;
    private  boolean ismIsBlackWin;

    private  int MAX_IN_LINE=5;
    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    private void init()
    {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhite= BitmapFactory.decodeResource(getResources(),R.drawable.huaqihuang);
        mblack=BitmapFactory.decodeResource(getResources(), R.drawable.huaqi);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=Math.min(widthSize,heightSize);
        if (widthMode==MeasureSpec.UNSPECIFIED)
        {
            width=heightSize;

        }else if(heightMode==MeasureSpec.UNSPECIFIED)
        {
            width=widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mpanelWidth=w;
        mLineHeight=mpanelWidth*1.0f/MAX_LINE;
        int pieceWidth=(int)(mLineHeight*ratioPeceofLineHeight);
        mWhite=Bitmap.createScaledBitmap(mWhite,pieceWidth,pieceWidth,false);//棋子尺寸
        mblack=Bitmap.createScaledBitmap(mblack,pieceWidth,pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(mIsGameOver)
            return false;
       int action=event.getAction();
        if(action ==MotionEvent.ACTION_UP)
        {
            int x=(int) event.getX();
            int y=(int)event.getY();
            Point p=getValidPoint(x,y);
            if(mWhiteArray.contains(p) || mBlackArray.contains(p))
            {
                return false;
            }
            if(mIsWhite)
            {
                mWhiteArray.add(p);
            }
            else
            {
                mBlackArray.add(p);
            }

            invalidate();//回调
            mIsWhite=!mIsWhite;
        }

        return true;

    }
//合法的point
    private Point getValidPoint(int x, int y)
    {
        return new Point((int) (x/mLineHeight),(int)(y/mLineHeight)) ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawpiece(canvas);
        checkGameOver();
    }

    private void checkGameOver()
    {
       boolean whitewin= checkFiveInLine(mWhiteArray);
        boolean blackWin=checkFiveInLine(mBlackArray);
        if(whitewin || blackWin)
        {
            mIsGameOver=true;
            ismIsWhiteWin=whitewin;

            String text =ismIsWhiteWin ? "白棋胜" :"黑棋胜";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points)
    {
        for(Point p : points)
        {
            int x=p.x;
            int y=p.y;

            boolean win =checkHprizontal(x,y,points);
            if (win)
                return true;
             win =checkvertical(x,y,points);
            if (win)
                return true;
             win =checkleftDiagonal(x,y,points);
            if (win)
                return true;
             win =checkrightDiagonal(x,y,points);
            if (win)
                return true;
        }
        return false;
    }

    /**
     *
     * 判断x，y位置的棋子，是否横向胜利。
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHprizontal(int x, int y, List<Point> points)
    {
        int count =1;
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x-i,y)))
            {
                count++;
            }else
            {
                break;
            }

        }
        if(count==MAX_IN_LINE)
            return true;
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x+i,y)))
            {
                count++;
            }else
            {
                break;
            }

        }
        if(count==MAX_IN_LINE)
            return true;
        return false;
    }
    private boolean checkvertical(int x, int y, List<Point> points)
    {
        int count =1;
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x,y-i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x,y+i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        if(count==MAX_IN_LINE)
            return true;
        return false;
    }
    private boolean checkleftDiagonal(int x, int y, List<Point> points)
    {
        int count =1;
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x-i,y+i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x+i,y-i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        if(count==MAX_IN_LINE)
            return true;
        return false;
    }
    private boolean checkrightDiagonal(int x, int y, List<Point> points)
    {
        int count =1;
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x-i,y-i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        for(int i=1;i<MAX_IN_LINE;i++)
        {
            if(points.contains(new Point(x+i,y+i)))
            {
                count++;
            }else
            {
                break;
            }

        }
        if(count==MAX_IN_LINE)
            return true;
        return false;
    }
    private void drawpiece(Canvas canvas)
    {
        for(int i=0,n=mWhiteArray.size();i<n;i++)
        {
            Point whitePoint=mWhiteArray.get(i);
            canvas.drawBitmap(mWhite,(whitePoint.x+(1-ratioPeceofLineHeight)/2)*mLineHeight,
                    (whitePoint.y+(1-ratioPeceofLineHeight)/2)*mLineHeight,null);

        }
        for(int i=0,n=mBlackArray.size();i<n;i++)
        {
            Point blackPoint=mBlackArray.get(i);
            canvas.drawBitmap(mblack,(blackPoint.x+(1-ratioPeceofLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-ratioPeceofLineHeight)/2)*mLineHeight,null);

        }

    }

    private void drawBoard(Canvas canvas)
    {
        int w=mpanelWidth;
        float Lineheight=mLineHeight;
        for(int i=0;i<MAX_LINE;i++)
        {
            int startx=(int)(Lineheight/2);
            int endx=(int)(w-Lineheight/2);

            int y=(int)((0.5+i)*Lineheight);
            canvas.drawLine(startx,y,endx,y,mPaint);
            canvas.drawLine(y,startx,y,endx,mPaint);
        }

    }

}
