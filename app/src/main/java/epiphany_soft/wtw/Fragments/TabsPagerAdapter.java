package epiphany_soft.wtw.Fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Camilo on 24/04/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private int count;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setCount(int count){
        this.count = count;
    }
    @Override
    public android.support.v4.app.Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FragmentConsultarPeliculas();
            case 1:
                // Games fragment activity
                return new FragmentConsultarSeries();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return count;
    }

}