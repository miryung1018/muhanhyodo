package kr.hs.emirim.app2015.muhanhyodo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

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

    @GET("/user/{user_id}/addresses")
    void address(
            @Path("user_id") int user_id,
            Callback<List<Address>> callback
    );

    @POST("/user")
    void createUser(
            @Body User user,
            Callback<User> callback
    );

    @Multipart
    @POST("/medicine")
    void createMedicine(
            @Part("file") TypedFile sound,
            @Part("title") String title,
            @Part("morning") int morning,
            @Part("afternoon") int afternoon,
            @Part("evening") int evening,
            @Part("message") String message,
            @Part("user_id") int user_id,
            Callback<Medicine> callback
    );

    @GET("/user/{user_id}/medicines")
    void medicine(
            @Path("user_id") int user_id,
            Callback<List<Medicine>> callback
    );

    @GET("/users")
    void user(
            Callback<List<User>> callback
    );

    @GET("/user/{user_id}/families")
    void family(
            @Path("user_id") int user_id,
            Callback<List<Family>> callback
    );

    @GET("/user/{user_id}/memo/notices")
    void notice(
            @Path("user_id") int user_id,
            Callback<List<Notice>> callback
    );

    @GET("/user/{user_id}/memo/normals")
    void normal(
            @Path("user_id") int user_id,
            Callback<List<Normal>> callback
    );

}
