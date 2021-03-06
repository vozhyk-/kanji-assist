/*
 * Copyright (C) 2016-2017 Witaut Bajaryn, Aleksander Mistewicz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package re.neutrino.kanji_assist.text_extractor;

import android.app.assist.AssistStructure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import re.neutrino.kanji_assist.BasicTest;
import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.assist_structure.FakeAssistStructure;
import re.neutrino.kanji_assist.assist_structure.RealAssistStructure;
import re.neutrino.kanji_assist.AssistStructureDebugUtil;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class TextExtractorUnitTest extends BasicTest {

    private AssistStructureDebugUtil util;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        util = new AssistStructureDebugUtil(context);
    }

    @Test
    public void getSelectedText_emptyStructure() throws Exception {
        final RealAssistStructure structure =
                RealAssistStructure.createFrom(new AssistStructure());
        final TextExtractor t = new TextExtractor(structure);

        Assert.assertThat(t.getSelectedText(), nullValue());
    }

    @Test
    public void getSelectedText_nothing() throws Exception {
        final FakeAssistStructure structure =
                util.readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        Assert.assertThat(t.getSelectedText(), nullValue());
    }

    // Selected text in an EditText
    @Test
    public void getSelectedText_textEdit() throws Exception {
        final FakeAssistStructure structure =
                util.readAssistStructure(R.raw.text_edit);

        final ScreenText sel = new TextExtractor(structure).getSelectedText();
        assertThat(sel,
                is(new ScreenText("first", new Rect(42, 252, 592, 370))));
    }

    // 4 kanji of different colors in a row, in Chrome
    @Test
    public void getTouchedText_colorful() throws Exception {
        final FakeAssistStructure structure =
                util.readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        assertThat(t.getTouchedText(new PointF(400, 320)),
                is(new ScreenText("錯", new Rect(373, 276, 430, 353))));
        assertThat(t.getTouchedText(new PointF(460, 320)),
                is(new ScreenText("視", new Rect(430, 276, 487, 353))));
        assertThat(t.getTouchedText(new PointF(515, 320)),
                is(new ScreenText("入", new Rect(488, 276, 545, 353))));
        assertThat(t.getTouchedText(new PointF(575, 320)),
                is(new ScreenText("門", new Rect(545, 276, 602, 353))));
    }

    @Test
    public void getTouchedText_emptySpace() throws Exception {
        final FakeAssistStructure structure =
                util.readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        assertThat(t.getTouchedText(new PointF(830, 80)),
                nullValue());
    }

    // Touching empty space when there is a big node with invisible text
    @Test
    public void getTouchedText_bigParentNodeWithText_emptySpace() throws Exception {
        final FakeAssistStructure structure =
                util.readAssistStructure(R.raw.colorful);
        final TextExtractor t = new TextExtractor(structure);

        assertThat(t.getTouchedText(new PointF(100, 1625)),
                is(new ScreenText("錯視入門", new Rect(0, 210, 1081, 16016))));
    }

}