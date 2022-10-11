# Android R8 proguard strips log

The Lab project aims to find Android R8 proguard rules to strip log statements in Java and Kotlin. Rules are useful for `android.util.Log` class and my custom logger (a wrapper around the `android.util.Log` class).

There are [verifications](./docs/AndroidR8ProguardStripsLogVerifications.md) of Java and Kotlin Apps.

Conclusively, there are some [unsolved issues](#unsolved-issues) and [fixed issues or workarounds](#fixed-issues-or-workarounds).


the usage of [my custom logger (java)](./app/src/main/java/com/alletsxlab/proguardstripslog/utils/LogUtil.java)
and [proguard rules](./app/proguard-rules.pro):

```
private final LogUtil log = new LogUtil(this);

log.d("LogUtil Log onCreate");
log.d("LogUtil Log concat " + localVar);
log.d("LogUtil Log %s", localVar);
```


the usage of [my custom logger (kotlin)](./appkt/src/main/java/com/alletsxlab/proguardstripslog/utils/LogUtil.kt)
and [proguard rules](./appkt/proguard-rules.pro):

```
private val log = LogUtil(this)

log.d("LogUtil Log onCreate")
log.d("LogUtil Log concat $localVar")
log.d("LogUtil Log %s", localVar)
```



## Comparison

-   total 12 (2x3x2)
	-   code lang x2: Java, Kotlin
	-   log wrapper x2: N, custom
	-   enable proguard x3: N, Y (enable), Rules (enable with proguard rule)


-   string concatenation in log messages
	-   constant string `"str1" + "str2"`
	-   local var `"str1 " + localVar` or `"str1 $localVar"`
	-   member var `"str1 " + memberVar` or `"str1 $memberVar"`


-   log
	-   Application onCreate()
	-   Activity onCreate()
	-   Fragment onCreateView()
	-   Fragment onPause()



## Unsolved issues

### R8 does not strip an unused instance of the custom logger in a super class (e.g. `BaseActivity`) in kotlin.

```
// kotlin
protected val log = LogUtil(this)

// bytecode before proguard
new-instance v0, Lcom/alletsxlab/proguardstripslog/utils/LogUtil;
const/4 v1, 0x0
const/4 v2, 0x2
const/4 v3, 0x0
invoke-direct {v0, p0, v1, v2, v3}, Lcom/alletsxlab/proguardstripslog/utils/LogUtil;-><init>(Ljava/lang/Object;ZILkotlin/jvm/internal/DefaultConstructorMarker;)V
iput-object v0, p0, Lcom/alletsxlab/proguardstripslog/base/BaseActivity;->log:Lcom/alletsxlab/proguardstripslog/utils/LogUtil;

// bytecode after proguard
new-instance v0, Le1/a;
const/4 v1, 0x0
const/4 v2, 0x2
invoke-direct {v0, p0, v1, v2}, Le1/a;-><init>(Ljava/lang/Object;ZI)V
iput-object v0, p0, Ld1/a;->w:Le1/a;
```



## Fixed issues or workarounds

### If `debuggable true` is set explicitly in the build config, the `minifyEnabled true` does not affect and doesn't obfuscate anything. It also worked fine on AGP versions < 7.2 (e.g. 7.0.4)

related issues
-   https://issuetracker.google.com/issues/242214899
-   https://issuetracker.google.com/issues/234309867#comment2
-   https://issuetracker.google.com/issues/236748718
-   https://issuetracker.google.com/issues/234480449#comment14
	
	> mk...@google.com #14  
	> minifyEnabled will not give the same result if placed in `debug` and `release` section.
	> 
	> Doing that in debug mode passes a debug flag to the R8 compiler that basically tells us to do `-dontobfuscate` and `-dontoptimize` and we also keep more code alive because we need to keep all local information.

-   https://issuetracker.google.com/issues/238655204#comment4

	> mk...@google.com #4:  
	> Using debuggable is essentially passing `-dontobfuscate` and `-dontoptmize` and also disable other optimizations. Debuggable means attaching a debugger to the release apk. For that to ever work it would require to not obfuscate the classes.
	
	[#9](https://issuetracker.google.com/issues/238655204#comment9)
	
	> ze...@google.com #9:  
	> Regarding the effects of compiling in debug mode with R8: the debug build has never been interchangeable with the release build when obfuscation was applied in both. The reason is that a debug build will have vastly different code and almost no optimizations applied. The consequence is that the amount of classes, fields and methods will be different and the effects of renaming them will then be different.  
	> ...  
	> The only real usage of the R8 debug mode is to obtain an APK that can be attached to for the purpose of debugging the user code. It does not provide any value for checking the effects of R8 compilation compared to its release mode build.


related commits in the project
-   6f3c3e8c
-   8ffd4906



#### test different versions of R8

To locate the R8 change, I tested from R8 3.2.47 (AGP 7.2.0) down to R8 3.2.8-dev (AGP 7.2.0-alpha01).
It is since 3.2.11-dev that R8 has not obfuscated anything.
Further, I looked into R8 commit log from 3.2.10-dev to 3.2.11-dev. 
I found that [the commit](https://r8-review.googlesource.com/c/r8/+/63766) 
 with [the issue ticket](https://issuetracker.google.com/issues/202486757) 
 probably changed the behavior.


```
// project-level settings.gradle
pluginManagement {
	repositories {
		mavenCentral()
		maven {
			url "https://storage.googleapis.com/r8-releases/raw"
		}
	}
}

// project-level build.gradle
buildscript {
	dependencies {
		// Must be before the Gradle Plugin for Android.
		//classpath("com.android.tools:r8:3.2.10-dev")
		classpath("com.android.tools:r8:3.2.11-dev")
	}
}

println(com.android.tools.r8.Version.getVersionString())
```

environment
-   Gradle 7.3.3, AGP 7.2.2, (default) R8 3.2.74, Kotlin 1.6.10
-   Gradle 7.2  , AGP 7.1.2, (default) R8 3.1.66, Kotlin 1.6.10

obfuscate
-   R8 3.1.66 (AGP 7.1.2)
-   R8 3.2.8-dev (AGP 7.2.0-alpha01)
-   R8 3.2.10-dev (AGP 7.2.0-alpha02 ~ 05) 
	
not obfuscate
-   R8 3.2.11-dev 
-   R8 3.2.12-dev 
-   R8 3.2.15-dev 
-   R8 3.2.20-dev 
-   R8 3.2.29-dev (AGP 7.2.0-alpha06) 
-   R8 3.2.47 (AGP 7.2.0) 
-   R8 3.2.74 
-   R8 3.2.75 
-   R8 3.3.70 (AGP 7.3)



### R8 does not support `assumenoexternalsideeffects` and `assumenoexternalreturnvalues`, but the usecases is partly covered by the built-in modeling of `StringBuilder` and `StringBuffer`, where the constructors are modeled as being side effect free (i.e. no external side effects) and all append methods are marked as returning receiver (i.e. no external return value).

Proguard uses `assumenoexternalsideeffects` and `assumenoexternalreturnvalues` to strip unused string concatenation (e.g. `StringBuilder`).

https://www.guardsquare.com/manual/configuration/examples#logging


related issues
-   https://issuetracker.google.com/issues/137038659#comment11
-   https://issuetracker.google.com/issues/73708157#comment4



### turn off `debuggable` to make proguard rules `-assumenosideeffects` work

related issues
-   https://stackoverflow.com/questions/71443326


related commits in the project
-   ddd02bbd



### make R8 remove the implicit logger null checks

The logger instance gets checked for NonNull, bytecode:

```
invoke-static {v0}, Ljava/util/Objects;->requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;
```


related issues
-   https://issuetracker.google.com/issues/174285670#comment19



### R8 does not strip unused string concatenation in kotlin. But a custom logger supporting parameterized strings can avoid that.

My env: Gradle 7.2, AGP 7.1.2, (default) R8 3.1.66, Kotlin 1.6.10

unused string concatenation in kotlin, bytecode:

```
// kotlin
val localVar = "local var"
Log.d(TAG, "Android Log concat $localVar") // `Log.d` was stripped but the string concatenation was left.

// bytecode before proguard
const-string v0, "local var"
.local v0, "localVar":Ljava/lang/String;
const-string v2, "Android Log concat "
invoke-static {v2, v0}, Lkotlin/jvm/internal/Intrinsics;->stringPlus(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
move-result-object v3

// bytecode after proguard
const-string v0, "Android Log concat "
const-string v1, "local var"
invoke-static {v0, v1}, Ls/d;->J(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
```


related issues:
-   https://issuetracker.google.com/issues/190489514


