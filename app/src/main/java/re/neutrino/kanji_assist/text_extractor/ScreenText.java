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

import android.graphics.Rect;

public class ScreenText {
    private String text;
    private Rect rect;

    public ScreenText(String text, Rect rect) {
        this.text = text;
        this.rect = rect;
    }

    @Override
    public String toString() {
        return getText() + " @ " + getRect();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenText) {
            final ScreenText other = (ScreenText) obj;
            return getText().equals(other.getText()) &&
                    getRect().equals(other.getRect());
        }
        return super.equals(obj);
    }

    public String getText() {
        return text;
    }

    public Rect getRect() {
        return rect;
    }
}
