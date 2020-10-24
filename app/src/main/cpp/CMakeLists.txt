# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 添加并设置库头文件
set(CMAKE_VERBOSE_MAKEFILE on)
set(libs ${CMAKE_SOURCE_DIR}/libs)

# 解决shared library text segment is not shareable
#set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wall -v -Wl,--no-warn-shared-textrel")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             cal-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
            cal-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                        cal-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

# ########gl2jni#############

# now build app's shared lib
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")

add_library(gl2jni SHARED gl_code.cpp)

# add lib dependencies
target_link_libraries(gl2jni
        android
        log
        EGL
        GLESv2)

########gl2jni#############

########freetype#############

#定义变量LIB_DIR, ${CMAKE_CURRENT_SOURCE_DIR}表示当前文件所在的目录
set(LIB_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../libs)

message(WARNING ${LIB_DIR})

add_library(libfreetype STATIC IMPORTED )  #添加预编译静态库，只需要告诉CMAKE导入项目即可

set_target_properties(libfreetype
        PROPERTIES IMPORTED_LOCATION
        LIB_DIR/${ANDROID_ABI}/libfreetype.a)

# 添加并设置静态库路径
add_library(
        freetypewrap
        SHARED
        free_type.cpp)   #对应/src/main/cpp/下的CPP源码，此处采用JNI_OnLoad函数注册。

# 3.添加并设置静态库连接，预构建库与本地库相关联：
target_link_libraries( # Specifies the target library.
        freetypewrap
        libfreetype

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )
########freetype#############