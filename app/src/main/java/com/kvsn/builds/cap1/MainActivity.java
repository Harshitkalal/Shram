package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity
{

     RelativeLayout r1, r2;
     ImageView login_icon, signup_icon;
     Handler h1 = new Handler();
     FirebaseAuth mAuth;
     DatabaseReference mDatabase, msubref; //for retrieving type of user
     EditText et_mail, et_pwd;
     String mail, pwd, type;
     ProgressDialog pd;
     Runnable run1 = new Runnable()
     {
	    @Override
	    public void run()
	    {
		   r1.setVisibility(View.VISIBLE);
		   r2.setVisibility(View.VISIBLE);
	    }
     };


     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    r1 = findViewById(R.id.relative_login);
	    login_icon = findViewById(R.id.imgView_logo);
	    signup_icon = findViewById(R.id.imgView_icon_signup);
	    r2 = findViewById(R.id.relative_signup);
	    et_mail = findViewById(R.id.login_mail);
	    et_pwd = findViewById(R.id.login_pwd);
	    pd = new ProgressDialog(this);

	    //initialising firebase services
	    mDatabase = FirebaseDatabase.getInstance().getReference();
	    mAuth = FirebaseAuth.getInstance();

	    h1.postDelayed(run1 , 1500); //to make the layouts visible after 1.5 seconds giving an animation
     }

     public void login(View v)
     {
	    //new checkInternetConnection(this).checkConnection();
	    final String m, p;
	    m = et_mail.getText().toString().trim();
	    //Toast.makeText(MainActivity.this , m , Toast.LENGTH_SHORT).show();
	    p = et_pwd.getText().toString().trim();
	    //Toast.makeText(MainActivity.this , p , Toast.LENGTH_SHORT).show();
	    pd.setTitle("Logging In.....");
	    pd.show();
	    pd.setCanceledOnTouchOutside(false);

	    mAuth.signInWithEmailAndPassword(m , p).addOnCompleteListener(this , new OnCompleteListener<AuthResult>()
	    {
		   @Override
		   public void onComplete(@NonNull final Task<AuthResult> task)
		   {
			  if(task.isSuccessful())
			  {
				 //Toast.makeText(MainActivity.this , "Success" , Toast.LENGTH_SHORT).show();
				 retrieve();
				 pd.dismiss();
			  }
			  else
			  {
				 Toast.makeText(MainActivity.this , "Sign In Failed" , Toast.LENGTH_SHORT).show();
				 pd.dismiss();
			  }
		   }
	    });
     }

     public void retrieve()
     {
	    FirebaseUser user = mAuth.getCurrentUser();
	    msubref = mDatabase.child("Users").child(user.getUid());
	    msubref.addListenerForSingleValueEvent(new ValueEventListener()
	    {
		   @Override
		   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
		   {
			  //Toast.makeText(MainActivity.this , "tesst" , Toast.LENGTH_SHORT).show();
			  String type = dataSnapshot.child("Type").getValue(String.class);
			  if(type.equals("Seeker"))
			  {
				 startActivity(new Intent(MainActivity.this , SeekerMain.class));
			  }
			  else if(type.equals("Recruiter"))
			  {
				 startActivity(new Intent(MainActivity.this , RecruiterMain.class));
			  }
			  //Toast.makeText(MainActivity.this , type , Toast.LENGTH_SHORT).show();
		   }

		   @Override
		   public void onCancelled(@NonNull DatabaseError databaseError)
		   {

		   }
	    });
     }

     public boolean validation()
     {
	    //Toast.makeText(MainActivity.this , "Inside Validation" , Toast.LENGTH_SHORT).show();
	    boolean valid = true;
	    if(mail.isEmpty())
	    {
		   et_mail.setError("Email is required");
		   et_mail.requestFocus();
		   valid = false;
	    }

	    if(! Patterns.EMAIL_ADDRESS.matcher(mail).matches())
	    {
		   et_mail.setError("Please Enter valid Email address");
		   et_mail.requestFocus();
		   valid = false;
	    }
	    if(pwd.isEmpty())
	    {
		   et_pwd.setError("Please Enter the password");
		   et_pwd.requestFocus();
		   valid = false;
	    }
	    if(pwd.length() < 6)
	    {
		   et_pwd.setError("Minimum password length is 6");
		   et_pwd.requestFocus();
		   valid = false;
	    }
	    return valid;

     }

     public void signup(View v) //called when signup button is clicked
     {
	    Intent shift_to_signup = new Intent(MainActivity.this , Signup.class);
	    ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , login_icon , ViewCompat.getTransitionName(login_icon));
	    startActivity((shift_to_signup) , actop.toBundle());
	    overridePendingTransition(R.anim.fadein , R.anim.fadeout); //to replace default animations with fade in and fade out;
	    //Snackbar.make(v , "Switch to Sign Up Page" , Snackbar.LENGTH_SHORT).show();
     }

     public void forgotpassword(View v)
     {
          startActivity(new Intent(MainActivity.this , ForgotPasswordModule.class));
     }

     @Override
     public void onBackPressed()
     {
	    finish();
     }
}
