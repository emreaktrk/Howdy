package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;

public abstract class DeepLink implements Serializable {

    protected RemoteMessage message;

    DeepLink(@NonNull RemoteMessage message) {
        this.message = message;
    }

    public abstract void navigate(Activity activity);

    final @Nullable
    Long getItemId() {
        String itemId = message.getData().get("itemid");
        return TextUtils.isEmpty(itemId) ? null : Long.valueOf(itemId);
    }
    final @Nullable
    String getNotificationMessage() {
        String notificationMessage = message.getData().get("pushtitle");
        return notificationMessage;
    }

    final @Nullable
    String getPlaceName() {
        String placeName = "";
        placeName = new Gson().fromJson(message.getData().get("extraData"),JsonObject.class).get("palce_name").toString();
        if (placeName.contains("\"")){
            placeName = placeName.replace("\"","");
        }
        return placeName;
    }

    final @Nullable
    String getAnswerHiWord() {
        String answerHiWord = "";
        answerHiWord = new Gson().fromJson(message.getData().get("extraData"),JsonObject.class).get("notification_answerhi_word_text").toString();
        answerHiWord = answerHiWord.replace("\"","");
        return answerHiWord;
    }

    final @Nullable
    String getAnswerHiWordImage() {
        String wordImage = "";
        wordImage = new Gson().fromJson(message.getData().get("extraData"),JsonObject.class).get("notification_answerhi_word_img").toString();
        wordImage = wordImage.replace("\"","");
        return wordImage;
    }
}
