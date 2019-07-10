package com.kvsn.builds.cap1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
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

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AvailableWorkers extends AppCompatActivity
{
     public static final String DEFAULT = "0";
     TextView professsion;
     ArrayList<Person> al;
     MyAdaptor md;
     ImageView work_image;
     RecyclerView rv;
     Person p;
     int integer_img_var;
     String City;
     private DatabaseReference database;
     private DatabaseReference mref;
     private DatabaseReference msubref;
     private DatabaseReference msubref_seeker;
     public FirebaseAuth mAuth;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);

	    new checkInternetConnection(this).checkConnection();
	    setTitle("Available Workers");
	    setContentView(R.layout.activity_available_workers);
	    professsion = (TextView)findViewById(R.id.ava_profession);
	    SharedPreferences sharedPreferences = getSharedPreferences("Categories" , Context.MODE_PRIVATE);
	    //category of the type of the worker
	    String categorie = sharedPreferences.getString("categorie" , DEFAULT);
	    String img_var = sharedPreferences.getString("imgvar" , DEFAULT);
	    City = sharedPreferences.getString("City" , DEFAULT).trim();
	    Toast.makeText(this , "City : " + City , Toast.LENGTH_SHORT).show();
	    Toast.makeText(getApplicationContext() , "String" + img_var , Toast.LENGTH_SHORT).show();

	    integer_img_var = Integer.parseInt(img_var);
	    //Toast.makeText(getApplicationContext() , "int" + integer_img_var , Toast.LENGTH_SHORT).show();
	    professsion.setText(categorie);

	    rv = (RecyclerView)findViewById(R.id.rec);
	    work_image = (ImageView)findViewById(R.id.work_image);
	    RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
	    rv.setLayoutManager(rlm);

	    mAuth = FirebaseAuth.getInstance();
	    database = FirebaseDatabase.getInstance().getReference();
	    mref = database.child("Users");
	    msubref_seeker = database.child("Seeker");
	    msubref = msubref_seeker.child(categorie);


	    al = new ArrayList<>();
	    p = new Person();


	    FirebaseDatabase.getInstance().getReference().child("Seeker").child(categorie).addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  for(DataSnapshot dataloop : dataSnapshot.getChildren())
			  {
				 //Toast.makeText(AvailableWorkers.this , "" + dataloop.child("City").getValue(String.class), Toast.LENGTH_SHORT).show();


				 String getcity = dataloop.child("City").getValue().toString().trim();
			//
				   if(getcity!=null)
				 Log.e("Get Data",getcity );
				   else
				        Log.e("OKay",City);
				 //getcity="Kapurthala";
				 //Toast.makeText(AvailableWorkers.this , getcity+"" , Toast.LENGTH_SHORT).show();
				 //String a = "a" , b = "a";


				 if(getcity.equalsIgnoreCase(City.trim()))
				 {
					Log.d("case:",City+" "+getcity);
					//Toast.makeText(AvailableWorkers.this , "TEST" , Toast.LENGTH_LONG).show();
					Person p = new Person();
					//Toast.makeText(getApplicationContext() , "Value:" + child.child("Name").getValue().toString() , Toast.LENGTH_SHORT).show();

					p.setName(dataloop.child("Name").getValue(String.class));
					p.setId(dataloop.child("Id").getValue(String.class));
					if(dataloop.hasChild("Rating"))
					{
					     String rating = dataloop.child("Rating").getValue().toString();
					     //Toast.makeText(AvailableWorkers.this , "" + rating, Toast.LENGTH_SHORT).show();
					     p.setRating(rating);
					}
					else
					{
					     p.setRating("No Work Record");
					}

					//p.setRating("4");

					if(dataloop.hasChild("urlToImage"))
					{
					     //Toast.makeText(AvailableWorkers.this , "Image exists" , Toast.LENGTH_SHORT).show();
					     //Picasso.get().load(dataSnapshot.child("urlToImage").getValue().toString());
                                 p.setUrl(dataloop.child("urlToImage").getValue(String.class));
					   //  p.setImage(R.drawable.profile);

					}
					else
					{
					     p.setImage(R.drawable.profile);
					}


					al.add(p);
				 }

			  }
			  md = new MyAdaptor(AvailableWorkers.this , al);
			  rv.setAdapter(md);
			  //Toast.makeText(AvailableWorkers.this , "" + City , Toast.LENGTH_SHORT).show();
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
	    /*msubref.addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  long count = dataSnapshot.getChildrenCount();
			  Toast.makeText(getApplicationContext() , "Children:" + count , Toast.LENGTH_SHORT).show();
			  for(DataSnapshot child : dataSnapshot.getChildren())
			  {
				 if((child.child("City").getValue().toString()).equals(City))
				 {
				      Toast.makeText(AvailableWorkers.this ,"TEST",Toast.LENGTH_LONG ).show();
					Person p = new Person();
					Toast.makeText(getApplicationContext() , "Value:" + child.child("Name").getValue().toString() , Toast.LENGTH_SHORT).show();

					p.setName(child.child("Name").getValue(String.class));
					p.setId(child.child("Id").getValue(String.class));
					//p.setRating(child.child("Rating").getValue().toString());
					p.setRating("4");

					if(dataSnapshot.hasChild("urlToImage"))
					{
					     Toast.makeText(AvailableWorkers.this , "Image exists" , Toast.LENGTH_SHORT).show();
					     Picasso.get().load(dataSnapshot.child("urlToImage").getValue().toString()).transform(new CropCircleTransformation()).into(work_image);
					}
					else
					{
					     p.setImage(R.drawable.profile);
					}


					al.add(p);
				 }
			  }
			  md = new MyAdaptor(AvailableWorkers.this , al);
			  rv.setAdapter(md);
		   }

		   @Override
		   public void onCancelled(DatabaseError databaseError)
		   {
			  Toast.makeText(getApplicationContext() , "Error in loading" , Toast.LENGTH_SHORT).show();
		   }
	    });*/
	    //Toast.makeText(getApplicationContext() , "Adapter" , Toast.LENGTH_SHORT).show();




	    /*
        for(int i=0;i<5;i++)
        {
            Person p = new Person();
            p.setName("Rohit");
            switch (integer_img_var)
            {
                case 1:
                    p.setImage(R.drawable.electrician);
                    break;
                case 2:
                    p.setImage(R.drawable.carpainter);
                    break;
                case 3:
                    p.setImage(R.drawable.plumber);
                    break;
                case 4:
                    p.setImage(R.drawable.bricklayer);
                    break;
                case 5:
                    p.setImage(R.drawable.painter);
                    break;
                case 6:
                    p.setImage(R.drawable.labour);
                    break;
                    default:
                        p.setImage(R.drawable.electrician);
            }
            p.setRating("4");
            al.add(p);
        }

	    md = new MyAdaptor(AvailableWorkers.this , al);
	    rv.setAdapter(md);

     }*/
     }

     @Override
     public void onBackPressed()
     {
	    startActivity(new Intent(AvailableWorkers.this , RecruiterMain.class));
     }
}