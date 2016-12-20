package re.neutrino.kanji_assist.text_extractor;


import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

public class TextExtractor {
    private AnyAssistStructure structure;

    public TextExtractor(AnyAssistStructure structure) {
        this.structure = structure;
    }

    @Nullable
    public ScreenText getSelectedText() {
        return walkWindows(new Walker() {
            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect rect, int depth) {
                if (node.getTextSelectionStart() == -1 ||
                        node.getTextSelectionStart() == node.getTextSelectionEnd()) {
                    return null;
                }

                final CharSequence text = node.getText().subSequence(
                        node.getTextSelectionStart(),
                        node.getTextSelectionEnd());
                return makeScreenText(text, rect);
            }
        });
    }

    @Nullable
    public ScreenText getTouchedText(final PointF touch) {
        final ScreenText[] mostNarrow = {null};

        final Walker walker = new Walker() {
            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect rect, int depth) {
                if (rect.contains((int) touch.x, (int) touch.y)) {
                    Log.d(getClass().getName(), getLogOffset(depth) +
                            rect.toShortString());

                    /*if (mostNarrow == null ||
                            mostNarrow)*/
                    mostNarrow[0] = makeScreenText(node.getText(), rect);
                }
                return null;
            }
        };
        walkWindows(walker);

        return mostNarrow[0];
    }

    @Nullable
    private ScreenText makeScreenText(CharSequence text, Rect rect) {
        if (text == null)
            return null;
        return new ScreenText(text.toString(), rect);
    }

    private String getLogOffset(int depth) {
        String result = "";
        for (int i = 0; i < depth; i++)
            result += " ";
        return result;
    }

    @Nullable
    private ScreenText walkWindows(Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AnyAssistStructure.WindowNode win = structure.getWindowNodeAt(i);
            final ScreenText found = walkViews(win.getRootViewNode(), walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.ViewNode node, Walker walker) {
        return walkViews(node, new Rect(0, 0, 0, 0), walker, 0);
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

    private interface Walker {
        ScreenText run(AnyAssistStructure.ViewNode node, Rect position, int depth);
    }
}
