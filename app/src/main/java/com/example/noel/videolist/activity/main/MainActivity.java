package com.example.noel.videolist.activity.main;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.base.BaseRecyclerListActivity;
import com.example.noel.videolist.activity.list.NestedListActivity;
import com.example.noel.videolist.activity.splash.SplashActivity;
import com.example.noel.videolist.activity.topic.TopicListActivity;
import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract.Model;
import com.example.noel.videolist.data.VideoListContract.ModuleEntry;

public class MainActivity extends BaseRecyclerListActivity {

    private final String TAG = MainActivity.class.getName();
    private static final int DB_LOADER = 0;

    final String greetingFormat = "Hello %s,";
    final String defaultName = "User";

    TextView textViewGreeting;
    RecyclerView recyclerView;
    ModuleListAdapter adapter;

    String savedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First-time launch personalization
        checkIfFirstTimeLaunched();

        // Main UI
        setContentView(R.layout.activity_main);

        // Body UI
        textViewGreeting = (TextView) findViewById(R.id.tv_main_greeting);
        textViewGreeting.setText(String.format(greetingFormat, savedName));
        recyclerView = (RecyclerView) findViewById(R.id.rv_main_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adapter that will connect the UI and DB fetch results
        adapter = new ModuleListAdapter(this);
        recyclerView.setAdapter(adapter);

        // Handles DB
        getLoaderManager().initLoader(DB_LOADER, null, this);
    }

    private void checkIfFirstTimeLaunched() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean firstLaunch = sharedPreferences.getBoolean(getString(R.string.saved_first_time), true);
        if (firstLaunch) {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            savedName = sharedPreferences.getString(getString(R.string.saved_name), defaultName);
        }
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
    public void onItemClick(Model model) {
        ModuleEntry moduleEntry = (ModuleEntry) model;
        Intent intent = new Intent(getApplicationContext(), NestedListActivity.class);
        intent.putExtra(NestedListActivity.INTENT_EXTRA_MODULE_ID, moduleEntry.getId());
        intent.putExtra(NestedListActivity.INTENT_EXTRA_MODULE_TITLE, moduleEntry.getTitle());
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DB_LOADER:
                return new CursorLoader(this,
                        VideoListContentProvider.MODULE_URI,
                        null, null, null,
                        ModuleEntry._ID);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
