package com.oelephant.androidsoundprofiles2.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TimeLine extends View {
	
	private static final int X = 0, Y = 1;
	
	private int mChartDepth = 16;
	private int mChartMax;
	private int mChartBarCount = 7;
	
	private float[] mPadding = new float[] { 10, 15 };
	private RectF mViewBounds;
	
	private float mAxisStrokeWidth = 2;
	private Paint mAxisPaint;
	private Path mAxisPath;
	
	private Paint mAxisXTextPaint;
	private float mAxisXDim;
	private RectF mAxisXBounds;
	private Path mAxisXTextPaths[];

	private float mAxisYDim;
	private RectF mAxisYBounds;
	private float mAxisYTextSpacing;
	private float mAxisTextSize = 15;
	private String[] mAxisYText = new String[] {"3a", "6a", "9a", "12p", "3p", "6p", "9p", "12a" };
	private Paint mAxisYTextPaint;
	
	private RectF mChartBounds;
	
	private Paint[] mChartBarPaint;
	private RectF[] mChartBarBounds;
	
	private float[] mLinePos = new float[2];
	private float mLineStrokeWidth = 3;
	private float mLineOffset = 10;
	private Paint mLinePaint;

	private int mChartScrollMax;
	private int mChartScrollPos = 0;
	
	private Path mHelpAxisXPath;
	private Paint mHelpAxisXPaint;
	private int mHelpAxisXTextSize = 20;
	
	private RectF mHelpChartRect;
	private Paint mHelpChartPaint;
	private Paint mHelpChartTextPaint;
	private int mHelpChartTextSize = 20;
	
	private RectF mHelpHotSpot;
	private Paint mHelpTextPaint;
	private RectF mCurrentHotSpot;
	
	private GestureDetector mGesture;
	
	public TimeLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		init();
		
	}
	
	private void init() {
		
		mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAxisPaint.setColor(Color.BLACK);
		mAxisPaint.setStyle(Paint.Style.STROKE);
		mAxisPaint.setStrokeWidth(mAxisStrokeWidth);
		
		mAxisXTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAxisXTextPaint.setColor(Color.BLACK);
		mAxisXTextPaint.setTextSize(mAxisTextSize);
		
		mAxisXTextPaths = new Path[mChartBarCount];
		
		mAxisYTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAxisYTextPaint.setColor(Color.BLACK);
		mAxisYTextPaint.setTextSize(mAxisTextSize);
		
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(Color.RED);
		mLinePaint.setStrokeWidth(mLineStrokeWidth);
		
		mGesture = new GestureDetector(this.getContext(), new MyGestureListener());
		
		// test values
		mChartMax = 7 * mChartDepth;
		mChartScrollMax = mChartMax - mChartBarCount;
		
		mChartBarPaint = new Paint[mChartMax];
		
		for (int i = 0; i < mChartMax; i++) {
			
			mChartBarPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			mChartBarPaint[i].setColor(Color.BLUE + (i * 1000));
			mChartBarPaint[i].setStyle(Paint.Style.FILL);
		}

		mHelpChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHelpChartPaint.setColor(Color.WHITE);
		mHelpChartPaint.setStyle(Paint.Style.FILL);
		
		mHelpChartTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHelpChartTextPaint.setColor(Color.YELLOW);
		mHelpChartTextPaint.setTextSize(mHelpAxisXTextSize);

		mHelpAxisXPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHelpAxisXPaint.setColor(Color.YELLOW);
		mHelpAxisXPaint.setStyle(Paint.Style.STROKE);
		mHelpAxisXPaint.setStrokeWidth(20);
		mHelpAxisXPaint.setStrokeCap(Paint.Cap.ROUND);
		mHelpAxisXPaint.setTextSize(mHelpAxisXTextSize);
		
	}
	
	public void setChartDepth(int chartDepth) {
		
		mChartDepth = chartDepth;
		postInvalidate();
		
	}
	
	public void setChartBarCount(int count) {
		
		mChartBarCount = count;
		postInvalidate();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
	
		for (int i = 0; i < mChartBarCount; i++) {
			
			canvas.drawRect(mChartBarBounds[i], mChartBarPaint[mChartScrollPos + i]);
			canvas.drawTextOnPath("SUN 11/25", mAxisXTextPaths[i], 0, 0, mAxisXTextPaint);
		}
		
		
		for (int i = 0; i < mAxisYText.length - 1; i++) {
			
			canvas.drawText(mAxisYText[i], mAxisYBounds.left, mAxisYBounds.bottom - ((i + 1) * mAxisYTextSpacing), mAxisYTextPaint);
			
		}

		canvas.drawText(mAxisYText[mAxisYText.length - 1], mAxisYBounds.left, mAxisYBounds.top, mAxisYTextPaint);

		canvas.drawPath(mAxisPath, mAxisPaint);

		canvas.drawLine(mLinePos[X], mChartBounds.top, mLinePos[X], mChartBounds.bottom + mLineOffset, mLinePaint);
		canvas.drawLine(mChartBounds.left - mLineOffset, mLinePos[Y], mChartBounds.right, mLinePos[Y], mLinePaint);
		
		canvas.drawText("?", mHelpHotSpot.left + (mHelpHotSpot.width() / 2 ) - mAxisXTextPaint.measureText("??"), mHelpHotSpot.top + (mHelpHotSpot.height() / 2), mAxisXTextPaint);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		
		float l, t, r, b;
		
		mAxisXDim = mAxisXTextPaint.measureText("XXX XX/XX");
		mAxisYDim = mAxisYTextPaint.measureText("XXaa");
		
		l = mPadding[X];
		t = mPadding[Y];
		r = w - mPadding[X];
		b = h - mPadding[Y];
		mViewBounds = new RectF(l, t, r, b);

		l = mViewBounds.left;
		t = mViewBounds.top;
		r = l + mAxisYDim;
		b = mViewBounds.bottom - mAxisXDim;
		mAxisYBounds = new RectF(l, t, r, b);

		mAxisYTextSpacing = mAxisYBounds.height() / mAxisYText.length;
		
		l = mAxisYBounds.right;
		t = mAxisYBounds.bottom;
		r = mViewBounds.right;
		b = mViewBounds.bottom;
		mAxisXBounds = new RectF(l, t, r, b);

		l = mAxisXTextPaint.measureText("???");
		mHelpHotSpot = new RectF(l, t, r, b);
		
		l = mAxisYBounds.right;
		t = mViewBounds.top;
		r = mViewBounds.right;
		b = mAxisXBounds.top;
		mChartBounds = new RectF(l, t, r, b);

		mLinePos[X] = l + (mChartBounds.width() / 2);
		mLinePos[Y] = l + (mChartBounds.height() / 2);
		
		float barWidth = mChartBounds.width() / mChartBarCount;
		mChartBarBounds = new RectF[mChartBarCount];
		
		for (int i = 0; i < mChartBarCount; i++) {
		
			l = mChartBounds.left + (i * barWidth);
			t = mChartBounds.top;
			r = l + barWidth;
			b = mChartBounds.bottom;
			mChartBarBounds[i] = new RectF(l, t, r, b);
			
			mAxisXTextPaths[i] = new Path();
			mAxisXTextPaths[i].moveTo(l, mAxisXBounds.bottom);
			mAxisXTextPaths[i].lineTo(r, mAxisXBounds.top);
			
		}
		
		mAxisPath = new Path();
		mAxisPath.moveTo(mChartBounds.left, mChartBounds.top);
		mAxisPath.lineTo(mChartBounds.left, mChartBounds.bottom);
		mAxisPath.lineTo(mChartBounds.right, mChartBounds.bottom);

		mHelpAxisXPath = new Path();
		mHelpAxisXPath.moveTo(mAxisXBounds.left, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.left + 20, mAxisXBounds.top);
		mHelpAxisXPath.moveTo(mAxisXBounds.left, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.left + 20, mAxisXBounds.bottom);
		mHelpAxisXPath.moveTo(mAxisXBounds.left, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.left + 40, mAxisXBounds.top + (mAxisXBounds.height() / 2));

		mHelpAxisXPath.moveTo(mAxisXBounds.right, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.right - 20, mAxisXBounds.top);
		mHelpAxisXPath.moveTo(mAxisXBounds.right, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.right - 20, mAxisXBounds.bottom);
		mHelpAxisXPath.moveTo(mAxisXBounds.right, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		mHelpAxisXPath.lineTo(mAxisXBounds.right - 40, mAxisXBounds.top + (mAxisXBounds.height() / 2));
		
		mHelpChartRect = mChartBounds;
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		mGesture.onTouchEvent(event);
		
		return true;
	
	}
	
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onDown(MotionEvent e) {

			Toast.makeText(TimeLine.this.getContext(), "test", Toast.LENGTH_LONG).show();
			
			return true;
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			float x = e2.getX();
			float y = e2.getY();
			
			if (x >= mChartBounds.left && x <= mChartBounds.right && 
					y >= mChartBounds.top && y <= mChartBounds.bottom) {

				if (y < mChartBounds.top) {
					
					y = mChartBounds.top;
					
				} else if (y > mChartBounds.bottom) {

					y = mChartBounds.bottom;
					
				}
				
				for (int i = 0; i < mChartBarCount; i++) {
					
					if (x >= mChartBarBounds[i].left && x <= mChartBarBounds[i].right) {
						
						x = mChartBarBounds[i].left + (mChartBarBounds[i].width() / 2);
						
						break;
						
					}
					
				}

				mLinePos[X] = x;
				mLinePos[Y] = y;
				postInvalidate();
				
				return true;
			
			} else if (x >= mAxisXBounds.left && x <= mAxisXBounds.right && 
					y >= mAxisXBounds.top && y <= mAxisXBounds.bottom) {
				
				if (distanceX < 0) {
					
					mChartScrollPos = mChartScrollPos < mChartScrollMax ? mChartScrollPos + 1 : mChartScrollMax;
					postInvalidate();
					
				} else if (distanceX > 0) {
					
					mChartScrollPos = mChartScrollPos > 0 ? mChartScrollPos - 1 : 0;
					postInvalidate();
					
				}
				
			}
			
			return true;
	
		}
		
	}
	
}
