package re.neutrino.kanji_assist;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Instrumentation instr;
    private Context context;
    private Context globalContext;
    private UiDevice device;
    private String appName;

    @Before
    public void setUp() throws Exception {
        instr = getInstrumentation();
        context = instr.getTargetContext();
        globalContext = instr.getContext();
        device = UiDevice.getInstance(instr);

        appName = context.getString(R.string.app_name);
    }

    @Test
    public void testAssistSettingsOpened() throws UiObjectNotFoundException {
        setAssistant("Google App");

        globalContext.startActivity(new Intent(context, MainActivity.class));

        final UiObject assistSettingsTitle = device.findObject(
                byTextAndType("Assist & voice input", TextView.class)
                        .packageName("com.android.settings"));
        assertNotNull(assistSettingsTitle);

        setAssistantOnly(appName);
    }

    private void setThisAssistant() throws UiObjectNotFoundException {
        setAssistant(appName);
    }

    private void setAssistant(String assistant) throws UiObjectNotFoundException {
        openAssistSettings();
        setAssistantOnly(assistant);
    }

    private void openAssistSettings() {
        globalContext.startActivity(
                new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));
    }

    // Assist Settings must be opened to call this
    private void setAssistantOnly(String assistant) throws UiObjectNotFoundException {
        // TODO Disable animations in code

        final UiObject selectAssist = device.findObject(byTextAndType(
                "Assist app", TextView.class));
        selectAssist.click();

        final UiObject appOption = device.findObject(byTextAndType(
                assistant, TextView.class));
        appOption.click();

        try {
            final UiObject agree = device.findObject(byTextAndType(
                    "AGREE", Button.class));
            agree.click();
        } catch (UiObjectNotFoundException e) {
        }

        device.pressBack();
    }

    private UiSelector byTextAndType(String text, Class type) {
        return new UiSelector()
                .text(text)
                .className(type);
    }
}
