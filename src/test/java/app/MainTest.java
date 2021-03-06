/*
package app;

import app.utils.ImgToken;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

    private static String WHITESPACE = "\\s"; */
/*
            + "\\u0009" // CHARACTER TABULATION
            + "\\u000A" // LINE FEED (LF)
            + "\\u000B" // LINE TABULATION
            + "\\u000C" // FORM FEED (FF)
            + "\\u000D" // CARRIAGE RETURN (CR)
            + "\\u0020" // SPACE
            + "\\u0085" // NEXT LINE (NEL)
            + "\\u00A0" // NO-BREAK SPACE
            + "\\u1680" // OGHAM SPACE MARK
            + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
            + "\\u2000" // EN QUAD
            + "\\u2001" // EM QUAD
            + "\\u2002" // EN SPACE
            + "\\u2003" // EM SPACE
            + "\\u2004" // THREE-PER-EM SPACE
            + "\\u2005" // FOUR-PER-EM SPACE
            + "\\u2006" // SIX-PER-EM SPACE
            + "\\u2007" // FIGURE SPACE
            + "\\u2008" // PUNCTUATION SPACE
            + "\\u2009" // THIN SPACE
            + "\\u200A" // HAIR SPACE
            + "\\u2028" // LINE SEPARATOR
            + "\\u2029" // PARAGRAPH SEPARATOR
            + "\\u202F" // NARROW NO-BREAK SPACE
            + "\\u205F" // MEDIUM MATHEMATICAL SPACE
            + "\\u3000" // IDEOGRAPHIC SPACE*//*


    private static final String REG_EX =
            "(\\/[a-zA-Z]+)+"
                    + "(\\?"
                    + "(\\&{0,1}"
                    + "[a-zA-Z]+"
                    + "\\={1}"
                    + "[a-zA-Zа-яА-ЯЁІіЙйЪъЇї0-9" + "\\s" + "]+"
                    + ")+"
                    + "){0,1}"
                    + "\\/{0,1}";
    private static final String NEG_REG_EX =
            "(?!"
                    + "(\\/[a-zA-Z]+)+"
                    + "(\\?"
                    + "(\\&{0,1}"
                    + "[a-zA-Z]+"
                    + "\\={1}"
                    + "[a-zA-Zа-яА-ЯЁІіЙйЪъЇї0-9" + "\\s" + "]+"
                    + ")+"
                    + "){0,1}"
                    + "\\/{0,1}"
                    + "$).*";

    private static List<TestHolder> tests = new LinkedList<>();

    private static TestHolder get(String str, boolean b) {
        return new TestHolder(str, b);
    }

    @BeforeEach
    void setUp() {
        tests.add(get("/auth/", false));
        tests.add(get("/auth?a=b", false));
        tests.add(get("/auth?a=b/", false));
        tests.add(get("/фівфівфівфів/asdasjehbfbwfuewfuwe/eshweugf", true));
        tests.add(get("/authasdasd/фівфівфівфів/eshweugf?asdasd=ergrh54rh", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/фвфівфів?asdasd=ergrh54rh", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?фівфівфів"
                + "=ergrh54rh&sjfbha=ashfbash=hsfhrf", true));
        tests.add(get("/authasdasd/asdasjehbfbwfuewfuwe/eshweugf", false));
        tests.add(get("/authasdasd/asdasjehbfbwfuewfuwe/eshweugf?asdasd=фіівфівфів", false));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasfффііі=ergrh54rh", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasfффііі==ergrh54rh", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasfффііі=%20ERGRH54RH", true));

        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf==івівE54hRGRH54RH", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf=%20ERGRH54RH", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf=%20ERGRH54RH=", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf"
                + "=ERGRH54RHsssssіііі&54gh45=5 g57g75 57gh57", true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf=ERGRH54RHsssssіііі&fgfg"
                + "=5 g57g75 57gh57&dfdfsf=іііі і і і і і і і і і і і і і", false));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf"
                        + "=ERGRH54RHsssssіііі&fgfg=5 g57g75 57gh57&dfdfsf="
                        + "іііі і і і і і і [[[і і і і і і і",
                true));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf"
                        + "=ERGRH54RHsssssіііі&fgfg=5 g57g75 57gh57&dfdfsf"
                        + "=іііі і і і і і іъъъъъ і і і і і і і",
                false));
        tests.add(get("/authasdasd/asdaswfuewfuwe/eshweugf?sasfasf"
                + "=ERGRH54RHsssssіііі&fgfg=5 g57g75 57gh57&dfdfsf="
                + "іііі і іъЪъъЇїїї і і і і і", false));
        tests.add(get("/??", true));
    }

    @Test
    void test() {
        System.out.println();
        for (TestHolder test : tests) {
            boolean res = Main.test(NEG_REG_EX, test.getStr());
            System.out.println(test.getStr() + " " + res);
            Assert.assertSame(res, test.isResult());
        }
    }

    @Test
    void testtest() {
        String name = "WEB-INF/views/status.hbs";
        name = FilenameUtils.getExtension(name);
        System.out.println(name);
    }

    @Test
    void testtesttest() {
        String name = "WEB-INF/views/statewj,fhvbewybr23yrvt2 "
                + " 37rt    672te7  236crt2 37cbr   23cus.hbs";
        name = ImgToken.generate(name);
        System.out.println(name);
    }

    private static class TestHolder {
        private String str;
        private boolean result;

        public TestHolder(String str, boolean result) {
            this.str = str;
            this.result = result;
        }

        public String getStr() {
            return str;
        }

        public boolean isResult() {
            return result;
        }
    }
}
*/
