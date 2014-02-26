package com.example.hellowebview;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MySQLClient {

    private String servicesEndPointUrl;

    public MySQLClient(String endpoint) {
      servicesEndPointUrl = endpoint;
    } 

    public String runSQL(String stmt) throws Exception {

      String getTeamsURL = "query=" + URLEncoder.encode(stmt, "UTF-8");

      URL url = new URL(servicesEndPointUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //conn.setRequestMethod("GET");
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      conn.setRequestProperty("Content-Length", "" + Integer.toString(getTeamsURL.getBytes().length));
      conn.setRequestProperty("Content-Language", "en-US");  
      conn.setUseCaches (false);
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setAllowUserInteraction(false);

      //Send request
      DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
      wr.writeBytes(getTeamsURL);
      wr.flush ();
      wr.close ();

      String response = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String nextline = br.readLine();
      while (nextline != null) {
        response += nextline;
        response += "\n";
        nextline = br.readLine();
      }
      //System.out.print(response);
      return response;
    }

}