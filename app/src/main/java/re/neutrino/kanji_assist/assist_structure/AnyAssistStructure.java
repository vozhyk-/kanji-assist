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
