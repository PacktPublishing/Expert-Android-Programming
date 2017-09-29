package com.androcid.zomato.retro;

import com.androcid.zomato.util.Constant;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Androcid on 23-11-2015.
 */
public class RetroInterface {

    private static final RestAdapter.LogLevel LOG_LEVEL = Constant.DEBUG ?
            RestAdapter.LogLevel.FULL
            : RestAdapter.LogLevel.NONE
    ;

     //WEB SERVER URL
     //"http://192.168.2.95";//"http://192.168.2.95/zomato/";
    //public static final String SERVER_URL ="http://www.kholdepoll.com";
    public static final String SERVER_URL ="http://www.unexploredgoa.com";

    //PROFILE IMAGE URL
    public static final String IMAGE_URL = SERVER_URL+"/zomato/images/";

    //CONTENT IMAGE URL
    public static final String VERSION = ":5002/";//"v1/";
    public static final String TARGET_URL = SERVER_URL + VERSION;

    private static RestAdapter getRestAdapter() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader(Constant.TAG_TOKEN, Constant.APP_TOKEN);
            }
        };

        return new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setEndpoint(RetroInterface.TARGET_URL)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
    }

    public static ZomatoRestApi getZomatoRestApi() {
        return getRestAdapter().create(ZomatoRestApi.class);
    }

    public interface ZomatoRestApi {

        @FormUrlEncoded
        @POST("/loginSocial")
        public void loginSocial(
                @FieldMap Map<String, String> params,
                Callback<UserResponse> cb
        );

        @FormUrlEncoded
        @POST("/loginNormal")
        public void loginNormal(
                @FieldMap Map<String, String> params,
                Callback<UserResponse> cb
        );

        @FormUrlEncoded
        @POST("/registerNormal")
        public void registerNormal(
                @FieldMap Map<String, String> params,
                Callback<UserResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRecommendedRestaurants")
        public void getRecommendedRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getAllRestaurants")
        public void getAllRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRecentRestaurants")
        public void getRecentRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getCollectionRestaurants")
        public void getCollectionRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.COLLECTION_ID) String collection_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getSearchRestaurants")
        public void getSearchRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.DATA) String data,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRestaurantDetails")
        public void getRestaurantDetails(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String id,
                Callback<RestaurantDetailResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserSearch")
        public void getUserSearch(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.NAME) String name,
                Callback<UserListResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserSuggestion")
        public void getUserSuggestion(
                @Field(Constant.USER_ID) String user_id,
                Callback<UserListResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserFollowers")
        public void getUserFollowers(
                @Field(Constant.ID) String _id,
                @Field(Constant.USER_ID) String user_id,
                Callback<UserListResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserFollowing")
        public void getUserFollowing(
                @Field(Constant.ID) String _id,
                @Field(Constant.USER_ID) String user_id,
                Callback<UserListResponse> cb
        );

        @FormUrlEncoded
        @POST("/followUser")
        public void followUser(
                @Field(Constant.ID) String _id,
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.STATUS) String status,
                Callback<NormalResponse> cb
        );

        @FormUrlEncoded
        @POST("/getNearbyLocation")
        public void getNearbyLocation(
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<UserLocationResponse> cb
        );

        @FormUrlEncoded
        @POST("/getSearchLocation")
        public void getSearchLocation(
                @Field(Constant.DATA) String data,
                Callback<UserLocationResponse> cb
        );

        @FormUrlEncoded
        @POST("/getPopularLocation")
        public void getPopularLocation(
                @Field(Constant.DATA) String data,
                Callback<UserLocationResponse> cb
        );

        @FormUrlEncoded
        @POST("/addUserAddress")
        public void addUserAddress(
                @FieldMap Map<String, String> params,
                Callback<UserAddressResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserAddress")
        public void getUserAddress(
                @Field(Constant.USER_ID) String user_id,
                Callback<UserAddressResponse> cb
        );

        @FormUrlEncoded
        @POST("/setUserHandle")
        public void setUserHandle(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.HANDLE) String handle,
                Callback<NormalResponse> cb
        );

        @FormUrlEncoded
        @POST("/setUserLocation")
        public void setUserLocation(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LOCATION_ID) String location_id,
                Callback<NormalResponse> cb
        );

        @FormUrlEncoded
        @POST("/setUserDetails")
        public void setUserDetails(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.NAME) String name,
                @Field(Constant.PHONE_NO) String phone_no,
                @Field(Constant.DESCRIPTION) String description,
                Callback<NormalResponse> cb
        );

        @FormUrlEncoded
        @POST("/deleteUserAddress")
        public void deleteUserAddress(
                @Field(Constant.ADDRESS_ID) String address_id,
                Callback<UserAddressResponse> cb
        );

        @FormUrlEncoded
        @POST("/getAllCuisine")
        public void getAllCuisine(
                @Field(Constant.ID) String id,
                Callback<CuisineResponse> cb
        );

        @FormUrlEncoded
        @POST("/getAllOffers")
        public void getAllOffers(
                @Field(Constant.ID) String id,
                Callback<OfferResponse> cb
        );

        @FormUrlEncoded
        @POST("/createCollection")
        public void createCollection(
                @FieldMap Map<String, String> params,
                Callback<NormalResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRecommendedCollection")
        public void getRecommendedCollection(
                @Field(Constant.USER_ID) String id,
                Callback<CollectionResponse> cb
        );

        @FormUrlEncoded
        @POST("/getSavedCollection")
        public void getSavedCollection(
                @Field(Constant.USER_ID) String id,
                Callback<CollectionResponse> cb
        );

        @FormUrlEncoded
        @POST("/getFollowCollection")
        public void getFollowCollection(
                @Field(Constant.USER_ID) String id,
                Callback<CollectionResponse> cb
        );

        @FormUrlEncoded
        @POST("/getMyCollection")
        public void getMyCollection(
                @Field(Constant.USER_ID) String id,
                Callback<CollectionResponse> cb
        );

        @FormUrlEncoded
        @POST("/saveCollection")
        public void saveCollection(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.COLLECTION_ID) String collection_id,
                @Field(Constant.STATUS) String status,
                Callback<SaveResponse> cb
        );

        @FormUrlEncoded
        @POST("/bookmarkRestaurant")
        public void bookmarkRestaurant(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_id,
                @Field(Constant.STATUS) String status,
                Callback<SaveResponse> cb
        );

        @FormUrlEncoded
        @POST("/beenthereRestaurant")
        public void beenthereRestaurant(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_id,
                @Field(Constant.STATUS) String status,
                Callback<SaveResponse> cb
        );

        @FormUrlEncoded
        @POST("/getBeenThereRestaurants")
        public void getBeenThereRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getBookmarkedRestaurants")
        public void getBookmarkedRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<RestaurantResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserDineline")
        public void getUserDineline(
                @Field(Constant.USER_ID) String user_id,
                Callback<DinelineResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserReviews")
        public void getUserReviews(
                @Field(Constant.USER_ID) String user_id,
                Callback<ProfileReviewResponse> cb
        );

        @FormUrlEncoded
        @POST("/getUserPhotos")
        public void getUserPhotos(
                @Field(Constant.USER_ID) String user_id,
                Callback<RestaurantImageResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRestaurantPhotos")
        public void getRestaurantPhotos(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_branch_id,
                Callback<RestaurantImageResponse> cb
        );

        @FormUrlEncoded
        @POST("/getRestaurantMenu")
        public void getRestaurantMenu(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_branch_id,
                Callback<RestaurantImageResponse> cb
        );

        //TODO new
        @FormUrlEncoded
        @POST("/getMealRestaurants")
        public void getMealRestaurants(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.LATITUDE) String latitude,
                @Field(Constant.LONGITUDE) String longitude,
                Callback<MealDetailResponse> cb
        );

        @FormUrlEncoded
        @POST("/addRestaurantPhotos")
        public void addRestaurantPhotos(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_ID) String restaurant_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_branch_id,
                @Field(Constant.CONTENT) String content,
                Callback<MealDetailResponse> cb
        );

        @FormUrlEncoded
        @POST("/addRestaurantReview")
        public void addRestaurantReview(
                @Field(Constant.USER_ID) String user_id,
                @Field(Constant.RESTAURANT_ID) String restaurant_id,
                @Field(Constant.RESTAURANT_BRANCH_ID) String restaurant_branch_id,
                @Field(Constant.RATING) String rating,
                @Field(Constant.DATA) String data,
                @Field(Constant.CONTENT) String content,
                Callback<MealDetailResponse> cb
        );

        @FormUrlEncoded
        @POST("/getProfileDetails")
        public void getProfileDetails(
                @Field(Constant.ID) String _id,
                @Field(Constant.USER_ID) String user_id,
                Callback<ProfileDetailResponse> cb
        );

    }

    //TODO FOR IMAGE
    private static RestAdapter getImageApiAdapter() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader(Constant.TAG_TOKEN, Constant.APP_TOKEN);
            }
        };

        return new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setEndpoint(RetroInterface.TARGET_URL)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
    }

    public static ImageApi getImageApi() {
        return getImageApiAdapter().create(ImageApi.class);
    }

    public interface ImageApi {

        @FormUrlEncoded
        @POST("/uploadPhoto")
        void uploadImageService(
                @Field(Constant.CONTENT) String content,
                @Field(Constant.NAME) String name,
                Callback<NormalResponse> cb);
    }

}
