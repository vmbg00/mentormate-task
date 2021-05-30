import com.opencsv.CSVWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        JSONParser jsonParser = new JSONParser();

        List<Data> employees = new ArrayList<>();
        List<ReportDefinition> reports = new ArrayList<>();
        List<FinalEmployee> finalEmployees = new ArrayList<>();

        System.out.println("Enter JSON data file path: ");
        String jsonDataFilePath = scanner.nextLine();

        dataParser(jsonDataFilePath, jsonParser, employees);

        System.out.println("Enter JSON reports file path: ");
        String jsonReportsFilePath = scanner.nextLine();

        reportParser(jsonReportsFilePath, jsonParser, reports);

        ReportDefinition reportDefinition = reports.get(0);

        for (Data employee : employees) {
            if (employee.getSalesPeriod() <= reportDefinition.getPeriodLimit()){
                if (reportDefinition.isUserExperienceMultiplier()) {
                    employee.setScore((double) (employee.getTotalSales() / employee.getSalesPeriod()) * employee.getExperienceMultiplier());
                }
                else {
                    employee.setScore((double) employee.getTotalSales() / employee.getSalesPeriod());
                }

                if (employee.getScore() >= reportDefinition.getTopPerformersThreshold()) {
                    FinalEmployee finalEmployee = new FinalEmployee();

                    finalEmployee.setName(employee.getName());
                    finalEmployee.setScore(employee.getScore());

                    finalEmployees.add(finalEmployee);
                }
            }
        }

        writeCSVFile(finalEmployees);
    }

    public static void writeCSVFile(List<FinalEmployee> employees)
    {
        File file = new File("C:\\Result.csv");
        try {
            FileWriter outputFile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputFile);

            String[] header = { "Name" , "Score" };
            writer.writeNext(header);

            for (FinalEmployee employee : employees) {
                String[] data = new String[2];
                data[0] = employee.getName();
                data[1] = String.valueOf(employee.getScore());

                writer.writeNext(data);
            }

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void reportParser(String jsonReportsFilePath, JSONParser jsonParser, List<ReportDefinition> reports) {
        try (FileReader reader = new FileReader(jsonReportsFilePath)) {
            Object obj = jsonParser.parse(reader);

            parseReport((JSONObject) obj, reports);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void dataParser(String jsonDataFile, JSONParser jsonParser, List<Data> employees) {

        try (FileReader reader = new FileReader(jsonDataFile)) {
            Object obj = jsonParser.parse(reader);

            JSONArray list = (JSONArray) obj;
            System.out.println(list);

            list.forEach(e -> parseEmployee((JSONObject) e, employees));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseReport(JSONObject object, List<ReportDefinition> reports) {
        ReportDefinition report = new ReportDefinition();

        Boolean userExperienceMultiplier = (Boolean) object.get("userExperienceMultiplier");
        report.setUserExperienceMultiplier(userExperienceMultiplier);

        Long topPerformersThreshold = (Long) object.get("topPerformersThreshold");
        report.setTopPerformersThreshold(topPerformersThreshold);

        Long periodLimit = (Long) object.get("periodLimit");
        report.setPeriodLimit(periodLimit);

        reports.add(report);
    }

    private static void parseEmployee(JSONObject obj, List<Data> employees)
    {
        Data employee = new Data();

        String name = (String) obj.get("name");
        employee.setName(name);

        Long totalSales = (Long) obj.get("totalSales");
        employee.setTotalSales(totalSales);

        Long salesPeriod = (Long) obj.get("salesPeriod");
        employee.setSalesPeriod(salesPeriod);

        double experienceMultiplier = (double) obj.get("experienceMultiplier");
        employee.setExperienceMultiplier(experienceMultiplier);

        employees.add(employee);
    }
}
