import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.CreateAccountActivity;
import com.mbembac.letsmeetup.LoginSignupActivity;
import com.mbembac.letsmeetup.LostPasswordActivity;
import com.mbembac.letsmeetup.Welcome;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.mbembac.letsmeetup.MainActivity;
import com.robotium.solo.Solo;


public class TestLogin extends ActivityInstrumentationTestCase2<LoginSignupActivity> {


    private Solo solo;

    public TestLogin() {
        super(LoginSignupActivity.class);
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


    public void testEmptyLoggingIn(){
        solo.clearEditText(0);
        solo.clearEditText(0);
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to Login with empty stuff", LoginSignupActivity.class);
        solo.enterText(0, "zzz");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to Login with empty stuff", LoginSignupActivity.class);
        solo.clearEditText(0);
        solo.enterText(1, "zzz");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to Login with empty stuff", LoginSignupActivity.class);
        solo.clearEditText(1);

    }

    public void testBadPassword(){
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.enterText(0, "zzz");
        solo.enterText(1, "zzzlskfjaklsjfalskdjfalskjdf");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to Login with empty stuff", LoginSignupActivity.class);
    }

    public void testRegisterButton(){
        solo.clickOnButton(1);
        solo.waitForActivity(CreateAccountActivity.class);
        solo.assertCurrentActivity("Didn't get to the register button", CreateAccountActivity.class);
    }

    public void testLostPassword(){
        solo.clickOnButton(2);
        solo.waitForActivity(LostPasswordActivity.class);
        solo.assertCurrentActivity("Didn't get to the lost password button", LostPasswordActivity.class);
    }


    public void testGoodLoggingIn() {
        solo.clearEditText(1);
        solo.clearEditText(0);
        solo.enterText(0, "aaa");
        solo.enterText(1, "aaa");

        solo.clickOnButton(0);
        solo.waitForActivity(Welcome.class);
        solo.assertCurrentActivity("Went to Login with empty stuff", Welcome.class);
        solo.clickOnButton(1);
        solo.waitForActivity(LoginSignupActivity.class);
        solo.assertCurrentActivity("Went to Login with empty stuff", LoginSignupActivity.class);


    }



}
