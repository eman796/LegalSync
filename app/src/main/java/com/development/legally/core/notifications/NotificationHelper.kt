package com.development.legally.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.development.legally.data.model.Event
import java.util.*

object NotificationHelper {

    fun scheduleEventNotification(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val eventTime = event.fechaHora?.toDate()?.time ?: return
        
        // Calcular tiempo de aviso
        val reminderMillis = parseReminderTime(event.recordar)
        val triggerTime = eventTime - reminderMillis

        if (triggerTime <= System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("event_title", event.titulo)
            putExtra("event_message", "Recordatorio: ${event.tipo} en ${event.recordar}")
            putExtra("event_id", event.eventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.eventId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    private fun parseReminderTime(recordar: String): Long {
        val lower = recordar.lowercase()
        return when {
            lower.contains("5 min") -> 5 * 60 * 1000L
            lower.contains("15 min") -> 15 * 60 * 1000L
            lower.contains("30 min") -> 30 * 60 * 1000L
            lower.contains("1 hora") || lower.contains("1 hr") -> 60 * 60 * 1000L
            lower.contains("2 horas") || lower.contains("2 hr") -> 2 * 60 * 60 * 1000L
            lower.contains("24 horas") || lower.contains("1 día") -> 24 * 60 * 60 * 1000L
            else -> 0L
        }
    }

    fun cancelNotification(context: Context, eventId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}
