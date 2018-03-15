package com.codify.howdy.api;

import com.codify.howdy.api.pojo.request.CommentPostRequest;
import com.codify.howdy.api.pojo.request.DislikePostRequest;
import com.codify.howdy.api.pojo.request.ForgotPasswordRequest;
import com.codify.howdy.api.pojo.request.GetCommentsRequest;
import com.codify.howdy.api.pojo.request.GetMessagesRequest;
import com.codify.howdy.api.pojo.request.GetNotificationsRequest;
import com.codify.howdy.api.pojo.request.GetSinglePostRequest;
import com.codify.howdy.api.pojo.request.GetUserChatWallRequest;
import com.codify.howdy.api.pojo.request.GetUserProfileRequest;
import com.codify.howdy.api.pojo.request.GetWallRequest;
import com.codify.howdy.api.pojo.request.GetWordsWithFilterRequest;
import com.codify.howdy.api.pojo.request.LikePostRequest;
import com.codify.howdy.api.pojo.request.LoginRequest;
import com.codify.howdy.api.pojo.request.RegisterRequest;
import com.codify.howdy.api.pojo.request.SearchUserRequest;
import com.codify.howdy.api.pojo.request.UploadImageRequest;
import com.codify.howdy.api.pojo.response.CommentPostResponse;
import com.codify.howdy.api.pojo.response.DislikePostResponse;
import com.codify.howdy.api.pojo.response.ForgotPasswordResponse;
import com.codify.howdy.api.pojo.response.GetCommentsResponse;
import com.codify.howdy.api.pojo.response.GetMessagesResponse;
import com.codify.howdy.api.pojo.response.GetNotificationsResponse;
import com.codify.howdy.api.pojo.response.GetSinglePostResponse;
import com.codify.howdy.api.pojo.response.GetUserChatWallResponse;
import com.codify.howdy.api.pojo.response.GetUserProfileResponse;
import com.codify.howdy.api.pojo.response.GetWallResponse;
import com.codify.howdy.api.pojo.response.GetWordsResponse;
import com.codify.howdy.api.pojo.response.GetWordsWithFilterResponse;
import com.codify.howdy.api.pojo.response.LikePostResponse;
import com.codify.howdy.api.pojo.response.LoginResponse;
import com.codify.howdy.api.pojo.response.RegisterResponse;
import com.codify.howdy.api.pojo.response.SearchUserResponse;
import com.codify.howdy.api.pojo.response.UploadImageResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("login")
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Single<RegisterResponse> register(@Body RegisterRequest request);

    @POST("register")
    Single<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("getwall")
    Single<GetWallResponse> getWall(@Body GetWallRequest request);

    @POST("getwords")
    Single<GetWordsResponse> getWords();

    @POST("getwordswithfilter")
    Single<GetWordsWithFilterResponse> getWordsWithFilter(@Body GetWordsWithFilterRequest request);

    @POST("searchuser")
    Single<SearchUserResponse> searchUser(@Body SearchUserRequest request);

    @POST("getuserchatwall")
    Single<GetUserChatWallResponse> getUserChatWall(@Body GetUserChatWallRequest request);

    @POST("getuserprofile")
    Single<GetUserProfileResponse> getUserProfile(@Body GetUserProfileRequest request);

    @POST("getmessages")
    Single<GetMessagesResponse> getMessages(@Body GetMessagesRequest request);

    @POST("getsinglepost")
    Single<GetSinglePostResponse> getSinglePost(@Body GetSinglePostRequest request);

    @POST("getcomments")
    Single<GetCommentsResponse> getComments(@Body GetCommentsRequest request);

    @POST("uploadimage")
    Single<UploadImageResponse> uploadImage(@Body UploadImageRequest request);

    @POST("getnotifications")
    Single<GetNotificationsResponse> getNotifications(@Body GetNotificationsRequest request);

    @POST("commentpost")
    Single<CommentPostResponse> commentPost(@Body CommentPostRequest request);

    @POST("commentpost")
    Single<LikePostResponse> likePost(@Body LikePostRequest request);

    @POST("commentpost")
    Single<DislikePostResponse> dislikePost(@Body DislikePostRequest request);
}
