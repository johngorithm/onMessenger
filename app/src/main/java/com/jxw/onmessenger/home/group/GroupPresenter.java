package com.jxw.onmessenger.home.group;

import android.support.annotation.NonNull;

import com.google.android.gms.common.FirstPartyScopes;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth fbAuth;


    GroupPresenter(GroupView view) {
        this.groupView = view;
        this.fbRootRef = FirebaseService.getFbRootRef();
        this.fbAuth = FirebaseService.getFbAuthService();
    }

    void fetchGroups() {
        if (fbAuth.getCurrentUser() != null) {
            String currentUserId = fbAuth.getCurrentUser().getUid();

            fbRootRef.child("Groups").orderByChild("members/"+currentUserId).equalTo("true").addValueEventListener(new ValueEventListener() {
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
        } else {
            // send user to login
        }
    }
}
