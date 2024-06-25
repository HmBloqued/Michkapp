package services;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import datas.jdbcDataAccess;
import models.Furniture;
import models.FurnitureStateInventory;
import models.Inventory;
import models.Property;
import models.Room;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class PdfWriterService {
    public static boolean createInventoryPdf(Inventory inventory, Property property) {
        Document document = new Document();

        try {
            String currentDir = System.getProperty("user.dir");
            Path path = Paths.get(currentDir + "/downloads");
            Files.createDirectories(path);

            String filename = currentDir + "/downloads/" +
                    property.getAddress().getStreetNumber() +
                    "_" + property.getAddress().getStreetName() +
                    "_" + property.getAddress().getZipCode() +
                    "_" + property.getAddress().getCity() +
                    "-" + new java.text.SimpleDateFormat("yyyy-MM-dd:hh-mm-ss", Locale.FRANCE).format(new java.util.Date()) +
                    ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filename));

            // Prepare font and separator
            LineSeparator separator = new LineSeparator();
            separator.setLineColor(BaseColor.BLACK);
            separator.setLineWidth(1);
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fontMedium = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph pictures = new Paragraph("Annexes\n\n", fontTitle);
            PdfPTable table;
            PdfPCell cell;
            Paragraph roomName;
            int annex = 1;

            document.open();

            Paragraph title = new Paragraph("MICHK'APP");
            title.getFont().setSize(32);
            title.getFont().setStyle("bold");
            document.add(title);
            Paragraph subtitle = new Paragraph("État des lieux");
            subtitle.getFont().setSize(20);
            document.add(subtitle);

            document.add(new Paragraph("\n"));
            document.add(separator);

            // Add property information
            document.add(new Paragraph(
                    "Adresse : " + property.getAddress().getStreetNumber() + " " + property.getAddress().getStreetName()
                            + ", " + property.getAddress().getZipCode() + " " + property.getAddress().getCity()));
            document.add(new Paragraph(
                    "Propriétaire : " + property.getOwner().getFirstname() + " " + property.getOwner().getLastname()));
            document.add(new Paragraph(
                    "Date : " + new java.text.SimpleDateFormat("yyyy-MM-dd:hh-mm-ss", Locale.FRANCE).format(new java.util.Date())));
            document.add(new Paragraph("\n"));

            // Show if owner and tenants were present during inventory
            if (inventory.getIsOwnerPresent()) {
                document.add(new Paragraph("Le propriétaire était présent lors de l'état des lieux."));
            } else {
                document.add(new Paragraph("Le propriétaire n'était pas présent lors de l'état des lieux."));
            }
            if (inventory.getIsOccupantPresent()) {
                document.add(new Paragraph("L'occupant était présent lors de l'état des lieux."));
            } else {
                document.add(new Paragraph("L'occupant n'était pas présent lors de l'état des lieux."));
            }

            document.add(new Paragraph("\n"));
            document.add(separator);

            document.add(new Paragraph("\n\n"));

            // Get datas to fill the table
            jdbcDataAccess dataAccess = new jdbcDataAccess();
            List<Room> rooms = dataAccess.getRoomsByProperty(property);

            for (Room room : rooms) {
                List<Furniture> furnitures = dataAccess.getFurnituresByRoom(room);

                roomName = new Paragraph(room.getName());
                roomName.getFont().setSize(15);
                document.add(new Paragraph("\n"));
                document.add(roomName);
                document.add(new Paragraph("\n"));

                // Prepare a table for each room
                table = new PdfPTable(6);
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Élément", fontBold));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Position", fontBold));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("État", fontBold));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Note", fontBold));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Image", fontBold));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Date", fontBold));
                table.addCell(cell);

                // Then get all furniture for each room
                for (Furniture furniture : furnitures) {
                    // Get furniture
                    FurnitureStateInventory furnitureStateInventory = dataAccess
                            .getFurnitureStateInventoryFromInventoryFurniture(inventory, furniture);

                    cell = new PdfPCell(new Paragraph(furniture.getName(), fontMedium));
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(furniture.getPosition(), fontMedium));
                    table.addCell(cell);

                    cell = new PdfPCell(
                            new Paragraph(getLabel(furnitureStateInventory.getFurnitureState()), fontMedium));
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(furnitureStateInventory.getComment(), fontMedium));
                    table.addCell(cell);

                    // Add image if there is one to an other part
                    if (furnitureStateInventory.getPicture() != null) {
                        Image img = Image.getInstance(furnitureStateInventory.getPicture());

                        // Scale image if necessary
                        img.scaleToFit(400, 300); // Redimensionner l'image à la taille souhaitée

                        // Ajouter l'image à un paragraphe
                        pictures.add(new Paragraph(
                                "Annexe " + annex++ + " : " + furniture.getName() + " - " + room.getName()));
                        pictures.add(img);
                        pictures.add(new Paragraph("\n"));

                        String annexString = "Annexe " + (annex - 1);
                        cell = new PdfPCell(new Paragraph(annexString, fontMedium));
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Paragraph(" ", fontMedium));
                        table.addCell(cell);
                    }

                    Date pictureDate = furnitureStateInventory.getDatetime();
                    cell = new PdfPCell(new Paragraph(new java.text.SimpleDateFormat("yyyy-MM-dd:hh-mm", Locale.FRANCE).format(pictureDate), fontMedium));
                    table.addCell(cell);
                }
                document.add(table);
            }

            if (annex > 1) {
                document.newPage();
                document.add(pictures);
            }

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document
            document.close();
        }

        System.out.println("PDF créé avec succès !");
        return true;
    }

    private static String getLabel(models.State state) {
        switch (state) {
            case NEW:
                return "Neuf";
            case GOOD:
                return "Bon";
            case BAD:
                return "Mauvais";
            case MISSING:
                return "Manquant";
            default:
                return "Inconnu";
        }
    }
}
