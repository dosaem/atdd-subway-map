package subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.line.LinePatchRequest;
import subway.controller.line.LineRequest;
import subway.controller.line.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.service.station.Station;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }
    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(() -> new EntityNotFoundException());
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(() -> new EntityNotFoundException());
        Line init = new Line(request.getName(), request.getColor(), upStation, downStation);
        Line line = lineRepository.save(init);
        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public LineResponse retrieveBy(Long id) {
        Optional<Line> line =lineRepository.findById(id);
        return LineResponse.from(line.orElseThrow(() -> new EntityNotFoundException()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> listAll() {
        return lineRepository.findAll().stream().map(LineResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public void updateBy(Long id, LinePatchRequest request) {
        Line line =lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        line.changeName(request.getName());
        line.changeColor(request.getColor());
    }

    @Transactional
    public void deleteBy(Long id) {
        lineRepository.deleteById(id);
    }
}