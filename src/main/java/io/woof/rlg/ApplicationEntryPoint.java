package io.woof.rlg;

/**
 * See solution for building an executable jar for a java-fx 11+ application
 * <a href="https://stackoverflow.com/questions/57019143/build-executable-jar-with-javafx11-from-maven">build-executable-jar-with-javafx11-from-maven</a>
 * <p>
 * It should also be possible to create such a jar with the JavaFX Maven plugin as described in <a
 * href="https://openjfx.io/openjfx-docs/#modular">Getting Started with JavaFX - Runtime images</a>
 * </p>
 */
public class ApplicationEntryPoint {

    public static void main(String[] args) {
        RlgApplication.main(args);
    }

}
