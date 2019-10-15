package com.gmail.mstudyplanner;

import android.app.TabActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tab extends TabActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabSpec spec;

        spec = tabHost.newTabSpec("A").setIndicator("tab1",
                res.getDrawable( R.drawable.calendar) ).setContent(R.id.imageView1);

        spec = tabHost.newTabSpec("B").setIndicator("tab2",
                res.getDrawable( R.drawable.calendar) ).setContent(R.id.imageView2);

        spec = tabHost.newTabSpec("C").setIndicator("tab3",
                res.getDrawable( R.drawable.home) ).setContent(R.id.imageView3);

        spec = tabHost.newTabSpec("D").setIndicator("tab4",
                res.getDrawable( R.drawable.check) ).setContent(R.id.imageView4);

        spec = tabHost.newTabSpec("E").setIndicator("tab5",
                res.getDrawable( R.drawable.my) ).setContent(R.id.imageView5);

        tabHost.setCurrentTab(0);

    }


}
