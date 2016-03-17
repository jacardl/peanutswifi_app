package com.peanutswifi;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity>  {

    private Solo solo ;
    private final int TIMEOUT = 30 * 1000;
    private String SSID = "peanuts";
    private String KEY = "12345678";
    private final String SUCCESS = "onSuccess";
    private final String FINISH = "onFinished : true";
    private final String NO_EXIST = "Specified SSID isnot exist!";
    private final String IPERF = "iperf";
    private final String UPLINK_COMPLETE = "Uplink complete:";
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

    public void test_2g_freq() throws Exception {
        boolean expected =true;
        TextView freqView = (TextView)solo.getView(R.id.cur_freq);
        String freq = (String)freqView.getText();
        String[] freqList = freq.split(":");
        int freqInt = 0;
        try
        {
            freqInt = Integer.parseInt(freqList[1]);
        }
        catch(NumberFormatException NFE)
        {
            System.out.println("格式错误");
        }
        boolean actual = (freqInt > 2000 && freqInt < 3000) ? true : false;
        assertEquals("Current wifi is not 2g", expected, actual);
    }

    public void test_5g_freq() throws Exception {
        boolean expected =true;
        TextView freqView = (TextView)solo.getView(R.id.cur_freq);
        String freq = (String)freqView.getText();
        String[] freqList = freq.split(":");
        int freqInt = 0;
        try
        {
            freqInt = Integer.parseInt(freqList[1]);
        }
        catch(NumberFormatException NFE)
        {
            System.out.println("格式错误");
        }
        boolean actual = (freqInt > 5000 && freqInt < 6000) ? true : false;
        assertEquals("Current wifi is not 5g", expected, actual);
    }

    public void test_speettest() throws Exception {
        solo.clickOnButton("speedtest");
        solo.clickOnButton("start");
        TextView uplink = (TextView)solo.getView(R.id.uplink_speed);
        TextView downlink = (TextView)solo.getView(R.id.downlink_speed);
        if (solo.waitForText(UPLINK_COMPLETE, 0, TIMEOUT)) {
            String[] downRateList = ((String) downlink.getText()).split(" ");
            String[] upRateList = ((String) uplink.getText()).split(" ");
            String downRate = downRateList[2];
            String upRate = upRateList[2];
            fail(String.format("downlink rate: %s KB/s, uplink rate: %s KB/s", downRate, upRate));
        }
    }

    public void test_assoc_clear_sta() throws Exception {
        boolean expected =true;
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.pressSpinnerItem(0, 0);
        while (count < REPEAT_ASSOC){
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
        solo.clearEditText(0);
        solo.enterText(0,SSID);
        solo.clearEditText(1);
        solo.enterText(1,KEY);
        solo.pressSpinnerItem(0, 3);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(SUCCESS, 0, TIMEOUT) && solo.waitForText(FINISH, 0, TIMEOUT);
        assertEquals("Connected to specified SSID is failed.", expected, actual);
    }

    public void test_repeat_assoc_psk2_sta() throws Exception {
        int count = 0;
        int actual = 0;
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 3);
        while (count < REPEAT_ASSOC) {
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 1);
        while (count < REPEAT_ASSOC){
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 4);
        while (count < REPEAT_ASSOC){
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
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
        solo.clearEditText(0);
        solo.enterText(0, SSID);
        solo.clearEditText(1);
        solo.enterText(1, KEY);
        solo.pressSpinnerItem(0, 2);
        while (count < REPEAT_ASSOC){
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
        solo.clearEditText(0);
        solo.enterText(0,SSID);
        solo.pressSpinnerItem(0,0);
        solo.clickOnButton(0);
        boolean actual = solo.waitForText(NO_EXIST, 0, TIMEOUT);
        assertEquals("Specified SSID should be hidden .", expected, actual);
    }
}