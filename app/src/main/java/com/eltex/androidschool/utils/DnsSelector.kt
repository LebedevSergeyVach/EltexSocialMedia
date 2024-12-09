package com.eltex.androidschool.utils

import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress

/**
 * Класс для выбора DNS, который принудительно использует IPv4 для разрешения доменных имен.
 *
 * Этот класс реализует интерфейс [Dns] из библиотеки OkHttp и переопределяет метод [lookup],
 * чтобы возвращать только IPv4 адреса.
 */
class DnsSelector : Dns {

    /**
     * Метод для разрешения доменного имени в список IP-адресов.
     *
     * @param hostname Доменное имя, которое нужно разрешить.
     * @property address Используем системный DNS для разрешения доменного имени. Фильтруем адреса, чтобы оставить только IPv4 адреса.
     * @return Список IP-адресов, соответствующих данному доменному имени, отфильтрованных по IPv4.
     */
    override fun lookup(hostname: String): List<InetAddress> {
        val addresses = Dns.SYSTEM.lookup(hostname)

        return addresses.filter { Inet4Address::class.java.isInstance(it) }
    }
}
