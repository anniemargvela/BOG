package com.example.annie_pc.hw2;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Annie-PC on 23-Nov-16.
 */

public class FileGridAdapter extends FileAdapter {
    public FileGridAdapter(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        ViewHolder viewHolder;
        if(convertView == null){
            rootView = View.inflate(mainActivity, R.layout.grid_item_view, null);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.grid_elem_image);
            TextView nameView = (TextView) rootView.findViewById(R.id.grid_elem_name);
            viewHolder = new ViewHolder();
            viewHolder.imageView = imageView;
            viewHolder.nameView = nameView;
            rootView.setTag(viewHolder);
        }else{
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        File file =  mainActivity.currentDirectory.listFiles()[position];
        String fileName = file.getName();
        viewHolder.nameView.setText(fileName);
        viewHolder.imageView.setImageResource(getImageId(file));

        if (checkedPositions.contains(position)) {
            rootView.setBackgroundColor(Color.RED);
        } else {
            rootView.setBackgroundColor(Color.TRANSPARENT);
        }

        return rootView;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView nameView;
    }
}
