import android.test.ActivityInstrumentationTestCase2;


import com.mbembac.letsmeetup.MainActivity;

import com.parse.ParseUser;
import com.robotium.solo.Solo;
import com.mbembac.letsmeetup.Welcome;



public class Test1 extends ActivityInstrumentationTestCase2<MainActivity> {


    private Solo solo;

    public Test1() {
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


    public void testActivityIsActive() {
        assertNotNull(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }



}
