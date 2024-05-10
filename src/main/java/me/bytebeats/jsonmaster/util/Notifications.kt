package me.bytebeats.jsonmaster.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ex.ProjectManagerEx

private const val NOTIFICATION_GROUP_ID = "Json Master"
private const val NOTIFICATION_GROUP_TITLE = "Json Master"

fun notifyInformation(message: String) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(NOTIFICATION_GROUP_TITLE, message, NotificationType.INFORMATION)
        .notify(ProjectManagerEx.getInstanceEx().defaultProject)
}

fun notifyWarning(warning: String) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(NOTIFICATION_GROUP_TITLE, warning, NotificationType.WARNING)
        .notify(ProjectManagerEx.getInstanceEx().defaultProject)
}

fun notifyError(error: String) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(NOTIFICATION_GROUP_TITLE, error, NotificationType.ERROR)
        .notify(ProjectManagerEx.getInstanceEx().defaultProject)
}