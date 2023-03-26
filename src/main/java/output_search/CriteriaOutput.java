package output_search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CriteriaOutput {

    public String lastName;
    public String productName;
    public int minTimes;
    public int minExpenses;
    public int maxExpenses;
    public int badCustomers;

}
