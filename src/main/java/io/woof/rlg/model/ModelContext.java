package io.woof.rlg.model;

public class ModelContext {

    private LetterGenerator letterGenerator = new LetterGenerator();

    public LetterGenerator getLetterGenerator() {
        return letterGenerator;
    }

    public void setLetterGenerator(LetterGenerator letterGenerator) {
        this.letterGenerator = letterGenerator;
    }

}
