package com.kvsn.builds.cap1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RecruiterProfile extends AppCompatActivity
{
     FirebaseAuth mAuth;
     DatabaseReference mDatabase, msubref;
     CircleImageView image;
     TextView name, mail, aadhaar, mobile, state, city, address;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_recruiter_profile);

	    setTitle("Profile");
	    new checkInternetConnection(this).checkConnection();

	    name = findViewById(R.id.name_recruiter);
	    mail = findViewById(R.id.mail_recruiter);
	    address = findViewById(R.id.recruiter_address);
	    aadhaar = findViewById(R.id.recruiter_aadhaar);
	    city = findViewById(R.id.recruiter_city);
	    mobile = findViewById(R.id.recruiter_mobile);
	    state = findViewById(R.id.recruiter_state);
	    image = findViewById(R.id.profile_image);

	    mDatabase = FirebaseDatabase.getInstance().getReference();
	    mAuth = FirebaseAuth.getInstance();

	    //Toast.makeText(this , "Test In Profile" , Toast.LENGTH_SHORT).show();
	    retrieve();
     }

     public void retrieve()
     {
	    FirebaseUser user = mAuth.getCurrentUser();
	    msubref = mDatabase.child("Users").child(user.getUid());
	    msubref.addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
			  name.setText(dataSnapshot.child("Name").getValue(String.class));
			  mail.setText(dataSnapshot.child("Email").getValue(String.class));
			  address.setText(dataSnapshot.child("Address").getValue(String.class));
			  aadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
			  city.setText(dataSnapshot.child("City").getValue(String.class));
			  mobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
			  state.setText(dataSnapshot.child("State").getValue(String.class));
			  if(dataSnapshot.hasChild("urlToImage"))
			  {
				 Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(image);
			  }
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
     }

     @Override
     public void onBackPressed()
     {
	    startActivity(new Intent(RecruiterProfile.this , RecruiterMain.class));
     }

     public void image(View v)
     {
	    startActivity(new Intent(RecruiterProfile.this , PhotoActivity.class));
     }

     public void edit_recruiter(View v)
     {
	    startActivity(new Intent(RecruiterProfile.this , EditRecruiter.class));
     }


}
