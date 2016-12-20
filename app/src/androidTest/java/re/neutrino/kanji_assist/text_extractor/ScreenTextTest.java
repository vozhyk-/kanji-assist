package re.neutrino.kanji_assist.text_extractor;

import android.graphics.Rect;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ScreenTextTest {
    @Test
    public void equals_true() throws Exception {
        assertTrue(new ScreenText("a", new Rect(10, 20, 30, 40)).equals(
                new ScreenText("a", new Rect(10, 20, 30, 40))));
    }

    @Test
    public void equals_differentPosition() throws Exception {
        assertFalse(new ScreenText("a", new Rect(10, 20, 30, 40)).equals(
                new ScreenText("a", new Rect(20, 20, 40, 40))));
    }

    @Test
    public void equals_differentText() throws Exception {
        assertFalse(new ScreenText("a", new Rect(10, 20, 30, 40)).equals(
                new ScreenText("b", new Rect(10, 20, 30, 40))));
    }

}