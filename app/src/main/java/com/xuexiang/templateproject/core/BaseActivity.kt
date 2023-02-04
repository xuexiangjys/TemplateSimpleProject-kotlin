/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.xuexiang.templateproject.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.xuexiang.xpage.base.XPageActivity
import com.xuexiang.xpage.base.XPageFragment
import com.xuexiang.xpage.core.CoreSwitchBean
import com.xuexiang.xrouter.facade.service.SerializationService
import com.xuexiang.xrouter.launcher.XRouter
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/**
 * 基础容器Activity
 *
 * @author XUE
 * @since 2019/3/22 11:21
 */
open class BaseActivity<Binding : ViewBinding?> : XPageActivity() {

    /**
     * ViewBinding
     */
    var binding: Binding? = null
        protected set

    override fun attachBaseContext(newBase: Context) {
        //注入字体
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun getCustomRootView(): View? {
        binding = viewBindingInflate(layoutInflater)
        onViewBindingUpdate(binding)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initStatusBarStyle()
        super.onCreate(savedInstanceState)
    }

    /**
     * 构建ViewBinding
     *
     * @param inflater  inflater
     * @return ViewBinding
     */
    open fun viewBindingInflate(inflater: LayoutInflater?): Binding? {
        return null
    }

    /**
     * ViewBinding更新
     * @param binding ViewBinding
     */
    open fun onViewBindingUpdate(binding: Binding?) {

    }

    /**
     * 初始化状态栏的样式
     */
    open fun initStatusBarStyle() {}

    override fun onDestroy() {
        super.onDestroy()
        onViewBindingUnbind()
    }

    /**
     * ViewBinding解绑
     */
    open fun onViewBindingUnbind() {
        binding = null
    }

    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> openPage(clazz: Class<T>?, addToBackStack: Boolean): T {
        val page = CoreSwitchBean(clazz).setAddToBackStack(addToBackStack)
        return openPage(page) as T
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> openNewPage(clazz: Class<T>?): T {
        val page = CoreSwitchBean(clazz).setNewActivity(true)
        return openPage(page) as T
    }

    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> switchPage(clazz: Class<T>?): T {
        return openPage(clazz, false)
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    fun serializeObject(`object`: Any?): String {
        return XRouter.getInstance().navigation(SerializationService::class.java)
            .object2Json(`object`)
    }
}