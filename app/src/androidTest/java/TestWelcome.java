import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.MainActivity;
import com.mbembac.letsmeetup.Welcome;
import com.parse.ParseUser;
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



    public void testLoggingIn() {
        assertNotNull(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity", Welcome.class);
       /* assertNotNull("ParseUser isn't the current user", ParseUser.getCurrentUser());
        getActivity();
        solo.waitForActivity(Welcome.class);
        solo.assertCurrentActivity("Not Welcome", Welcome.class);*/

       // solo.assertCurrentActivity("Not Login", LoginSignupActivity.class);
    }



}
