package org.example.agenciadeviajes.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import org.example.agenciadeviajes.model.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;

public class PDFReservaGenerator {

    public static void generarPDF(Reserva reserva) {

        try {

            String nombreArchivo =
                    "Reserva_" +
                            reserva.getIdReserva() +
                            ".pdf";

            Document document =
                    new Document();

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(nombreArchivo)
            );

            document.open();

            // TÍTULO
            Font tituloFont =
                    new Font(
                            Font.HELVETICA,
                            22,
                            Font.BOLD
                    );

            Paragraph titulo =
                    new Paragraph(
                            "COMPROBANTE DE RESERVA",
                            tituloFont
                    );

            titulo.setAlignment(Element.ALIGN_CENTER);

            document.add(titulo);

            document.add(new Paragraph(" "));

            // DATOS GENERALES
            document.add(new Paragraph(
                    "Folio: " +
                            reserva.getFolio()
            ));

            document.add(new Paragraph(
                    "Cliente: " +
                            reserva.getUsuario().getNombreCompleto()
            ));

            document.add(new Paragraph(
                    "Correo: " +
                            reserva.getUsuario().getCorreo()
            ));

            document.add(new Paragraph(
                    "Tipo: " +
                            reserva.getTipoReserva()
            ));

            document.add(new Paragraph(
                    "Fecha: " +
                            reserva.getFechaReserva()
            ));

            document.add(new Paragraph(" "));

            // VUELOS
            if (!reserva.getDetallesVuelo().isEmpty()) {

                document.add(
                        new Paragraph(
                                "VUELOS",
                                new Font(
                                        Font.HELVETICA,
                                        18,
                                        Font.BOLD
                                )
                        )
                );

                for (DetalleReservaVuelo dv :
                        reserva.getDetallesVuelo()) {

                    document.add(
                            new Paragraph(
                                    dv.toString()
                            )
                    );
                }

                document.add(new Paragraph(" "));
            }

            // HOTELES
            if (!reserva.getDetallesHotel().isEmpty()) {

                document.add(
                        new Paragraph(
                                "HOTELES",
                                new Font(
                                        Font.HELVETICA,
                                        18,
                                        Font.BOLD
                                )
                        )
                );

                for (DetalleReservaHotel dh :
                        reserva.getDetallesHotel()) {

                    document.add(
                            new Paragraph(
                                    dh.toString()
                            )
                    );
                }

                document.add(new Paragraph(" "));
            }

            // AUTOS
            if (!reserva.getDetallesAuto().isEmpty()) {

                document.add(
                        new Paragraph(
                                "AUTOS",
                                new Font(
                                        Font.HELVETICA,
                                        18,
                                        Font.BOLD
                                )
                        )
                );

                for (DetalleReservaAuto da :
                        reserva.getDetallesAuto()) {

                    document.add(
                            new Paragraph(
                                    da.toString()
                            )
                    );
                }

                document.add(new Paragraph(" "));
            }

            // TOTAL
            Font totalFont =
                    new Font(
                            Font.HELVETICA,
                            18,
                            Font.BOLD
                    );

            Paragraph total =
                    new Paragraph(
                            "TOTAL PAGADO: $" +
                                    reserva.getTotalPagado() +
                                    " " +
                                    reserva.getCodigoDivisa(),
                            totalFont
                    );

            total.setAlignment(Element.ALIGN_RIGHT);

            document.add(total);

            document.close();
            java.awt.Desktop.getDesktop().open(
                    new java.io.File(nombreArchivo)
            );

            System.out.println(
                    "PDF generado correctamente."
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}