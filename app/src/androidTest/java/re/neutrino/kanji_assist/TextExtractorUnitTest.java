package re.neutrino.kanji_assist;

import android.app.assist.AssistStructure;
import android.graphics.Point;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.ObjectInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class TextExtractorUnitTest extends BasicTest {

    private ScreenText getSelectedText(AnyAssistStructure structure) {
        return new TextExtractor(structure).getSelectedText();
    }

    @Test
    public void getSelectedText_emptyStructure() throws Exception {
        final RealAssistStructure empty =
                new RealAssistStructure(new AssistStructure());

        Assert.assertThat(getSelectedText(empty), nullValue());
    }

    @Test
    public void getSelectedText_textEdit() throws Exception {
        final FakeAssistStructure structure =
                readFakeAssistStructure(R.raw.text_edit);

        final ScreenText sel = new TextExtractor(structure).getSelectedText();
        assertThat(sel, notNullValue());
        assertThat(sel.getText(), equalTo("first"));
        assertThat(sel.getPosition(), equalTo(new Point(42, 252)));
    }

    private FakeAssistStructure readFakeAssistStructure(int res) throws Exception {
        final InputStream input = context.getResources()
                .openRawResource(res);
        return (FakeAssistStructure)
                new ObjectInputStream(input).readObject();
    }

}