package com.kvsn.builds.cap1;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Show_Available_worker_Profile extends AppCompatActivity
{

     String TAG = getCallingPackage();
     TextView tv_name1, tv_profession1, tv_phone1, tv_alter_phone1, tv_rating_num1, tv_experience_num1, tv_email_id;
     LinearLayout l1;
     Button b1 , b2;

     Float prevrating;
     Long oldexp;
     ImageView img_profile1;
     private DatabaseReference database;
     private DatabaseReference mref;
     private DatabaseReference msubref;
     private DatabaseReference detailref;
     private DatabaseReference expref;
     private DatabaseReference refrev, dupref;
     private FirebaseAuth mAuth;
     private String UrlToImg, type;
     private String key;
     private String seeker_id;
     private RequestQueue rRequestQueue;
     private String mob;


     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_show__available_worker__profile);
	    SharedPreferences sharedPreferences = getSharedPreferences("Getkey" , Context.MODE_PRIVATE);
	    key = sharedPreferences.getString("key" , "");
	    Toast.makeText(getApplicationContext() , "key in profile" + key , Toast.LENGTH_SHORT).show();
	    tv_name1 = (TextView)findViewById(R.id.tv_name);

	    setTitle("Shram");

	    new checkInternetConnection(this).checkConnection();
	    b1 = (Button)findViewById(R.id.confirm);
	    b2 = findViewById(R.id.finish);
	    b1.setClickable(false);
	    l1 = (LinearLayout)findViewById(R.id.confirmandfinish);
	    tv_profession1 = (TextView)findViewById(R.id.tv_profession);
	    tv_phone1 = (TextView)findViewById(R.id.tv_phone);
	    tv_alter_phone1 = (TextView)findViewById(R.id.tv_alter_phone);
	    tv_rating_num1 = (TextView)findViewById(R.id.tv_rating_num);
	    tv_experience_num1 = (TextView)findViewById(R.id.tv_experience_num);
	    img_profile1 = (ImageView)findViewById(R.id.img_profile);
	    tv_email_id = (TextView)findViewById(R.id.tv_email_id);
	    //for volley
	    rRequestQueue = Volley.newRequestQueue(this);
	    database = FirebaseDatabase.getInstance().getReference();
	    detailref = database.child("Extra detail of seeker").child("New work of all");
	    expref = database.child("Extra detail of seeker").child("Experience of all");
	    refrev = database.child("Review");
	    mAuth = FirebaseAuth.getInstance();
	    displayProfile();
     }

     public void displayProfile()
     {

	    //  Toast.makeText(getApplicationContext(),"id"+FirebaseAuth.getInstance().getCurrentUsers().getUid().toString(),Toast.LENGTH_SHORT).show();
	    database.child("Users").child(key).addValueEventListener(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  if(dataSnapshot.exists())
			  {
				 tv_name1.setText(dataSnapshot.child("Name").getValue().toString());
				 tv_profession1.setText(dataSnapshot.child("Profession").getValue().toString());
				 tv_phone1.setText(dataSnapshot.child("Mobile").getValue().toString());
				 tv_alter_phone1.setText(dataSnapshot.child("Alternate Mobile").getValue().toString());
				 tv_email_id.setText(dataSnapshot.child("Email").getValue().toString());
				 seeker_id = dataSnapshot.child("Id").getValue().toString();
				 mob = dataSnapshot.child("Mobile").getValue().toString();
				 if(dataSnapshot.hasChild("Experience"))
				 {
					tv_experience_num1.setText(dataSnapshot.child("Experience").getValue().toString());
				 }
				 else
				 {
					tv_experience_num1.setText("No Work Record");
				 }
				 if(dataSnapshot.hasChild("urlToImage"))
				 {
					UrlToImg = dataSnapshot.child("urlToImage").getValue().toString();
					if(! UrlToImg.isEmpty())
					{

					     Picasso.get().load(UrlToImg).transform(new CropCircleTransformation()).into(img_profile1);

					}
				 }

				 if(dataSnapshot.hasChild("Rating"))
				 {
					tv_rating_num1.setText(dataSnapshot.child("Rating").getValue(Float.class).toString());
					prevrating = dataSnapshot.child("Rating").getValue(Float.class);
				 }
				 else
				 {
					tv_rating_num1.setText("No Work Record");
					prevrating = 0.0f;
				 }
				 if(dataSnapshot.hasChild("Experience"))
				 {
					oldexp = dataSnapshot.child("Experience").getValue(Long.class);
				 }
				 else
				 {
					oldexp = 0l;
				 }


			  }

			  else
			  {
				 Toast.makeText(getApplicationContext() , "Data does not exit" , Toast.LENGTH_SHORT).show();
			  }
			  type = dataSnapshot.child("Profession").getValue(String.class);
			  dupref = database.child("Seeker").child(type).child(key);
		   }

		   @Override
		   public void onCancelled(DatabaseError databaseError)
		   {
			  Toast.makeText(getApplicationContext() , "Data loading failed" , Toast.LENGTH_SHORT).show();
		   }
	    });


     }

     public void showconfirm(View v)
     {

	    String number = tv_phone1.getText().toString();
	    Intent i = new Intent(Intent.ACTION_DIAL);
	    i.setData(Uri.parse("tel:+91" + number));
	    startActivity(i);
	    l1.setVisibility(View.VISIBLE);
	    //b1.setBackgroundColor(Color.rgb(100 , 65 , 15));
	    //b2.setBackgroundColor(Color.rgb(100 , 100 , 55));
	    b2.setClickable(false);
	    b1.setClickable(true);


	 /* detailref.addValueEventListener(new ValueEventListener()
	  {
	       @Override
	       public void onDataChange(DataSnapshot dataSnapshot)
	       {

		    if (dataSnapshot.hasChild(seeker_id)){


		         updateNewWorkCounter(true);


		    }
		    else
		    {
			// dataSnapshot.child(seeker_id).child("New work");
			 updateNewWorkCounter(true);
		    }
	       }

	       @Override
	       public void onCancelled(DatabaseError databaseError)
	       {
		    Toast.makeText(getApplicationContext() , "Data loading failed" , Toast.LENGTH_SHORT).show();
	       }
	  });

	 */
	    expref.child(seeker_id).addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  if(dataSnapshot.hasChild("New work") && dataSnapshot.child("New work").getValue() != null && dataSnapshot.child("New work").getValue(Integer.class) > 0)
			  {
				 int val = dataSnapshot.child("New work").getValue(Integer.class);
				 val++;
				 expref.child(seeker_id).child("New work").setValue(val);

			  }
			  else
			  {
				 expref.child(seeker_id).child("New work").setValue(1);
			  }


		   }

		   @Override
		   public void onCancelled(DatabaseError databaseError)
		   {

		   }
	    });


     }
   /*  private void updateNewWorkCounter(final boolean increment){

          DatabaseReference newwork=detailref.child(key).child("New work");
          newwork.runTransaction(new Transaction.Handler()
	  {
	       @Override
	       public Transaction.Result doTransaction(MutableData mutableData)
	       {

                    if(mutableData.getValue()!=null){
			 int value = mutableData.getValue(Integer.class);
			 if(increment)
			      value++;
			 else
			      value--;
			 mutableData.setValue(value);
		    }


	            return Transaction.success(mutableData);
	       }

	       @Override
	       public void onComplete(DatabaseError databaseError , boolean b , DataSnapshot dataSnapshot)
	       {

		    Log.d(TAG, "likeTransaction:onComplete:" + databaseError);

	       }
	  });
     }*/

     public void showdone(View v)
     {
          b2.setClickable(true);
          //b2.setBackgroundColor(Color.rgb(100 , 65 , 15));


	    expref.child(seeker_id).addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  if(dataSnapshot.hasChild("New work") && dataSnapshot.child("New work").getValue() != null && dataSnapshot.child("New work").getValue(Integer.class) > 0)
			  {
				 Long val = dataSnapshot.child("New work").getValue(Long.class);
				 val--;
				 expref.child(seeker_id).child("New work").setValue(val);

			  }
			  else
			  {
				 expref.child(seeker_id).child("New work").setValue(0);
			  }


		   }

		   @Override
		   public void onCancelled(DatabaseError databaseError)
		   {

		   }
	    });


	    sendmsg();
	    b1.setClickable(false);
	    //b1.setBackgroundColor(Color.rgb(100 , 100 , 55));
     }

     public void updaterating(Float currentrating)
     {
	    if(prevrating == 0.0f)
	    {
		   database.child("Users").child(key).child("Rating").setValue(currentrating);
	    }
	    else
	    {
		   Long newxp = oldexp + 1;
		   long newrating = Math.round((((prevrating * oldexp) + currentrating) / newxp) * 100.0) / 100;

		   database.child("Users").child(key).child("Rating").setValue(newrating);
		   dupref.child("Rating").setValue(newrating);
	    }
	    b2.setClickable(false);
	    //b2.setBackgroundColor(Color.rgb(100 , 100 , 55));
     }

     public void workdone(final View v)
     {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(Show_Available_worker_Profile.this);
	    LayoutInflater inflater = getLayoutInflater();
	    builder.setTitle("Please Review The Work..");
	    View dialogLayout = inflater.inflate(R.layout.activity_reviewdailog , null);
	    final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingbar);
	    final EditText comment = dialogLayout.findViewById(R.id.review_comment);
	    builder.setView(dialogLayout);
	    builder.setCancelable(false);
	    builder.setPositiveButton("Submit" , new DialogInterface.OnClickListener()
	    {
		   @Override
		   public void onClick(DialogInterface dialog , int which)
		   {

			  String com = comment.getText().toString();
			  //Toast.makeText(getApplicationContext() , "com:" + com , Toast.LENGTH_LONG).show();
			  refrev.child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(com);
			  // Toast toast = Toast.makeText(Show_Available_worker_Profile.this , "Rating and Review Updated" , Toast.LENGTH_SHORT);
			  //toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL , 0 , 0);
			  // toast.show();
		   }
	    });
	    builder.show();
	    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
	    {
		   @Override
		   public void onRatingChanged(RatingBar ratingBar , float rating , boolean fromUsers)
		   {
			  Float ratingVal = rating;
			  Float ratingvalue = ratingBar.getRating();
			  //Toast.makeText(getApplicationContext() , " Ratings : " + String.valueOf(ratingVal) + "" , Toast.LENGTH_SHORT).show();
			  //Toast.makeText(getApplicationContext() , " Ratings1 : " + ratingvalue + "" , Toast.LENGTH_SHORT).show();

			  //Toast.makeText(Show_Available_worker_Profile.this , num_stars , Toast.LENGTH_SHORT).show();

			  database.child("Users").child(key).child("Rating").setValue(ratingvalue);
			  expref.child(seeker_id).child("Experience").setValue(ratingvalue);

			  updaterating(ratingvalue);
		   }
	    });
	    expref.child(seeker_id).addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(DataSnapshot dataSnapshot)
		   {
			  String type = database.child("Users").child(seeker_id).child("Type").toString();
			  if(dataSnapshot.hasChild("Experience"))
			  {

				 long val = dataSnapshot.child("Experience").getValue(Long.class);
				 ++ val;
				 expref.child(seeker_id).child("Experience").setValue(val);
				 database.child("Users").child(seeker_id).child("Experience").setValue(val);
				 dupref.child("Experience").setValue(val);
			  }
			  else
			  {
				 expref.child(seeker_id).child("Experience").setValue(1);
				 database.child("Users").child(seeker_id).child("Experience").setValue(1);
			  }

		   }

		   @Override
		   public void onCancelled(DatabaseError databaseError)
		   {

		   }
	    });


	    b1.setClickable(false);
     }

     public void sendmsg()
     {

	    parseJASON();


     }

     public void parseJASON()
     {
	    SharedPreferences sharedPreferences = getSharedPreferences("Categories" , Context.MODE_PRIVATE);
	    //category of the type of the worker
	    String Address = sharedPreferences.getString("Address" , "");
	    Toast.makeText(getApplicationContext() , "Adress" + Address , Toast.LENGTH_LONG).show();

	    String ms = "This is from Sharm Address of the work Area:\n" + Address;

	    String url = "https://www.fast2sms.com/dev/bulk?authorization=zjKM1fZomdYNROkyDsvHc2wSVB0Jnex8rTWQbIX6ClgLUita3GzLFh9iHA4gdyKrV1oN6CYcJUSMjD73&sender_id=FSTSMS&message=" + ms + "&language=english&route=p&numbers=7691043330";
	    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET , url , null , new Response.Listener<JSONObject>()
	    {
		   @Override
		   public void onResponse(JSONObject response)
		   {


			  try
			  {
				 String s = response.getString("return");
				 if(s.equals("true"))
				 {
					Toast.makeText(getApplicationContext() , "Message sent successfully" , Toast.LENGTH_LONG).show();
				 }
				 else
				 {
					Toast.makeText(getApplicationContext() , "Message sending is failed " , Toast.LENGTH_LONG).show();
				 }
			  }catch(JSONException e)
			  {
				 e.printStackTrace();
			  }


		   }
	    } , new Response.ErrorListener()
	    {
		   @Override
		   public void onErrorResponse(VolleyError error)
		   {
			  error.printStackTrace();
		   }

	    });
	    rRequestQueue.add(request);
     }
}
