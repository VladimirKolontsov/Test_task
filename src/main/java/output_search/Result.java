package output_search;

import java.util.ArrayList;
import java.util.List;

public class Result {
    List<Customer> results = new ArrayList<>();


    public List<Customer> getResults() {
        return results;
    }

    public void setResults(List<Customer> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Result{" +
                "results=" + results +
                '}';
    }
}
