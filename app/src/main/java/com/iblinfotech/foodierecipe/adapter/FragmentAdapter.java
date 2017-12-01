package com.iblinfotech.foodierecipe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iblinfotech.foodierecipe.fragments.CategoryItem_mostPoppularFragment;
import com.iblinfotech.foodierecipe.fragments.CategoryItem_mostcommentedFragment;
import com.iblinfotech.foodierecipe.fragments.CategoryItem_newestFragment;


public class FragmentAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Newest", "Most Popular", "Most Commented"};
    public static String tabName;

    public FragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new CategoryItem_newestFragment();
                break;
            case 1:
                result = new CategoryItem_mostPoppularFragment();
                break;
            case 2:
                result = new CategoryItem_mostcommentedFragment();
                break;
            default:
                result = null;
                break;
        }

        return result;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}