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

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

public class AssistStructureWalker {
    private AnyAssistStructure structure;
    private String TAG = getClass().getName();

    public AssistStructureWalker(AnyAssistStructure structure) {
        this.structure = structure;
    }

    @Nullable
    public ScreenText walkWindows(Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AnyAssistStructure.WindowNode win = structure.getWindowNodeAt(i);

            final int left = win.getLeft();
            final int top = win.getTop();
            final Rect rect = new Rect(left, top,
                    left + win.getWidth(), top + win.getHeight());

            Log.d(TAG, "Window " + win.getTitle() + "@" + rect.toShortString());

            final ScreenText found = walkViews(
                    win.getRootViewNode(), rect, rect, walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.ViewNode node, Rect rect, Rect windowRect, Walker walker) {
        return walkViews(node, rect, windowRect, walker, 0);
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.ViewNode node, Rect rect, Rect windowRect, Walker walker, int depth) {
        if (node.getVisibility() != View.VISIBLE)
            return null;

        final int left = rect.left + node.getLeft();
        final int top = rect.top + node.getTop();
        rect = new Rect(
                left, top,
                left + node.getWidth(), top + node.getHeight());

        if (!Rect.intersects(rect, windowRect))
            return null;

        AbsoluteViewNode absNode = new AbsoluteViewNode(node, rect, depth);

        ScreenText found = walker.run(absNode);
        if (found != null)
            return found;

        for (int i = 0; i < node.getChildCount(); i++) {
            rect.offset(-node.getScrollX(), -node.getScrollY());

            found = walkViews(node.getChildAt(i), rect, windowRect, walker, depth + 1);
            if (found != null)
                return found;
        }

        return null;
    }

    private static String nodeVisibilityString(AnyAssistStructure.ViewNode node) {
        String visibility = "unknown visibility";
        if (node.getVisibility() == View.INVISIBLE)
            visibility = "INVISIBLE";
        else if (node.getVisibility() == View.GONE)
            visibility = "GONE";
        return visibility;
    }

    public interface Walker {
        ScreenText run(AbsoluteViewNode absNode);
    }

    public static class AbsoluteViewNode {
        private final AnyAssistStructure.ViewNode node;
        private final Rect rect;
        private final int depth;

        public AbsoluteViewNode(AnyAssistStructure.ViewNode node, Rect rect,
                                int depth) {
            this.node = node;
            this.rect = rect;
            this.depth = depth;
        }

        public AnyAssistStructure.ViewNode getNode() {
            return node;
        }

        public Rect getRect() {
            return rect;
        }

        public int getDepth() {
            return depth;
        }

        @Override
        public String toString() {
            final AnyAssistStructure.ViewNode node = getNode();

            StringBuilder msg = new StringBuilder()
                    .append(this.getTextToDisplay())
                    .append("@")
                    .append(getRect().toShortString())
                    .append(", class name: ")
                    .append(node.getClassName())
                    .append(", text size: ")
                    .append(node.getTextSize())
                    .append(", scroll: ")
                    .append(new Point(node.getScrollX(), node.getScrollY()))
                    .append(", elevation: ")
                    .append(node.getElevation())
                    .append(", alpha: ")
                    .append(node.getAlpha());

            if (node.getTextLineBaselines() != null)
                msg = msg.append(", line baselines: ")
                        .append(Arrays.toString(node.getTextLineBaselines()));

            if (node.getTextLineCharOffsets() != null)
                msg = msg.append(", line char offsets: ")
                        .append(Arrays.toString(node.getTextLineCharOffsets()));

            if (node.getTransformation() != null)
                msg = msg.append(", transformation: ")
                        .append(node.getTransformation());

            return msg.toString();
        }

        public String getLogOffset() {
            String result = "";
            for (int i = 0; i < getDepth(); i++)
                result += " ";
            return result;
        }

        public CharSequence getTextToDisplay() {
            final AnyAssistStructure.ViewNode node = getNode();

            final CharSequence text = node.getText();
            if (isNotEmpty(text))
                return text;

            return node.getHint();
        }

        public boolean hasText() {
            return isNotEmpty(getTextToDisplay());
        }

        public static boolean isNotEmpty(CharSequence seq) {
            return seq != null && !seq.toString().isEmpty();
        }
    }
}
