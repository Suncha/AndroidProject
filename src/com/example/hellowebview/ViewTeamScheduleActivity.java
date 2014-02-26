package com.example.hellowebview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class ViewTeamScheduleActivity extends ListActivity {

	ArrayList<String> teamScheduleList = new ArrayList<String>();
	String teamNo = "";

	ListView classList;

	private void initClasses(String teamNo) {
		String result = "";

		MySQLClient mc = new MySQLClient(
				"http://tinman.cs.gsu.edu:9999/~slee172/servlet/EMBLServices");
		// query
		// http://tinman.cs.gsu.edu:9999/~slee172/servlet/EMBLServices?query=select%20gametime,%20gym,%20awayteam,%20hometeam,%20awayscore,%20homescore%20from%20schedule
		try {
			String query = "select gametime, gym, awayteam, hometeam, awayscore, homescore "
					+ "from schedule "
					+ "where awayteam = '"
					+ teamNo
					+ "' or hometeam = '"
					+ teamNo
					+ "' "
					+ "order by gametime asc";
			// query select * from schedule where awayteam = '5' or hometeam =
			// '5' order by gametime asc
			System.out.println("The query is " + query);

			Log.d("initclasses", query);
			Log.d("initclasses", query.length() + "");
			result = mc.runSQL(query);
			Log.d("initclasses", result.length() + "");
			String[] rows = result.split("\n");
			Log.d("initclasses", rows[0]);
			for (int i = 1; i < rows.length; i++) {
				String[] cols = rows[i].split(",");
				Log.d("View Games", cols[0]);
				String temp = "";
				temp += cols[0] + "@" + cols[1];
				if(!cols[4].equalsIgnoreCase("-1") && !cols[5].equalsIgnoreCase("-1")){
					
					if(cols[2].equalsIgnoreCase(teamNo)){
					temp += " Score:"+ "*" +cols[4] + "-" + cols[5];
					}else{
						temp += " Score:"+ cols[4] + "-" +"*" +cols[5];
					}
				}else{
					temp += " Score:"+"-" + ":" + "-";
				}
				
				teamScheduleList.add(temp);
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

		teamNo = getIntent().getStringExtra("TEAMID");
		initClasses(teamNo);

		classList = getListView();
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, teamScheduleList);
		classList.setAdapter(arrayAdapter);
	}

}
