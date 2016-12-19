package re.neutrino.kanji_assist;

import android.app.assist.AssistStructure;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class TextExtractorUnitTest extends BasicTest {

    @Test
    public void getSelectedText_emptyStructure() throws Exception {
        final RealAssistStructure structure =
                new RealAssistStructure(new AssistStructure());
        final TextExtractor t = new TextExtractor(structure);

        Assert.assertThat(t.getSelectedText(), nullValue());
    }

    @Test
    public void getSelectedText_nothing() throws Exception {
        final FakeAssistStructure structure =
                readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        Assert.assertThat(t.getSelectedText(), nullValue());
    }

    // Selected text in an EditText
    @Test
    public void getSelectedText_textEdit() throws Exception {
        final FakeAssistStructure structure =
                readAssistStructure(R.raw.text_edit);

        final ScreenText sel = new TextExtractor(structure).getSelectedText();
        assertThat(sel,
                is(new ScreenText("first", new Point(42, 252))));
    }

    // 4 kanji of different colors in a row, in Chrome
    @Test
    public void getTouchedText_colorful() throws Exception {
        final FakeAssistStructure structure =
                readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        assertThat(t.getTouchedText(new PointF(400, 320)),
                is(new ScreenText("錯", new Point(373, 276))));
        assertThat(t.getTouchedText(new PointF(460, 320)),
                is(new ScreenText("視", new Point(430, 276))));
        assertThat(t.getTouchedText(new PointF(515, 320)),
                is(new ScreenText("入", new Point(488, 276))));
        assertThat(t.getTouchedText(new PointF(575, 320)),
                is(new ScreenText("門", new Point(545, 276))));
    }

    @Test
    public void getTouchedText_emptySpace() throws Exception {
        final FakeAssistStructure structure =
                readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        assertThat(t.getTouchedText(new PointF(100, 1625)),
                nullValue());
    }

    private FakeAssistStructure readAssistStructure(int res) throws Exception {
        try (InputStream input = context.getResources()
                .openRawResource(res)) {
            try (InputStreamReader reader = new InputStreamReader(input)) {
                return new Gson().fromJson(reader,
                        FakeAssistStructure.class);
            }
        }
    }

}