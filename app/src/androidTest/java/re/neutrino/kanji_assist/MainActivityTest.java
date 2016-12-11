package re.neutrino.kanji_assist;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends AssistTest {

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
}
