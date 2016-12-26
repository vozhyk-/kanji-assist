package re.neutrino.kanji_assist.text_extractor;


import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

public class TextExtractor {

    private final AssistStructureWalker structureWalker;

    public TextExtractor(AnyAssistStructure structure) {
        structureWalker = new AssistStructureWalker(structure);
    }

    @Nullable
    public ScreenText getSelectedText() {
        return structureWalker.walkWindows(new AssistStructureWalker.Walker() {
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

        final AssistStructureWalker.Walker walker = new AssistStructureWalker.Walker() {

            private String TAG = getClass().getName();

            @Override
            public ScreenText run(AnyAssistStructure.ViewNode node, Rect rect, int depth) {
                final RectF rectF = new RectF(rect);
                if (!rectF.contains(touch.x, touch.y) ||
                        node.getText() == null ||
                        node.getText().toString().isEmpty()) {
                    return null;
                }

                final String PREFIX = AssistStructureWalker.getLogOffset(depth);
                Log.d(TAG, PREFIX +
                        "Node: " +
                        node.getText() + " @ " +
                        rect.toShortString());

                if (mostNarrow[0] != null) {
                    if (area(rect) > area(mostNarrow[0].getRect())) {
                        Log.d(TAG, PREFIX + "ignoring larger node ^^^");
                        return null;
                    }

                    final boolean ok = rect.intersect(mostNarrow[0].getRect());
                    if (!ok)
                        Log.w(TAG, "Found a view containing the touch that doesn't intersect with the other such views!");
                }

                mostNarrow[0] = makeScreenText(node.getText(), rect);
                Log.d(TAG, PREFIX + "most narrow: " + mostNarrow[0]);
                Log.d(TAG, PREFIX + "most narrow: " +
                        node.getHint() + " " +
                        node.getContentDescription() + " " +
                        "\n" +
                        node.isAccessibilityFocused() + " " +
                        node.isActivated() + " " +
                        node.isFocusable() + " " +
                        node.isFocused() + " " +
                        node.isSelected() + " " +
                        "\n" +
                        node.isEnabled() + " " +
                        node.isClickable() + " " +
                        node.isContextClickable() + " " +
                        node.isLongClickable() + " " +
                        node.isCheckable() + " " +
                        node.isChecked());
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
