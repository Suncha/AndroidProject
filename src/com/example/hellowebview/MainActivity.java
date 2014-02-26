package com.example.hellowebview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class MainActivity extends ListActivity {

	ArrayList<String> divisions = new ArrayList<String>();
	ArrayList<String> divisionCodeList = new ArrayList<String>();

	ListView classList;

	private void initClasses() {
		String result = "";

		// MySQLClient mc = new MySQLClient(
		// "http://tinman.cs.gsu.edu:9999/raj/servlet/GSUServices");

		MySQLClient mc = new MySQLClient(
				"http://tinman.cs.gsu.edu:9999/~slee172/servlet/EMBLServices");
		try {
			String query = "select dcode,dname from division";

			Log.d("initclasses", query);
			Log.d("initclasses", query.length() + "");
			result = mc.runSQL(query);
			Log.d("initclasses", result.length() + "");
			String[] rows = result.split("\n");
			Log.d("initclasses", rows[0]);
			for (int i = 1; i < rows.length; i++) {
				String[] cols = rows[i].split(",");
				Log.d("View Games", cols[0]);
				divisions.add(cols[0] + ":" + cols[1]);
				divisionCodeList.add(cols[0].toString());
			}
		} catch (Exception e) {
			Log.d("MyAppException", e.getMessage());
		}

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		initClasses();

		classList = getListView();
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, divisions);
		classList.setAdapter(arrayAdapter);;
		classList.setOnItemClickListener(divListViewListener);

	}

	public OnItemClickListener divListViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("classactivity", "In Listener" + divisionCodeList.get(position));
			Intent processTeam = new Intent(MainActivity.this,
					ViewStandingActivity.class);
			
			processTeam.putExtra("DIVNAME", divisionCodeList.get(position) + "");
			startActivity(processTeam);
		}
	};

}
