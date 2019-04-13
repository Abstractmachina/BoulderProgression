package android.taole.boulderprogression;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class SectionsPageAdapter extends FragmentPagerAdapter
{

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addFragment(Fragment f, String t)
    {
        mFragmentList.add(f);
        mFragmentTitleList.add(t);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i)
    {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }
}
