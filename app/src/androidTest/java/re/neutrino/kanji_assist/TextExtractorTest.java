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
