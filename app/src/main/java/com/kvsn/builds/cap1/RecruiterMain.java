package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class RecruiterMain extends AppCompatActivity implements DuoMenuView.OnMenuClickListener
{
     private MenuAdapter mMenuAdapter;
     private ViewHolder mViewHolder;
     CircleImageView header;
     FirebaseAuth mAuth;
     DatabaseReference mDatabase , mref , msubref;
     ProgressDialog pd;

     TextView name , mail;

     private ArrayList<String> mTitles = new ArrayList<>();

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_recruiter_main);

	    new checkInternetConnection(this).checkConnection();
	  name = findViewById(R.id.header_name);
	  mail = findViewById(R.id.header_mail);

	  pd = new ProgressDialog(this);
	  mAuth = FirebaseAuth.getInstance();
	  mDatabase = FirebaseDatabase.getInstance().getReference();
	  mref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
	  mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
	  header = findViewById(R.id.image_header);

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

	  mref.addValueEventListener(new ValueEventListener()
	  {
	       @Override
	       public void onDataChange(@NonNull DataSnapshot dataSnapshot)
	       {
	            if(dataSnapshot.exists())
		    {
			 name.setText(dataSnapshot.child("Name").getValue(String.class));
			 mail.setText(dataSnapshot.child("Email").getValue(String.class));
			 if(dataSnapshot.hasChild("urlToImage"))
			 {
				Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(header);
			 }
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
	  startActivity(new Intent(RecruiterMain.this , MainActivity.class));
	  finish();
     }

     @Override
     public void onHeaderClicked()
     {
	  Intent i = new Intent(this , RecruiterProfile.class);
	  ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
	  startActivity(i , actop.toBundle());
	  overridePendingTransition(R.anim.fadein , R.anim.fadeout);
     }

     @Override
     public void onOptionClicked(int position , Object objectClicked)
     {
	  switch(position)
	  {
		 case 2:
		      startActivity(new Intent(RecruiterMain.this , AboutUs.class));
		      break;
	       case 1:
		    Intent i = new Intent(this , RecruiterProfile.class);
		    ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
		    startActivity(i , actop.toBundle());
		    overridePendingTransition(R.anim.fadein , R.anim.fadeout);
		    break;
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
	  SharedPreferences sharedPreferences = getSharedPreferences("Categories" , Context.MODE_PRIVATE);
	  SharedPreferences.Editor editor = sharedPreferences.edit();
	  String categorie;
	  String img_var;
	  Intent intent;
	  switch(v.getId())
	  {

	       case R.id.electrician:
		    categorie = "Electrician";
		    editor.putString("categorie" , categorie);
		    editor.commit();
		    //Toast.makeText(RecruiterMain.this , "Electrician" , Toast.LENGTH_SHORT).show();
		    intent = new Intent(RecruiterMain.this , GetLocation.class);
		    startActivity(intent);
		    break;

	       case R.id.bricklayer:
		    Toast.makeText(RecruiterMain.this , "Mason" , Toast.LENGTH_SHORT).show();
		    categorie="Mason";
		    editor.putString("categorie",categorie);
		    editor.commit();
		    intent=new Intent(RecruiterMain.this,GetLocation.class);
		    startActivity(intent);
		    break;
	       case R.id.carpainter:
		    Toast.makeText(RecruiterMain.this , "Carpainter" , Toast.LENGTH_SHORT).show();
		    categorie = "Carpenter";
		    editor.putString("categorie" , categorie);
		    editor.commit();
		    intent = new Intent(this , GetLocation.class);
		    startActivity(intent);
		    break;
	       case R.id.plumber:
		    Toast.makeText(RecruiterMain.this , "Plumber" , Toast.LENGTH_SHORT).show();
		    categorie="Plumber";
		    editor.putString("categorie",categorie);
		    editor.commit();
		    intent=new Intent(RecruiterMain.this,GetLocation.class);
		    startActivity(intent);
		    break;
	       case R.id.painter:
		    Toast.makeText(RecruiterMain.this , "Painter" , Toast.LENGTH_SHORT).show();
		    categorie="Painter";
		    editor.putString("categorie",categorie);
		    editor.commit();
		    intent=new Intent(RecruiterMain.this,GetLocation.class);
		    startActivity(intent);
		    break;
	       case R.id.labour:
		    Toast.makeText(RecruiterMain.this , "Labour" , Toast.LENGTH_SHORT).show();
		    categorie="Labour";
		    editor.putString("categorie",categorie);
		    editor.commit();
		    intent=new Intent(RecruiterMain.this,GetLocation.class);
		    startActivity(intent);
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
