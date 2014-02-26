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
public class ViewGameActivity extends ListActivity {

	ListView classList;
	ArrayList<String> classes = new ArrayList<String>();
	String divCode="";
	int teamNum = 0;
	String Coach ="";

	private void initClasses(String divCode) {
		String team_result = "";
		String schedule_result = "";
		MySQLClient mc = new MySQLClient(
				"http://tinman.cs.gsu.edu:9999/~slee172/servlet/EMBLServices");
		try {
			String team_query = "select tno, coach from team where dcode= '"+ divCode
						+ "' order by coach";
			String schedule_query = "select * from schedule where dcode='" + divCode+"'"; 
			team_result = mc.runSQL(team_query);
			schedule_result = mc.runSQL(schedule_query);
			
			//Log.d("initclasses", result.length() + "");
			String[] team_rows = team_result.split("\n");
			String[] schedule_rows = schedule_result.split("\n");
			//Log.d("initclasses", rows[0]);
					
			int MaxTeam = 30;
			int wins_home[] = new int [MaxTeam];
			int wins_away[] = new int [MaxTeam];
			int losses_home[] = new int [MaxTeam];
			int losses_away[] = new int [MaxTeam];
			
			for (int i = 1; i < schedule_rows.length; i++) {
				String[] cols = schedule_rows[i].split(",");
				int hometeam = Integer.parseInt(cols[5]);
				int awayteam = Integer.parseInt(cols[4]);
				
				String gamedate = "";
				String gametime = "";
				String gym ="";
				if((teamNum == awayteam)||(teamNum == hometeam)){
					gamedate = cols[2];
					gym = cols[3];
				}
				
				//Log.d("View Games", cols[0]);
				if(Integer.parseInt(cols[6]) > Integer.parseInt(cols[7])){
					wins_home[hometeam] = wins_home[hometeam] +0;
					wins_away[awayteam] = wins_away[awayteam] +1;
					losses_home[hometeam] = losses_home[hometeam] +1;
					losses_away[awayteam] = losses_away[awayteam] +0;
				}
				else if (Integer.parseInt(cols[6]) < Integer.parseInt(cols[7])){
					wins_home[hometeam] = wins_home[hometeam] +1;
					wins_away[awayteam] = wins_away[awayteam] +0;
					losses_home[hometeam] = losses_home[hometeam] +0;
					losses_away[awayteam] = losses_away[awayteam] +1;
				}
				else{
					wins_home[hometeam] = wins_home[hometeam] +0;
					wins_away[awayteam] = wins_away[awayteam] +0;
					losses_home[hometeam] = losses_home[hometeam] +0;
					losses_away[awayteam] = losses_away[awayteam] +0;
					
				}
			}
			for (int j = 1; j < team_rows.length; j++) {
				int wins = 0;
				int losses = 0;
				float winpercent = 0;
				String[] cols = team_rows[j].split(",");
				int tno = Integer.parseInt(cols[0]);

				wins = wins_home[tno] + wins_away[tno];
				losses = losses_home[tno] +losses_away[tno];
				winpercent = Math.round((wins/losses)* 100);
				System.out.println(cols[1]+"="+wins);
				classes.add(cols[1] + "|" + wins + "|" + losses + "|" + winpercent);
			}
			
			
			
		} catch (Exception e) {
			Log.d("MyAppException", e.getMessage());
		}

	}

	/** Called when the activity is first created. */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		divCode = getIntent().getStringExtra("DIVNAME");
		initClasses(divCode);

		classList = getListView();
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, classes);
		classList.setAdapter(arrayAdapter);
	}

}