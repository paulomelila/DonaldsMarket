package com.paulomelila.donaldsmarket;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private TabPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateWidgets();

        setupViewPager(mViewPager);

        getIntentFromService();

        mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStop() {
        startService(new Intent(this, NotificationService.class));
        super.onStop();
    }

    private void instantiateWidgets() {
        mViewPager = findViewById(R.id.container);
        mTabLayout = findViewById(R.id.tabs);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new HomeFragment(), getString(R.string.home_title));
        mAdapter.addFragment(new SpecialsFragment(), getString(R.string.specials_title));
        mAdapter.addFragment(new LocationsFragment(), getString(R.string.locations_title));

        viewPager.setAdapter(mAdapter);
    }

    private void getIntentFromService() {
        int mSpecialsTabPosition = getIntent().getIntExtra("Specials", 0);

        if (mSpecialsTabPosition == 1) {
            mViewPager.setCurrentItem(mSpecialsTabPosition);
        }
    }
}

