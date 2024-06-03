import java.util.ArrayList;
import java.util.HashMap;

class Persona {
    protected int id;
    protected String nombre;
    protected String apellido;
    protected String fechaDeNacimiento;

    public Persona(int id, String nombre, String apellido, String fechaDeNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}

class Estudiante extends Persona {
    private String estado;

    public Estudiante(int id, String nombre, String apellido, String fechaDeNacimiento, String estado) {
        super(id, nombre, apellido, fechaDeNacimiento);
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }
}

class Curso {
    private int id;
    private String nombre;
    private String descripcion;
    private int numeroCreditos;
    private String version;

    public Curso(int id, String nombre, String descripcion, int numeroCreditos, String version) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroCreditos = numeroCreditos;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNumeroCreditos() {
        return numeroCreditos;
    }

    public String getVersion() {
        return version;
    }
}

interface ServiciosAcademicosI {
    void matricularEstudiante(Estudiante estudiante);
    void agregarCurso(Curso curso);
    void inscribirEstudianteCurso(Estudiante estudiante, int idCurso) throws EstudianteYaInscritoException;
    void desinscribirEstudianteCurso(int idEstudiante, int idCurso) throws EstudianteNoInscritoEnCursoException;
}

class GestorAcademico implements ServiciosAcademicosI {
    private ArrayList<Estudiante> estudiantes;
    private ArrayList<Curso> cursos;
    private HashMap<Integer, ArrayList<Integer>> estudiantesPorCurso;

    public GestorAcademico() {
        this.estudiantes = new ArrayList<>();
        this.cursos = new ArrayList<>();
        this.estudiantesPorCurso = new HashMap<>();
    }

    @Override
    public void matricularEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    @Override
    public void agregarCurso(Curso curso) {
        cursos.add(curso);
    }

    @Override
    public void inscribirEstudianteCurso(Estudiante estudiante, int idCurso) throws EstudianteYaInscritoException {
        // Verificamos si el estudiante está matriculado
        if (!estudiantes.contains(estudiante)) {
            throw new IllegalArgumentException("El estudiante no está matriculado.");
        }

        // Verificamos si el estudiante ya está inscrito en el curso
        if (estudiantesPorCurso.containsKey(idCurso) && estudiantesPorCurso.get(idCurso).contains(estudiante.id)) {
            throw new EstudianteYaInscritoException("El estudiante ya está inscrito en el curso.");
        }

        // Inscribimos al estudiante en el curso
        if (!estudiantesPorCurso.containsKey(idCurso)) {
            estudiantesPorCurso.put(idCurso, new ArrayList<>());
        }
        estudiantesPorCurso.get(idCurso).add(estudiante.id);
    }

    @Override
    public void desinscribirEstudianteCurso(int idEstudiante, int idCurso) throws EstudianteNoInscritoEnCursoException {
        // Verificamos si el estudiante está inscrito en el curso
        if (!estudiantesPorCurso.containsKey(idCurso) || !estudiantesPorCurso.get(idCurso).contains(idEstudiante)) {
            throw new EstudianteNoInscritoEnCursoException("El estudiante no está inscrito en el curso o el ID del curso no es válido.");
        }

        // Desinscribimos al estudiante del curso
        estudiantesPorCurso.get(idCurso).remove(Integer.valueOf(idEstudiante));
    }

    // Método para imprimir estudiantes
    public void imprimirEstudiantes() {
        System.out.println("---ESTUDIANTES---");
        for (Estudiante estudiante : estudiantes) {
            System.out.println("- " + estudiante.nombre);
        }
    }

    // Método para imprimir cursos
    public void imprimirCursos() {
        System.out.println("---CURSOS---");
        for (Curso curso : cursos) {
            System.out.println("- " + curso.getNombre());
        }
    }

    // Método para imprimir qué estudiante está inscrito en qué curso
    public void imprimirEstudiantesPorCurso() {
        for (Curso curso : cursos) {
            System.out.println("Curso " + curso.getNombre() + ":");
            ArrayList<Integer> estudiantesIds = estudiantesPorCurso.get(curso.getId());
            if (estudiantesIds != null) {
                for (Integer estudianteId : estudiantesIds) {
                    for (Estudiante estudiante : estudiantes) {
                        if (estudiante.id == estudianteId) {
                            System.out.println("- " + estudiante.getNombre());
                        }
                    }
                }
            }
            System.out.println();
        }
    }
}

class EstudianteYaInscritoException extends Exception {
    public EstudianteYaInscritoException(String mensaje) {
        super(mensaje);
    }
}

class EstudianteNoInscritoEnCursoException extends Exception {
    public EstudianteNoInscritoEnCursoException(String mensaje) {
        super(mensaje);
    }
}

public class Main {
    public static void main(String[] args) {
        // Crear una instancia del gestor académico
        GestorAcademico gestor = new GestorAcademico();

        // Agregar algunos estudiantes por defecto
        Estudiante estudiante1 = new Estudiante(1, "Juan", "Perez", "2000-01-01", "matriculado");
        Estudiante estudiante2 = new Estudiante(2, "María", "García", "2001-02-02", "matriculado");

        // Matricular estudiantes
        gestor.matricularEstudiante(estudiante1);
        gestor.matricularEstudiante(estudiante2);

        // Agregar algunos cursos por defecto
        Curso curso1 = new Curso(1, "Programación Java", "Curso introductorio a Java", 4, "1.0");
        Curso curso2 = new Curso(2, "Base de Datos", "Introducción a las bases de datos", 3, "1.0");

        // Agregar cursos
        gestor.agregarCurso(curso1);
        gestor.agregarCurso(curso2);

        // Inscribir estudiantes en cursos
        try {
            gestor.inscribirEstudianteCurso(estudiante1, curso1.getId());
            gestor.inscribirEstudianteCurso(estudiante1, curso2.getId());
            gestor.inscribirEstudianteCurso(estudiante2, curso1.getId());
        } catch (EstudianteYaInscritoException e) {
            System.out.println(e.getMessage());
        }

        // Imprimir estudiantes
        gestor.imprimirEstudiantes();

        // Imprimir cursos
        gestor.imprimirCursos();

        // Imprimir estudiantes por curso
        gestor.imprimirEstudiantesPorCurso();

        // Desinscribir un estudiante de un curso (ejemplo)
        try {
            gestor.desinscribirEstudianteCurso(estudiante1.id, curso1.getId());
        } catch (EstudianteNoInscritoEnCursoException e) {
            System.out.println(e.getMessage());
        }
    }
}
