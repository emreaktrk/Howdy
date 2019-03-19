package istanbul.codify.monju.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;

/**
 * Created by egesert on 19.07.2018.
 */

public class PicassoHelper {
    public static Picasso picassoInstance = null;

    public PicassoHelper(Context context, AppCompatImageView imageView, String path){
               Picasso.with(context)
                .load(BuildConfig.URL + path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(BuildConfig.URL + path)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public PicassoHelper(Context context, CircleImageView imageView, String path){

        Picasso.with(context)
                .load(BuildConfig.URL + path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(BuildConfig.URL + path)
                                .placeholder(R.drawable.ic_avatar)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public static void setImageWithPlaceHolder(CircleImageView imageView, String path){
        Picasso.with(imageView.getContext())
                .load(path == null ? null : BuildConfig.URL + path)
                .placeholder(R.mipmap.ic_launcher_round)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(imageView.getContext())
                                .load(BuildConfig.URL + path)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public static void setImageWithPlaceHolder(CircleImageView imageView, String path,int placeHolderImage){
        Picasso.with(imageView.getContext())
                .load(path == null ? null : BuildConfig.URL + path)
                .placeholder(placeHolderImage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(imageView.getContext())
                                .load(BuildConfig.URL + path)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
    public static void setImage(CircleImageView imageView, String path){
        Picasso.with(imageView.getContext())
                .load(path == null ? null : BuildConfig.URL + path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(imageView.getContext())
                                .load(BuildConfig.URL + path)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
    public static void setImage(AppCompatImageView imageView, String path){
        Picasso.with(imageView.getContext())
                .load(path == null ? null : BuildConfig.URL + path)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(imageView.getContext())
                                .load(BuildConfig.URL + path)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
}
