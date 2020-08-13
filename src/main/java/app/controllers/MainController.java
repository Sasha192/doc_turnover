package app.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private static String WHITESPACE = "\\s" /*""       *//* dummy empty string for homogeneity *//*
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
            + "\\u3000" // IDEOGRAPHIC SPACE*/
            ;

    private static final String REG_EX =
            "(\\/[a-zA-Z]+)+"
                    + "(\\?"
                    + "(\\&{0,1}"
                    + "[a-zA-Z]+"
                    + "\\={1}"
                    + "[a-zA-Zа-яА-ЯЁІіЙйЪъЇї0-9"+ WHITESPACE +"]+"
                    + ")+"
                    + "){0,1}"
                    + "\\/{0,1}";
    private static final String NEG_REG_EX = "(?!" + REG_EX + "$).*";

    @RequestMapping(value =
            "(?!"
                + "(\\/[a-zA-Z]+)+"
                + "(\\?"
                        + "(\\&{0,1}"
                        + "[a-zA-Z]+"
                        + "\\={1}"
                        + "[a-zA-Zа-яА-ЯЁІіЙйЪъЇї0-9"+ "\\s" +"]+"
                    + ")+"
                + "){0,1}"
                + "\\/{0,1}"
            + "$).*")
    public String noSuchMapping(HttpServletRequest request,
                               HttpServletResponse response) {
        // DO SOMETHING
        return "400";
    }

}
