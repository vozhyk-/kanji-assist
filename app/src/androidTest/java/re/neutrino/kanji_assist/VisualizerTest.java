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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VisualizerTest extends BasicTest {

    @Rule
    public ActivityTestRule<VisualizerActivity> rule =
            new ActivityTestRule<VisualizerActivity>
                    (VisualizerActivity.class, true, false);

    @Test
    public void visualizer_edittext_hint() {
        rule.launchActivity(intentWithFixture("edittext_hint"));

        // when no text is entered, hint is used
        onView(withText("場所を追加")).check(matches(isDisplayed()));

        // when some text is entered, hint is not used
        onView(withText("test title")).check(matches(isDisplayed()));
        onView(withText("タイトルを入力")).check(doesNotExist());
    }

    private Intent intentWithFixture(String fixtureName) {
        return new Intent(context, VisualizerActivity.class)
                .putExtra(VisualizerActivity.FIXTURE_NAME_KEY,
                        fixtureName);
    }
}
