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


import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker.AbsoluteViewNode;

public class TextExtractor {

    private final AssistStructureWalker structureWalker;

    public TextExtractor(AnyAssistStructure structure) {
        structureWalker = new AssistStructureWalker(structure);
    }

    @Nullable
    public ScreenText getSelectedText() {
        return structureWalker.walkWindows(new AssistStructureWalker.Walker() {
            @Override
            public ScreenText run(AbsoluteViewNode absNode) {
                final AnyAssistStructure.ViewNode node = absNode.getNode();

                if (node.getTextSelectionStart() == -1 ||
                        node.getTextSelectionStart() == node.getTextSelectionEnd()) {
                    return null;
                }

                final CharSequence text = node.getText().subSequence(
                        node.getTextSelectionStart(),
                        node.getTextSelectionEnd());
                return makeScreenText(text, absNode.getRect());
            }
        });
    }

    @Nullable
    public ScreenText getTouchedText(final PointF touch) {
        final ScreenText[] mostNarrow = {null};

        final AssistStructureWalker.Walker walker = new AssistStructureWalker.Walker() {

            private String TAG = getClass().getName();

            @Override
            public ScreenText run(AbsoluteViewNode absNode) {
                final Rect rect = absNode.getRect();

                final RectF rectF = new RectF(rect);
                if (!rectF.contains(touch.x, touch.y) ||
                        !absNode.hasText()) {
                    return null;
                }

                final String logOffset = absNode.getLogOffset();
                Log.d(TAG, logOffset + "Node: " + absNode);

                if (mostNarrow[0] != null) {
                    if (area(rect) > area(mostNarrow[0].getRect())) {
                        Log.d(TAG, logOffset + "ignoring larger node ^^^");
                        return null;
                    }

                    final boolean ok = rect.intersect(mostNarrow[0].getRect());
                    if (!ok)
                        Log.w(TAG, "Found a view containing the touch that doesn't intersect with the other such views!");
                }

                mostNarrow[0] = makeScreenText(absNode.getTextToDisplay(), rect);
                Log.d(TAG, logOffset + "most narrow: " + mostNarrow[0]);
                Log.d(TAG, logOffset + "most narrow: " + absNode);
                return null;
            }

            private int area(Rect rect) {
                return rect.width() * rect.height();
            }
        };
        structureWalker.walkWindows(walker);

        return mostNarrow[0];
    }

    @NonNull
    private ScreenText makeScreenText(CharSequence text, Rect rect) {
       return new ScreenText(text.toString(), rect);
    }

}
