
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import model.Car;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestingFiles {

    private ClassLoader cl = TestingFiles.class.getClassLoader();
    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    void xlsxFileInZipParsingTest() throws Exception {
        try (ZipInputStream inputStream = new ZipInputStream(
                cl.getResourceAsStream("zip/Files.zip")
        )) {
            ZipEntry entry;
            String i = "Filexlsx.xlsx";
            boolean file = false;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (entry.getName().equals(i)) {
                    file = true;
                    XLS xls = new XLS(inputStream);
                    String actualValue = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                    assertTrue(actualValue.contains("Тихо"));
                }
            }
            assertTrue(file, i + " отсутствует в архиве");
        }
    }

    @Test
    void pdfFileInZipParsingTest() throws Exception {
        try (ZipInputStream inputStream = new ZipInputStream(
                cl.getResourceAsStream("zip/Files.zip")
        )) {
            ZipEntry entry;
            String i = "Filepdf.pdf";
            boolean file = false;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (entry.getName().equals(i)) {
                    file = true;
                    PDF pdf = new PDF(inputStream);
                    assertEquals(1,pdf.numberOfPages);
                }
            }
            assertTrue(file, i + " отсутствует в архиве");
        }
    }

    @Test
    void csvFileInZipParsingTest() throws Exception {
        try (ZipInputStream inputStream = new ZipInputStream(
                cl.getResourceAsStream("zip/Files.zip")
        )) {
            ZipEntry entry;
            String i = "Filecsv.csv";
            boolean file = false;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(i) ) {
                    file = true;
                    CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

                    List<String[]> data = csvReader.readAll();
                    assertEquals(1, data.size());
                }
            }
            assertTrue(file, i + " отсутствует в архиве");
        }
    }

    @Test
    void jsonFileParsing() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("json/car.json")
        )) {
            Car actual = mapper.readValue(reader, Car.class);
            assertEquals("Toyota", actual.getValue());
            assertEquals("Supra", actual.getData().getModel());
            assertEquals("12345", actual.getData().getID());
        }
    }
}
