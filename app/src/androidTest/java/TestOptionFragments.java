import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.FragmentMainPage;
import com.robotium.solo.Solo;

/**
 * Created by ClaudiusThaBeast on 12/1/14.
 */
public class TestOptionFragments extends ActivityInstrumentationTestCase2<FragmentMainPage> {

    private Solo solo;

    public TestOptionFragments() {
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
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);
        solo.enterText(0, "aaa");
        solo.clickOnButton(0);
        solo.clickInList(0);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("Added Friend Failed", FragmentMainPage.class);
        solo.clearEditText(0);
    }

    public void testRefreshFriendNoFriend(){
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);
        solo.clearEditText(0);
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Friend", FragmentMainPage.class);
    }


    public void testAddFriendNoClickOnName(){
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);
        solo.clearEditText(0);
        solo.enterText(0, "aaa");
        solo.clickOnButton(0);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("FindFriend", FragmentMainPage.class);
        solo.clearEditText(0);
    }

    public void testAddFriendNoFriend(){
        solo.clearEditText(0);
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);
        solo.clickOnButton(1);
        solo.assertCurrentActivity("Find Friend", FragmentMainPage.class);

    }


}
