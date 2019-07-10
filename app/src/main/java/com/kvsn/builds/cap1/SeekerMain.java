package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class SeekerMain extends AppCompatActivity implements DuoMenuView.OnMenuClickListener
{
     private MenuAdapter mMenuAdapter;
     private ViewHolder mViewHolder;
     CircleImageView header , image;
     FirebaseAuth mAuth;
     ProgressDialog pd;
     DatabaseReference mDatabase, msubref , workref;
     TextView name , aadhaar , mobile , comments , exper , newwork , category , headername , headermail;

     private ArrayList<String> mTitles = new ArrayList<>();

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_seeker_main);
	  setTitle("Home");
	  name = findViewById(R.id.Seeker_name);
	  aadhaar = findViewById(R.id.Seeker_adhar_no);
	  mobile = findViewById(R.id.Seeker_mob_no);
	  comments = findViewById(R.id.textviewcomments);
	  exper = findViewById(R.id.exp);
	  newwork = findViewById(R.id.new_work);
	  category = findViewById(R.id.Seeker_type);
	  headername = findViewById(R.id.header_name);
	  headermail = findViewById(R.id.header_mail);

	  pd = new ProgressDialog(this);
	  mAuth = FirebaseAuth.getInstance();
	  mDatabase = FirebaseDatabase.getInstance().getReference();
	  msubref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
	  mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
	  header = findViewById(R.id.image_header);
	  image = findViewById(R.id.Img_profile_Seeker);
	  workref = mDatabase.child("Extra detail of seeker").child("Experience of all").child(mAuth.getCurrentUser().getUid());

	  // Initialize the views
	  mViewHolder = new ViewHolder();

	  // Handle toolbar actions
	  handleToolbar();

	  // Handle menu actions
	  handleMenu();

	  // Handle drawer actions
	  handleDrawer();

	  mMenuAdapter.setViewSelected(0 , true);
	  setTitle(mTitles.get(0));

	  retrieve();
     }
     public void showComments(View view)
     {
          startActivity(new Intent(SeekerMain.this , ShowComments.class));
     }

     public void retrieve()
     {
	    msubref.addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
			  name.setText(dataSnapshot.child("Name").getValue(String.class));
			  mobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
			  aadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
			  category.setText(dataSnapshot.child("Profession").getValue(String.class));
			  Long experience = dataSnapshot.child("Experience").getValue(Long.class);
			  String xp = String.valueOf(experience);
			  exper.setText(xp);
			  headermail.setText(dataSnapshot.child("Email").getValue(String.class));
			  headername.setText(dataSnapshot.child("Name").getValue(String.class));
			  //newwork.setText(dataSnapshot.child("").getValue(String.class));
			  if(dataSnapshot.hasChild("urlToImage"))
			  {
				 Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(header);
				 Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(image);
			  }
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
	    workref.addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
		        if(dataSnapshot.hasChild("New work"))
			  {
				 newwork.setText(dataSnapshot.child("New work").getValue(Long.class).toString());
			  }
			  else
			  {
			       newwork.setText("0");
			  }
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
     }

     private void handleToolbar()
     {
	  setSupportActionBar(mViewHolder.mToolbar);
     }

     private void handleDrawer()
     {
	  DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this , mViewHolder.mDuoDrawerLayout , mViewHolder.mToolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);

	  mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
	  duoDrawerToggle.syncState();

     }

     private void handleMenu()
     {
	  mMenuAdapter = new MenuAdapter(mTitles);

	  mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
	  mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
     }

     @Override
     public void onFooterClicked()
     {
	  pd.setTitle("Logging out");
	  pd.show();
	  mAuth.signOut();
	  startActivity(new Intent(SeekerMain.this , MainActivity.class));
	  finish();
     }

     @Override
     public void onHeaderClicked()
     {
	  //Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
	  Intent i = new Intent(this , SeekerProfile.class);
	  ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
	  startActivity(i , actop.toBundle());
	  overridePendingTransition(R.anim.fadein , R.anim.fadeout);
     }

     @Override
     public void onOptionClicked(int position , Object objectClicked)
     {
	  // Set the toolbar title
	  //setTitle(mTitles.get(position));

	  // Set the right options selected
	  //mMenuAdapter.setViewSelected(position , true);

	  // Navigate to the right fragment
	  switch(position)
	  {
		 case 2:
		      startActivity(new Intent(SeekerMain.this , AboutUs.class));
		      break;
	       case 1:
		    //Toast.makeText(this , "WOrking " , Toast.LENGTH_SHORT).show();
		    Intent i = new Intent(this , SeekerProfile.class);
		    ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
		    startActivity(i , actop.toBundle());
		    overridePendingTransition(R.anim.fadein , R.anim.fadeout);

	       default:
		    break;
	  }

	  // Close the drawer
	  mViewHolder.mDuoDrawerLayout.closeDrawer();
     }

     private class ViewHolder
     {
	  private DuoDrawerLayout mDuoDrawerLayout;
	  private DuoMenuView mDuoMenuView;
	  private Toolbar mToolbar;

	  ViewHolder()
	  {
	       mDuoDrawerLayout = findViewById(R.id.drawer);
	       mDuoMenuView = (DuoMenuView)mDuoDrawerLayout.getMenuView();
	       mToolbar = findViewById(R.id.toolbar);
	  }
     }

     public void dothis(View v)
     {
	  switch(v.getId())
	  {
	       case R.id.electrician:
		    Toast.makeText(getApplicationContext() , "Electrician" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.carpainter:
		    Toast.makeText(getApplicationContext() , "Carpainter" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.plumber:
		    Toast.makeText(getApplicationContext() , "Plumber" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.bricklayer:
		    Toast.makeText(getApplicationContext() , "BrickLayer" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.painter:
		    Toast.makeText(getApplicationContext() , "Painter" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.labour:
		    Toast.makeText(getApplicationContext() , "Labour" , Toast.LENGTH_SHORT).show();
		    break;
	       default:
		    break;
	  }
     }

     @Override
     public void onBackPressed()
     {

     }
}
