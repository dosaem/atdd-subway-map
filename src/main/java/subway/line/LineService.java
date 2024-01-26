package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse createLine(LineRequest request) {
        Line createdLine = new Line(request.getName(), request.getColor(), request.getDistance());
        lineRepository.save(createdLine);
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseGet(() -> {
                Station station = new Station(
                    newStationName(request.getUpStationId()),
                    createdLine.getId());
                stationRepository.save(station);
                return station;
            });
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseGet(() -> {
                Station station = new Station(
                    newStationName(request.getDownStationId()),
                    createdLine.getId());
                stationRepository.save(station);
                return station;
            });
        upStation.updateLineId(createdLine.getId());
        downStation.updateLineId(createdLine.getId());
        return new LineResponse(
            createdLine.getId(),
            createdLine.getName(),
            createdLine.getColor(),
            List.of(
                new StationResponse(upStation.getId(), upStation.getName()),
                new StationResponse(downStation.getId(), downStation.getName())
            )
        );
    }

    private String newStationName(Long id) {
        return "지하철역" + id;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream().map(line -> new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stationRepository.findAllByLineId(line.getId()).stream()
                .map(station -> new StationResponse(
                    station.getId(),
                    station.getName()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<StationResponse> stations = stationRepository.findAllByLineId(id).stream()
            .map(station -> new StationResponse(
                station.getId(),
                station.getName()
            )).collect(Collectors.toList());
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations
        );
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}