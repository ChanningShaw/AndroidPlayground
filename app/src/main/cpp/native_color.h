//
// Created by xiaochunming on 2020/10/27.
//
#include <jni.h>
#ifndef ANDROIDDEMO_NATIVE_COLOR_H
#define ANDROIDDEMO_NATIVE_COLOR_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL surfaceCreated(JNIEnv *, jobject, jint);

JNIEXPORT void JNICALL surfaceChanged(JNIEnv *, jobject, jint, jint);

JNIEXPORT void JNICALL onDrawFrame(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif

#endif //ANDROIDDEMO_NATIVE_COLOR_H

