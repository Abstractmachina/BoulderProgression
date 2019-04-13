package android.taole.boulderprogression;

import android.content.res.Resources;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utility
{

    Utility() {}

    /**
     * Round double to specified number of decimal points
     * @param value input value
     * @param places decimal points
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
