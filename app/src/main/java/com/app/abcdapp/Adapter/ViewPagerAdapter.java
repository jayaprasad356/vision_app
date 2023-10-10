package com.app.abcdapp.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.abcdapp.fragment.CompleteJobDetailFragment;
import com.app.abcdapp.fragment.InformationVideosFragment;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    public ViewPagerAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new CompleteJobDetailFragment();
        else if (position == 1)
            fragment = new InformationVideosFragment();
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Complete Job Detail";
        else if (position == 1)
            title = "Information Videos";
        return title;
    }
}
