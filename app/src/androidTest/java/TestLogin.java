import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.LoginSignupActivity;
import com.mbembac.letsmeetup.Welcome;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.mbembac.letsmeetup.MainActivity;
import com.robotium.solo.Solo;


public class TestLogin extends ActivityInstrumentationTestCase2<MainActivity> {


    private Solo solo;

    public TestLogin() {
        super(MainActivity.class);
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



    public void testLoggingIn() {
        assertNotNull(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertNotNull("ParseUser isn't the current user", ParseUser.getCurrentUser());
        getActivity();
        solo.waitForActivity(Welcome.class);
        solo.assertCurrentActivity("Not Welcome", Welcome.class);

       // solo.assertCurrentActivity("Not Login", LoginSignupActivity.class);
    }



}
