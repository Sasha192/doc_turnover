package app;

import org.apache.commons.io.FilenameUtils;

public class Main {

    public static void main(String[] args) {
        String name = "/home/kolmogorov/Java_Practice/bcrew/doc_turnover/src/main/java/app/Main.java";
        System.out.println(FilenameUtils.getExtension(name));
    }
}
