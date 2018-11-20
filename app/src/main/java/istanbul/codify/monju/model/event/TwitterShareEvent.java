package istanbul.codify.monju.model.event;

/**
 * Created by egesert on 30.08.2018.
 */

public class TwitterShareEvent {
    public enum TwitterShare {
        SUCCESS,FAILURE,CANCEL
    }
    public TwitterShare result;
}
