package com.example.vaccinationapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.example.vaccinationapp.fragments.DoctorFragment;
import com.example.vaccinationapp.fragments.ParentFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MembersActivity extends AppCompatActivity {

    ImageView mImageBack;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        mInit();
    }

    private void mInit() {
        mImageBack = findViewById(R.id.back);
        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        mImageBack.setOnClickListener( v -> {
                    onBackPressed();
                }
        );
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorFragment(), "Doctors");
        adapter.addFragment(new ParentFragment(), "Parents");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}