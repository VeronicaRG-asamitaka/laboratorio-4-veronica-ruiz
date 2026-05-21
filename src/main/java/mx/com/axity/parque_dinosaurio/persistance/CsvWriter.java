package mx.com.axity.parque_dinosaurio.persistance;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter {

    private final Path revenuePath;
    private final Path expensePath;
    private final Path eventPath;

    public CsvWriter(String outputDir) throws IOException {
        
        Path dir = Paths.get(outputDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        this.revenuePath = dir.resolve("revenues.csv");
        this.expensePath = dir.resolve("expenses.csv");
        this.eventPath = dir.resolve("events.csv");

        initFile(revenuePath, "id,type,amount,touristId,zone,timestamp");
        initFile(expensePath, "id,type,amount,description,timestamp");
        initFile(eventPath, "step,eventName,description,affectedEntities,timestamp");
    }

    private void initFile(Path path, String header) throws IOException {
        try (FileWriter fw = new FileWriter(path.toFile(), false)) { // false = sobrescribir
            fw.write(header + "\n");
        }
    }

    public synchronized void appendRevenue(RevenueRecord record) {
        try (FileWriter fw = new FileWriter(revenuePath.toFile(), true)) {
            fw.write(record.toCsvLine() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing revenue: " + e.getMessage());
        }
    }

    public synchronized void appendExpense(ExpenseRecord record) {
        try (FileWriter fw = new FileWriter(expensePath.toFile(), true)) {
            fw.write(record.toCsvLine() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing expense: " + e.getMessage());
        }
    }

    public synchronized void appendEvent(EventRecord record) {
        try (FileWriter fw = new FileWriter(eventPath.toFile(), true)) {
            fw.write(record.toCsvLine() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing event: " + e.getMessage());
        }
    }
}
