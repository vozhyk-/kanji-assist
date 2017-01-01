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
import android.provider.Settings;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;
import android.widget.TextView;

class AssistTest extends BasicTest {

    protected void setThisAssistant() throws UiObjectNotFoundException {
        setAssistant(appName);
    }

    protected void setAssistant(String assistant) throws UiObjectNotFoundException {
        openAssistSettings();
        setAssistantOnly(assistant);
    }

    private void openAssistSettings() {
        startActivityAsNewTask(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));
    }

    protected void startActivityAsNewTask(Intent intent) {
        globalContext.startActivity(
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    // Assist Settings must be opened to call this
    protected void setAssistantOnly(String assistant) throws UiObjectNotFoundException {
        // TODO Disable animations in code

        final UiObject selectAssist = device.findObject(byTextAndType(
                "Assist app", TextView.class));
        selectAssist.click();

        final UiObject appOption = device.findObject(byTextAndType(
                assistant, TextView.class));
        appOption.click();

        try {
            final UiObject agree = device.findObject(byTextAndType(
                    "AGREE", Button.class));
            agree.click();
        } catch (UiObjectNotFoundException ignored) {
        }

        device.pressBack();
    }

    protected UiSelector byTextAndType(String text, Class type) {
        return new UiSelector()
                .text(text)
                .className(type);
    }
}
