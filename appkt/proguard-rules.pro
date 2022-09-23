# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# +++ Logger +++
# This will strip `Log.v`, `Log.d`, `Log.i`, `Log.w` and `Log.e` statements.
# https://www.guardsquare.com/manual/configuration/examples#logging
# https://issuetracker.google.com/issues/73708157#comment4
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

-assumenosideeffects class com.alletsxlab.proguardstripslog.utils.LogUtil {
    public boolean canLog();
    public *** d(...);
    public *** i(...);
    public *** w(...);
    public *** e(...);
}

# make R8 remove the implicit logger null checks
# https://issuetracker.google.com/issues/174285670#comment19
# the logger instance gets checked for NonNull, bytecode:
# invoke-static {v0}, Ljava/util/Objects;->requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;
-assumevalues class com.alletsxlab.proguardstripslog.** {
    public com.alletsxlab.proguardstripslog.utils.LogUtil * return 1;
    private com.alletsxlab.proguardstripslog.utils.LogUtil * return 1;
    protected com.alletsxlab.proguardstripslog.utils.LogUtil * return 1;
}
# --- Logger ---
