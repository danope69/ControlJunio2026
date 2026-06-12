package us.dit.muit.fs.gestordispositivos.control;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import us.dit.muit.fs.gestordispositivos.modelo.Dispositivo;


/**
 * Clase que implementa la interfaz GestorDispositivos, realiza las labores de gestor de dispositivos 
 * médicos para un centro sanitario.
 * Un gestor de dispositivos es responsable de asignar dispositivos a pacientes, 
 * liberarlos cuando el paciente termina su tratamiento, enviar dispositivos a mantenimiento 
 * cuando se detecta un fallo, dar de alta y baja dispositivos en el sistema de gestión.
 */
public class GestorDispositivosCentro implements GestorDispositivos {    
    private static final Logger logger = LogManager.getLogger(GestorDispositivosCentro.class);
    private String idCentro; // Identificador unívoco del centro
    private DispositivoDAO dispositivoDAO; // DAO para manejar la persistencia de dispositivos
   

    public GestorDispositivosCentro(String idCentro, DispositivoDAO dispositivoDAO) {
        this.dispositivoDAO = dispositivoDAO;
        this.idCentro = idCentro;
     
    }


    public String getIdCentro() { return idCentro; }

    @Override
    public String asignarDispositivo(String idPaciente, Dispositivo.TipoDispositivo tipoDispositivo) throws IllegalArgumentException {
        String numeroSerie = null;
       
        // Buscar un dispositivo libre del tipo solicitado
        List<Dispositivo> dispositivosTipo = dispositivoDAO.getDispositivosByType(tipoDispositivo);
        for (Dispositivo d : dispositivosTipo) {
            if (d.getEstado() == Dispositivo.EstadoDispositivo.LIBRE) {
                d.setEstado(Dispositivo.EstadoDispositivo.ASIGNADO);
                d.setPaciente(idPaciente);
                dispositivoDAO.updateDispositivo(d);
                numeroSerie = d.getNumeroSerie();
                break;
            }
        }
       
        return numeroSerie;
    }

    @Override
    public void liberarDispositivo(String idPaciente, Dispositivo.TipoDispositivo tipoDispositivo) throws IllegalArgumentException {
        Dispositivo dispositivoAsignado = null;
        List<Dispositivo> dispositivosPaciente = dispositivoDAO.getDispositivosByPatient(idPaciente);
        for (Dispositivo d : dispositivosPaciente) {
            if (d.getTipo() == tipoDispositivo) {
                dispositivoAsignado = d;
                break;
            }
        }
       
        dispositivoAsignado.setEstado(Dispositivo.EstadoDispositivo.LIBRE);
        dispositivoAsignado.setPaciente(null);
        dispositivoDAO.updateDispositivo(dispositivoAsignado);  
    }

    @Override
    public String enviarMantenimiento(String numeroSerie) {
        String newDispositivo=null;
        Dispositivo dispositivo = dispositivoDAO.getDispositivoByNumSerie(numeroSerie);
        if (dispositivo == null) {
            throw new IllegalArgumentException("Dispositivo con Numero de Serie " + numeroSerie + " no existe");
        }
        String idPaciente = dispositivo.getPaciente();
         
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.EN_MANTENIMIENTO);
        dispositivo.setPaciente(null);
       
        dispositivoDAO.updateDispositivo(dispositivo);    
        if(idPaciente != null) {
             newDispositivo = asignarDispositivo(idPaciente, dispositivo.getTipo());
           
             
        }       
        return newDispositivo;
    }

    @Override
    public String resolverMantenimiento(String numeroSerie) {
        String idPaciente = null;
        Dispositivo dispositivo = dispositivoDAO.getDispositivoByNumSerie(numeroSerie);
       
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.LIBRE);
        dispositivoDAO.updateDispositivo(dispositivo);
        // Si el dispositivo estaba asignado a un paciente, devolverle el mismo dispositivo
        if (dispositivo.getPaciente() != null) {
            idPaciente = dispositivo.getPaciente();
            dispositivo.setEstado(Dispositivo.EstadoDispositivo.ASIGNADO);
            dispositivoDAO.updateDispositivo(dispositivo);
        }
        return idPaciente;
    }

    @Override
    public void altaDispositivo(String modelo, Dispositivo.TipoDispositivo tipo, String numeroSerie) {
       dispositivoDAO.addDispositivo(new Dispositivo(tipo, modelo, numeroSerie));
    }

    @Override
    public void bajaDispositivo(String numeroSerie) {
        Dispositivo dispositivo = dispositivoDAO.getDispositivoByNumSerie(numeroSerie);
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.BAJA);
        dispositivoDAO.updateDispositivo(dispositivo);
    }

}