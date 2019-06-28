package com.weizhixiang.imagetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weizhixiang.imagetest.adapter.SpaceItemDecoration;
import com.weizhixiang.imagetest.adapter.WaterFallAdapter;
import com.weizhixiang.imagetest.data.User;
import com.weizhixiang.imagetest.data.image;
import com.weizhixiang.imagetest.data.works;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.scwang.smartrefresh.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private WaterFallAdapter mAdapter;
    private BottomNavigationView mBottomNavigationView;
    private User user = new User();
    private List<works> works = new ArrayList<>() ;
    private List<works> data = new ArrayList<>();
    private works work = new works();
    private List<image> images ;
    private static final int REQUEST_CODE_MAINACTIVITY = 1;
    public static final String USERNAME="com.weizhixang.imagetest.MainActivity.USERNAME";
    private int i;


    static {
        //全局构建器
        //Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.albumWhite);
                return new ClassicsHeader(context);
            }
        });
        //Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9bfe7b07ff60f8bc689320203b96149d");
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);


        buildworks();

        initBottomNavigation();
        //初始化下拉刷新
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                data.clear();
                buildworks();
                WaterFallAdapter mAdapter = new WaterFallAdapter(MainActivity.this, data);
                mRecyclerView.setAdapter(mAdapter);
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        fab = findViewById(R.id.fab);
        //mBottomNavigationView.findViewById(R.id.nav_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 1));
        mAdapter = new WaterFallAdapter(this, data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Intent intent = getIntent();
        user.setUsername(intent.getStringExtra(LoginActivity.USERNAME));
        fab.setOnClickListener(fabClickListeber);
        mRecyclerView.setAdapter(mAdapter);
        Toast.makeText(MainActivity.this,"hello："+user.getUsername(),3*1000).show();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(MainActivity.this, postion+"", 3*1000).show();
            }
        });
    }

    private FloatingActionButton.OnClickListener fabClickListeber = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,ImageLoad.class);
            if(user.getUsername()!=null){
                intent.putExtra(MainActivity.USERNAME,user.getUsername());
                startActivityForResult(intent, MainActivity.REQUEST_CODE_MAINACTIVITY);
        }
            else{
                Toast.makeText(MainActivity.this,"请登录！",3*1000).show();
            }
        }
    };

    private void buildworks() {
        String createdAt = "2019-6-23 10:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = null;
        try {
            createdAtDate = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<works> worksBmobQuery = new BmobQuery<>();
        worksBmobQuery.addWhereGreaterThan("createdAt", bmobCreatedAtDate);
        worksBmobQuery.findObjects(MainActivity.this, new FindListener<com.weizhixiang.imagetest.data.works>() {
            @Override
            public void onSuccess(List<com.weizhixiang.imagetest.data.works> list) {
                if (list!=null && list.size()>0){
                    //works = list;
//                    for( i = 0;i<list.size();i++){
//                        work = list.get(i);
//                        BmobQuery<image> imageBmobQuery = new BmobQuery<>();
//                        imageBmobQuery.addWhereEqualTo("work",list.get(i).getObjectId());
//                        //Toast.makeText(MainActivity.this, list.glet(i).getObjectId(),3*1000).show();
//                        //imageBmobQuery.include("url");
//                        imageBmobQuery.findObjects(MainActivity.this, new FindListener<image>() {
//                            @Override
//                            public void onSuccess(List<image> list) {
//                                if (list!=null && list.size()>0) {
//                                    work.setUrl(list.get(0).getUrl());
//                                    work.height = (i % 2) * 100 + 400;
//                                    data.add(work);
//                                }
//                            }
//                            @Override
//                            public void onError(int i, String s) {
//                                Toast.makeText(MainActivity.this, s,3*1000).show();
//                            }
//                        });
//                    }
                    for (i=list.size()-1;i >= 0;i--){
                        work = list.get(i);
                        work.height= (i % 2) * 100 + 400;
                        data.add(work);
                        //Toast.makeText(MainActivity.this,work.getTitle(),3*1000).show();
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, s,3*1000).show();
            }
        });
    }


    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.nav_view);
        // 添加监听
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //setContentView(R.layout.activity_main);
                        //setFragmentPosition(0);
                        break;
                    case R.id.navigation_dashboard:
                        //setFragmentPosition(1);
                        break;
                    case R.id.navigation_notifications:
                        //setContentView(R.layout.mine_msg);
                        //setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }


    /**
     * 回调接口
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_MAINACTIVITY:
                if (resultCode == RESULT_OK) {
                    user.setUsername(intent.getStringExtra(LoginActivity.USERNAME));
                }
        }
    }



}
