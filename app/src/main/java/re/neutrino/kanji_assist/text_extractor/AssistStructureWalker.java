package re.neutrino.kanji_assist.text_extractor;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

public class AssistStructureWalker {
    private AnyAssistStructure structure;

    public AssistStructureWalker(AnyAssistStructure structure) {
        this.structure = structure;
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

    public String getLogOffset(int depth) {
        String result = "";
        for (int i = 0; i < depth; i++)
            result += " ";
        return result;
    }

    @Nullable
    public ScreenText walkWindows(Walker walker) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            final AnyAssistStructure.WindowNode win = structure.getWindowNodeAt(i);
            final ScreenText found = walkViews(win.getRootViewNode(), walker);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public interface Walker {
        ScreenText run(AnyAssistStructure.ViewNode node, Rect position, int depth);
    }
}
