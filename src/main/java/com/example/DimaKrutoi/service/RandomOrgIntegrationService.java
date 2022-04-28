package com.example.DimaKrutoi.service;

import com.example.DimaKrutoi.dto.RandomAggregationDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RandomOrgIntegrationService {

    String url  = "https://www.random.org/integers";

    public void setUrl(String url){
        this.url = url;
    }

    String result = "";
    public String getRandomNumber(RandomAggregationDto dto){

        String from = dto.getFrom();
        String to = dto.getTo();

        String query = url + "/?num=1&min=" + from + "&max=" + to + "&col=1&base=10&format=plain&rnd=new";

        StringBuilder builder = new StringBuilder();

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader generatedNum = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = generatedNum.readLine()) != null){
                    builder.append(line);
                }
                result = builder.toString();
            } else {
                System.out.println("Status : " + connection.getResponseCode() + "\n" + "Failed cause : " + connection.getResponseMessage());
            }

        } catch (Throwable cause){
            cause.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }

        return result;
    }
}
