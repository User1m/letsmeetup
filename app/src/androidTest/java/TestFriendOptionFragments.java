import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.FragmentMainPage;
import com.robotium.solo.Solo;

/**
 * Created by ClaudiusThaBeast on 12/1/14.
 */
public class TestFriendOptionFragments extends ActivityInstrumentationTestCase2<FragmentMainPage> {

    private Solo solo;

    public TestFriendOptionFragments() {
        super(FragmentMainPage.class);
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


    public void testFindFriend(){
        solo.waitForFragmentById(0);
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);
    }



}
