package com.sked.gdg.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sked.gdg.R;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.sked.gdg.fragment.HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "arg_section_number";
    private int section;
    private FragmentTabHost mTabHost;


    public static HomeFragment newInstance(int section) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabhot_fragment, container, false);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        Bundle fBundle = new Bundle();
        fBundle.putInt("key_post_type", section);
        fBundle.putInt("key_feeds", 0);

        mTabHost.addTab(mTabHost.newTabSpec("Feeds")
                        .setIndicator("Feeds"),
                FeedFragment.class, fBundle
        );
        Bundle eBundle = new Bundle();
        eBundle.putInt("key_feeds", 1);
        eBundle.putInt("key_post_type", section);
        mTabHost.addTab(mTabHost.newTabSpec("Events")
                        .setIndicator("Events"),
                FeedFragment.class, eBundle
        );

        return rootView;
    }


}
