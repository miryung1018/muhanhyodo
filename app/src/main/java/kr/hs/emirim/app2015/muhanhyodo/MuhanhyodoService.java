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

    @GET("/user/{user_id}")
    void user(
            @Path("user_id") int user_id,
            Callback<User> callback
    );


    @GET("/user/{user_id}/addresses")
    void address(
            @Path("user_id") int user_id,
            Callback<List<Address>> callback
    );

    @Multipart
    @POST("/user")
    void createUser(
            @Part("name") String name,
            @Part("tel") String tel,
            @Part("iid") String iid,
            Callback<User> callback
    );

    @Multipart
    @POST("/user/{user_id}/address")
    void createAddress(
            @Path("user_id") int user_id,
            @Part("tel") String tel,
            @Part("name") String name,
            @Part("address") String address,
            Callback<Address> callback
    );

    @Multipart
    @POST("/user/{user_id}/address/delete")
    void deleteAddress(
            @Path("user_id") int user_id,
            @Part("id") int id,
            @Part("tel") String tel,
            @Part("name") String name,
            @Part("address") String address,
            Callback<Address> callback
    );

    @Multipart
    @POST("/user/{user_id}/memo/notice")
    void createNotice(
            @Path("user_id") int user_id,
            @Part("title") String title,
            Callback<Notice> callback
    );

    @Multipart
    @POST("/user/{user_id}/memo/notice/delete")
    void deleteNotice(
            @Path("user_id") int user_id,
            @Part("id") int id,
            @Part("title") String title,
            Callback<Notice> callback
    );




    @Multipart
    @POST("/user/{user_id}/memo/normal")
    void createNormal(
            @Path("user_id") int user_id,
            @Part("title") String title,
            @Part("chk") int chk,
            Callback<Normal> callback
    );

    @Multipart
    @POST("/user/{user_id}/memo/normal/delete")
    void deleteNormal(
            @Path("user_id") int user_id,
            @Part("id") int id,
            @Part("title") String title,
            @Part("chk") int chk,
            Callback<Normal> callback
    );



    @Multipart
    @POST("/user/{user_id}/medicine")
    void createMedicine(
            @Path("user_id") int user_id,
            @Part("file") TypedFile sound,
            @Part("title") String title,
            @Part("morning") int morning,
            @Part("afternoon") int afternoon,
            @Part("evening") int evening,
            @Part("message") String message,
            Callback<Medicine> callback
    );


    @Multipart
    @POST("/user/{user_id}/medicine/delete")
    void deleteMedicine(
            @Path("user_id") int user_id,
            @Part("id") int id,
            @Part("title") String title,
            @Part("morning") int morning,
            @Part("afternoon") int afternoon,
            @Part("evening") int evening,
            @Part("sound") String sound,
            @Part("message") String message,
            Callback<Medicine> callback
    );

    @GET("/user/{user_id}/medicines")
    void medicine(
            @Path("user_id") int user_id,
            Callback<List<Medicine>> callback
    );

    @GET("/users")
    void userList(
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
