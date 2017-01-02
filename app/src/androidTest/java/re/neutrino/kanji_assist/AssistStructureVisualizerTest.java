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

import org.junit.Test;
import org.junit.runner.RunWith;

import re.neutrino.kanji_assist.assist_structure.FakeAssistStructure;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AssistStructureVisualizerTest extends BasicTest {

    @Test
    public void assistStructureVisualizerTest() throws Exception {
        final FakeAssistStructure structure = new AssistStructureDebugUtil(context)
                .readAssistStructure(R.raw.settings_with_baselines);

        startActivityAsNewTask(
                new Intent(context, AssistStructureVisualizerActivity.class)
                        .putExtra("structure", structure));
    }

}
