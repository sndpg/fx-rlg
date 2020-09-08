package io.woof.rlg.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureCollection<T> extends ArrayList<CompletableFuture<T>> {

    public CompletableFutureCollection() {
        super();
    }

    public CompletableFutureCollection(int size) {
        super(size);
    }

    @Override
    public boolean add(CompletableFuture<T> tCompletableFuture) {
        return super.add(tCompletableFuture);
    }

    @SafeVarargs
    public final void add(CompletableFuture<T>... tCompletableFuture) {
        Arrays.stream(tCompletableFuture)
                .forEach(super::add);
    }

    public void cancelAll() {
        forEach(tCompletableFuture -> tCompletableFuture.cancel(true));
        clear();
    }

    public boolean isDone() {
        return stream().allMatch(CompletableFuture::isDone);
    }

}
