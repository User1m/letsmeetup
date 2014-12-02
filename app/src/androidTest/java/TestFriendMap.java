import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.FriendMapActivty;
import com.robotium.solo.Solo;

/**
 * Created by ClaudiusThaBeast on 12/1/14.
 */
public class TestFriendMap extends ActivityInstrumentationTestCase2<FriendMapActivty> {


    private Solo solo;

    public TestFriendMap() {
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
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.enterText(0, "3pm");
    }

    public void testClearWhere() {
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(0);
    }


    public void testEnterWhen() {
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.enterText(1, "The Union");
    }

    public void testClearWhen() {
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(1);
    }

    public void testEnterWhereAndWhen() {
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.enterText(0, "3pm");
        solo.enterText(1, "The Union");
    }

    public void testClearWhereAndWhen() {
        solo.assertCurrentActivity("FriendMap", FriendMapActivty.class);
        solo.clearEditText(0);
        solo.clearEditText(1);
    }

    public void testSubmitMeetup() {
//        assertNotNull(FriendMapActivty.class);
        solo.assertCurrentActivity("You are in FriendMap", FriendMapActivty.class);
        solo.enterText(0, "3pm");
        solo.enterText(1, "The Union");
        solo.clickOnButton(0);
        solo.waitForText("You're message has been sent!!");
        solo.assertCurrentActivity("You're message was sent", FriendMapActivty.class);
    }


}
