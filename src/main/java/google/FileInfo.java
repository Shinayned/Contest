package google;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;


public class FileInfo implements Serializable{
    private String name;
    private String id;
    private long size;

    protected FileInfo() {}

    public FileInfo(String name, String id, long size) {
        this.name = name;
        this.id = id;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        if (size != fileInfo.size) return false;
        if (!name.equals(fileInfo.name)) return false;
        return id.equals(fileInfo.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", size=" + size +
                '}';
    }
}
