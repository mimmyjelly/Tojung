package com.example.tojung;
import android.os.AsyncTask;
import java.sql.Connection;
import java.sql.DriverManager;


class ConnectToDatabase extends AsyncTask<Void, Void, Void> {

    private String url = "jdbc:mysql://RDS-endpoint/tojungDB";
    private String user = "username";
    private String password = "RDS_PW";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            // 데이터베이스 작업 수행
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
