package re.neutrino.kanji_assist;

import android.app.assist.AssistStructure;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static re.neutrino.kanji_assist.TextExtractor.getSelectedText;

@RunWith(AndroidJUnit4.class)
public class TextExtractorTest {
    @Test
    public void withEmptyStructure() throws Exception {
        final AssistStructure empty = new AssistStructure();

        assertThat(getSelectedText(empty), nullValue());
    }
}
