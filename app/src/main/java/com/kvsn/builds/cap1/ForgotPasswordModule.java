package com.kvsn.builds.cap1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordModule extends Activity {
     private String Email;
     EditText editTextEmail;
     private FirebaseAuth mAuth;
     ProgressDialog pd;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_forgot_password_module);
	    setTitle("Forgot Password");

	    pd=new ProgressDialog(this);

	    mAuth=FirebaseAuth.getInstance();

     }

     private void PasswordReset(String Email){
	    pd.setMessage("Sending");
	    pd.show();


	    findViewById(R.id.button_forgotpassword);

	    mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
		   @Override
		   public void onComplete(@NonNull Task<Void> task) {

			  if(task.isSuccessful()){
				 pd.dismiss();
				 Toast.makeText(getApplicationContext(),"Reset Password intruction has sent to your Registered mail id",Toast.LENGTH_SHORT).show();
			  }

		   }
	    }).addOnFailureListener(new OnFailureListener() {
		   @Override
		   public void onFailure(@NonNull Exception e) {
			  pd.dismiss();
			  Toast.makeText(getApplicationContext(),"Verification failed Invalid mail id try again",Toast.LENGTH_SHORT).show();
		   }
	    });



     }


     boolean validation(String Email){
	    boolean valid=true;
	    if(Email.isEmpty()){
		   editTextEmail.setError("Email is required");
		   editTextEmail.requestFocus();
		   valid =false;
	    }

	    if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
		   editTextEmail.setError("Please Enter valid Email address");
		   editTextEmail.requestFocus();
		   valid =false;
	    }



	    return valid;
     }
     public void Reset(View view){
	    editTextEmail=(EditText) findViewById(R.id.editTextForgotPassword);
	    String Email =editTextEmail.getText().toString();
	    if(validation(Email))
	    {
		   PasswordReset(Email);
	    }
     }

}
