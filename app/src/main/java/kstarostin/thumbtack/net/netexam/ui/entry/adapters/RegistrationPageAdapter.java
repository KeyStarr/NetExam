package kstarostin.thumbtack.net.netexam.ui.entry.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationProfessorFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment;

/**
 * Created by Cyril on 01.03.2017.
 */
public class RegistrationPageAdapter extends FragmentPagerAdapter {

    private Context context;

    public RegistrationPageAdapter(FragmentManager fm, Context context){
       super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 1:
                return new RegistrationProfessorFragment();
            default:
                return new RegistrationStudentFragment();
        }
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public CharSequence getPageTitle (int position){
        switch (position){
            case 1 : return context.getString(R.string.teacher);
            default : return context.getString(R.string.student);
        }
    }
}
