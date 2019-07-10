package com.kvsn.builds.cap1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentAdapter extends BaseAdapter
{
      String comment;

     public CommentAdapter(String comment)
     {
	    this.comment = comment;
     }


     @Override
     public int getCount()
     {
	    return 0;
     }

     @Override
     public Object getItem(int position)
     {
	    return null;
     }

     @Override
     public long getItemId(int position)
     {
	    return 0;
     }

     @Override
     public View getView(int position , View convertView , ViewGroup parent)
     {
	    return null;
     }
}
