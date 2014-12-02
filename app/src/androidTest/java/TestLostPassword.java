import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.CreateAccountActivity;
import com.mbembac.letsmeetup.LoginSignupActivity;
import com.mbembac.letsmeetup.LostPasswordActivity;
import com.mbembac.letsmeetup.Welcome;
import com.robotium.solo.Solo;


public class TestLostPassword extends ActivityInstrumentationTestCase2<LostPasswordActivity> {


    private Solo solo;

    public TestLostPassword() {
        super(LostPasswordActivity.class);
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


    public void testGoBack() {
        solo.clickOnButton(1);
        solo.waitForActivity(Welcome.class);
        solo.assertCurrentActivity("Got to sign in", LoginSignupActivity.class);
    }

    public void testNoEmail(){
        solo.clearEditText(0);
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Didn't stay in Lost Password", LostPasswordActivity.class);
    }

    public void testBadEmail(){
        solo.clearEditText(0);
        solo.enterText(0, "adlksfjalksdjla");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Didn't stay in Lost Password", LostPasswordActivity.class);

    }

    public void testGoodEmail(){
        solo.clearEditText(0);
        solo.enterText(0, "aaa@aaa.aaa");
        solo.clickOnButton(0);
        solo.waitForActivity(LoginSignupActivity.class);
        solo.assertCurrentActivity("Stayed In Lost Password", LoginSignupActivity.class);

    }


}
