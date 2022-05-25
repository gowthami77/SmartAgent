#include <jni.h>


JNIEXPORT jstring JNICALL
Java_ai_yantranet_smartagent_retrofithelper_Keys_getFirstKey(JNIEnv *env, jclass clazz) {

    return (*env)->NewStringUTF(env, "https://demo6055087.mockable.io");
}
JNIEXPORT jstring JNICALL
Java_ai_yantranet_smartagent_retrofithelper_Keys_getSecondKey(JNIEnv *env, jclass clazz) {

return (*env)->NewStringUTF(env, "fetch_config");
}
