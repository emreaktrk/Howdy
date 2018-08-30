package istanbul.codify.muudy.ui.compose;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import org.greenrobot.eventbus.EventBus;

import istanbul.codify.muudy.model.event.TwitterShareEvent;

/**
 * Created by egesert on 30.08.2018.
 */

public class TwitterShareResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        TwitterShareEvent twitterShareEvent = new TwitterShareEvent();

        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            twitterShareEvent.result = TwitterShareEvent.TwitterShare.SUCCESS;
            final Long tweetId = intentExtras.getLong(TweetUploadService.EXTRA_TWEET_ID);
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            // failure
            twitterShareEvent.result = TwitterShareEvent.TwitterShare.FAILURE;
            final Intent retryIntent = intentExtras.getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            // cancel
            twitterShareEvent.result = TwitterShareEvent.TwitterShare.CANCEL;
        }


    }
}
