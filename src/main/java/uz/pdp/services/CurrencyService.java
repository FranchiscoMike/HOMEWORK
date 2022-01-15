package uz.pdp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import uz.pdp.model.Currency;
import uz.pdp.raspiratory.DataBase;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CurrencyService {

    public static final Scanner SCANNER_NUM = new Scanner(System.in);
    public static final Scanner SCANNER_STR = new Scanner(System.in);

    static String path = "src/main/resources/";

    public static void showByDate() {
        //   https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/2018-12-11/
        System.out.println("Enter the date you wanted : (2018-12-11)");

        String date = SCANNER_STR.nextLine();

        LocalDate localDate = LocalDate.parse(date);

        try {
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + localDate + "/");

            URLConnection urlConnection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Type type = new TypeToken<List<Currency>>() {
            }.getType();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            List<Currency> dateCurrencies = gson.fromJson(reader, type);

            DataBase.currenciesByDate.addAll(dateCurrencies);

            // ekranga chiqaradi bu
//            DataBase.currenciesByDate.forEach(System.out::println);

            File datePDF = new File(path + "currencyByDate.pdf");

            datePDF.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(datePDF);

            PdfWriter pdfWriter = new PdfWriter(outputStream);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            pdfDocument.addNewPage();
            Paragraph paragraph = new Paragraph("Currencies in : " + localDate);
            paragraph.setBold();
            paragraph.setFontSize(22);
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document.add(paragraph);

            Table table = new Table(5);

            table.addCell("T/r ");
            table.addCell("Name");
            table.addCell("Rate");
            table.addCell("Diff");
            table.addCell("Code");

            int tr = 1;
            for (Currency currency : DataBase.currenciesByDate) {
                table.addCell((tr++) + "");
                table.addCell(currency.getCcyNm_EN());
                table.addCell(currency.getRate());
                table.addCell(currency.getDiff());
                table.addCell(currency.getCode());
            }

            document.add(table);
            pdfDocument.close();
            outputStream.close();


            Desktop.getDesktop().open(datePDF);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    public static void showAll() {

        try {
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");

            URLConnection urlConnection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type type = new TypeToken<List<Currency>>() {
            }.getType();


            List<Currency> latestCurrencies = gson.fromJson(reader, type);

            DataBase.currencies.addAll(latestCurrencies);


            // ekranga chiqarish
//            for (Currency latestCurrency : latestCurrencies) {
//                System.out.println(latestCurrency);
//            }


            System.out.println("1.WORD appearance ");
            System.out.println("2.EXCEL appearance ");
            System.out.println("3.PDF appearance ");

            int option = 0;
            System.out.print("Choose : ");
            try {
                option = SCANNER_NUM.nextInt();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            switch (option) {
                case 1 -> {
                    showFileWord();
                }
                case 2 -> {
                    showEXCEL();
                }
                case 3 -> {
                    showPDF();
                }
                default -> {
                    System.out.println("Wrong option ");
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showPDF() {

        File pdf = new File(path + "currencyPDF.pdf");

        try {
            pdf.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(pdf);

            PdfWriter pdfWriter = new PdfWriter(outputStream);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph("Currencies :");

            document.add(paragraph);
            Table table = new Table(5);

            table.addCell("T/r ");
            table.addCell("Name");
            table.addCell("Rate");
            table.addCell("Diff");
            table.addCell("Code");

            int tr = 1;
            for (Currency currency : DataBase.currencies) {
                table.addCell((tr++) + "");
                table.addCell(currency.getCcyNm_EN());
                table.addCell(currency.getRate());
                table.addCell(currency.getDiff());
                table.addCell(currency.getCode());
            }

            document.add(table);
            pdfDocument.close();
            outputStream.close();
            Desktop.getDesktop().open(pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SneakyThrows
    private static void showEXCEL() {
        // creating workbook

        File excel = new File(path + "currencyEXCEL.xlsx");
        try {
            excel.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(excel);

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Currencies");
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("T/r ");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Rate");
            row.createCell(3).setCellValue("Diff");
            row.createCell(4).setCellValue("Code");

            for (int i = 0; i < DataBase.currencies.size(); i++) {
                XSSFRow row1 = sheet.createRow(i + 1);

                row1.createCell(0).setCellValue((i + 1) + "");
                row1.createCell(1).setCellValue(DataBase.currencies.get(i).getCcyNm_EN());
                row1.createCell(2).setCellValue(DataBase.currencies.get(i).getRate());
                row1.createCell(3).setCellValue(DataBase.currencies.get(i).getDiff());
                row1.createCell(4).setCellValue(DataBase.currencies.get(i).getCode());
            }

            // avtomatik moslashadi
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);


            workbook.write(outputStream);
            outputStream.close();
            Desktop.getDesktop().open(excel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private static void showFileWord() {
        // creating word

        File word = new File(path + "currencyWORD.docx");
        try {

            word.createNewFile();

            XWPFDocument document = new XWPFDocument();

            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun run = paragraph.createRun();
            run.setBold(true);

            run.setText("Currencies in " + LocalDate.now());

            run.setStyle("Consolas");
            XWPFTable table = document.createTable();
            table.setWidth("100%");
            XWPFTableRow row = table.getRow(0);


            row.getCell(0).setText("T/r ");
            row.createCell().setText("Name");
            row.createCell().setText("Rate");
            row.createCell().setText("Diff");
            row.createCell().setText("Code");

            for (int i = 0; i < DataBase.currencies.size(); i++) {
                // har safar yangi qator yaratib keyin qo'shib ketiladi

                XWPFTableRow tableRow = table.createRow();

                tableRow.getCell(0).setText("" + (i + 1));
                tableRow.getCell(1).setText(DataBase.currencies.get(i).getCcyNm_EN());
                tableRow.getCell(2).setText(DataBase.currencies.get(i).getRate());
                tableRow.getCell(3).setText(DataBase.currencies.get(i).getDiff());
                tableRow.getCell(4).setText(DataBase.currencies.get(i).getCode());

            }


            FileOutputStream outputStream = new FileOutputStream(word);
            document.write(outputStream);
            document.close();
            outputStream.close();
            Desktop.getDesktop().open(word);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void showByName() {
        System.out.println("Enter the date you wanted : (2018-12-11)");

        String date = SCANNER_STR.nextLine();

        LocalDate localDate = LocalDate.parse(date);

        try {
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + localDate + "/");

            URLConnection urlConnection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Type type = new TypeToken<List<Currency>>() {
            }.getType();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            List<Currency> dateCurrencies = gson.fromJson(reader, type);

            DataBase.currenciesByDate.addAll(dateCurrencies);

            int count = 1;
            for (Currency currency : DataBase.currenciesByDate) {
                System.out.println((count++) + ". " + currency.getCcy());
            }

            System.out.print("Choose :");
            int option = SCANNER_NUM.nextInt();

//            System.out.println(DataBase.currenciesByDate.get(option - 1));

            Currency currency = DataBase.currenciesByDate.get(option - 1);

            System.out.println(gson.toJson(currency));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void converter() {

    }
}
