package com.peanutswifi;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity>  {

    private Solo solo ;
    private final int TIMEOUT = 30 * 1000;
    private String SSID = "peanuts_automatic_test_suite";
    private String KEY = "12345678";
    private final String SSID_5G = "peanuts_automatic_test_suite-5G";
    private final String CHINESE_SSID = "业界良心_花生自动化";
    private final String CHINESE_SSID_5G = "业界良心_花生自动化-5G";
//    original special_ssid :   `~!@#$%^&*() =+\|]}[{'";:/?.>,<
    private final String SPECIAL_SSID = "`~!@#$%^&*() =+\\|]}[{'\";:/?.>,<";
    private final String SPECIAL_SSID_5G = "`~!@#$%^&*() =+\\|]}[{'\";:/?.-5G";
    private final String GUEST_SSID = "peanuts_guest";
//   original special_ssid : `~!@#$%^&*() =+\|]}[{'";:/?.>,<`~!@#$%^&*() =+\|]}[{'";:/?.>,<1
    private final String SPECIAL_KEY = "`~!@#$%^&*() =+\\|]}[{'\";:/?.>,<`~!@#$%^&*() =+\\|]}[{'\";:/?.>,<1";
    private final String CLEAR = "onClearConfig";
    private final String SHUTDOWN = "onShutDownWifi";
    private final String SUCCESS = "onSuccess";
    private final String FINISH = "onFinished : true";
    private final String NO_EXIST = "Specified SSID isnot exist!";
    private final String IPERF = "iperf";
    private final int IPERF_TIME = 3600;
    private final int IPERF_TIME2 = 120;
    private final int REPEAT_ASSOC = 100;

    public ApplicationTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (MyTestRunner.SSID != null) {
            SSID = MyTestRunner.SSID;
        }
        if (MyTestRunner.KEY != null) {
            KEY = MyTestRunner.KEY;
        }
        solo = new Solo(this.getInstrumentation(), this.getActivity());
    }

    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void test_iperf() throws Exception{
        if(solo.isToggleButtonChecked(IPERF)) {
            solo.sleep(IPERF_TIME * 1000);
            assertEquals(true, true);
        } else {
            solo.clickOnToggleButton(IPERF);
            solo.sleep(IPERF_TIME * 1000);
            assertEquals(true, true);
        }
    }

    public void test_iperf2() throws Exception{
        if(solo.isToggleButtonChecked(IPERF)) {
            solo.sleep(IPERF_TIME2 * 1000);
            assertEquals(true, true);
        } else {
            solo.clickOnToggleButton(IPERF);
            solo.sleep(IPERF_TIME2 * 1000);
            assertEquals(true, true);
        }
    }

    public void test_assoc_clear_sta() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.pressSpinnerItem(0, 0);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_clear_sta() throws Exception {
        int count = 0;
        int actual = 0;

        while (count < REPEAT_ASSOC){
            solo.clickOnButton(1);
            solo.waitForText(CLEAR);
            solo.waitForText(SHUTDOWN);
            solo.clearEditText(0);
            solo.enterText(0, SSID);
            solo.pressSpinnerItem(0, 0);
            solo.clickOnButton(0);
            if (solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT)){
                actual++;
            } else if(solo.waitForText("Connecting to ", 0, TIMEOUT)) {
                solo.goBack();
            }
            count++;
        }
        assertEquals("Not all association were successful", REPEAT_ASSOC, actual);
    }

    public void test_assoc_psk2_sta() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0,SSID);
        solo.clearEditText(1);
        solo.enterText(1,KEY);
        solo.pressSpinnerItem(0, 3);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_psk2_sta_keyspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, SPECIAL_KEY);
        solo.pressSpinnerItem(0, 3);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_psk2_sta_ssidspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SPECIAL_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 3);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_psk2_sta_ssidchinese() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, CHINESE_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 3);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_psk2_sta() throws Exception {
        int count = 0;
        int actual = 0;

        while (count < REPEAT_ASSOC){
            solo.clickOnButton(1);
            solo.waitForText(CLEAR);
            solo.waitForText(SHUTDOWN);
            solo.clearEditText(0);
            solo.enterText(0, SSID);
            solo.clearEditText(1);
            solo.enterText(1, KEY);
            solo.pressSpinnerItem(0, 3);
            solo.clickOnButton(0);
            if (solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT)){
                actual++;
            } else if(solo.waitForText("Connecting to ", 0, TIMEOUT)) {
                solo.goBack();
            }
            count++;
        }
        assertEquals("Not all association were successful", REPEAT_ASSOC, actual);
    }

    public void test_assoc_psk_sta() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 1);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_psk_sta_keyspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, SPECIAL_KEY);
        solo.pressSpinnerItem(0, 1);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_psk_sta_ssidspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SPECIAL_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 1);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }


    public void test_assoc_psk_sta_ssidchinese() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, CHINESE_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 1);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_psk_sta() throws Exception {
        int count = 0;
        int actual = 0;

        while (count < REPEAT_ASSOC){
            solo.clickOnButton(1);
            solo.waitForText(CLEAR);
            solo.waitForText(SHUTDOWN);
            solo.clearEditText(0);
            solo.enterText(0, SSID);
            solo.clearEditText(1);
            solo.enterText(1, KEY);
            solo.pressSpinnerItem(0, 1);
            solo.clickOnButton(0);
            if (solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT)){
                actual++;
            } else if(solo.waitForText("Connecting to ", 0, TIMEOUT)) {
                solo.goBack();
            }
            count++;
        }
        assertEquals("Not all association were successful", REPEAT_ASSOC, actual);
    }

    public void test_assoc_tkippsk2_sta() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 4);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk2_sta_keyspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, SPECIAL_KEY);
        solo.pressSpinnerItem(0, 4);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk2_sta_ssidspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SPECIAL_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 4);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk2_sta_ssidchinese() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, CHINESE_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 4);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_tkippsk2_sta() throws Exception {
        int count = 0;
        int actual = 0;

        while (count < REPEAT_ASSOC){
            solo.clickOnButton(1);
            solo.waitForText(CLEAR);
            solo.waitForText(SHUTDOWN);
            solo.clearEditText(0);
            solo.enterText(0, SSID);
            solo.clearEditText(1);
            solo.enterText(1, KEY);
            solo.pressSpinnerItem(0, 4);
            solo.clickOnButton(0);
            if (solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT)){
                actual++;
            } else if(solo.waitForText("Connecting to ", 0, TIMEOUT)) {
                solo.goBack();
            }
            count++;
        }
        assertEquals("Not all association were successful", REPEAT_ASSOC, actual);
    }

    public void test_assoc_tkippsk_sta() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 2);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk_sta_keyspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, SPECIAL_KEY);
        solo.pressSpinnerItem(0, 2);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk_sta_ssidspec() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, SPECIAL_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 2);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_assoc_tkippsk_sta_ssidchinese() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0, CHINESE_SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 2);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_tkippsk_sta() throws Exception {
        int count = 0;
        int actual = 0;

        while (count < REPEAT_ASSOC){
            solo.clickOnButton(1);
            solo.waitForText(CLEAR);
            solo.waitForText(SHUTDOWN);
            solo.clearEditText(0);
            solo.enterText(0, SSID);
            solo.clearEditText(1);
            solo.enterText(1, KEY);
            solo.pressSpinnerItem(0, 2);
            solo.clickOnButton(0);
            if (solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT)){
                actual++;
            } else if(solo.waitForText("Connecting to ", 0, TIMEOUT)) {
                solo.goBack();
            }
            count++;
        }
        assertEquals("Not all association were successful", REPEAT_ASSOC, actual);
    }

    public void test_ssidhide() throws Exception {
        boolean expected =true;
        solo.clickOnButton(1);
        solo.waitForText(CLEAR);
        solo.waitForText(SHUTDOWN);
        solo.clearEditText(0);
        solo.enterText(0,SSID);
        solo.pressSpinnerItem(0,0);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(NO_EXIST, 0, TIMEOUT);
        assertEquals("Specified SSID should be hidden .", expected, actual);
    }
}