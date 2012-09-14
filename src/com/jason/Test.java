package com.jason;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Intent.ACTION_MAIN;

public class Test extends ListActivity {
    private static final String CATEGORY_SAMPLE = "com.jason.test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);

        listView.setId(android.R.id.list);

        setContentView(listView);

        List<TitleIntent> testCases = makeTestCases();

        ListAdapter adapter = new ArrayAdapter<TitleIntent>(this, R.layout.list_item, testCases);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TitleIntent titleIntent = (TitleIntent) l.getItemAtPosition(position);

        startActivity(titleIntent.intent);
    }

    private List<TitleIntent> makeTestCases() {
        List<TitleIntent> data = new ArrayList<TitleIntent>();

        Intent mainIntent = new Intent(ACTION_MAIN);
        mainIntent.addCategory(CATEGORY_SAMPLE);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list) return data;

        for (int i = 0, len = list.size(); i < len; i++) {
            ResolveInfo info = list.get(i);

            Intent intent = activityIntent(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name
            );

            data.add(
                    new TitleIntent(info.activityInfo.name, intent)
            );
        }

        Collections.sort(data);

        System.out.println("data = " + data.size());

        return data;
    }

    private Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    private class TitleIntent implements Comparable<TitleIntent> {

        TitleIntent(String title, Intent intent) {
            this.title = title;
            this.intent = intent;
        }

        String title;
        Intent intent;

        @Override
        public String toString() {
            return title;
        }

        @Override
        public int compareTo(TitleIntent another) {
            return title.compareTo(another.title);
        }

    }

}
