package hieubd.mobilefinal.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import hieubd.dto.GroupDTO;
import hieubd.mobilefinal.GroupInfoFragment;
import hieubd.mobilefinal.MemberFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    private GroupDTO dto;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs, GroupDTO dto) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.dto = dto;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GroupInfoFragment groupInfoFragment = new GroupInfoFragment(dto);
                return groupInfoFragment;
            case 1:
                MemberFragment memberFragment = new MemberFragment(dto);
                return memberFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
