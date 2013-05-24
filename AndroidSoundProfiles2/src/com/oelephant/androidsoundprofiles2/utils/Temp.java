package com.oelephant.androidsoundprofiles2.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Temp extends View {

	private Context mContext;
	private AttributeSet mAttrs;

	private static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;
	
	private float[] mViewPadding = new float[] {10, 10, 10, 10};
	
	private RectF mXAxisBounds;
	private Paint mXAxisPaint;
	
	private RectF mYAxisBounds;
	private Paint mYAxisPaint;
	
	private RectF mChartBounds;
	private Paint mChartBackgroundPaint;
	private Paint mNoDataPaint;
	private Paint mChartAxisPaint;
	
	private RectF mChartLinesBounds;
	private Paint mChartLinesPaint;
	private float[] mChartLinesPadding = new float[] {5, 5, 5, 5};
	
	private ArrayList<ASPDB.QueueItem> mQueue;
	private int mBarWindow;
	
	private ChartData mChartData;
	private YAxisData mYAxisData;
	private XAxisData mXAxisData;
	private ChartLinesData mChartLinesData;
	
	private GestureDetector mGesture;
	
	public Temp(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		mContext = context;
		mAttrs = attrs;
		
		init();
		
	}
	
	public void init() {

		mGesture = new GestureDetector(this.getContext(), new MyGestureListener());
		
		ASPDB db = new ASPDB(mContext);
		
		mQueue = db.getQueue();
		
		// TODO
		mBarWindow = 7;
		
		db.close();
		
		mXAxisPaint = new Paint();
		mXAxisPaint.setColor(Color.BLACK);
		mXAxisPaint.setTextSize(12);
		
		mYAxisPaint = new Paint();
		mYAxisPaint.setColor(Color.BLACK);
		mYAxisPaint.setTextSize(12);

		mChartBackgroundPaint = new Paint();
		mChartBackgroundPaint.setColor(Color.LTGRAY);
		mChartBackgroundPaint.setStyle(Paint.Style.FILL);
		
		mNoDataPaint = new Paint();
		mNoDataPaint.setColor(Color.BLACK);
		mNoDataPaint.setTextSize(20);
		
		mChartAxisPaint = new Paint();
		mChartAxisPaint.setColor(Color.BLACK);
		mChartAxisPaint.setStyle(Paint.Style.STROKE);
		
		mChartLinesPaint = new Paint();
		mChartLinesPaint.setColor(Color.RED);
		mChartLinesPaint.setStyle(Paint.Style.STROKE);
		mChartLinesPaint.setStrokeWidth(2);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
	
		float l, t, r, b;
		float yWidth = mYAxisPaint.measureText("XXXXX");
		float xHeight = mXAxisPaint.measureText("XXX XX/XX");
		
		l = mViewPadding[LEFT] + yWidth;
		t = mViewPadding[TOP];
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM] - xHeight;
		
		mChartBounds = new RectF(l, t, r, b);
		mChartData = new ChartData(mQueue, mBarWindow, mChartBounds);
		
		l = mViewPadding[LEFT] + yWidth - mChartLinesPadding[LEFT];
		t = mViewPadding[TOP];
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM] - xHeight + mChartLinesPadding[BOTTOM];
		
		mChartLinesBounds = new RectF(l, t, r, b);
		mChartLinesData = new ChartLinesData(mChartBounds.width() / 2, mChartBounds.height() / 2, mChartLinesBounds);
		
		l = mViewPadding[LEFT] + yWidth;
		t = h - mViewPadding[BOTTOM] - xHeight;
		r = w - mViewPadding[RIGHT];
		b = h - mViewPadding[BOTTOM];
		
		mXAxisBounds = new RectF(l, t, r, b);
		mXAxisData = new XAxisData(mXAxisBounds);
		
		l = mViewPadding[LEFT];
		t = mViewPadding[TOP];
		r = mViewPadding[LEFT] + yWidth;
		b = h - mViewPadding[BOTTOM] - xHeight;
		
		mYAxisBounds = new RectF(l, t, r, b);
		mYAxisData = new YAxisData(mYAxisBounds);
		
		// TODO help overlay
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		mChartData.drawBackground(canvas);
		
		mChartData.drawBars(canvas);
		
		mYAxisData.drawLabel(canvas);
		
		mXAxisData.drawLabel(canvas);

		mChartData.drawAxis(canvas);
		
		mChartLinesData.drawLines(canvas);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		mGesture.onTouchEvent(event);
		
		return true;
	
	}
	
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onDown(MotionEvent e) {

			
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
				
				ArrayList<RectF> mBarBounds = mChartData.getBarBounds();
				
				for (int i = 0; i < mChartData.getBarCount(); i++) {
					
					if (x >= mBarBounds.get(i).left && x <= mBarBounds.get(i).right) {
						
						x = mBarBounds.get(i).left + (mBarBounds.get(i).width() / 2);
						
						break;
						
					}
					
				}

				mChartLinesData.set(x, y);
				postInvalidate();
				
				return true;
			
			} else if (x >= mXAxisBounds.left && x <= mXAxisBounds.right && 
					y >= mXAxisBounds.top && y <= mXAxisBounds.bottom) {
				
				if (distanceX < 0 && mChartData.getScrollPos() < mChartData.getScrollMax()) {
					
					mChartData.incScrollPos();
					postInvalidate();
					
				} else if (distanceX > 0 && mChartData.getScrollPos() > 0) {
					
					mChartData.decScrollPos();
					postInvalidate();
					
				}
				
			}
			
			return true;
	
		}
		
	}
	
	private class ChartLinesData {
		
		private float mX;
		private float mY;
		private RectF mBounds;
		
		public ChartLinesData(float x, float y, RectF bounds) {
			
			mX = x;
			mY = y;
			mBounds = bounds;
			
		}
		
		public void set(float x, float y) {
			
			mX = x;
			mY = y;
			
		}
		
		public void setBounds(RectF bounds) {
			
			mBounds = bounds;
			
		}
		
		public float getX() {
			
			return mX;
			
		}
		
		public float getY() {
			
			return mY;
			
		}
		
		public RectF getBounds() {
			
			return mBounds;
			
		}
		
		public void drawLines(Canvas canvas) {

			canvas.drawLine(mBounds.left, mY, mBounds.right, mY, mChartLinesPaint);
			canvas.drawLine(mX, mBounds.top, mX, mBounds.bottom, mChartLinesPaint);
			
		}
		
		
	}
	
	private class YAxisData {
		
		String[] labelText = new String[] { "3am", "6am", "9am", "12pm", "3pn", "6pm", "9pm", "12am" };
		Point[] labelPos;
		
		public YAxisData(RectF bounds) {
			
			labelPos = new Point[labelText.length];
			
			float x, y;
			x = bounds.left;
			
			for (int i = 0; i < labelText.length; i++) {
				
				y = bounds.bottom - ((i + 1) * (bounds.height() / labelText.length));
				
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
		
		SimpleDateFormat sdf;
		Calendar cal;

		ArrayList<Path> mLabelPaths;
		
		public XAxisData(RectF bounds) {
			
			sdf = new SimpleDateFormat("EEE MM/dd");
			cal = Calendar.getInstance();
			
			mLabelPaths = new ArrayList<Path>();
			
			ArrayList<RectF> barBounds = mChartData.getBarBounds();
			
			for (int i = 0; i < barBounds.size(); i++) {
				
				RectF barBound = barBounds.get(i);
				
				Path path = new Path();
				path.moveTo(barBound.left, bounds.bottom);
				path.moveTo(barBound.right, bounds.top);
				
				mLabelPaths.add(path);
				
			}
			
		}
		
		public void drawLabel(Canvas canvas) {
			
			if (mChartData.getBarCount() > 0) {
			
				for (int i = 0; i < mChartData.getBarWindow(); i++) {
					
					cal.setTimeInMillis(mChartData.getChartBar(mChartData.getScrollPos() + i).getOriginRef());
					canvas.drawTextOnPath(sdf.format(cal.getTime()), mLabelPaths.get(i), 0, 0, mXAxisPaint);
					
				}
			
			} else {
				
				
				
			}
			
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
		private Path mAxisPath;
		
		private String mNoData;
		private Point mNoDataPoint;
		
		/**
		 * data MUST be pulled from ASPDB.getQueue() so that the data is sorted correctly.
		 */
		public ChartData(ArrayList<ASPDB.QueueItem> data, int barWindow, RectF bounds) {
			
			mNoData = "No Data!";
			mNoDataPoint = new Point();
			mNoDataPoint.x = (int) (bounds.width() / 2);
			mNoDataPoint.y = (int) (bounds.height() / 2 + (mNoDataPaint.getTextSize() / 2));
			
			mAxisPath = new Path();
			mAxisPath.moveTo(bounds.left, bounds.top);
			mAxisPath.lineTo(bounds.left, bounds.bottom);
			mAxisPath.lineTo(bounds.right, bounds.bottom);
			
			mData = data;
			
			mBarCount = getBarCount(data);
			mChartBars = new ArrayList<ChartBar>();
			
			mBarWindow = barWindow;
			mBarBounds = new ArrayList<RectF>();
			
			mScrollPos = 0;
			mScrollMax = mBarCount - barWindow > 0 ? mBarCount - barWindow : 0;

			if (mBarCount == 0) {
				
				return;
				
			}
			
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
			
			for (int bar = 0; bar < mBarCount; bar++) {
				
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
		
		public int getBarCount(ArrayList<ASPDB.QueueItem> data) {
			
			if (data.size() == 0) {
				
				return 0;
				
			}
			
			long minStartUTC = Long.MAX_VALUE;
			long maxEndUTC = Long.MIN_VALUE;
			
			long startUTC, endUTC;
			
			for (int i = 0; i < data.size(); i++) {

				startUTC = data.get(i).getStartUTC();
				endUTC = data.get(i).getEndUTC();

				if (startUTC < minStartUTC) {
					
					minStartUTC = startUTC;
					
				}

				if (endUTC < maxEndUTC) {
					
					minStartUTC = startUTC;
					
				}
				
			}
			
			long diff = maxEndUTC - minStartUTC;
			
			return (int) diff / 1000 / 60 / 60 / 24;
			
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
		
		public ChartBar getChartBar(int pos) {
			
			return mChartBars.get(pos);
			
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
		
		public ASPDB.QueueItem getItemAt(float x, float y) {
			
			for (int i = 0; i < mBarBounds.size(); i++) {
				
				RectF bounds = mBarBounds.get(i);
				
				if (x >= bounds.left && x <= bounds.right) {
					
					return mChartBars.get(mScrollPos + i).getItemAt(y);
					
				}
				
			}
			
			return null;
			
		}
		
		public void drawBars(Canvas canvas) {
			
			if (mBarCount > 0) {
			
				for (int i = 0; i < mBarWindow; i++) {
					
					mChartBars.get(mScrollPos + i).drawBar(canvas, mBarBounds.get(i));
					
				}
			
			} else {
				
				drawNoData(canvas);
				
			}
			
		}
		
		public void drawBackground(Canvas canvas) {
			
			canvas.drawRect(mChartBounds, mChartBackgroundPaint);
			
		}
		
		public void drawNoData(Canvas canvas) {
			
			canvas.drawText(mNoData, mNoDataPoint.x, mNoDataPoint.y, mNoDataPaint);
			
		}
		
		public void drawAxis(Canvas canvas) {
			
			canvas.drawPath(mAxisPath, mChartAxisPaint);
			
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
