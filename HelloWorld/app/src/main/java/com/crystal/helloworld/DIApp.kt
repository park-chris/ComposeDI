package com.crystal.helloworld

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 단계 2: DIApp을 `@HiltAndroidApp`로 어노테이션합니다.
// Dagger Hilt 의존성 주입을 위해서는 Application이 만들어져있어야 한다.
// @HiltAndroidApp으로 Application 단을 초기화
@HiltAndroidApp
class DIApp: Application()