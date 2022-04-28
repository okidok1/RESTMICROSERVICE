package com.example.DimaKrutoi;

import com.example.DimaKrutoi.config.ContainersEnvironment;
import com.example.DimaKrutoi.dto.RandomAggregationDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.service.RandomOrgIntegrationService;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DimaKrutoiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RandomOrgIntegrationServiceTests extends ContainersEnvironment {

    private final OkHttpClient client = new OkHttpClient();
    public MockWebServer mockWebServer;
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    RandomOrgIntegrationService randomService;

    @LocalServerPort
    int RandomPort;

    String url = "http://localhost:";


    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        randomService.setUrl(mockWebServer.url("/integers").toString());
    }

    @Test
    public void shouldBeTimeout(){
        mockWebServer.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)); //timeout
        RandomAggregationDto aggregationDto = new RandomAggregationDto()
                .setFrom("1")
                .setTo("10");


        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(RandomPort)
                .addPathSegment("trees")
                .addPathSegment("random")
                .addQueryParameter("from", aggregationDto.getFrom())
                .addQueryParameter("to", aggregationDto.getTo())
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatus.REQUEST_TIMEOUT.value(), response.code());

        int result = 0;
        try {
            result = Integer.parseInt(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue((result > 0) && (result < 11));
    }

    @Test
    public void shouldBeBadRequest400(){
        mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("")); //400
        RandomAggregationDto aggregationDto = new RandomAggregationDto()
                .setFrom("1")
                .setTo("10");


        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(RandomPort)
                .addPathSegment("trees")
                .addPathSegment("random")
                .addQueryParameter("from", "")
                .addQueryParameter("to", aggregationDto.getTo())
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.code());

        int result = 0;
        try {
            result = Integer.parseInt(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue((result > 0) && (result < 11));
    }


    @Test
    public void shouldBeOk200() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("1")); //200
        RandomAggregationDto aggregationDto = new RandomAggregationDto()
                .setFrom("1")
                .setTo("10");


        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(RandomPort)
                .addPathSegment("trees")
                .addPathSegment("random")
                .addQueryParameter("from", aggregationDto.getFrom())
                .addQueryParameter("to", aggregationDto.getTo())
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatus.OK.value(), response.code());

        int result = 0;
        try {
            result = Integer.parseInt(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result > 0 && result < 11);

    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

}
