package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hieubd.mobilefinal.ui.ViewPagerAdapter;

public class GroupDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        tabLayout = findViewById(R.id.tabsGroup);
        viewPager = findViewById(R.id.viewPager);
        Intent intent = this.getIntent();
        name = intent.getStringExtra("NAME");
        setUpViewPager();
        //tabLayout.setupWithViewPager(viewPager);
        setIcon();
    }

    private void setUpViewPager(){
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount(), name);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
}

    private void setIcon(){
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_launcher);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_launcher);
    }
}
