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

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class BasicTest {
    protected Instrumentation instr;
    protected Context globalContext;
    protected Context context;
    protected UiDevice device;

    protected String appName;

    @Before
    public void setUp() throws Exception {
        instr = getInstrumentation();
        globalContext = instr.getContext();
        context = instr.getTargetContext();
        device = UiDevice.getInstance(instr);

        appName = context.getString(R.string.app_name);
    }

    protected void startActivityAsNewTask(Intent intent) {
        globalContext.startActivity(
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
