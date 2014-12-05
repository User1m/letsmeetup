import
        android.test.ActivityInstrumentationTestCase2;
import com.mbembac.letsmeetup.Welcome;
import com.mbembac.letsmeetup.FriendMapActivty;
import com.robotium.solo.Solo;

/**
 * Created by ClaudiusThaBeast on 12/1/14.
 */
public class TestMapFriend extends ActivityInstrumentationTestCase2<FriendMapActivty> {


    private Solo solo;

    public TestMapFriend() {
        super(FriendMapActivty.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testEnterWhere() {
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.enterText(0, "3pm");
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(0);

    }

    public void testClearWhere() {
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(0);

    }


    public void testEnterWhen() {
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.enterText(1, "The Union");
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(0);

    }

    public void testSubmitMeetup() {
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.assertCurrentActivity("You are in FriendMap", FriendMapActivty.class);
        solo.enterText(0, "3pm");
        solo.enterText(1, "The Union");
    }


}
