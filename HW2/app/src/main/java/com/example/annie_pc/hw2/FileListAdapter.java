package com.example.annie_pc.hw2;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Annie-PC on 23-Nov-16.
 */

public class FileListAdapter extends FileAdapter {
    public FileListAdapter(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        ViewHolder viewHolder;
        if(convertView == null){
            rootView = View.inflate(mainActivity, R.layout.list_item_view, null);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
            TextView nameView = (TextView) rootView.findViewById(R.id.name_view);
            TextView sizeView = (TextView) rootView.findViewById(R.id.size_view);
            TextView dateView = (TextView) rootView.findViewById(R.id.date_view);

            viewHolder = new ViewHolder();
            viewHolder.imageView = imageView;
            viewHolder.nameView = nameView;
            viewHolder.sizeView = sizeView;
            viewHolder.dateView = dateView;

            rootView.setTag(viewHolder);
        }else{
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        File file = mainActivity.currentDirectory.listFiles()[position];
        String fileName = file.getName();
        viewHolder.nameView.setText(fileName);
        viewHolder.imageView.setImageResource(getImageId(file));
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                viewHolder.sizeView.setText(file.listFiles().length + mainActivity.getString(R.string.list_number_items_name));
            } else {
                viewHolder.sizeView.setText(0 + mainActivity.getString(R.string.list_number_items_name));
            }
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            double fileSizeKB = Double.valueOf(df.format(file.length()/1024.0));
            viewHolder.sizeView.setText(fileSizeKB + mainActivity.getString(R.string.file_size_unit_name));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mainActivity.getString(R.string.date_format));
        viewHolder.dateView.setText(simpleDateFormat.format(new Date(file.lastModified())));

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
        TextView sizeView;
        TextView dateView;
    }
}
