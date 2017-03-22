package comapps.com.sportsreference2;

/**
 * Created by me on 2/8/2017.
 */

class SportsItem {

    private String type;
    private String link;
    private String name;

    @Override
    public String toString() {
        return "SportsItem{" +
                "type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
