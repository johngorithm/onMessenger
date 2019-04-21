package com.jxw.onmessenger.home.group;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jxw.onmessenger.models.Group;
import com.jxw.onmessenger.services.FirebaseService;

import java.util.ArrayList;
import java.util.List;

class GroupPresenter {
    private GroupView groupView;
    private DatabaseReference fbRootRef;


    GroupPresenter(GroupView view) {
        this.groupView = view;
        this.fbRootRef = FirebaseService.getFbRootRef();
    }

    void fetchGroups() {
        fbRootRef.child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Group> groups = new ArrayList<>();

                for (DataSnapshot dataShot : dataSnapshot.getChildren()) {
                    Group group = dataShot.getValue(Group.class);
                    groups.add(group);
                }
                groupView.displayGroups(groups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                groupView.handNetworkError();
            }
        });
    }
}
