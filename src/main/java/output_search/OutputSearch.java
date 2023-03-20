package output_search;

import java.util.ArrayList;
import java.util.List;

public class OutputSearch {
    String type;
    List<Result> results = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "OutputSearch{" +
                "type='" + type + '\'' +
                ", results=" + results +
                '}';
    }
}
