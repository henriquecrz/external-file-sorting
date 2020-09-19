import java.io.IOException;
import java.io.RandomAccessFile;

public class Person implements Comparable<Person> {
    private int _rg;
    private String _name;
    private String _birthday;

    public Person(int rg, String name, String birthday) {
        this._rg = rg;
        this._name = name;
        this._birthday = birthday;
    }

    public Person(String[] value) throws NumberFormatException {
        this._rg = Integer.parseInt(value[Index.RG]);
        this._name = value[Index.NAME];
        this._birthday = value[Index.BIRTHDAY];
    }

    public int getRg() {
        return this._rg;
    }

    public String getName() {
        return this._name;
    }

    public String getBirthday() {
        return this._birthday;
    }

    @Override
    public String toString() {
        return "\nPerson\n------\nRG: " + getRg() + "\nName: " + getName() + "\nBirthday: " + getBirthday() + "\n";
    }

    @Override
    public int compareTo(Person person) {
        if (getRg() > person.getRg()) {
            return 1;
        } else if (getRg() < person.getRg()) {
            return -1;
        }

        return 0;
    }

    public void saveToFile(RandomAccessFile file) throws IOException {
        file.seek(file.length());
        file.writeInt(getRg());
        file.writeUTF(getName());
        file.writeUTF(getBirthday());
    }

    public static Person getPerson(RandomAccessFile file, int rgKey) {
        boolean isFileEnded = false;

        try {
            file.seek(Integer.BYTES);

            while (!isFileEnded) { // Improve flag
                int rg = file.readInt();
                String name = file.readUTF();
                String birthday = file.readUTF();

                if (rg == rgKey) {
                    return new Person(rg, name, birthday);
                }
            }
        } catch (IOException ex) {
            isFileEnded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Person getPerson(RandomAccessFile file, long pos) {
        try {
            file.seek(pos);

            int rg = file.readInt();
            String name = file.readUTF();
            String birthday = file.readUTF();

            return new Person(rg, name, birthday);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Person readFromFile(RandomAccessFile file) {
        int rg = 0;
        String name = Constant.EMPTY_STRING;
        String birthday = Constant.EMPTY_STRING;

        try {
            rg = file.readInt();
            name = file.readUTF();
            birthday = file.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Person(rg, name, birthday);
    }

    public static Person getSeparator() {
        return new Person(-1, " ", " ");
    }
}
