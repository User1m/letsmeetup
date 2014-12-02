import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.LoginSignupActivity;
import com.mbembac.letsmeetup.MainActivity;
import com.mbembac.letsmeetup.Welcome;
import com.robotium.solo.Solo;



public class TestWelcome extends ActivityInstrumentationTestCase2<Welcome> {


    private Solo solo;

    public TestWelcome() {
        super(Welcome.class);
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

    public void testLoggingOut() {

        assertNotNull(MainActivity.class);
        assertNotNull(Welcome.class);

        solo.assertCurrentActivity("Wrong Activity", Welcome.class);
        solo.clickOnButton("Log Out");
        solo.assertCurrentActivity("Didn't get to login after logging out", LoginSignupActivity.class);
    }



}
