package istanbul.codify.muudy.api;

import io.reactivex.Single;
import istanbul.codify.muudy.api.pojo.request.*;
import istanbul.codify.muudy.api.pojo.response.*;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServices {

    @POST("login")
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Single<RegisterResponse> register(@Body RegisterRequest request);

    @POST("lostpass")
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

    @POST("getuserweeklytop")
    Single<GetUserWeeklyTopResponse> getUserWeeklyTop(@Body GetUserWeeklyTopRequest request);

    @POST("getmessages")
    Single<GetMessagesResponse> getMessages(@Body GetMessagesRequest request);

    @POST("getsinglepost")
    Single<GetSinglePostResponse> getSinglePost(@Body GetSinglePostRequest request);

    @POST("getcomments")
    Single<GetCommentsResponse> getComments(@Body GetCommentsRequest request);

    @POST("uploadimage")
    Single<UploadImageResponse> uploadImage(@Body UploadImageRequest request);

    @Multipart
    @POST("uploadvideo")
    Call<UploadVideoResponse> uploadVideo(@Part MultipartBody.Part part);

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

    @POST("getfollowers")
    Single<GetFollowersResponse> getFollowers(@Body GetFollowersRequest request);

    @POST("getphonefriends")
    Single<GetPhoneFriendsResponse> getPhoneFriends(@Body GetPhoneFriendsRequest request);

    @POST("getactivities")
    Single<GetActivitiesResponse> getActivities();

    @POST("getactivitystats")
    Single<GetActivityStatsResponse> getActivityStats(@Body GetActivityStatsRequest request);

    @POST("getactivityposts")
    Single<GetActivityPostsResponse> getActivityPosts(@Body GetActivityPostsRequest request);

    @POST("updateprofile")
    Single<UpdateProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    @POST("createposttext")
    Single<CreateTextPostResponse> createPostText(@Body CreatePostTextRequest request);

    @POST("newpost")
    Single<NewPostResponse> newPost(@Body NewPostRequest request);

    @POST("deletepost")
    Single<DeletePostResponse> deletePost(@Body DeletePostRequest request);

    @POST("deletecomment")
    Single<DeleteCommentResponse> deleteComment(@Body DeleteCommentRequest request);

    @POST("suggest")
    Single<SuggestResponse> suggest(@Body SuggestRequest request);

    @POST("facebooklogin")
    Single<FacebookLoginResponse> facebookLogin(@Body FacebookLoginRequest request);

    @POST("getfacebookfriends")
    Single<FacebookFriendsResponse> facebookFriends(@Body FacebookFriendsRequest request);

    @POST("logout")
    Single<LogoutResponse> logout(@Body LogoutRequest request);

    @POST("sayhi")
    Single<SayHiResponse> sayHi(@Body SayHiRequest request);

    @POST("sayhi")
    Single<AnswerHiResponse> answerHi(@Body AnswerHiRequest request);

    @POST("sendfeedback")
    Single<SendFeedbackResponse> sendFeedback(@Body SendFeedbackRequest request);

    @POST("gettopemojisonmap")
    Single<GetTopEmojisOnMapResponse> getTopEmojisOnMapRequest(@Body GetTopEmojisOnMapRequest request);

    @POST("updatepushsettings")
    Single<UpdatePushNotificationResponse> updatePushSettings(@Body UpdatePushSettingsRequest request);

    @POST("aroundusers")
    Single<AroundUsersResponse> aroundUsers(@Body AroundUsersRequest request);

    @POST("follow")
    Single<FollowResponse> follow(@Body FollowRequest request);

    @POST("unfollow")
    Single<UnfollowResponse> unfollow(@Body UnfollowRequest request);

    @POST("sendfollowrequest")
    Single<SendFollowResponse> sendFollowRequest(@Body SendFollowRequest request);

    @POST("cancelfollowrequest")
    Single<CancelFollowResponse> cancelFollowRequest(@Body CancelFollowRequest request);

    @POST("answerfollowrequest")
    Single<AnswerFollowResponse> answerFollowRequest(@Body AnswerFollowRequest request);

    @POST("banuser")
    Single<BanUserResponse> banUser(@Body BanUserRequest request);

    @POST("report")
    Single<ReportResponse> report(@Body ReportRequest request);

    @POST("savesendnotificationonpost")
    Single<SaveSendNotificationOnPostResponse> saveSendNotificationOnPost(@Body SaveSendNotificationOnPostRequest request);

    @POST("deletesendnotificationonpost")
    Single<DeleteSendNotificationOnPostResponse> deleteSendNotificationOnPost(@Body DeleteSendNotificationOnPostRequest request);

    @POST("givepoint")
    Single<GivePointResponse> givePoint(@Body GivePointRequest request);

    @POST("getweeklytopusers")
    Single<WeeklyTopsUsersResponse> getWeeklyTopUsers(@Body WeeklyTopUsersRequest request);

    @POST("getrecomennedplaces")
    Call<GetRecommendedPlacesResponse> getRecommendedPlaces(@Body GetRecommendedPlacesRequest request);

    @POST("deleteMessages")
    Single<DeleteUserMessagesResponse> deleteUserMessages(@Body DeleteUserMessagesRequest request);

    @POST("seelikers")
    Single<SeeLikersResponse> seeLikers(@Body SeeLikersRequest request);
}
