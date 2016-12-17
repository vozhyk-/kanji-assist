package re.neutrino.kanji_assist;

import android.app.assist.AssistStructure;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static re.neutrino.kanji_assist.TextExtractor.getSelectedText;

@RunWith(AndroidJUnit4.class)
public class TextExtractorTest extends AssistTest {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setThisAssistant();
    }

    @Test
    public void testExtract_emptyStructure() throws Exception {
        final AssistStructure empty = new AssistStructure();

        assertThat(getSelectedText(empty), nullValue());
    }

    @Test
    public void testExtract_textEdit() throws UiObjectNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        startActivityAsNewTask(new Intent(context,
                re.neutrino.kanji_assist.TestExtractTextViewActivity.class));

        final UiObject edit = device.findObject(new UiSelector()
                .resourceId("re.neutrino.kanji_assist:id/editText"));
        edit.setText("first second");
        edit.longClickTopLeft();

        requestAssist();
    }

    private void requestAssist() throws UiObjectNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        longPressHome();
    }

    private void longPressHome() throws UiObjectNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        throw new Error("Not implemented");
    }
}
