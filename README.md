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

-   R8 does not strip an unused instance of the custom logger in a super class (e.g. `BaseActivity`) in kotlin.
	
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

-   [6f3c3e8c]() If `debuggable true` is set explicitly in the build config, the `minifyEnabled true` does not affect and doesn't obfuscate anything. It also worked fine on AGP versions < 7.2 (e.g. 7.0.4)

    https://issuetracker.google.com/issues/242214899


-   R8 does not support `assumenoexternalsideeffects` and `assumenoexternalreturnvalues`, but the usecases is partly covered by the built-in modeling of `StringBuilder` and `StringBuffer`, where the constructors are modeled as being side effect free (i.e. no external side effects) and all append methods are marked as returning receiver (i.e. no external return value).

	https://issuetracker.google.com/issues/137038659#comment11
	
	https://issuetracker.google.com/issues/73708157#comment4

	Proguard uses `assumenoexternalsideeffects` and `assumenoexternalreturnvalues` to strip unused string concatenation (e.g. `StringBuilder`).

	https://www.guardsquare.com/manual/configuration/examples#logging


-   [ddd02bbd]() turn off `debuggable` to make proguard rules `-assumenosideeffects` work

	https://stackoverflow.com/questions/71443326


-   make R8 remove the implicit logger null checks

    https://issuetracker.google.com/issues/174285670#comment19

    The logger instance gets checked for NonNull, bytecode:

	```
	invoke-static {v0}, Ljava/util/Objects;->requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;
	```


-   R8 does not strip unused string concatenation in kotlin. 
 	But a custom logger supporting parameterized strings can avoid that.

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


