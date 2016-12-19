package re.neutrino.kanji_assist;

interface AnyAssistStructure {
    int getWindowNodeCount();

    WindowNode getWindowNodeAt(int i);

    interface WindowNode {
        ViewNode getRootViewNode();
    }

    interface ViewNode {
        int getVisibility();

        int getLeft();
        int getTop();
        int getWidth();
        int getHeight();

        CharSequence getText();
        int getTextSelectionStart();
        int getTextSelectionEnd();

        int getChildCount();
        ViewNode getChildAt(int i);
    }
}
