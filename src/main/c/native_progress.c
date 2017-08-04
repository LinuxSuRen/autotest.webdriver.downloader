#include <stdio.h>
#include <jni.h>
#include "org_suren_autotest_webdriver_downloader_NativeProgress.h"
 
JNIEXPORT void JNICALL Java_org_suren_autotest_webdriver_downloader_NativeProgress_print
    (JNIEnv *env, jobject object){
    printf("ab\bc");
}