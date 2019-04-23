package com.jxw.onmessenger.home.group;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        return groupFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupListPresenter.fetchGroups();
    }

    private void initializeProperties() {
        groupsRecyclerView = groupFragmentView.findViewById(R.id.groups_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        groupsRecyclerView.setLayoutManager(mLayoutManager);

        groupListPresenter = new GroupPresenter(this);
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public void displayGroups(List<Group> groups) {
        progressDialog.dismiss();
        GroupAdapter groupListAdapter = new GroupAdapter(getContext(), groups);
        groupsRecyclerView.setAdapter(groupListAdapter);

    }

    @Override
    public void handNetworkError() {
        progressDialog.dismiss();

    }
}
