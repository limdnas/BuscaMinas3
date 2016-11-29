package com.sandamil.buscaminas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

public class ButtonsAppearance {
	public static void setDrawableTo(final Button button, int opt) {
    	switch (opt) {
    		case 2: {
    			// Bot�n con el foco
    			int width = button.getLayoutParams().width;
    	    	int height = button.getLayoutParams().height;
    	    	float border = width * 0.015f;
    	    	float radius = width * 0.08f;
    	    	int light = Color.rgb(255, 255, 0);
    			int dark = Color.rgb(255, 215, 0);
    			Paint p = new Paint();
    	    	p.setAntiAlias(true); 	
    	    	p.setARGB(255, 50, 50, 50);
    	    	Paint p2 = new Paint();
    	    	p2.setAntiAlias(true);
    	    	//p2.setARGB(255, 255, 69, 0);
    	    	Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	    	b.eraseColor(Color.argb(0, 0, 0, 0));
    	    	Canvas c = new Canvas(b);
    	    	c.drawRoundRect(new RectF(0, 0, width, height), radius, radius, p);
    	    	//c.drawRoundRect(new RectF(0, 0, width, height-border), radius, radius, p2);
    	    	RadialGradient shader = new RadialGradient(width/2, height/2, width/2, light, dark, Shader.TileMode.CLAMP);
    	    	p.setShader(shader);
    	    	c.drawRoundRect(new RectF(0, 0, width, height-(border)), radius, radius, p);
    	    	BitmapDrawable d = new BitmapDrawable(b);
    	    	button.setBackgroundDrawable(d);
    			break;
    		}
    		case 3: {
    			// Bot�n pulsado
    			int width = button.getLayoutParams().width;
    	    	int height = button.getLayoutParams().height;
    	    	float border = width * 0.015f;
    	    	float radius = width * 0.08f;
    	    	int light = Color.rgb(255, 215, 0);
    			int dark = Color.rgb(255, 165, 0);
    			Paint p = new Paint();
    	    	p.setAntiAlias(true); 	
    	    	p.setARGB(255, 50, 50, 50);
    	    	Paint p2 = new Paint();
    	    	p2.setAntiAlias(true);
    	    	p2.setARGB(255, 255, 255, 255);
    	    	Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	    	b.eraseColor(Color.argb(0, 0, 0, 0));
    	    	Canvas c = new Canvas(b);
    	    	//c.drawRoundRect(new RectF(0, 0, width, height), radius, radius, p);
    	    	//c.drawRoundRect(new RectF(border, border, width-border, height-border), radius, radius, p2);
    	    	RadialGradient shader = new RadialGradient(width/2, height/2, width/2, light, dark, Shader.TileMode.CLAMP);
    	    	p.setShader(shader);
    	    	c.drawRoundRect(new RectF(border, border, width-border, height), radius, radius, p);
    	    	BitmapDrawable d = new BitmapDrawable(b);
    	    	button.setBackgroundDrawable(d);
    	    	break;
    		}
    		case 4: {
    			// Bot�n inactivo
    			int width = button.getLayoutParams().width;
    			int height = button.getLayoutParams().height;
    			float border = width * 0.015f;
    			float radius = width * 0.08f;
    			int light = Color.rgb(205, 200, 177);
    			int dark = Color.rgb(139, 136, 120);
    			Paint p = new Paint();
    			p.setAntiAlias(true); 	
    			p.setARGB(255, 50, 50, 50);
    			Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    			b.eraseColor(Color.argb(0, 0, 0, 0));
    			Canvas c = new Canvas(b);
    			c.drawRoundRect(new RectF(0, 0, width, height), radius+(border/2), radius+(border/2), p);
    			RadialGradient shader = new RadialGradient(width/2, height/2, width/2, light, dark, Shader.TileMode.CLAMP);
    			p.setShader(shader);
    			c.drawRoundRect(new RectF(0, 0, width, height-border), radius, radius, p);
    			BitmapDrawable d = new BitmapDrawable(b);
    			button.setBackgroundDrawable(d);
    			break;
    		}
    		default: {
    			// Bot�n normal
    			int width = button.getLayoutParams().width;
    	    	int height = button.getLayoutParams().height;
    	    	float border = width * 0.015f;
    	    	float radius = width * 0.08f;
    			int light = Color.rgb(255, 215, 0);
    			int dark = Color.rgb(255, 165, 0);
    	    	Paint p = new Paint();
    	    	p.setAntiAlias(true); 	
    	    	p.setARGB(255, 50, 50, 50);
    	    	Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    			b.eraseColor(Color.argb(0, 0, 0, 0));
    			Canvas c = new Canvas(b);
    			c.drawRoundRect(new RectF(0, 0, width, height), radius+(border/2), radius+(border/2), p);
    			RadialGradient shader = new RadialGradient(width/2, height/2, width/2, light, dark, Shader.TileMode.CLAMP);
    			p.setShader(shader);
    			c.drawRoundRect(new RectF(0, 0, width, height-border), radius, radius, p);
    			BitmapDrawable d = new BitmapDrawable(b);
    			button.setBackgroundDrawable(d);
    			break;
    		}
    	}
    }
	
	public static void disminuirTamTexto(Button button) {
    	button.setPadding(3, 3, 0, 0);
    	button.setTextSize(button.getTextSize()-0.5f);
    }
    
    public static void aumentarTamTexto(Button button) {
    	button.setPadding(3, -3, 0, 0);
    	button.setTextSize(button.getTextSize()+0.5f);
    }
}
