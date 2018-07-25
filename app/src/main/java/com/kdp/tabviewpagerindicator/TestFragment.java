package com.kdp.tabviewpagerindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kangdongpu on 2017/2/17.
 */
public class TestFragment extends Fragment {

    private static TestFragment instance;

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);
        textView = (TextView) view.findViewById(R.id.text);
        textView.setText(getArguments().getString("title","示例"));
        return view;
    }


    public static TestFragment newInstance(String title) {
        instance = new TestFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        instance.setArguments(b);

        return instance;
    }
}
