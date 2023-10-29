package com.example.purchaserequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCustomersTable {
    public Boolean result;

    public UpdateCustomersTable(){
        try {
            URL url = new URL("http://localhost/updateCustomers.php");
            String postData = "updateCustomer=1";

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length()));
            conn.setUseCaches(false);

            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(postData);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream())))
            {

                String line;
                while ((line = br.readLine()) != null) {
                    this.result = line.equals("true"); //возвращает результат обновления
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
