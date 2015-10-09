package com.example.student.muhanhyodo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Student on 2015-10-09.
 */


interface GitHubService {

    public static final String API_URL = "https://api.github.com";

    @GET("/repos/{owner}/{repo}/contributors")
    void contributors(
            @Path("owner") String owner,
            @Path("repo") String repo,
            Callback<List<Contributor>> callback
    );
}