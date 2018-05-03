package istanbul.codify.muudy.api;

import io.reactivex.Single;
import istanbul.codify.muudy.api.pojo.ServiceCallback;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {

    @POST("login")
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Single<RegisterResponse> register(@Body RegisterRequest request);

    @POST("register")
    Single<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("changepass")
    Single<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("getwall")
    Single<GetWallResponse> getWall(@Body GetWallRequest request);

    @POST("getwords")
    Single<GetWordsResponse> getWords(@Body GetWordsRequest request);

    @POST("getwordswithfilter")
    Single<GetWordsWithFilterResponse> getWordsWithFilter(@Body GetWordsWithFilterRequest request);

    @POST("searchplaces")
    Single<SearchPlacesResponse> searchPlaces(@Body SearchPlacesRequest request);

    @POST("searchuser")
    Single<SearchUserResponse> searchUser(@Body SearchUserRequest request);

    @POST("getuserchatwall")
    Single<GetUserChatWallResponse> getUserChatWall(@Body GetUserChatWallRequest request);

    @POST("sendmessage")
    Single<SendMessageResponse> sendMessage(@Body SendMessageRequest request);

    @POST("getuserprofile")
    Single<GetUserProfileResponse> getUserProfile(@Body GetUserProfileRequest request);

    @POST("getuserprofile")
    Call<GetUserProfileResponse> me(@Body GetUserProfileRequest request);

    @POST("getuserposts")
    Single<GetUserPostsResponse> getUserPosts(@Body GetUserPostsRequest request);

    @POST("getmessages")
    Single<GetMessagesResponse> getMessages(@Body GetMessagesRequest request);

    @POST("getsinglepost")
    Single<GetSinglePostResponse> getSinglePost(@Body GetSinglePostRequest request);

    @POST("getcomments")
    Single<GetCommentsResponse> getComments(@Body GetCommentsRequest request);

    @POST("uploadimage")
    Single<UploadImageResponse> uploadImage(@Body UploadImageRequest request);

    @POST("getnotifications")
    Single<GetNotificationsMeResponse> getNotificationsMe(@Body GetNotificationsMeRequest request);

    @POST("getnotifications")
    Single<GetNotificationsFollowingResponse> getNotificationsFollowing(@Body GetNotificationsFollowingRequest request);

    @POST("commentpost")
    Single<CommentPostResponse> commentPost(@Body CommentPostRequest request);

    @POST("likepost")
    Single<LikePostResponse> likePost(@Body LikePostRequest request);

    @POST("dislikepost")
    Single<DislikePostResponse> dislikePost(@Body DislikePostRequest request);

    @POST("getfollowedusers")
    Single<GetFollowedUsersResponse> getFollowedUsers(@Body GetFollowedUsersRequest request);

    @POST("getActivities")
    Single<GetActivitiesResponse> getActivities();

    @POST("getActivityStats")
    Single<GetActivityStatsResponse> getActivityStats(@Body GetActivityStatsRequest request);

    @POST("updateprofile")
    Single<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    @POST("createposttext")
    Single<CreateTextPostResponse> createPostText(@Body CreatePostTextRequest request);

    @POST("newpost")
    Single<NewPostResponse> newPost(@Body NewPostRequest request);

    @POST("suggest")
    Single<SuggestResponse> suggest(@Body SuggestRequest request);

    @POST("facebooklogin")
    Single<FacebookLoginResponse> facebookLogin(@Body FacebookLoginRequest request);

    @POST("logout")
    Single<LogoutResponse> logout(@Body LogoutRequest request);
}
