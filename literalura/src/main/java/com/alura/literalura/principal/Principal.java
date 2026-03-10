package com.alura.literalura.principal;

import com.alura.literalura.dto.DatosRespuesta;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu() {

        int opcion = -1;

        while (opcion != 0) {

            var menu = """

                ********** LITERALURA **********

                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma

                0 - Salir

                ********************************
                """;

            System.out.println(menu);

            try {

                String entrada = teclado.nextLine();
                opcion = Integer.parseInt(entrada);

                switch (opcion) {

                    case 1:
                        buscarLibro();
                        break;

                    case 2:
                        listarLibros();
                        break;

                    case 3:
                        listarAutores();
                        break;

                    case 4:
                        listarAutoresVivos();
                        break;

                    case 5:
                        listarLibrosPorIdioma();
                        break;

                    case 0:
                        System.out.println("Cerrando aplicación...");
                        break;

                    default:
                        System.out.println("Opción inválida");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠ Debe ingresar un número válido.");
            }
        }
    }

    private void buscarLibro() {

        System.out.println("Ingrese el nombre del libro que desea buscar:");

        var nombreLibro = teclado.nextLine();

        ConsumoAPI consumo = new ConsumoAPI();

        var json = consumo.obtenerDatos(
                "https://gutendex.com/books/?search=" + nombreLibro.replace(" ", "%20")
        );

        ConvierteDatos conversor = new ConvierteDatos();

        DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

        System.out.println(datos);
    }

    private void listarLibros() {

        var libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        libros.forEach(libro -> {
            System.out.println("-----------");
            System.out.println("Libro: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getNumeroDescargas());
        });
    }

    private void listarAutores() {

        var autores = autorRepository.findAll();

        autores.forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Fallecimiento: " + autor.getFechaFallecimiento());
        });
    }

    private void listarAutoresVivos(){
        System.out.println("Ingrese el año:");
    }

    private void listarLibrosPorIdioma(){
        System.out.println("Ingrese idioma (es, en, fr, pt)");
    }
}
