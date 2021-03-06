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

import android.content.ComponentName;
import android.graphics.Matrix;
import android.os.Bundle;

public interface AnyAssistStructure {
    int getWindowNodeCount();
    WindowNode getWindowNodeAt(int i);

    ComponentName getActivityComponent();

    interface WindowNode {
        ViewNode getRootViewNode();
        int getDisplayId();

        int getLeft();
        int getTop();
        int getWidth();
        int getHeight();

        CharSequence getTitle();
    }

    interface ViewNode {
        int getVisibility();
        boolean isAssistBlocked();

        String getClassName();

        int getLeft();
        int getTop();
        int getWidth();
        int getHeight();
        int getScrollX();
        int getScrollY();

        CharSequence getText();
        String getHint();
        CharSequence getContentDescription();

        int getTextSelectionStart();
        int getTextSelectionEnd();
        int[] getTextLineBaselines();
        int[] getTextLineCharOffsets();
        float getTextSize();

        float getAlpha();
        float getElevation();
        int getTextColor();
        int getTextBackgroundColor();
        int getTextStyle();
        Matrix getTransformation();

        int getId();
        String getIdEntry();
        String getIdPackage();
        String getIdType();

        Bundle getExtras();

        boolean isAccessibilityFocused();
        boolean isActivated();
        boolean isFocusable();
        boolean isFocused();
        boolean isSelected();

        boolean isEnabled();
        boolean isClickable();
        boolean isContextClickable();
        boolean isLongClickable();
        boolean isCheckable();
        boolean isChecked();

        int getChildCount();
        ViewNode getChildAt(int i);
    }
}
