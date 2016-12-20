package re.neutrino.kanji_assist.assist_structure;

import android.app.assist.AssistStructure;

public class RealAssistStructure implements AnyAssistStructure {
    private final AssistStructure structure;

    public RealAssistStructure(AssistStructure structure) {
        this.structure = structure;
    }

    @Override
    public int getWindowNodeCount() {
        return structure.getWindowNodeCount();
    }

    @Override
    public AnyAssistStructure.WindowNode getWindowNodeAt(int i) {
        return new WindowNode(structure.getWindowNodeAt(i));
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
        public CharSequence getText() {
            return node.getText();
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
        public int getChildCount() {
            return node.getChildCount();
        }

        @Override
        public ViewNode getChildAt(int index) {
            return new ViewNode(node.getChildAt(index));
        }
    }
}
