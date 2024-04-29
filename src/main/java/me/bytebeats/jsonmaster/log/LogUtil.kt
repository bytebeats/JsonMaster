package me.bytebeats.jsonmaster.log

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:32
 * @Version 1.0
 * @Description LogUtil show logs in Console
 */

object LogUtil {
    private val project: Project = ProjectManager.getInstance().defaultProject
    private const val GROUP_ID = "json_master"
    private const val GROUP_TITLE = "Json Master"

    fun i(msg: String) {
        println(msg)
//        Notifications.Bus.notify(Notification(GROUP_ID, GROUP_TITLE, msg, NotificationType.INFORMATION), project)
    }

    fun e(msg: String) {
        System.err.println(msg)
        Notifications.Bus.notify(Notification(GROUP_ID, GROUP_TITLE, msg, NotificationType.ERROR), project)
    }

    fun w(msg: String) {
        System.err.println(msg)
        Notifications.Bus.notify(Notification(GROUP_ID, GROUP_TITLE, msg, NotificationType.WARNING), project)
    }

    fun e(throwable: Throwable) {
        throwable.printStackTrace()
        Notifications.Bus.notify(
            Notification(
                GROUP_ID,
                GROUP_TITLE,
                throwable.localizedMessage,
                NotificationType.ERROR
            ), project
        )
    }
}