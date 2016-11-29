package com.sandamil.buscaminas;

import android.content.Context;
import android.graphics.Typeface;

public enum Fonts {
	TEXTCELL(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Abbeyline.ttf");
		}
	},
	TEXTCELL2(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Acquaintance.ttf");
		}
	},
	TEXTCELL3(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/AltamonteNF.ttf");
		}
	},
	TEXTCELL4(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/BACKB.ttf");
		}
	},
	TEXTCELL5(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/backlash.ttf");
		}
	},
	TEXTCELL6(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/BlackCastleMF.ttf");
		}
	},
	TEXTCELL7(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Bolide-Regular.ttf");
		}
	},
	TEXTCELL8(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Brushed.ttf");
		}
	},
	TEXTCELL9(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/CHAMPGNE.TTF");
		}
	},
	TEXTCELL10(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/commersial_script.ttf");
		}
	},
	TEXTCELL11(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Hawaii_Killer.ttf");
		}
	},
	TEXTCELL12(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/knot.ttf");
		}
	},
	TEXTCELL13(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/Punk.ttf");
		}
	},
	TEXTCELL14(){
		public Typeface create (Context c){
			return Typeface.createFromAsset(c.getAssets(),"Fonts/TypewriterKeys.ttf");
		}
	},
	SELECTED_TEXTCELL(){
		public Typeface create(Context c) {
			return TEXTCELL6.getTypeFace(c);
		}
	};
	
	public abstract Typeface create(Context c); 
	public Typeface typeFace;
	
	public Typeface getTypeFace(Context c){
		if (typeFace == null)
			typeFace = create(c);
		return typeFace;
	}
}
