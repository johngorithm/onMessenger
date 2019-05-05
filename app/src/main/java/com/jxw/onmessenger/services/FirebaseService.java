package com.jxw.onmessenger.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseService {

    private FirebaseService() {
        // Utility class
    }

    public static DatabaseReference getFbRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getFbAuthService() {
        return FirebaseAuth.getInstance();
    }

    public static StorageReference getFbStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }
}
