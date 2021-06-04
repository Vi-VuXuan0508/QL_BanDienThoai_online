package com.example.project1.nhan.DAO;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageDAO {

    public static void deleteImage(String uri)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref  = storage.getReferenceFromUrl(uri);
        ref.delete();
    }

}
