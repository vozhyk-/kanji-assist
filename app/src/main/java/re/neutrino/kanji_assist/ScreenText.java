package re.neutrino.kanji_assist;

import android.graphics.Point;

class ScreenText {
    private String text;
    private Point position;

    public ScreenText(String text, Point position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public Point getPosition() {
        return position;
    }
}
