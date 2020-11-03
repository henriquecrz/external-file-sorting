import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Converter.toBinaryFile(Path.INPUT_FILE, Path.BINARY_FILE);

        try {
            createFixedBlocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFixedBlocks() throws IOException {
        int currentPersonIndex = 0;
        int currentFileIndex = 0;
        int numberOfBlocks = 0;
        Person[] persons = new Person[Constant.BLOCK_LENGTH];
        RandomAccessFile binaryFile = new RandomAccessFile(new File(Constant.BINARY_FILE_NAME), AccessMode.READ);
        RandomAccessFile[] tempFile = new RandomAccessFile[Constant.NUMBER_OF_FILES];

        for (int i = 0; i < Constant.NUMBER_OF_FILES; ++i) {
            tempFile[i] = new RandomAccessFile(new File(i + "temp.tmp"), AccessMode.READ_WRITE);

            tempFile[i].setLength(0);
        }

        int numberOfPeople = binaryFile.readInt();

        for (int i = 0; i < numberOfPeople; i++) {
            Person tempPerson = Person.readFromFile(binaryFile);
            persons[currentPersonIndex] = tempPerson;
            currentPersonIndex++;

            if (currentPersonIndex == Constant.BLOCK_LENGTH) {
                saveBlock(persons, tempFile[currentFileIndex]);

                numberOfBlocks++;
                currentPersonIndex = 0;

                Arrays.fill(persons, Person.getSeparator());

                currentFileIndex++;
                currentFileIndex = currentFileIndex % Constant.NUMBER_OF_FILES;
            }
        }

        System.out.println("Number of blocks: " + numberOfBlocks);
        binaryFile.close();

        for (int i = 0; i < Constant.NUMBER_OF_FILES; ++i) {
            tempFile[i].close();
        }
    }

    public static void saveBlock(Person[] persons, RandomAccessFile file) throws IOException {
        Arrays.sort(persons);

        for (Person person : persons) {
            person.saveToFile(file);
        }

        Person.getSeparator().saveToFile(file);
    }
}
