package re.neutrino.kanji_assist.dictionary;

import org.junit.Test;

import java.io.InputStream;

import re.neutrino.kanji_assist.BasicTest;
import re.neutrino.kanji_assist.R;

import static org.junit.Assert.*;

public class DictionaryParserTest extends BasicTest {

    @Test
    public void read() throws Exception {
        InputStream inputStream = context.getResources().openRawResource(R.raw.dictionaryparse_1);
        DictionaryParser dictionaryParser = new DictionaryParser(inputStream);
        dictionaryParser.read();
        assertTrue("Examples size mismatch",
                dictionaryParser.getExamples().size() == 21);
        assertTrue("Senses size mismatch",
                dictionaryParser.getSenses().size() == 18);
        inputStream.close();
    }

}