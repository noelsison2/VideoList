package com.example.noel.videolist;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noel.videolist.content.ContentListActivity;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract;

public class MainActivity extends AppCompatActivity implements ModuleListAdapter.ActivityListAdapterClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.getName();
    private static final int DB_LOADER = 0;

    RecyclerView recyclerView;
    ModuleListAdapter activityListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main UI
        setContentView(R.layout.activity_main);

        // Body UI
        recyclerView = (RecyclerView) findViewById(R.id.rv_activity_main);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        activityListAdapter = new ModuleListAdapter(this, null);
        recyclerView.setAdapter(activityListAdapter);

        // Handles DB
        getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
            case R.id.action_assessment:
                Toast.makeText(this, "Assessment menu option clicked.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:
                Toast.makeText(this, "Profile menu option clicked.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int moduleId, String title) {
        Intent intent = new Intent(getApplicationContext(), ContentListActivity.class);
        intent.putExtra(ContentListActivity.INTENT_EXTRA_MODULE_ID, moduleId);
        intent.putExtra(ContentListActivity.INTENT_EXTRA_MODULE_TITLE, title);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                return new CursorLoader(this,
                        VideoListContentProvider.MODULE_URI,
                        new String[]{
                                VideoListContract.ModuleEntry._ID,
                                VideoListContract.ModuleEntry.COLUMN_TITLE
                        },
                        null,
                        null,
                        VideoListContract.ModuleEntry._ID);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activityListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activityListAdapter.swapCursor(null);
    }
}
