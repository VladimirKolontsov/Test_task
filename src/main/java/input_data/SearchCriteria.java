package input_data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class SearchCriteria {

    public String lastName;
    public String productName;
    public int minTimes;
    public int minExpenses;
    public int maxExpenses;
    public int badCustomers;

}
