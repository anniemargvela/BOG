package com.example.annie_pc.hw2;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String START_DIRECTORY = "";
    private static final String CWD_KEY = "cwd";
    ListView listView;
    GridView gridView;
    TextView cwdView;
    FileListAdapter fileListAdapter;
    FileGridAdapter fileGridAdapter;
    File currentDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory().toString()+"/";
        if (currentDirectory == null) {
            currentDirectory = Environment.getRootDirectory();
        }

        listView = (ListView) findViewById(R.id.list_view);
        gridView = (GridView) findViewById(R.id.grid_view);
        cwdView = (TextView) findViewById(R.id.cwd_textView);


        fileListAdapter = new FileListAdapter(this);
        fileGridAdapter = new FileGridAdapter(this);

        assert listView != null;
        assert gridView != null;

        listView.setAdapter(fileListAdapter);
        gridView.setAdapter(fileGridAdapter);
        cwdView.setText(currentDirectory.getAbsolutePath());

        gridView.setVisibility(View.VISIBLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File clickedFile = currentDirectory.listFiles()[position];
                if (clickedFile.isDirectory() && clickedFile.listFiles() != null) {
                    currentDirectory = currentDirectory.listFiles()[position];
                    cwdView.setText(currentDirectory.getAbsolutePath());
                    fileGridAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File clickedFile = currentDirectory.listFiles()[position];
                if (clickedFile.isDirectory() && clickedFile.listFiles() != null) {
                    currentDirectory = currentDirectory.listFiles()[position];
                    cwdView.setText(currentDirectory.getAbsolutePath());
                    fileListAdapter.notifyDataSetChanged();
                }
            }
        });

        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                fileGridAdapter.setChecked(position, checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete_menu_item){
                    Set<Integer> selected = fileGridAdapter.getSelectedPositions();
                    for (int pos: selected) {
                        fileGridAdapter.deleteFileAt(pos);
                    }
                    mode.finish();
                    fileGridAdapter.clearSelected();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                fileGridAdapter.clearSelected();
            }
        });


        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                fileListAdapter.setChecked(position, checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete_menu_item){
                    Set<Integer> selected = fileListAdapter.getSelectedPositions();
                    for (int pos: selected) {
                        fileListAdapter.deleteFileAt(pos);
                    }
                    mode.finish();
                    fileListAdapter.clearSelected();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                fileListAdapter.clearSelected();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (currentDirectory.getName().equals(START_DIRECTORY)) {
            super.onBackPressed();
            return;
        }
        currentDirectory = currentDirectory.getParentFile();
        cwdView.setText(currentDirectory.getAbsolutePath());
        fileListAdapter.notifyDataSetChanged();
        fileGridAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_view:
                if (gridView.getVisibility() == View.INVISIBLE) {
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                } else {
                    gridView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CWD_KEY, currentDirectory.getAbsolutePath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String cwd = savedInstanceState.getString(CWD_KEY);
        if (cwd == null)
            cwd = START_DIRECTORY;
        currentDirectory = new File(cwd);
        fileGridAdapter.notifyDataSetChanged();
        fileListAdapter.notifyDataSetChanged();
        cwdView.setText(currentDirectory.getAbsolutePath());
    }
}
