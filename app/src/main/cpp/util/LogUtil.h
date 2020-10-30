//
// Created by xiaochunming on 2020/10/26.
#include <android/log.h>

//
#ifndef ANDROIDDEMO_LOGUTIL_H
#define ANDROIDDEMO_LOGUTIL_H


#define  LOG_TAG    "libgl2jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#endif //ANDROIDDEMO_LOGUTIL_H
