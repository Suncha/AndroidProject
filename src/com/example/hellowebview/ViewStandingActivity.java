package com.example.hellowebview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class ViewStandingActivity extends ListActivity {

	ListView classList;

	ArrayList<String> classes_sort = new ArrayList<String>();
	String divCode = "";
	ArrayList<String> teamNoList = new ArrayList<String>();

	private void initClasses(String divCode) {
		String team_result = "";
		String schedule_result = "";
		MySQLClient mc = new MySQLClient(
				"http://tinman.cs.gsu.edu:9999/~slee172/servlet/EMBLServices");
		try {
			String team_query = "select tno, coach from team where dcode= '"
					+ divCode + "'";// + "' order by tno ASC";
			String schedule_query = "select * from schedule where dcode='"
					+ divCode + "'";

			team_result = mc.runSQL(team_query);
			schedule_result = mc.runSQL(schedule_query);

			// Log.d("initclasses", team_result.length() + "");
			String[] team_rows = team_result.split("\n");
			String[] schedule_rows = schedule_result.split("\n");
			// Log.d("initclasses", rows[0]);

			int MaxTeam = 30;
			int wins_home[] = new int[MaxTeam];
			int wins_away[] = new int[MaxTeam];
			int losses_home[] = new int[MaxTeam];
			int losses_away[] = new int[MaxTeam];

			for (int i = 1; i < schedule_rows.length; i++) {
				String[] cols = schedule_rows[i].split(",");
				int hometeam = Integer.parseInt(cols[5]);
				int awayteam = Integer.parseInt(cols[4]);
				// Log.d("View Games", cols[0]);
				if (Integer.parseInt(cols[6]) > Integer.parseInt(cols[7])) {
					wins_home[hometeam] = wins_home[hometeam] + 0;
					wins_away[awayteam] = wins_away[awayteam] + 1;
					losses_home[hometeam] = losses_home[hometeam] + 1;
					losses_away[awayteam] = losses_away[awayteam] + 0;
				} else if (Integer.parseInt(cols[6]) < Integer
						.parseInt(cols[7])) {
					wins_home[hometeam] = wins_home[hometeam] + 1;
					wins_away[awayteam] = wins_away[awayteam] + 0;
					losses_home[hometeam] = losses_home[hometeam] + 0;
					losses_away[awayteam] = losses_away[awayteam] + 1;
				} else {
					wins_home[hometeam] = wins_home[hometeam] + 0;
					wins_away[awayteam] = wins_away[awayteam] + 0;
					losses_home[hometeam] = losses_home[hometeam] + 0;
					losses_away[awayteam] = losses_away[awayteam] + 0;

				}
			}
			ArrayList<Standings> classes = new ArrayList<Standings>();
			System.out.println("The team row length is " + team_rows.length);
			for (int j = 1; j < team_rows.length; j++) {

				int wins = 0;
				int losses = 0;
				int gamenum = 0;
				float winpercent = 0;
				System.out.println("The team row is " + team_rows[j]);
				String[] cols = team_rows[j].split(",");
				int tno = Integer.parseInt(cols[0]);

				wins = wins_home[tno] + wins_away[tno];
				losses = losses_home[tno] + losses_away[tno];
				gamenum = wins + losses;
				winpercent = Math.round((wins / gamenum) * 100);
				System.out.println("Standing element " + cols[1] + " " + wins
						+ " " + losses + " " + winpercent);
				Standings temp = new Standings(cols[1], wins, losses,
						winpercent, tno);
				System.out.println(temp);
				classes.add(temp);
			}

			Collections.sort(classes);

			Iterator iterator = classes.iterator();
			System.out.println("No error till here");
			while (iterator.hasNext()) {
				// Object element = iterator.next();
				Standings data = (Standings) iterator.next();
				String collection = data.getdname() + " W:"
						+ String.valueOf(data.getwins()) + " L:"
						+ String.valueOf(data.getlosses()) + " PCT:"
						+ String.valueOf(data.getwinpercent());
				classes_sort.add(collection);
				teamNoList.add(String.valueOf(data.getTeamNo()));
			}
			System.out.println("The sorted string is " + classes_sort);

		} catch (Exception e) {
			Log.d("MyAppException", e.getMessage());
			e.printStackTrace();
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
				android.R.layout.simple_list_item_1, classes_sort);
		classList.setAdapter(arrayAdapter);
		classList.setOnItemClickListener(teamListViewListener);
	}

	public OnItemClickListener teamListViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("classactivity", "In Listener" + teamNoList.get(position));
			Intent processTeam = new Intent(ViewStandingActivity.this,
					ViewTeamScheduleActivity.class);

			processTeam.putExtra("TEAMID", teamNoList.get(position) + "");
			startActivity(processTeam);
		}
	};

}
