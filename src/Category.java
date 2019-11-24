import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    static final long serialVersionUID = 42L;
    private String chooseCat;
    private String cat1;
    private String cat2;
    private String cat3;
    private String cat4;

    public Category(String chooseCat, String cat1, String cat2, String cat3, String cat4) {
        this.chooseCat = chooseCat;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.cat4 = cat4;
    }
    public String getChooseCat() {
        return chooseCat;
    }

    public String getCat1() {
        return cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public String getCat4() {
        return cat4;
    }



}
