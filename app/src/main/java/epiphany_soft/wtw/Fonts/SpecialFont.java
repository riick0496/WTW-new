package epiphany_soft.wtw.Fonts;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Camilo on 31/03/2016.
 */
public class SpecialFont {

    private static SpecialFont instance;
    private static Typeface typeface;

    public static SpecialFont getInstance(Context context) {
        synchronized (SpecialFont.class) {
            if (instance == null) {
                instance = new SpecialFont();
                typeface = Typeface.createFromAsset(context.getResources().getAssets(), "Capture_it.ttf");
            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
