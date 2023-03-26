package output_search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OutputSearch {
    String type;
    List<Result> results = new ArrayList<>();

}
