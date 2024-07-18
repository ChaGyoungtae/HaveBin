package com.HaveBinProject.HaveBin.Trashcan;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Reverse_Geocoding {


    private static String GEOCODE_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json?";
    private static String GEOCODE_USER_INFO = "KakaoAK 5b13725a23fd9697415c84837e2c48c4";


    public String loadLocation(double latitude, double longitude) throws IOException {
        String address = null;
        try {
            URL obj;

            String coordinatesystem = "WGS84";

            obj = new URL(GEOCODE_URL + "x=" + longitude + "&y=" + latitude + "&input_coord=" + coordinatesystem);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // JSON 문자열을 JSONObject로 변환
            JSONObject jsonObject = new JSONObject(response.toString());

            // documents 배열에서 첫 번째 객체 추출
            JSONArray documents = jsonObject.getJSONArray("documents");
            JSONObject firstDocument = documents.getJSONObject(0);

            // road_address에서 address_name 추출
            JSONObject roadAddress = firstDocument.getJSONObject("address");
            address = roadAddress.getString("address_name");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }
}