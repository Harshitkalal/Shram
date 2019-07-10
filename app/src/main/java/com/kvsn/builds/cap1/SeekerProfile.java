package com.kvsn.builds.cap1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SeekerProfile extends AppCompatActivity
{
     FirebaseAuth mAuth;
     DatabaseReference mDatabase, msubref;
     TextView name, mail, aadhaar, mobile, state, city, address, profession;
     CircleImageView profile;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_seeker_profile);

	    setTitle("Profile");
	    new checkInternetConnection(this).checkConnection();
	    name = (TextView)findViewById(R.id.name_seeker);
	    mail = (TextView)findViewById(R.id.mail_seeker);
	    address = (TextView)findViewById(R.id.seeker_address);
	    aadhaar = (TextView)findViewById(R.id.seeker_aadhaar);
	    city = (TextView)findViewById(R.id.seeker_city);
	    mobile = (TextView)findViewById(R.id.seeker_mobile);
	    state = (TextView)findViewById(R.id.seeker_state);
	    profession = (TextView)findViewById(R.id.seeker_profile_profession);
	    profile = (CircleImageView)findViewById(R.id.profile_image_seeker);

	    mDatabase = FirebaseDatabase.getInstance().getReference();
	    mAuth = FirebaseAuth.getInstance();

	    //Toast.makeText(this , "Test In Profile" , Toast.LENGTH_SHORT).show();
	    retrieve();

	    profile.setOnClickListener(new View.OnClickListener()
	    {
		   @Override
		   public void onClick(View v)
		   {
			  ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(SeekerProfile.this , profile , ViewCompat.getTransitionName(profile));
			  startActivity(new Intent(SeekerProfile.this , PhotoActivity.class));
		   }
	    });
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
			  profession.setText(dataSnapshot.child("Profession").getValue(String.class));
			  if(dataSnapshot.hasChild("urlToImage"))
			  {
				 Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(profile);
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
	    startActivity(new Intent(SeekerProfile.this , SeekerMain.class));
	    //finish();
     }

     public void edit_seeker(View v)
     {
	    startActivity(new Intent(SeekerProfile.this , EditSeeker.class));
     }
}
