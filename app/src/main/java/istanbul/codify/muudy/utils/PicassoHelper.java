package istanbul.codify.muudy.utils;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import istanbul.codify.muudy.BuildConfig;

/**
 * Created by egesert on 19.07.2018.
 */

public class PicassoHelper {
    public static Picasso picassoInstance = null;

    private PicassoHelper(Context context){
              /*  Picasso.with(context)
                .load(BuildConfig.URL + post.imgpath1)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(none.mImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(holder.itemView.getContext())
                                .load(BuildConfig.URL + post.imgpath1)
                                .into(none.mImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });*/
    }
}
