package com.example.annie_pc.hw2;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Annie-PC on 23-Nov-16.
 */

public abstract class FileAdapter extends BaseAdapter {
    protected MainActivity mainActivity;
    protected Set<Integer> checkedPositions;

    public FileAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        checkedPositions = new TreeSet<>();
    }

    @Override
    public int getCount() {
        if (mainActivity.currentDirectory.listFiles() == null) {
            return 0;
        }
        return mainActivity.currentDirectory.listFiles().length;
    }

    @Override
    public Object getItem(int position) {
        return mainActivity.currentDirectory.listFiles()[position].getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    protected int getImageId(File file) {
        if (file.isDirectory())
            return R.drawable.folder;

        String extension = getFileExtension(file.getName());

        switch (extension) {
            case ".doc":
                return R.drawable.doc;
            case ".mp3":
                return R.drawable.mp3;
            case ".png":
                return R.drawable.png;
            case ".txt":
                return R.drawable.txt;
            case ".xls":
                return R.drawable.xls;
            case ".zip":
                return R.drawable.zip;
            default:
                return R.drawable.default_name;
        }
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1)
            return fileName;
        return fileName.substring(index);
    }

    public void setChecked(int position, boolean checked) {
        if (checked) {
            checkedPositions.add(position);
        } else {
            checkedPositions.remove(position);
        }
        notifyDataSetChanged();
    }

    public Set<Integer> getSelectedPositions() {
        return checkedPositions;
    }

    public void deleteFileAt(int pos) {
        boolean res = mainActivity.currentDirectory.listFiles()[pos].delete();
        notifyDataSetChanged();
    }

    public void clearSelected(){
        checkedPositions.clear();
        notifyDataSetChanged();
    }
}
