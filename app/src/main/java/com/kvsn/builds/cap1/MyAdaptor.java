package com.kvsn.builds.cap1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.MyHolder>
{
     Context ct;
     ArrayList<Person> al;

     MyAdaptor(Context cont , ArrayList<Person> al)
     {
	    this.ct = cont;
	    this.al = al;

     }

     @Override
     public MyHolder onCreateViewHolder(ViewGroup parent , int viewType)
     {
	    //Toast.makeText(ct , "Inside Adapter" , Toast.LENGTH_SHORT).show();
	    LayoutInflater li = LayoutInflater.from(ct);
	    View v = li.inflate(R.layout.my_layout , parent , false);
	    return new MyHolder(v);
     }

     @Override
     public void onBindViewHolder(MyAdaptor.MyHolder holder , final int position)
     {


          // SharedPreferences sharedPreferences= getSharedPreferences("Categories", Context.MODE_PRIVATE);
	    //SharedPreferences.Editor editor=sharedPreferences.edit();
	    //String categorie;
	    SharedPreferences sharedPreferences;
	    final SharedPreferences.Editor editor;
	    sharedPreferences = ct.getSharedPreferences("Getkey" , Context.MODE_PRIVATE);
	    editor = sharedPreferences.edit();
	    final String key;

	    final Person p1 = al.get(position);
	    holder.Name.setText(p1.getName());
	    Log.d("1st check" , String.valueOf(0));

	    key = p1.getId();

	    holder.Rating.setText(p1.getRating());
	    Log.d("2nd check" , String.valueOf(1));

	    //holder.WorkImage.setImageResource(p1.getImage());
	      if(p1.getUrl()!=null)
	           Picasso.get().load(p1.getUrl()).into(holder.WorkImage);
	      else
	           holder.WorkImage.setImageResource(R.drawable.profile);
	    Log.d("3rd check" , String.valueOf(2));

	    holder.cld.setOnClickListener(new View.OnClickListener()
	    {
		   @Override
		   public void onClick(View view)
		   {
			  //Toast.makeText(ct , "Clicked " + position , Toast.LENGTH_SHORT).show();
			  //ct.startActivity(new Intent(ct,Rec_Show_Seeker.class));
			  //Toast.makeText(ct , "Id" + key , Toast.LENGTH_SHORT).show();
			  editor.putString("key" , key);
			  editor.commit();
			  ct.startActivity(new Intent(ct , Show_Available_worker_Profile.class));

		   }
	    });

     }

     @Override
     public int getItemCount()
     {
	    return al.size();
     }

     public class MyHolder extends RecyclerView.ViewHolder
     {
	    ImageView WorkImage;
	    TextView Name;
	    TextView Rating;
	    CardView cld;

	    public MyHolder(View itemView)
	    {
		   super(itemView);
		   WorkImage = (ImageView)itemView.findViewById(R.id.work_image);
		   Name = (TextView)itemView.findViewById(R.id.worker_name);
		   Rating = (TextView)itemView.findViewById(R.id.work_rating);
		   cld = (CardView)itemView.findViewById(R.id.cl);

	    }
     }


}
