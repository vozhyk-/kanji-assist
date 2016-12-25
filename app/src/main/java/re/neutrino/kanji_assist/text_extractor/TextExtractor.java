package re.neutrino.kanji_assist.text_extractor;


import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
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

            private String TAG = getClass().getName();

            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect rect, int depth) {
                final RectF rectF = new RectF(rect);
                if (!rectF.contains(touch.x, touch.y) ||
                        node.getText() == null ||
                        node.getText().toString().isEmpty()) {
                    return null;
                }

                final String PREFIX = getLogOffset(depth);
                Log.d(TAG, PREFIX +
                        "Node: " +
                        node.getText() + " @ " +
                        rect.toShortString());

                if (mostNarrow[0] != null) {
                    final boolean ok = rect.intersect(mostNarrow[0].getRect());
                    if (!ok)
                        Log.w(TAG, "Found a view containing the touch that doesn't intersect with the other such views!");
                }

                mostNarrow[0] = makeScreenText(node.getText(), rect);
                Log.d(TAG, PREFIX + "most narrow: " + mostNarrow[0]);
                return null;
            }
        };
        walkWindows(walker);

        return mostNarrow[0];
    }

    @NonNull
    private ScreenText makeScreenText(CharSequence text, Rect rect) {
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
