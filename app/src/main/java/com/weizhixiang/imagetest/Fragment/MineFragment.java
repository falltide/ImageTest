package com.weizhixiang.imagetest.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weizhixiang.imagetest.R;

public class MineFragment extends Fragment {

    public View OnCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.mine_msg, container, false);
        //TextView mTextView = view.findViewById(R.id.username);
        //mTextView.setText();
        return view;
    }
}
