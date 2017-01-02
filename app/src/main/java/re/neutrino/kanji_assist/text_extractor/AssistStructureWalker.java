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
import android.support.annotation.Nullable;
import android.view.View;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

public class AssistStructureWalker {
    private AnyAssistStructure structure;

    public AssistStructureWalker(AnyAssistStructure structure) {
        this.structure = structure;
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.ViewNode node, Rect rect, Walker walker) {
        return walkViews(node, rect, walker, 0);
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.ViewNode node, Rect rect, Walker walker, int depth) {
        if (node.getVisibility() != View.VISIBLE)
            return null;

        final int left = rect.left + node.getLeft();
        final int top = rect.top + node.getTop();
        rect = new Rect(
                left, top,
                left + node.getWidth(), top + node.getHeight());

        ScreenText found = walker.run(node, rect, depth);
        if (found != null)
            return found;

        for (int i = 0; i < node.getChildCount(); i++) {
            found = walkViews(node.getChildAt(i), rect, walker, depth + 1);
            if (found != null)
                return found;
        }

        return null;
    }

    public static String getLogOffset(int depth) {
        String result = "";
        for (int i = 0; i < depth; i++)
            result += " ";
        return result;
    }

    @Nullable
    public ScreenText walkWindows(Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AnyAssistStructure.WindowNode win = structure.getWindowNodeAt(i);

            final int left = win.getLeft();
            final int top = win.getTop();
            final Rect rect = new Rect(left, top,
                    left + win.getWidth(), top + win.getHeight());

            final ScreenText found = walkViews(
                    win.getRootViewNode(), rect, walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public interface Walker {
        ScreenText run(AnyAssistStructure.ViewNode node, Rect position, int depth);
    }
}
