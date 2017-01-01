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
