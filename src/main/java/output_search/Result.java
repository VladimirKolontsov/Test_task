package output_search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Result {
//    String criteriaOutput;  // до этого у меня тут была CriteriaOutput, но не понял как мне через нее в методах где запросы
//    // по поиску достучаться до каждого ее понял, сделал тупо стринг и понял что глупость полная


//    public String lastName;
//    public String productName;
//    public int minTimes;
//    public int minExpenses;
//    public int maxExpenses;
//    public int badCustomers;
    CriteriaOutput criteriaOutput;
    List<Customer> customerList = new ArrayList<>();

}
