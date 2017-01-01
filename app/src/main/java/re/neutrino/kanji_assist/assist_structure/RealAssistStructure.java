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

package re.neutrino.kanji_assist.assist_structure;

import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.graphics.Matrix;
import android.os.Bundle;

public class RealAssistStructure implements AnyAssistStructure {
    private final AssistStructure structure;

    private RealAssistStructure(AssistStructure structure) {
        this.structure = structure;
    }

    public static RealAssistStructure createFrom(AssistStructure structure) {
        return structure != null ?
                new RealAssistStructure(structure) :
                null;
    }

    @Override
    public int getWindowNodeCount() {
        return structure.getWindowNodeCount();
    }

    @Override
    public AnyAssistStructure.WindowNode getWindowNodeAt(int i) {
        return new WindowNode(structure.getWindowNodeAt(i));
    }

    @Override
    public ComponentName getActivityComponent() {
        return structure.getActivityComponent();
    }

    private class WindowNode implements AnyAssistStructure.WindowNode {
        private final AssistStructure.WindowNode window;

        WindowNode(AssistStructure.WindowNode window) {
            this.window = window;
        }

        @Override
        public AnyAssistStructure.ViewNode getRootViewNode() {
            return new ViewNode(window.getRootViewNode());
        }

        @Override
        public int getDisplayId() {
            return window.getDisplayId();
        }

        @Override
        public int getLeft() {
            return window.getLeft();
        }

        @Override
        public int getTop() {
            return window.getTop();
        }

        @Override
        public int getWidth() {
            return window.getWidth();
        }

        @Override
        public int getHeight() {
            return window.getHeight();
        }

        @Override
        public CharSequence getTitle() {
            return window.getTitle();
        }
    }

    private class ViewNode implements AnyAssistStructure.ViewNode {
        private final AssistStructure.ViewNode node;

        ViewNode(AssistStructure.ViewNode node) {
            this.node = node;
        }

        @Override
        public int getVisibility() {
            return node.getVisibility();
        }

        @Override
        public boolean isAssistBlocked() {
            return node.isAssistBlocked();
        }

        @Override
        public String getClassName() {
            return node.getClassName();
        }

        @Override
        public int getLeft() {
            return node.getLeft();
        }

        @Override
        public int getTop() {
            return node.getTop();
        }

        @Override
        public int getWidth() {
            return node.getWidth();
        }

        @Override
        public int getHeight() {
            return node.getHeight();
        }

        @Override
        public int getScrollX() {
            return node.getScrollX();
        }

        @Override
        public int getScrollY() {
            return node.getScrollY();
        }

        @Override
        public CharSequence getText() {
            return node.getText();
        }

        @Override
        public String getHint() {
            return node.getHint();
        }

        @Override
        public CharSequence getContentDescription() {
            return node.getContentDescription();
        }

        @Override
        public int getTextSelectionStart() {
            return node.getTextSelectionStart();
        }

        @Override
        public int getTextSelectionEnd() {
            return node.getTextSelectionEnd();
        }

        @Override
        public int[] getTextLineBaselines() {
            return node.getTextLineBaselines();
        }

        @Override
        public int[] getTextLineCharOffsets() {
            return node.getTextLineCharOffsets();
        }

        @Override
        public float getTextSize() {
            return node.getTextSize();
        }

        @Override
        public float getAlpha() {
            return node.getAlpha();
        }

        @Override
        public float getElevation() {
            return node.getElevation();
        }

        @Override
        public int getTextColor() {
            return node.getTextColor();
        }

        @Override
        public int getTextBackgroundColor() {
            return node.getTextBackgroundColor();
        }

        @Override
        public int getTextStyle() {
            return node.getTextStyle();
        }

        @Override
        public Matrix getTransformation() {
            return node.getTransformation();
        }

        @Override
        public int getId() {
            return node.getId();
        }

        @Override
        public String getIdEntry() {
            return node.getIdEntry();
        }

        @Override
        public String getIdPackage() {
            return node.getIdPackage();
        }

        @Override
        public String getIdType() {
            return node.getIdType();
        }

        @Override
        public Bundle getExtras() {
            return node.getExtras();
        }

        @Override
        public boolean isAccessibilityFocused() {
            return node.isAccessibilityFocused();
        }

        @Override
        public boolean isActivated() {
            return node.isActivated();
        }

        @Override
        public boolean isFocusable() {
            return node.isFocusable();
        }

        @Override
        public boolean isFocused() {
            return node.isFocused();
        }

        @Override
        public boolean isSelected() {
            return node.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return node.isEnabled();
        }

        @Override
        public boolean isClickable() {
            return node.isClickable();
        }

        @Override
        public boolean isContextClickable() {
            return node.isContextClickable();
        }

        @Override
        public boolean isLongClickable() {
            return node.isLongClickable();
        }

        @Override
        public boolean isCheckable() {
            return false;
        }

        @Override
        public boolean isChecked() {
            return false;
        }

        @Override
        public int getChildCount() {
            return node.getChildCount();
        }

        @Override
        public ViewNode getChildAt(int index) {
            return new ViewNode(node.getChildAt(index));
        }
    }
}
