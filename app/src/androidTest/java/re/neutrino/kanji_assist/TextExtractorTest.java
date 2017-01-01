package re.neutrino.kanji_assist;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TextExtractorTest extends AssistTest {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setThisAssistant();
    }

    @Test
    public void getSelectedText_textEdit() throws Exception {
        startActivityAsNewTask(new Intent(context,
                TestExtractTextEditActivity.class));

        final UiObject edit = device.findObject(new UiSelector()
                .resourceId("re.neutrino.kanji_assist:id/editText"));
        edit.setText("first second");
        edit.longClickTopLeft();

        requestAssist();
    }

    private void requestAssist() throws Exception {
        final UiObject callAssist = device.findObject(new UiSelector()
                .resourceId("re.neutrino.kanji_assist:id/request_assist_button"));
        callAssist.click();
    }
}
