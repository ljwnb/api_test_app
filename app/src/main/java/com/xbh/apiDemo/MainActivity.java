package com.xbh.apiDemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.xbh.apiDemo.fragment.Api905APage;
import com.xbh.apiDemo.fragment.ApiCommonPage;
import com.xbh.apiDemo.fragment.FragmentAdapter;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;

public class MainActivity extends AppCompatActivity {

    private VerticalTabLayout vTab;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        vTab=findViewById(R.id.vTab);
        mViewPager=findViewById(R.id.viewPager);

    }

    private void initData() {
        ArrayList<String> tabList = new ArrayList<>();
        ArrayList<Fragment> pageList = new ArrayList<>();

        /*
        * Add new tab and page by (tabName.add, fragments.add)
        * */
        //1. add tab and page: api common
        tabList.add(getResources().getString(R.string.api_common_title));
        pageList.add(ApiCommonPage.newInstance(getResources().getString(R.string.api_common_title)));

        //2. add tab and page: api 905A hardware
        tabList.add(getResources().getString(R.string.api_905a_title));
        pageList.add(Api905APage.newInstance(getResources().getString(R.string.api_905a_title)));


        /*
         * load tab and page
         * */
        FragmentAdapter fragTabAdapter = new FragmentAdapter(this.getSupportFragmentManager(), pageList,
                tabList);
        mViewPager.setAdapter(fragTabAdapter);
        vTab.setupWithViewPager(mViewPager);
    }

}