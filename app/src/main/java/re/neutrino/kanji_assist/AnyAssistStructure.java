package re.neutrino.kanji_assist;

interface AnyAssistStructure {
    int getWindowNodeCount();

    AnyWindowNode getWindowNodeAt(int i);

    interface AnyWindowNode {
        AnyViewNode getRootViewNode();
    }

    interface AnyViewNode {
        int getVisibility();

        int getLeft();
        int getTop();

        CharSequence getText();
        int getTextSelectionStart();
        int getTextSelectionEnd();

        int getChildCount();
        AnyViewNode getChildAt(int i);
    }
}
