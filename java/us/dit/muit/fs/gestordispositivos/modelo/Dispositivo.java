package us.dit.muit.fs.gestordispositivos.modelo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Clase de tipo entidad, que representa un dispositivo médico en el sistema de gestión de dispositivos.
 * Contiene información sobre el tipo de dispositivo, el paciente al que está asignado,
 */
public class Dispositivo {
    private static final Logger logger = LogManager.getLogger(Dispositivo.class);
    
    public static enum TipoDispositivo {
        RESPIRADOR, MONITOR_SIGNOS, DESFIBRILADOR, BOMBA_INFUSION, ELECTROCARDIOGRAFO
    }
    public static enum EstadoDispositivo {
        ASIGNADO, LIBRE, EN_MANTENIMIENTO, BAJA
    }

    //Identificador unívoco del dispositivo, se corresponde con el número de serie
    private String numeroSerie;   

    private TipoDispositivo tipo;
    private EstadoDispositivo estado;
    private String paciente;
    private String modelo;

  

    public Dispositivo(TipoDispositivo tipo, String modelo, String numeroSerie) {
        this.tipo = tipo;
        //Un dispositivo nuevo siempre está libre
        this.estado = EstadoDispositivo.LIBRE;
        this.paciente = null;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
       
    }
    public TipoDispositivo getTipo() { return tipo; }
    public EstadoDispositivo getEstado() { return estado; }
    public String getPaciente() { return paciente; }        
    public String getModelo() { return modelo; }
    public String getNumeroSerie() { return numeroSerie; }

    public void setTipo(TipoDispositivo tipo) { this.tipo = tipo; }
    public void setEstado(EstadoDispositivo estado) { this.estado = estado; }   
    public void setPaciente(String paciente) { this.paciente = paciente; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
}
