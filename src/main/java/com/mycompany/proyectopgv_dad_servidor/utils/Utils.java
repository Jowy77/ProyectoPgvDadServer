package com.mycompany.proyectopgv_dad_servidor.utils;

import com.mycompany.proyectopgv_dad_servidor.modelo.ModeloServidor;
import com.mycompany.proyectopgv_dad_servidor.modelo.Proceso;
import com.mycompany.proyectopgv_dad_servidor.modelo.Servicio;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

import java.util.List;
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;

/**
 *
 * @author Iproy
 */
public class Utils {
    
    private long totalRam;
    private long usedRam;
    private double percentageUsed;
    
    SystemInfo systemInfo = new SystemInfo();
    GlobalMemory memory = systemInfo.getHardware().getMemory();
    
    private void getMemoryInfo() {
        totalRam = memory.getTotal();
        usedRam = totalRam - memory.getAvailable();
        percentageUsed = (usedRam * 100.0) / totalRam;
    }
    
    public static long getMemoriaTotal() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return memory.getTotal();
    }
    
    public static long getMemoriaRamUsada() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return getMemoriaTotal() - memory.getAvailable();
    }
    
    public static double getPorcetanjeDeRamUsado() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return (getMemoriaRamUsada() * 100) / getMemoriaTotal();
    }
    
    public static void mostrarInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    public static boolean validarString(String texto) {
        return texto != null && !texto.isEmpty();
    }
    
    public static boolean esCorreoGmail(String correo) {
        // Patrón para validar el correo electrónico de Gmail
        String patron = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*@gmail\\.com$";

        // Compilamos el patrón en un objeto Pattern
        Pattern pattern = Pattern.compile(patron);

        // Creamos un objeto Matcher para comparar el correo con el patrón
        Matcher matcher = pattern.matcher(correo);

        // Devolvemos true si el correo coincide con el patrón, de lo contrario, false
        return matcher.matches();
    }
    
    public static String[] obtenerInfoDisco() {
        String[] infoDisco = new String[3];

        // Obtener la lista de discos duros
        SystemInfo systemInfo = new SystemInfo();
        List<HWDiskStore> discos = systemInfo.getHardware().getDiskStores();
        if (!discos.isEmpty()) {
            HWDiskStore primerDisco = discos.get(0);

            // Obtener el nombre del disco y el tamaño total
            String nombreDisco = primerDisco.getModel();
            long tamanoTotal = primerDisco.getSize();

            // Obtener el espacio disponible utilizando el directorio raíz del disco
            File directorioRaiz = new File("C:");
            long espacioDisponible = directorioRaiz.getUsableSpace();

            // Guardar la información en el array
            infoDisco[0] = nombreDisco;
            infoDisco[1] = formatBytes(tamanoTotal);
            infoDisco[2] = formatBytes(espacioDisponible);
        } else {
            // Si no se encontraron discos, establecer valores predeterminados
            infoDisco[0] = "N/A";
            infoDisco[1] = "N/A";
            infoDisco[2] = "N/A";
        }
        
        return infoDisco;
    }
    
    public static double obtenerInfoDiscoTotalDouble() {
        
        File directorioRaiz = new File("C:");
        long espacioDisponible = directorioRaiz.getUsableSpace();
        
        return formatBytesToDouble(espacioDisponible);
    }
    
    public static double obtenerInfoDiscoDisponibleDouble() {

        // Obtener la lista de discos duros
        SystemInfo systemInfo = new SystemInfo();
        List<HWDiskStore> discos = systemInfo.getHardware().getDiskStores();
        
        HWDiskStore primerDisco = discos.get(0);

        // Obtener el nombre del disco y el tamaño total
        String nombreDisco = primerDisco.getModel();
        long tamanoTotal = primerDisco.getSize();
        
        return formatBytesToDouble(tamanoTotal);
    }
    
    public static String obtenerDireccionMAC() {
        // Obtener la lista de interfaces de red
        SystemInfo systemInfo = new SystemInfo();
        List<NetworkIF> interfaces = systemInfo.getHardware().getNetworkIFs();

        // Iterar sobre las interfaces de red y buscar la dirección MAC
        for (NetworkIF interfaz : interfaces) {
            String direccionMAC = interfaz.getMacaddr();
            if (direccionMAC != null && !direccionMAC.isEmpty()) {
                return direccionMAC;
            }
        }

        // Si no se encuentra ninguna dirección MAC, devolver un mensaje de error
        return "No se encontró una dirección MAC";
    }
    
    public static String[] obtenerDireccionIP() {
        // Obtener la lista de interfaces de red
        SystemInfo systemInfo = new SystemInfo();
        List<NetworkIF> interfaces = systemInfo.getHardware().getNetworkIFs();

        // Iterar sobre las interfaces de red y buscar la dirección IP
        for (NetworkIF interfaz : interfaces) {
            String[] direccionesIP = interfaz.getIPv4addr();
            if (direccionesIP != null && direccionesIP.length > 0) {
                return direccionesIP;
            }
        }

        // Si no se encuentra ninguna dirección IP, devolver un array vacío
        return new String[0];
    }
    
    public static long obtenerUsoTotalRed() {
        // Inicializar el total de uso de red
        long usoTotalRed = 0;

        // Obtener la lista de interfaces de red
        SystemInfo systemInfo = new SystemInfo();
        List<NetworkIF> interfaces = systemInfo.getHardware().getNetworkIFs();

        // Iterar sobre las interfaces de red y sumar los bytes enviados y recibidos
        for (NetworkIF interfaz : interfaces) {
            usoTotalRed += interfaz.getBytesSent() + interfaz.getBytesRecv();
        }
        
        return usoTotalRed;
    }
    
    public static String[] obtenerInformacionSistema() {
        String[] informacion = new String[5];
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        // Sistema Operativo
        informacion[0] = systemInfo.getOperatingSystem().toString();

        // Procesador
        CentralProcessor procesador = hardware.getProcessor();
        informacion[1] = procesador.toString();

        // Almacenamiento (Disco Duro)
        List<HWDiskStore> discos = hardware.getDiskStores();
        
        informacion[2] = discos.get(0).getModel();

        // Tarjeta de Red
        List<NetworkIF> tarjetas = hardware.getNetworkIFs();
        
        informacion[3] = tarjetas.get(0).getName();

        // Cantidad de Memoria RAM
        GlobalMemory memoriaGlobal = hardware.getMemory();
        informacion[4] = formatBytes(memoriaGlobal.getTotal()) + " Gb";
        
        return informacion;
    }
    
    public static String formatBytes(long bytes) {
        double kilobytes = bytes / 1024.0;
        double megabytes = kilobytes / 1024.0;
        double gigabytes = megabytes / 1024.0;
        
        if (gigabytes > 1) {
            return String.format("%.2f GB", gigabytes);
        } else if (megabytes > 1) {
            return String.format("%.2f MB", megabytes);
        } else {
            return String.format("%.2f KB", kilobytes);
        }
    }
    
    public static double formatBytesToDouble(long bytes) {
        double kilobytes = bytes / 1024.0;
        double megabytes = kilobytes / 1024.0;
        double gigabytes = megabytes / 1024.0;
        
        if (gigabytes > 1) {
            return Math.round(gigabytes * 100.0) / 100.0;
        } else if (megabytes > 1) {
            return Math.round(megabytes * 100.0) / 100.0;
        } else {
            return Math.round(kilobytes * 100.0) / 100.0;
        }
    }
    
    public static String obtenerPorcentajeUsoProcesador() {
        String informacion = "";
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        // Porcentaje de uso de la CPU
        double porcentajeUsoCpu = hardware.getProcessor().getSystemCpuLoad(500) * 100;
        
        informacion = String.format("%.2f", porcentajeUsoCpu) + "%";
        
        return informacion;
    }
    
    public static double getCPULoad() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        return hardware.getProcessor().getSystemCpuLoad(500) * 100;
    }
    
    public static List<OSProcess> getProcesosOS(){
        SystemInfo system = new SystemInfo();
        List<OSProcess> procesos = system.getOperatingSystem().getProcesses();
        return procesos;
    }
    
    public static List<OSService> getServiciosOS(){
        SystemInfo system = new SystemInfo();
        List<OSService> servicios = system.getOperatingSystem().getServices();
        return servicios;
    }
    
    public static List<Proceso> getProcesos(){
         
        List<Proceso> listaDeProcesos = new ArrayList<>();

        List<OSProcess> process = getProcesosOS();

        process.forEach((proceso) -> {

            Proceso nuevoProceso = new Proceso();
            nuevoProceso.setNombre(proceso.getName());
            nuevoProceso.setPID(String.valueOf(proceso.getProcessID()));
            nuevoProceso.setEstado(String.valueOf(proceso.getState()));
            nuevoProceso.setPrioridad(String.valueOf(proceso.getPriority()));

            listaDeProcesos.add(nuevoProceso);

        });
        
        return listaDeProcesos;
    }
    
    public static List<Servicio> getServicios(){
        List<Servicio> listaDeServicios = new ArrayList<>();
        
        List<OSService> servicios = getServiciosOS();
        
        servicios.forEach((servicio)->{
            Servicio nuevoServicio = new Servicio();
            nuevoServicio.setNombre(servicio.getName());
            nuevoServicio.setPID(String.valueOf(servicio.getProcessID()));
            nuevoServicio.setEstadoDelServicio(String.valueOf(servicio.getState()));
            
            listaDeServicios.add(nuevoServicio);
        });
        
        return listaDeServicios;
    }
    
    public static ModeloServidor crearModelo() {
        ModeloServidor modelo = new ModeloServidor();
        
        modelo.setDireccionIP(obtenerDireccionIP());
        modelo.setDireccionMac(obtenerDireccionMAC());
        modelo.setEspacioDisponibleDisco(obtenerInfoDiscoDisponibleDouble());
        modelo.setEspacioTotalEnDisco(obtenerInfoDiscoTotalDouble());
        modelo.setInfoDisco(obtenerInfoDisco());
        modelo.setMemoriaRamTotal(getMemoriaTotal());
        modelo.setMemoriaRamUsada(getMemoriaRamUsada());
        modelo.setPorcentajeRamUsado(getPorcetanjeDeRamUsado());
        modelo.setPorcentajeUsoProcesador(obtenerPorcentajeUsoProcesador());
        modelo.setUsoTotalDeRed(obtenerUsoTotalRed());
        modelo.setObetenerInfoDelSistema(obtenerInformacionSistema());
        modelo.setPorcentajeDoubleProcesador(getCPULoad());
        modelo.setProcesos(getProcesos());
        modelo.setServicios(getServicios());
        return modelo;
    }
}
