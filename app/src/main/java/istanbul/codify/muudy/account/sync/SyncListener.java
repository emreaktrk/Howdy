package istanbul.codify.muudy.account.sync;

import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.model.User;

public interface SyncListener extends EventSupport {

    void onSync(User me);

}
