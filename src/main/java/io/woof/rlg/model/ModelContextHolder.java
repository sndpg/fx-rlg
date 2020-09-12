package io.woof.rlg.model;

public class ModelContextHolder {

    private final static ModelContext INSTANCE = new ModelContext();

    public static ModelContext getModelContext(){
        return INSTANCE;
    }

}
