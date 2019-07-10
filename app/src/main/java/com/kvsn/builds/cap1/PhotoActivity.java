package com.kvsn.builds.cap1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoActivity extends AppCompatActivity
{

     CircleImageView iv;
     StorageReference UserProfileImagesReference;
     FirebaseUser currentUserId;
     FirebaseAuth mauth;
     FirebaseUser user;
     DatabaseReference mDatabase, msubref;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {


          super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_photo);



	    setTitle("Profile Image");

	    new checkInternetConnection(this).checkConnection();
	    iv = findViewById(R.id.iv);
	    currentUserId = FirebaseAuth.getInstance().getCurrentUser();
	    UserProfileImagesReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
	    mDatabase = FirebaseDatabase.getInstance().getReference();
	    mauth = FirebaseAuth.getInstance();
	    user= mauth.getCurrentUser();
	    msubref = mDatabase.child("Users").child(user.getUid());
	    msubref.addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
			  if(dataSnapshot.hasChild("urlToImage"))
			  {
				 Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(iv);
			  }
			  else
			  {
			       iv.setImageResource(R.drawable.profile);
			  }
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });

     }
}