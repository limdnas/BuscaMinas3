package com.sandamil.buscaminas.game;

import com.sandamil.buscaminas.Fonts;
import com.sandamil.buscaminas.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

public enum CellEnum {
	CLOSE() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			int color1 = Color.argb(0xFF, 36, 168, 236);
			int color2 = Color.argb(0xFF, 160, 240, 230);
			int border = Color.argb(0xFF, 117, 208, 255);
			Paint p = new Paint();
			p.setAntiAlias(true);
			float width = dest.width() / 20.0f;
			float insideSize = dest.width() - 2 * width;
			RectF inside1 = new RectF(dest.left + width-1, dest.top + width-1, dest.right - width+1, dest.bottom - width+1);
			RectF inside = new RectF(dest.left + width, dest.top + width, dest.right - width, dest.bottom - width);
			LinearGradient linear = new LinearGradient(
					inside1.left,
					inside1.top + insideSize/3,
					inside1.right,
					inside1.top + 2*insideSize/3,
					color1, color2, Shader.TileMode.CLAMP);
			p.setShader(linear);
			c.drawRoundRect(inside1, width, width, p);
			p.setShader(null);
			p.setColor(border);
			Path path = new Path();
			path.addRoundRect(inside, width, width, Path.Direction.CCW);
			path.addRect(new RectF(dest), Path.Direction.CW);
			c.drawPath(path, p);
		}
	},
	MINE1() {
		Bitmap bitmap = null;
		Paint paint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (bitmap == null){
				bitmap = getResBitmap(context.getResources(), R.drawable.mine1);
				paint = new Paint();
				paint.setAntiAlias(false);
				paint.setFilterBitmap(true);
			}
			c.drawBitmap(bitmap, null, dest, paint);
		}
	},
	MINE2() {
		Bitmap bitmap = null;
		Paint paint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (bitmap == null){
				bitmap = getResBitmap(context.getResources(), R.drawable.mine2);
				paint = new Paint();
				paint.setAntiAlias(false);
				paint.setFilterBitmap(true);
			}
			c.drawBitmap(bitmap, null, dest, paint);
		}
	},
	CURSE() {
		Bitmap bitmap = null;
		Paint paint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (bitmap == null){
				bitmap = getResBitmap(context.getResources(), R.drawable.skull);
				paint = new Paint();
				paint.setAntiAlias(false);
				paint.setFilterBitmap(true);
			}
			c.drawBitmap(bitmap, null, dest, paint);
		}
	},
	EMPTY() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			int color0 = Color.argb(0xFF, 196, 181, 51);
			int color1 = Color.argb(0xFF, 251, 219, 39);
			int color2 = Color.argb(0xFF, 255, 239, 151);
			int border = Color.argb(0xFF, 220, 200, 80);
			Paint p = new Paint();
			p.setAntiAlias(true);
			float width = dest.width() / 20.0f;
			float insideSize = dest.width() - 2 * width;
			RectF inside1 = new RectF(dest.left + width-1, dest.top + width-1, dest.right - width+1, dest.bottom - width+1);
			RectF inside = new RectF(dest.left + width, dest.top + width, dest.right - width, dest.bottom - width);
			LinearGradient linear = new LinearGradient(
				inside1.left,
				inside1.top + insideSize/3,
				inside1.right,
				inside1.top + 2*insideSize/3,
				new int[]{color0, color1, color2},null, Shader.TileMode.CLAMP);
			p.setShader(linear);
			c.drawRoundRect(inside1, width, width, p);
			p.setShader(null);
			p.setColor(border);
			Path path = new Path();
			path.addRoundRect(inside, width, width, Path.Direction.CCW);
			path.addRect(new RectF(dest), Path.Direction.CW);
			c.drawPath(path, p);			
		}
	},
	EMPTY2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			int color0 = Color.argb(0xFF, 196, 181, 51);
			int color1 = Color.argb(0xFF, 251, 219, 39);
			int color2 = Color.argb(0xFF, 255, 239, 151);
			int border = Color.argb(0xFF, 220, 200, 80);
			Paint p = new Paint();
			p.setAntiAlias(true);
			float width = dest.width() / 20.0f;
			float insideSize = dest.width() - 2 * width;
			RectF inside1 = new RectF(dest.left + width-1, dest.top + width-1, dest.right - width+1, dest.bottom - width+1);
			RectF inside = new RectF(dest.left + width, dest.top + width, dest.right - width, dest.bottom - width);
			LinearGradient linear = new LinearGradient(
				inside1.left,
				inside1.top + insideSize/3,
				inside1.right,
				inside1.top + 2*insideSize/3,
				new int[]{color0, color1, color2},null, Shader.TileMode.CLAMP);
			p.setShader(linear);
			c.drawRoundRect(inside1, width, width, p);
			p.setShader(null);
			p.setColor(border);
			Path path = new Path();
			path.addRoundRect(inside, width, width, Path.Direction.CCW);
			path.addRect(new RectF(dest), Path.Direction.CW);
			c.drawPath(path, p);
		}
	},
	ZERO() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0xFFFFFF);
			}
			drawNumber(c, 0, dest, textPaint);
		}
	},
	ONE() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x0000AA);
			}
			drawNumber(c, 1, dest, textPaint);
		}
	},
	TWO() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x00BB00);
			}
			drawNumber(c, 2, dest, textPaint);
		}
	},
	THREE() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0xFF0000);
			}
			drawNumber(c, 3, dest, textPaint);
		}
	},
	FOUR() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x8888FF);
			}
			drawNumber(c, 4, dest, textPaint);
		}
	},
	FIVE() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x88FF88);
			}
			drawNumber(c, 5, dest, textPaint);
		}
	},
	SIX() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0xFF8888);
			}
			drawNumber(c, 6, dest, textPaint);
		}
	},
	SEVEN() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x888888);
			}
			drawNumber(c, 7, dest, textPaint);
		}
	},
	EIGHT() {
		Paint textPaint = null;

		public void paintBitmap(Context context, Canvas c, Rect dest) {
			if (textPaint == null){
				textPaint = createTextPaint(context, 0x888888);
			}
			drawNumber(c, 8, dest, textPaint);
		}
	},
	
	ZERO1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	ONE1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	TWO1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	THREE1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	FOUR1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	FIVE1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	SIX1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	SEVEN1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	EIGHT1() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	ZERO2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	ONE2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	TWO2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	THREE2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	FOUR2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	FIVE2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	SIX2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	SEVEN2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	},
	EIGHT2() {
		public void paintBitmap(Context context, Canvas c, Rect dest) {
			// TODO Auto-generated method stub
			
		}
	};

	private final static CellEnum[] states = values();
	private Integer value = null;

	public abstract void paintBitmap(Context context, Canvas c, Rect dest);

	public Bitmap getResBitmap(Resources res, int bmpResId) {
		Options opts = new Options();
		opts.inDither = false;
		return BitmapFactory.decodeResource(res, bmpResId, opts);
	}
	
	public Bitmap getResBitmapMutable(Resources res, int bmpResId) {
		Options opts = new Options();
		opts.inDither = false;
		Bitmap b = BitmapFactory.decodeResource(res, bmpResId, opts);
		
		int w = b.getWidth();
		int h = b.getHeight();
		Bitmap mutable	= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		int[] pixels = new int[w * h];
		b.getPixels(pixels, 0, w, 0, 0, w, h);
		mutable.setPixels(pixels, 0, w, 0, 0, w, h);
		return mutable;
	}
	
	private static Paint createTextPaint(Context context, int color){
		Paint textPaint = new Paint();
		textPaint.setColor(color);
		textPaint.setAlpha(255);
		textPaint.setAntiAlias(true);
		textPaint.setTypeface(Fonts.SELECTED_TEXTCELL.getTypeFace(context));
		textPaint.setTextAlign(Paint.Align.CENTER);
		return textPaint;
	}
	
	private static void drawNumber(Canvas c, int number, Rect dest, Paint textPaint){
		textPaint.setTextSize(dest.width() * 0.8f);
		String strNumber = "" + number;
		Rect bounds = new Rect();
		textPaint.getTextBounds(strNumber, 0, 1, bounds);
//		S.m("BOUNDS: left: " + bounds.left + ", top: " + bounds.top + ", right: " + bounds.right + ", bottom: " + bounds.bottom);
		float y = dest.top + bounds.height() + (dest.height() - bounds.height()) / 2.0f - bounds.bottom;
//		float x = dest.left + dest.width()/2.0f - bounds.left;
		float x = dest.left + dest.width() / 2.0f;  // HACE FALTA EL - bounds.left?
		c.drawText(strNumber, x, y, textPaint);
	}
	
	public Bitmap toRight(Bitmap source){
		Bitmap right = Bitmap.createBitmap(
				(int)(source.getWidth() * 1.2),
				(int)(source.getHeight() * 1.2),
				Bitmap.Config.ARGB_8888);
		Rect src = new Rect(0, 0, source.getWidth() - (int) (source.getWidth() * 0.2), source.getHeight());
		int dif = right.getWidth() - source.getWidth();
		Rect dest = new Rect(right.getWidth() - (src.right - src.left), dif / 2, right.getWidth(), dif / 2 + src.bottom);
		Canvas c = new Canvas(right);
		c.drawBitmap(source, src, dest, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		return right;
	}
	
	public Bitmap toLeft(Bitmap source){
		Bitmap right = Bitmap.createBitmap(
				(int)(source.getWidth() * 1.2),
				(int)(source.getHeight() * 1.2),
				Bitmap.Config.ARGB_8888);
		Rect src = new Rect((int) (source.getWidth() * 0.2), 0, source.getWidth(), source.getHeight());
		int dif = right.getWidth() - source.getWidth();
		Rect dest = new Rect(0, dif / 2, src.right - src.left, dif / 2 + src.bottom);
		Canvas c = new Canvas(right);
		c.drawBitmap(source, src, dest, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		return right;
	}
	
	public void setColorToNumber(Bitmap number, int color){
		for (int col = 0; col < number.getWidth(); col++){
			for (int row = 0; row < number.getHeight(); row++){
				number.setPixel(col, row, (number.getPixel(col, row) & 0xFF000000) | (0x00FFFFFF & color));
			}
		}
	}
	
	public int getIndex(){
		if (value == null){
			for (int i = 0; i < states.length; i++){
				if (states[i] == this){
					value = i;
					break;
				}
			}
		}
		return value;
	}
	
	public static CellEnum fromNumber(int i){
		return get(ZERO.getIndex() + i);
	}
	
	public static CellEnum fromNumber1(int i){
		return get(ZERO1.getIndex() + i);
	}
	
	public static CellEnum fromNumber2(int i){
		return get(ZERO2.getIndex() + i);
	}
	
	public static CellEnum get(int i) {
		return states[i];
	}
	
	public static CellEnum getMine(int player){
		return get(MINE1.getIndex() + player);
	}
}
