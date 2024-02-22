/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectopgv_dad_servidor;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

public class Temperatura {
    public static void main(String[] args) {
        double cpuTemperature = getWindowsCPUTemperature();
        System.out.println("Temperatura de la CPU: " + cpuTemperature + "°C");
    }

    public static double getWindowsCPUTemperature() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        Sensors sensors = hardware.getSensors();

        double cpuTemperature = -1;

        // Obtener la temperatura de la CPU
        if (sensors != null) {
            cpuTemperature = sensors.getCpuTemperature();
        }

        return cpuTemperature;
    }
}