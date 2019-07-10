package com.kvsn.builds.cap1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowComments extends AppCompatActivity
{

     private DatabaseReference database;
     private DatabaseReference mref;
     private DatabaseReference msubref;
     private DatabaseReference msubref_seeker;
     public FirebaseAuth mAuth;

     ListView lv;
     ArrayList<String> commentslist = new ArrayList<String>();
     ArrayAdapter<String> arrayAdapter;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_show_comments);
	    lv = findViewById(R.id.lv);

	    setTitle("Comments");

	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commentslist);

	    mAuth = FirebaseAuth.getInstance();
	    database = FirebaseDatabase.getInstance().getReference();
	    mref = database.child("Review").child(mAuth.getCurrentUser().getUid());

	    mref.addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
			  for(DataSnapshot dataloop : dataSnapshot.getChildren())
			  {
			       String id = dataloop.getKey().trim();
				 Toast.makeText(ShowComments.this , "" +id, Toast.LENGTH_SHORT).show();
				 String comment = dataloop.getValue(String.class);
				 Toast.makeText(ShowComments.this , "" + comment , Toast.LENGTH_SHORT).show();
				 commentslist.add(comment);
			  }
			  adapterload();
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
     }

     public void adapterload()
     {
	    lv.setAdapter(arrayAdapter);
     }
}
