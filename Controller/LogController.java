package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class LogController
{
    private static final String LOG_DIR = "Logs";
    private static final String LOG_FILE = LOG_DIR + "/LogJava.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String LINE_SEP = System.lineSeparator();

    public LogController()
    {
        ensureLogDirectory();
    }

    private void ensureLogDirectory()
    {
        File dir = new File(LOG_DIR);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }

    public void writeLog(String event, String bookName, double oldPrice, double newPrice,
                         int oldNumber, int newNumber, String oldStatus, String newStatus)
    {
        String timestamp = LocalDateTime.now().format(FORMATTER);

        String jsonEntry = String.format(Locale.ROOT,
                "{\"timestamp\":\"%s\",\"event\":\"%s\",\"bookName\":\"%s\",\"oldPrice\":%.2f,\"newPrice\":%.2f," +
                        "\"oldNumber\":%d,\"newNumber\":%d,\"oldStatus\":\"%s\",\"newStatus\":\"%s\"}",
                timestamp, event, escapeJson(bookName), oldPrice, newPrice,
                oldNumber, newNumber, escapeJson(oldStatus), escapeJson(newStatus));

        File logFile = new File(LOG_FILE);
        boolean fileExists = logFile.exists() && logFile.length() > 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true)))
        {
            if (!fileExists)
            {
                writer.write("[" + LINE_SEP);
                writer.write("  " + jsonEntry);
                writer.write(LINE_SEP + "]");
            }
            else
            {
                replaceLastCharWithCommaAndNewline(logFile);
                writer.write("  " + jsonEntry);
                writer.write(LINE_SEP + "]");
            }
        }
        catch (IOException e)
        {
            System.err.println("Помилка запису логу: " + e.getMessage());
        }
    }

    private void replaceLastCharWithCommaAndNewline(File file) throws IOException
    {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                content.append(line).append(LINE_SEP);
            }
        }

        if (content.length() > 0)
        {
            int lastBracket = content.lastIndexOf("]");
            if (lastBracket >= 0)
            {
                content.setCharAt(lastBracket, ',');
                content.insert(lastBracket + 1, LINE_SEP);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            writer.write(content.toString());
        }
    }

    private String escapeJson(String value)
    {
        if (value == null)
        {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public String getLogFilePath()
    {
        return LOG_FILE;
    }
}
