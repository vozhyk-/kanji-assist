package re.neutrino.kanji_assist;

import android.graphics.Point;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ScreenTextTest {
    @Test
    public void equals_true() throws Exception {
        assertTrue(new ScreenText("a", new Point(10, 20)).equals(
                new ScreenText("a", new Point(10, 20))));
    }

    @Test
    public void equals_differentPosition() throws Exception {
        assertFalse(new ScreenText("a", new Point(10, 20)).equals(
                new ScreenText("a", new Point(20, 20))));
    }

    @Test
    public void equals_differentText() throws Exception {
        assertFalse(new ScreenText("a", new Point(10, 20)).equals(
                new ScreenText("b", new Point(10, 20))));
    }

}