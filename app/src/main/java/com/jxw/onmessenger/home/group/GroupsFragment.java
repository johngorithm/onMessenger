package com.jxw.onmessenger.home.group;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxw.onmessenger.R;
import com.jxw.onmessenger.models.Group;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment implements GroupView{
    private View groupFragmentView;
    private GroupPresenter groupListPresenter;
    private ProgressDialog progressDialog;
    private RecyclerView groupsRecyclerView;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        initializeProperties();
        groupListPresenter.fetchGroups();

        return groupFragmentView;
    }

    private void initializeProperties() {
        groupsRecyclerView = groupFragmentView.findViewById(R.id.groups_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        groupsRecyclerView.setLayoutManager(mLayoutManager);

        groupListPresenter = new GroupPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Groups ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void displayGroups(List<Group> groups) {
        progressDialog.dismiss();
        Log.d("========>", "displayGroups: "+groups.size());
        GroupAdapter groupListAdapter = new GroupAdapter(getContext(), groups);
        groupsRecyclerView.setAdapter(groupListAdapter);

    }

    @Override
    public void handNetworkError() {
        progressDialog.dismiss();

    }
}
