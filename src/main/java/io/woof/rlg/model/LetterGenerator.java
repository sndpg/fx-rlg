package io.woof.rlg.model;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LetterGenerator {

    private static final String DEFAULT_ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvxyz";

    private final Set<Character> initialLetters = new HashSet<>();
    private final List<Character> remainingLetters = new ArrayList<>(26);

    public LetterGenerator(Set<Character> validLetters) {
        initialLetters.addAll(validLetters);
        remainingLetters.addAll(validLetters);
    }

    public LetterGenerator() {
        this(getDefaultCharacters());
    }

    public static Set<Character> getDefaultCharacters() {
        char[] chars = DEFAULT_ALLOWED_CHARACTERS.toUpperCase().toCharArray();
        return IntStream.range(0, chars.length)
                .mapToObj(i -> chars[i])
                .collect(Collectors.toSet());
    }

    public Set<Character> getAllowedCharacters(){
        return initialLetters;
    }

    public void reset() {
        remainingLetters.clear();
        remainingLetters.addAll(initialLetters);
    }

    public String next() {
        if (remainingLetters.isEmpty()) {
            reset();
        }
        return remainingLetters.remove(new SecureRandom().nextInt(remainingLetters.size() - 1)).toString();
    }

    public Optional<String> nextOptional() {
        return !remainingLetters.isEmpty() ? Optional.of(next()) : Optional.empty();
    }
}
