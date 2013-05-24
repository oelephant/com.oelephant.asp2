package com.oelephant.androidsoundprofiles2.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Temp2 extends View {

	final String XAXIS_LABEL_TEST = "XXX XX/XX ";
	final String YAXIS_LABEL_TEST = "XXXX ";
	final String[] mYLabel = new String[] { "12am", "3am", "6am", "9am", "12am", "3pm", "6pm", "9pm", "12am" };
	final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;
	
	Context mContext;
	AttributeSet attrs;
	
	float[] mViewPadding;
	
	RectF mChartRect;
	Paint mChartBGPaint;
	Paint mChartTextPaint;
	Paint mChartGridPaint;
	float mChartGridSpacing;
	float mChartBarSpacing;
	ArrayList<ChartBar> mChartBarData;
	
	RectF mCrossRect;
	Paint mCrossLinePaint;
	Paint mCrossTextPaint;

	RectF mXAxisRect;
	RectF mYAxisRect;
	Paint mAxisLinePaint;
	Paint mAxisTextPaint;
	Path mAxisPath;
	Path[] mXAxisLabelPaths;
	
	public Temp2(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		init();
		
	}
	
	public void init() {

		mViewPadding = new float[] { 10, 20, 10, 10 };
		
		mChartBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mChartBGPaint.setColor(Color.LTGRAY);
		mChartBGPaint.setAlpha(100);
		mChartBGPaint.setStyle(Paint.Style.FILL);
		
		mChartTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mChartTextPaint.setColor(Color.BLACK);
		mChartTextPaint.setTextSize(40);

		mChartGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mChartGridPaint.setColor(Color.LTGRAY);
		mChartGridPaint.setAlpha(150);
		mChartGridPaint.setStyle(Paint.Style.STROKE);
		mChartGridPaint.setStrokeWidth(2);
		
		mAxisLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAxisLinePaint.setColor(Color.BLACK);
		mAxisLinePaint.setStyle(Paint.Style.STROKE);
		mAxisLinePaint.setStrokeWidth(4);
		
		mAxisTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAxisTextPaint.setColor(Color.BLACK);
		mAxisTextPaint.setTextSize(20);
		
		mCrossLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrossLinePaint.setColor(Color.RED);
		mCrossLinePaint.setStyle(Paint.Style.STROKE);
		mCrossLinePaint.setStrokeWidth(2);
		
		mCrossTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrossTextPaint.setColor(Color.BLACK);
		mCrossTextPaint.setTextSize(20);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
		
		float x, y, l, t, r, b;
		
		// -------------------------------------------------------------------------------- CALCULATE BAR SPACING
		
		// TODO 
		float chartWidth = (w - mViewPadding[RIGHT]) - (mViewPadding[LEFT] + mAxisTextPaint.measureText(YAXIS_LABEL_TEST));
		mChartBarSpacing = chartWidth / 7; 
		
		// -------------------------------------------------------------------------------- CALCULATE X AXIS HEIGHT
		
		float len = mAxisTextPaint.measureText(XAXIS_LABEL_TEST);
		float xHeight = (float) (Math.sqrt(len * len - mChartBarSpacing * mChartBarSpacing) + mAxisTextPaint.getTextSize());

		// -------------------------------------------------------------------------------- CHART AREA
		
		l = mViewPadding[LEFT] + mAxisTextPaint.measureText(YAXIS_LABEL_TEST);
		t = mViewPadding[TOP];
		r = w - mViewPadding[RIGHT];
		b = h - xHeight - mViewPadding[BOTTOM];
		mChartRect = new RectF(l, t, r, b);
		
		// -------------------------------------------------------------------------------- CALCULATE GRID SPACING
		
		mChartGridSpacing = mChartRect.height() / (mYLabel.length - 1);

		// -------------------------------------------------------------------------------- X AXIS AREA
		
		l = mAxisTextPaint.measureText(YAXIS_LABEL_TEST) + mViewPadding[LEFT];
		t = h - xHeight - mViewPadding[BOTTOM];
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM];
		mXAxisRect = new RectF(l, t, r, b);
		
		// -------------------------------------------------------------------------------- Y AXIS AREA
		
		l = mViewPadding[LEFT];
		t = mViewPadding[TOP];
		r = mViewPadding[LEFT] + mAxisTextPaint.measureText(YAXIS_LABEL_TEST);
		b = h - xHeight - mViewPadding[BOTTOM];
		mYAxisRect = new RectF(l, t, r, b);

		// -------------------------------------------------------------------------------- CROSS AREA
		
		// TODO
		l = mViewPadding[LEFT] + mAxisTextPaint.measureText("XXXX");
		t = mViewPadding[TOP] + 0;
		r = w - mViewPadding[RIGHT];
		b = h - mAxisTextPaint.measureText("XXX XX/XX") - mViewPadding[BOTTOM];
		mCrossRect = new RectF(l, t, r, b);
		
		// -------------------------------------------------------------------------------- AXIS PATH
		
		mAxisPath = new Path();

		x = mYAxisRect.right;
		y = mYAxisRect.top;
		
		mAxisPath.moveTo(x, y);
		
		x = mYAxisRect.right;
		y = mYAxisRect.bottom;
		
		mAxisPath.lineTo(x, y);
		
		x = mXAxisRect.left;
		y = mXAxisRect.top;
		
		mAxisPath.lineTo(x, y);
		
		x = mXAxisRect.right;
		y = mXAxisRect.top;
		
		mAxisPath.lineTo(x, y);
		
		// -------------------------------------------------------------------------------- X AXIS LABEL PATHS
		
		// TODO
		mXAxisLabelPaths = new Path[7];
		
		for (int i = 0; i < 7; i++) {
			
			Path p = new Path();
			
			x = mXAxisRect.left + (i * mChartBarSpacing);
			y = mXAxisRect.bottom;
			
			p.moveTo(x, y);

			x += mChartBarSpacing;
			y = mXAxisRect.top;
			
			p.lineTo(x, y);
			
			mXAxisLabelPaths[i] = p;
			
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawRect(mChartRect, mChartBGPaint);
		
		float y;
		
		for (int i = 0; i < mYLabel.length; i++) {

			y = mYAxisRect.bottom - (i * mChartGridSpacing); 

			canvas.drawLine(mChartRect.left, y, mChartRect.right, y, mChartGridPaint);
			
			y += (mAxisTextPaint.getTextSize() * 0.25);
			
			canvas.drawText(mYLabel[i], mYAxisRect.left, y, mAxisTextPaint);
			
		}
		
		// TODO test
		for (int i = 0; i < 7; i++) {
			
			canvas.drawTextOnPath("EEE MM/dd", mXAxisLabelPaths[i], 0, 0, mAxisTextPaint);
			
		}
		
		canvas.drawPath(mAxisPath, mAxisLinePaint);
		
	}

	private void getData() {
		
		mChartBarData = new ArrayList<ChartBar>();
		
		ASPDB db = new ASPDB(mContext);
		
		ArrayList<ASPDB.QueueItem> queue = db.getQueue();
		
		for (ASPDB.QueueItem item : queue) {
			
			
			
		}
		
	}
	
	private class ChartBar {
		
		String label;
		
		public ChartBar(long start) {
			
			
		}
		
	}
	
}
