package org.itsnat.itsnatdroidtest.testact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.local.TestSetupLocalLayout1;
import org.itsnat.itsnatdroidtest.testact.local.TestSetupLocalLayout2;
import org.itsnat.itsnatdroidtest.testact.local.TestSetupLocalLayoutDrawables;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemoteControl;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemoteCore;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemoteIncludeLayout;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemotePage;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemotePageNoItsNat;

/**
 * Created by jmarranz on 12/08/14.
 */
public class TestActivityTabFragment extends Fragment
{
    protected View rootView;
    protected int sectionNumber;
    public boolean changed = false;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TestActivityTabFragment newInstance(int sectionNumber) {
        TestActivityTabFragment fragment = new TestActivityTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public int getSectionNumber()
    {
        return sectionNumber;
    }

    public void setRootView(View rootView)
    {
        this.rootView = rootView;
    }

    public TestActivity getTestActivity()
    {
        return (TestActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) return rootView;

        Bundle bundle = getArguments();
        this.sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

        this.rootView = inflater.inflate(R.layout.fragment_test_index, container, false);

        View testLocal1 = rootView.findViewById(R.id.testLocalLayout1);
        testLocal1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TestSetupLocalLayout1(TestActivityTabFragment.this).test();
            }
        });

        View testLocal2 = rootView.findViewById(R.id.testLocalLayout2);
        testLocal2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TestSetupLocalLayout2(TestActivityTabFragment.this).test();
            }
        });

        View testLocal3 = rootView.findViewById(R.id.testLocalDrawables);
        testLocal3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TestSetupLocalLayoutDrawables(TestActivityTabFragment.this).test();
            }
        });

        final TestActivity act = getTestActivity();

        View testRemoteCore = rootView.findViewById(R.id.testRemoteLayoutCore);
        testRemoteCore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestCore();
                TestRemoteCore test = new TestRemoteCore(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        View testRemoteIncludeLayout = rootView.findViewById(R.id.testRemoteIncludeLayout);
        testRemoteIncludeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestIncludeLayout();
                TestRemoteIncludeLayout test = new TestRemoteIncludeLayout(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });


        View testRemoteResources = rootView.findViewById(R.id.testRemoteLayoutDrawables);
        testRemoteResources.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestRemoteResources();
                TestRemotePage test = new TestRemotePage(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        View testRemoteControl = rootView.findViewById(R.id.testRemoteControl);
        testRemoteControl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestRemCtrl();
                TestRemoteControl test = new TestRemoteControl(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        View testRemoteStatelessCore = rootView.findViewById(R.id.testRemoteStatelessCore);
        testRemoteStatelessCore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestStatelessCore();
                TestRemotePage test = new TestRemotePage(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        View testRemoteComponents = rootView.findViewById(R.id.testRemoteComponents);
        testRemoteComponents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestComponents();
                TestRemotePage test = new TestRemotePage(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        View testRemoteNoItsNat = rootView.findViewById(R.id.testRemoteNoItsNat);
        testRemoteNoItsNat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestRemoteNoItsNat();
                TestRemotePageNoItsNat test = new TestRemotePageNoItsNat(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });

        return rootView;
    }

    public void gotoLayoutIndex()
    {
        this.rootView = null;
        updateFragmentLayout();
    }

    public void updateFragmentLayout()
    {
        this.changed = true;
        TestActivityPagerAdapter pagerAdapter = getTestActivity().getTestActivityPagerAdapter();
        pagerAdapter.notifyDataSetChanged(); // Provoca la llamada FragmentPagerAdapter.getItemPosition(Object) para cada fragmento

        //act.getViewPager().invalidate();
        //act.getViewPager().destroyDrawingCache();
        //act.getViewPager().forceLayout();
        //act.getViewPager().requestLayout();
    }
}
