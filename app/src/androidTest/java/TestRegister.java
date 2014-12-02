import android.test.ActivityInstrumentationTestCase2;

import com.mbembac.letsmeetup.CreateAccountActivity;
import com.mbembac.letsmeetup.LoginSignupActivity;
import com.mbembac.letsmeetup.LostPasswordActivity;
import com.mbembac.letsmeetup.Welcome;
import com.robotium.solo.Solo;


public class TestRegister extends ActivityInstrumentationTestCase2<CreateAccountActivity> {


    private Solo solo;

    public TestRegister() {
        super(CreateAccountActivity.class);
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


    public void testEmptyRegistration() {
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.clearEditText(4);
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to Welcome with empty stuff",  CreateAccountActivity.class);
    }
    public void testAlreadyTakenUsername(){
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.clearEditText(4);
        solo.enterText(0, "Jon");
        solo.enterText(1, "Ruben");
        solo.enterText(2, Double.toString(Math.random()));
        solo.enterText(3, "aaa");
        solo.enterText(4, Double.toString(Math.random()));
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to welcome with same username",  CreateAccountActivity.class);

    }
    public void testNewUsername(){
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        solo.clearEditText(3);
        solo.clearEditText(4);
        solo.enterText(0, "Jon");
        solo.enterText(1, "Ruben");
        solo.enterText(2, "a" + Integer.toString((int) (Math.random() * 1000000) / 1) + "@gmail.com");
        solo.enterText(3, "a" + Integer.toString((int) (Math.random() * 1000000) /1 ));
        solo.enterText(4, "a" + Integer.toString((int) (Math.random() * 1000000) /1 ));
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Went to welcome with same username",  Welcome.class);

    }




}
