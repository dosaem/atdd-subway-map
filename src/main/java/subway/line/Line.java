package subway.line;

import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany
    private List<Station> stations;
    private Integer distance;

    public Line() {
    }

    public Line(String name, String color, List<Station> stations, Integer distance) {
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

}
