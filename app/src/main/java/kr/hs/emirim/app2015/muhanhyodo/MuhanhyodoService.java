package kr.hs.emirim.app2015.muhanhyodo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

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

}
