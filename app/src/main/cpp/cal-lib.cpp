#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jint JNICALL
Java_com_wedream_demo_jni_NativeCalculator_add(JNIEnv *env, jobject thiz, jint a, jint b) {
    return a + b;
}
