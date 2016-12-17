package re.neutrino.kanji_assist;


import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.view.View;

class TextExtractor {
    @Nullable
    static ScreenText getSelectedText(AssistStructure structure) {
        return walkWindows(structure, new Walker() {
            @Override
            public ScreenText run(ViewNode node, Point position, int depth) {
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
    private static ScreenText walkWindows(AssistStructure structure, Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final WindowNode win = structure.getWindowNodeAt(i);
            final ScreenText found = walkViews(win.getRootViewNode(), walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Nullable
    private static ScreenText walkViews(
        ViewNode node, Walker walker) {
        return walkViews(node, new Point(0, 0), walker, 0);
    }

    @Nullable
    private static ScreenText walkViews(
            ViewNode node, Point position, Walker walker, int depth) {
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

    private interface Walker {
        ScreenText run(ViewNode node, Point position, int depth);
    }
}
