#指定压缩级别
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类,是否混淆第三方jar
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度
-dontpreverify
# 混淆时是否记录日志,包含有类名->混淆后类名的映射关系
-verbose
#混淆映射文件输出
-printmapping proguardMapping.txt
#保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留源文件名及行号
-keepattributes SourceFile,LineNumberTable
# 指定混淆是采用的算法，后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#保留我们使用的四大组件，自定义的Application等等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Fragment

#lifeCycle相关混淆配置    viewModel相关 lifecycle相关
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

-keep class android.view.** { *; }
-dontwarn android.view.**


#AndroidX混淆开始
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
#AndroidX混淆结束

#测试反射混淆
-keep class java.lang.reflect.** { *; }
-dontwarn java.lang.reflect.**

#ViewBinding相关混淆配置
-keep class androidx.viewbinding.** { *; }
-dontwarn androidx.viewbinding.**

#databinding混淆配置
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }


#保留特定的类
-keep class com.wanghao.mvvmapp.mvvm.utils.BindingReflex



# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#过滤R文件的混淆：
-keep class **.R$* {
 *;
}


#swiperefreshLayout
-keep class androidx.swiperefreshlayout.** { *; }
-dontwarn androidx.swiperefreshlayout.**

#StatusBar
-dontwarn com.jaeger.library.**
-keep class com.jaeger.library.** { *; }

#MMKV
-dontwarn com.tencent.mmkv.**
-keep class com.tencent.mmkv.** { *; }

#ACP
-dontwarn com.mylhyl.acp.**
-keep class com.mylhyl.acp.** { *; }

#okhttp3.x
-keep class okhttp3.** { *;}
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn javax.annotation.**

# biometric
-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

#resultBack
-dontwarn cn.com.woong.resultbacklib.**
-keep class cn.com.woong.resultbacklib.** { *; }

# eventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keep class androidx.core.app.CoreComponentFactory { *; }

#-keep @androidx.annotation.Keep class *
#-keepclassmembers class * {
#    @androidx.annotation.Keep *;
#}


#不混淆所有类名中包含了“bean”的类及其成员
#-keep public class **.*bean*.** {*;}