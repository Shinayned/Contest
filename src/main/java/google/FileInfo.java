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

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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
