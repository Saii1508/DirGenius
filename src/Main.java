import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path: ");
        String createPath = scanner.nextLine();
        Path path = Paths.get(createPath);
        scanner.close();
        try (Stream<Path> files = Files.walk(path)) {
            files.filter(Files::isRegularFile)        // in streams, we write eachFile -> Files.isRegularFile(eachFile)
                    .forEach(filePath -> {
                        String extension = getExtension(filePath.toString());
                        Path newDirectory = Paths.get(path.toString(), extension.replace(".", ""));
                        if (!Files.exists(newDirectory)) {
                            try {
                                Files.createDirectories(newDirectory);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Path targetPath = newDirectory.resolve(filePath.getFileName());
                        try {
                            Files.move(filePath, targetPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
    public static String getExtension(String filePath) {
        int extensionChar = filePath.lastIndexOf(".");
        return filePath.substring(extensionChar);
    }
}