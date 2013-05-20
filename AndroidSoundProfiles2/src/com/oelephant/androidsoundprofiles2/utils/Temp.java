package com.oelephant.androidsoundprofiles2.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Temp extends View {

	private Context mContext;
	private AttributeSet mAttrs;

	private static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;
	private float[] mViewPadding = new float[] {2, 2, 2, 2};
	
	private RectF mXAxisBounds;
	private Paint mXAxisPaint;
	
	private RectF mYAxisBounds;
	private Paint mYAxisPaint;
	
	private RectF mChartBounds;
	private Paint mChartBackgroundPaint;
	private Paint mChartAxisPaint;
	
	private ArrayList<ASPDB.QueueItem> mQueue;
	private int mBarCount;
	private int mBarWindow;
	
	private ChartData mChartData;
	private YAxisData mYAxisData;
	private XAxisData mXAxisData;
	
	
	public Temp(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		mContext = context;
		mAttrs = attrs;
		
		init();
		
	}
	
	public void init() {
		
		ASPDB db = new ASPDB(mContext);
		
		mQueue = db.getQueue();
		
		// TODO
		mBarCount = 28; 
		
		// TODO
		mBarWindow = 7;
		
		db.close();
		
		mXAxisPaint = new Paint();
		
		mYAxisPaint = new Paint();
		
		mChartBackgroundPaint = new Paint();
		
		mChartAxisPaint = new Paint();
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
	
		float l, t, r, b;
		float yWidth = mYAxisPaint.measureText("XXXX");
		float xHeight = mXAxisPaint.measureText("XXX XX/XX");
		
		l = mViewPadding[LEFT] + yWidth;
		t = h - mViewPadding[BOTTOM] - xHeight;
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM];
		
		mXAxisBounds = new RectF(l, t, r, b);
		// TODO mXAxisData
		
		l = mViewPadding[LEFT];
		t = mViewPadding[TOP];
		r = mViewPadding[LEFT] + yWidth;
		b = h - mViewPadding[BOTTOM] - xHeight;
		
		mYAxisBounds = new RectF(l, t, r, b);
		mYAxisData = new YAxisData(mYAxisBounds);
		
		l = mViewPadding[LEFT] + yWidth;
		t = mViewPadding[TOP];
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM] - xHeight;
		
		mChartBounds = new RectF(l, t, r, b);
		mChartData = new ChartData(mQueue, mBarCount, mBarWindow, mChartBounds);
		
		// TODO help overlay
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		mChartData.drawBars(canvas);
		
		mYAxisData.drawLabel(canvas);
		
	}
	
	private class YAxisData {
		
		String[] labelText = new String[] { "3am", "6am", "9am", "12pm", "3pn", "6pm", "9pm", "12am" };
		Point[] labelPos;
		
		public YAxisData(RectF bounds) {
			
			labelPos = new Point[labelText.length];
			
			float x, y;
			x = bounds.left;
			
			for (int i = 0; i < labelText.length; i++) {
				
				y = bounds.bottom - (i * (bounds.height() / labelText.length));
				
				labelPos[i] = new Point((int) x, (int) y);
				
			}
			
		}
		
		public void drawLabel(Canvas canvas) {
			
			for (int i = 0; i < labelText.length; i++) {
				
				canvas.drawText(labelText[i], labelPos[i].x, labelPos[i].y, mYAxisPaint);
				
			}
			
		}
		
	}

	private class XAxisData {

		// TODO get from ChartData?
		
		public XAxisData() {
			
		}
		
		public void drawLabel(Canvas canvas) {
			
			
			
		}
		
	}
	
	private class ChartData {
		
		private ArrayList<ASPDB.QueueItem> mData;
		
		private int mBarCount;
		private int mBarWindow;
		private int mScrollPos;
		private int mScrollMax;
		
		private ArrayList<RectF> mBarBounds;
		private ArrayList<ChartBar> mChartBars;
		
		/**
		 * data MUST be pulled from ASPDB.getQueue() so that the data is sorted correctly.
		 */
		public ChartData(ArrayList<ASPDB.QueueItem> data, int barCount, int barWindow, RectF bounds) {
			
			mData = data;
			
			mBarCount = barCount;
			mChartBars = new ArrayList<ChartBar>();
			
			mBarWindow = barWindow;
			mBarBounds = new ArrayList<RectF>();
			
			mScrollPos = 0;
			mScrollMax = barCount - barWindow > 0 ? barCount - barWindow : 0;

			float l, t, r, b;
			float barWidth = bounds.width() / barWindow;
			
			t = bounds.top;
			b = bounds.bottom;
			
			for (int i = 0; i < barWindow; i++) {

				l = bounds.left + (i * barWidth);
				r = bounds.left + (i * barWidth) + barWidth;
				mBarBounds.add(new RectF(l, t, r, b));
				
			}
			
			ArrayList<ASPDB.QueueItem> chartBarData = new ArrayList<ASPDB.QueueItem>();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(data.get(0).getStartUTC());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			long originRef, endRef;
			
			for (int bar = 0; bar < barCount; bar++) {
				
				originRef = cal.getTimeInMillis();
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endRef = cal.getTimeInMillis();
				
				for (int i = 0; i < data.size(); i++) {
					
					ASPDB.QueueItem item = data.get(i);
					
					if (item.getEndUTC() > originRef && item.getEndUTC() <= endRef) {
						
						chartBarData.add(item);
						
					} else if (item.getStartUTC() >= originRef && item.getStartUTC() < endRef) {
						
						chartBarData.add(item);
						
					} else if (item.getStartUTC() < originRef && item.getEndUTC() > endRef) {
						
						chartBarData.add(item);
						break;
						
					}
					
				}
				
				mChartBars.add(new ChartBar(chartBarData, originRef, endRef));
				chartBarData.clear();
				
			}
			
		}
		
		public ArrayList<ASPDB.QueueItem> getData() {
			
			return mData;
			
		}
		
		public int getBarCount() {
			
			return mBarCount;
			
		}
		
		public int getBarWindow() {
			
			return mBarWindow;
			
		}
		
		public int getScrollPos() {
			
			return mScrollPos;
			
		}
		
		public int getScrollMax() {
			
			return mScrollMax;
			
		}
		
		public ArrayList<RectF> getBarBounds() {
			
			return mBarBounds;
			
		}
		
		public ArrayList<ChartBar> getChartBars() {
			
			return mChartBars;
			
		}
		
		public void incScrollPos() {
			
			mScrollPos += mScrollPos < mScrollMax ? 1 : 0;
			
		}
		
		public void decScrollPos() {
			
			mScrollPos -= mScrollPos > mScrollMax ? 1 : 0;
			
		}
		
		public void drawBars(Canvas canvas) {
			
			for (int i = 0; i < mBarWindow; i++) {
				
				mChartBars.get(mScrollPos + i).drawBar(canvas, mBarBounds.get(i));
				
			}
			
		}
		
		public ASPDB.QueueItem getItemAt(float x, float y) {
			
			for (int i = 0; i < mBarBounds.size(); i++) {
				
				RectF bounds = mBarBounds.get(i);
				
				if (x >= bounds.left && x <= bounds.right) {
					
					return mChartBars.get(mScrollPos + i).getItemAt(y);
					
				}
				
			}
			
			return null;
			
		}
		
	}
	
	private class ChartBar {

		private long mOriginRef;
		private long mEndRef;
		
		private ArrayList<ASPDB.QueueItem> mData;
		private ArrayList<Paint> mPaint;
		private ArrayList<float[]> mBounds;
		
		public ChartBar(ArrayList<ASPDB.QueueItem> data, long originRef, long endRef) {


			mOriginRef = originRef;
			mEndRef = endRef;
			
			mData = new ArrayList<ASPDB.QueueItem>();
			mPaint = new ArrayList<Paint>();
			mBounds = new ArrayList<float[]>();
			
			if (data != null) {
			
				mData = data;
				
				long itemStartRef, itemEndRef;
				
				for (int i = 0; i < data.size(); i++) {
				
					Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
					paint.setColor(data.get(i).getColor());
					paint.setStyle(Paint.Style.FILL);
				
					mPaint.add(paint);
				
					itemStartRef = data.get(i).getStartUTC() - originRef;
					
					if (itemStartRef < 0 ) {
						
						itemStartRef = 0;
						
					}
					
					itemEndRef = data.get(i).getEndUTC() - originRef;
					
					if (itemEndRef > endRef) {
						
						itemEndRef = endRef;
						
					}
					
					mBounds.add(new float[] { itemStartRef, itemEndRef });
					
				}
		
			}
				
		}
		
		public ASPDB.QueueItem getItemAt(float y) {
			
			for (int i = 0; i < mBounds.size(); i++) {
				
				float[] bounds = mBounds.get(i);
				
				if (y >= bounds[0] && y <= bounds[1]) {
					
					return mData.get(i);
					
				}
				
			}
			
			return null;
			
		}
		
		public long getOriginRef() {
			
			return mOriginRef;
			
		}
		
		public long getEndRef() {
			
			return mEndRef;
			
		}
	
		public void drawBar(Canvas canvas, RectF barRectF) {
			
			for (int i = 0; i < mBounds.size(); i++) {
				
				float[] bounds = mBounds.get(i);
				float length = mEndRef - mOriginRef;
				
				float l = barRectF.left;
				float t = barRectF.bottom + ((bounds[1] / length) * barRectF.height());
				float r = barRectF.right;
				float b = barRectF.bottom + ((bounds[0] / length) * barRectF.height());
				RectF rectF = new RectF(l, t, r, b);
				
				canvas.drawRect(rectF, mPaint.get(i));
			
			}
			
		}
	}
	
}
