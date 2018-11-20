package istanbul.codify.monju.account.sync;

import istanbul.codify.monju.EventSupport;
import istanbul.codify.monju.model.User;

public interface SyncListener extends EventSupport {

    void onSync(User me);

}
