package re.neutrino.kanji_assist;


import android.app.assist.AssistStructure;
import android.view.View;

class TextExtractor {
    public static float x;
    public static float y;
    static String getSelectedText(AssistStructure structure) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AssistStructure.WindowNode win =
                    structure.getWindowNodeAt(i);
            final String selected = walkViews(win.getRootViewNode(), 0, 0, 0);
            if (selected != null) {
                return selected;
            }
        }
        return null;
    }

    private static String walkViews(AssistStructure.ViewNode node, float x, float y, int depth) {
        if (node.getVisibility() != View.VISIBLE)
            return null;

        if (node.getTextSelectionStart() != -1 &&
                node.getTextSelectionStart() != node.getTextSelectionEnd()) {
            TextExtractor.x = x + node.getLeft();
            TextExtractor.y = y + node.getTop();
            return node.getText().subSequence(
                        node.getTextSelectionStart(),
                        node.getTextSelectionEnd())
                    .toString();
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            final String selected = walkViews(node.getChildAt(i), x + node.getLeft(), y + node.getTop(), depth + 1);
            if (selected != null)
                return selected;
        }

        return null;
    }
}
