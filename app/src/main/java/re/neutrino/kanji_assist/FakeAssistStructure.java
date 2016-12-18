package re.neutrino.kanji_assist;

import java.io.Serializable;
import java.util.ArrayList;

class FakeAssistStructure implements AnyAssistStructure, Serializable {
    private ArrayList<WindowNode> windowNodes = new ArrayList<>();

    public FakeAssistStructure(AnyAssistStructure structure) {
        for (int i = 0; i < structure.getWindowNodeCount(); i++)
            windowNodes.add(new WindowNode(structure.getWindowNodeAt(i)));
    }

    @Override
    public int getWindowNodeCount() {
        return windowNodes.size();
    }

    @Override
    public AnyWindowNode getWindowNodeAt(int i) {
        return windowNodes.get(i);
    }

    private class WindowNode implements AnyWindowNode, Serializable {
        private ViewNode rootViewNode;
        public WindowNode(AnyWindowNode window) {
            this.rootViewNode = new ViewNode(window.getRootViewNode());
        }

        @Override
        public AnyViewNode getRootViewNode() {
            return rootViewNode;
        }
    }

    private class ViewNode implements AnyViewNode, Serializable {
        private int visibility;
        private int left;
        private int top;
        private String text;
        private int textSelectionStart;
        private int textSelectionEnd;

        private ArrayList<ViewNode> children = new ArrayList<>();

        public ViewNode(AnyViewNode node) {
            this.visibility = node.getVisibility();
            this.left = node.getLeft();
            this.top = node.getTop();
            final CharSequence text = node.getText();
            this.text = text != null ? text.toString() : null;
            this.textSelectionStart = node.getTextSelectionStart();
            this.textSelectionEnd = node.getTextSelectionEnd();

            for (int i = 0; i < node.getChildCount(); i++)
                children.add(new ViewNode(node.getChildAt(i)));
        }

        @Override
        public int getVisibility() {
            return visibility;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public int getTop() {
            return top;
        }

        @Override
        public CharSequence getText() {
            return text;
        }

        @Override
        public int getTextSelectionStart() {
            return textSelectionStart;
        }

        @Override
        public int getTextSelectionEnd() {
            return textSelectionEnd;
        }

        @Override
        public int getChildCount() {
            return children.size();
        }

        @Override
        public AnyViewNode getChildAt(int i) {
            return children.get(i);
        }
    }
}
