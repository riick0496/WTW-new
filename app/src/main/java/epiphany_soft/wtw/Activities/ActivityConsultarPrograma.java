package epiphany_soft.wtw.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Fragments.TabsPagerAdapter;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 22/03/2016.
 */
public class ActivityConsultarPrograma extends ActivityBase implements
        android.support.v7.app.ActionBar.TabListener {
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Peliculas", "Series"};

    public static boolean actualizado=false;
    @Override
    public void onResume(){
        super.onResume();
        if (actualizado) this.recreate();
        actualizado=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mAdapter.setCount(tabs.length);

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener((android.support.v7.app.ActionBar.TabListener) this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
