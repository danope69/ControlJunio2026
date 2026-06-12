package us.dit.muit.fs.gestordispositivos.control;
import us.dit.muit.fs.gestordispositivos.modelo.Dispositivo;
/**
 * Interfaz que define las operaciones de gestión de dispositivos médicos 
 * Un gestor de dispositivos es responsable de asignar dispositivos a pacientes, liberarlos cuando 
 * el paciente termina su tratamiento, enviar dispositivos a mantenimiento cuando se detecta un fallo, 
 * dar de baja dispositivos que ya no se usan y registrar nuevos dispositivos en el sistema.
 */

public interface GestorDispositivos {
    /**
     * Asigna un dispositivo de un tipo específico a un paciente. El método debe buscar un dispositivo del tipo solicitado que esté libre,
     * asignarlo al paciente y devolver el número de serie del dispositivo asignado. 
     * Si no hay dispositivos libres del tipo solicitado, el método debe devolver la excepción IllegalArgumentException con el mensaje 
     * "Dispositivos de tipo {tipoDispositivo} no disponibles".
     * Si el paciente tiene ya un dispositivo asignado del mismo tipo, el método debe devolver la excepción IllegalArgumentException con el mensaje 
     * "Paciente {idPaciente} ya tiene un dispositivo de tipo {tipoDispositivo}
     * @param idPaciente
     * @param tipoDispositivo
     * @return numero de serie del dispositivo asignado, o null si no hay dispositivos libres del tipo solicitado.
     */
    public String asignarDispositivo(String idPaciente, Dispositivo.TipoDispositivo tipoDispositivo) throws IllegalArgumentException;
    /**
     * Libera un dispositivo asignado a un paciente. El método debe marcar el dispositivo como libre y eliminar la asociación con el paciente.
     * Si paciente no tiene asignado un dispositivo de este tipo debe devolver la excepción IllegalArgumentException 
     * con el mensaje "Dispositivo de tipo {tipoDispositivo} no asignado al paciente {idPaciente}".
     * 
     * @param idPaciente
     * @param tipoDispositivo
     */
    public void liberarDispositivo(String idPaciente, Dispositivo.TipoDispositivo tipoDispositivo) throws IllegalArgumentException;
    /**
     * Envía un dispositivo a mantenimiento. El método debe marcar el dispositivo como en mantenimiento.
     * Si el dispositivo está asignado a un paciente el método debe asignarle otro dispositivo de este tipo y devolver
     * el número de serie del nuevo dispositivo asignado. 
     * Si no hay dispositivos libres del tipo solicitado, el método debe devolver la excepción IllegalArgumentException con el mensaje 
     * "Dispositivos de tipo {tipoDispositivo} no disponibles para reasignar al paciente {idPaciente}"
     * El dispositivo se manda a mantenimiento pero sigue teniendo asignado al paciente, con la finalidad
     * de devolvérselo una vez resuelta la incidencia.
     * @param idDispositivo
     * @return numero de serie del nuevo dispositivo asignado al paciente, o null si el dispositivo no estaba asignado a ningún paciente.
     */
    public String enviarMantenimiento(String idDispositivo);
    /**
     * Resuelve el mantenimiento de un dispositivo. Si el dispositivo estaba asignado a un paciente, 
     * el método debe devolverle el mismo dispositivo al paciente, marcándolo como asignado y eliminando
     * la marca de en mantenimiento.
     * Si no estaba asignado se marca como libre.
     * Devuelve el id del paciente al que se le ha devuelto el dispositivo, o null si el dispositivo no estaba asignado a ningún paciente.
     * @param idDispositivo
     * @return id del paciente al que se le ha devuelto el dispositivo, o null si el dispositivo no estaba asignado a ningún paciente.
     */
    public String resolverMantenimiento(String idDispositivo);
    /**
     * Registra un nuevo dispositivo en el sistema. El método debe crear una instancia de Dispositivo y añadirla a la lista de dispositivos.
     * Si ya existe un dispositivo con el mismo número de serie, el método debe devolver la excepción IllegalArgumentException con el mensaje 
     * "Dispositivo con Numero de Serie {numeroSerie} ya existe".
     * @param modelo
     * @param tipo
     * @param numeroSerie
     */
    public void altaDispositivo(String modelo, Dispositivo.TipoDispositivo tipo, String numeroSerie);
    /**
     * Da de baja un dispositivo en el sistema. El método debe marcar el dispositivo como dado de baja, lo que implica que ya no estará disponible para asignación ni mantenimiento.
     * Si el dispositivo no existe, el método debe devolver la excepción IllegalArgumentException 
     * con el mensaje "Dispositivo con Numero de Serie {numeroSerie} no existe".
     * Si el dispositivo está asignado a un paciente, el método debe devolver una excepción IllegalArgumentException 
     * con el mensaje 
     * "Dispositivo con Numero de Serie {numeroSerie} está asignado al paciente {idPaciente}" y no se puede dar de baja
     * @param numeroSerie
     */
    public void bajaDispositivo(String numeroSerie);

}