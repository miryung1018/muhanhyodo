package com.example.student.muhanhyodo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Student on 2015-10-09.
 */
public interface MuhanhyodoService {

    public static final String API_URL = "http://muhanhyodo.cafe24.com/api";

//    @GET("/repos/{owner}/{repo}/contributors")
//    void contributors(
//            @Path("owner") String owner,
//            @Path("repo") String repo,
//            Callback<List<Contributor>> callback
//    );

    @GET("/address")
    void address(
            Callback<List<Address>> callback
    );

    @GET("/medicine")
    void medicine(
            Callback<List<Medicine>> callback
    );

    @GET("/user")
    void user(
            Callback<List<User>> callback
    );

    @GET("/family")
    void family(
            Callback<List<Family>> callback
    );

    @GET("/memo/notice")
    void notice(
            Callback<List<Notice>> callback
    );

    @GET("/memo/normal")
    void normal(
            Callback<List<Normal>> callback
    );

}
