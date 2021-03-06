package com.client.vpman.weatherwall.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.client.vpman.weatherwall.ui.Fragment.Explore;
import com.client.vpman.weatherwall.ui.Fragment.LastFragment;
import com.client.vpman.weatherwall.ui.Fragment.Popular;
import com.client.vpman.weatherwall.ui.Fragment.WeatherFragment;

public class DemoFragmentStateAdapter extends FragmentStatePagerAdapter {
    public DemoFragmentStateAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WeatherFragment.newInstance("WeatherFragment, Instance 1");
            case 1:
                return LastFragment.newInstance("LastFragment, Instance1");

            case 2:
                return Popular.newInstance("Popular, Instance1");

            case 3:
                return Explore.newInstance("Explore, Instance1");

            default:
                return WeatherFragment.newInstance("WeatherFragment, Instance 1");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
