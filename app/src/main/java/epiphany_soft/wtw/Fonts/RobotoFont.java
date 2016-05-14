package epiphany_soft.wtw.Fonts;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Camilo on 31/03/2016.
 */
public class RobotoFont {

    private static RobotoFont instance;
    private static Typeface typeface;

    public static RobotoFont getInstance(Context context) {
        synchronized (RobotoFont.class) {
            if (instance == null) {
                instance = new RobotoFont();
                typeface = Typeface.createFromAsset(context.getResources().getAssets(), "Roboto-Light.ttf");
            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
