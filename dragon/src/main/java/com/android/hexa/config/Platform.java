package com.android.hexa.config;

public class Platform {

    public static final boolean DEPENDENCY_SUPPORT_DESIGN;
    public static final boolean DEPENDENCY_GLIDE;

    static {
        DEPENDENCY_SUPPORT_DESIGN = findClassByClassName("android.support.design.widget.Snackbar");
        DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide");
    }

    private static boolean findClassByClassName(String className) {
        boolean hasDependency;
        try {
            Class.forName(className);
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }
        return hasDependency;
    }
}
