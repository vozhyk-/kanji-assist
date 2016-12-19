package re.neutrino.kanji_assist;


import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.view.View;

class TextExtractor {
    private AnyAssistStructure structure;

    public TextExtractor(AnyAssistStructure structure) {
        this.structure = structure;
    }

    @Nullable
   public ScreenText getSelectedText() {
        return walkWindows(new Walker() {
            @Override
            public ScreenText run(AnyAssistStructure.AnyViewNode node, Point position, int depth) {
                if (node.getTextSelectionStart() == -1 ||
                        node.getTextSelectionStart() == node.getTextSelectionEnd()) {
                    return null;
                }

                final String text = node.getText().subSequence(
                        node.getTextSelectionStart(),
                        node.getTextSelectionEnd())
                        .toString();
                return new ScreenText(text, position);
            }
        });
    }

    @Nullable
    private ScreenText walkWindows(Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AnyAssistStructure.AnyWindowNode win = structure.getWindowNodeAt(i);
            final ScreenText found = walkViews(win.getRootViewNode(), walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.AnyViewNode node, Walker walker) {
        return walkViews(node, new Point(0, 0), walker, 0);
    }

    @Nullable
    private static ScreenText walkViews(
            AnyAssistStructure.AnyViewNode node, Point position, Walker walker, int depth) {
        if (node.getVisibility() != View.VISIBLE)
            return null;

        position = new Point(
                position.x + node.getLeft(),
                position.y + node.getTop());

        ScreenText found = walker.run(node, position, depth);
        if (found != null)
            return found;

        for (int i = 0; i < node.getChildCount(); i++) {
            found = walkViews(node.getChildAt(i), position, walker, depth + 1);
            if (found != null)
                return found;
        }

        return null;
    }

    public ScreenText getTouchedText(PointF touchLocation) {
        return new ScreenText("a", new Point(200, 200));
    }

    private interface Walker {
        ScreenText run(AnyAssistStructure.AnyViewNode node, Point position, int depth);
    }
}
