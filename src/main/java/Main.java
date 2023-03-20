import com.fasterxml.jackson.databind.ObjectMapper;
import input_data.Criterias;
import input_data.SearchCriteria;
import output_search.OutputSearch;
import output_search.Result;
import repository.Repo;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class Main {

    static String lastName = "lastName";
    static String productName = "productName";
    static String minExpenses = "minExpenses";
    static String badCustomers = "badCustomers";

    public static void main(String[] args) {

        OutputSearch outputSearch = new OutputSearch();
        ObjectMapper objectMapper = new ObjectMapper();

        if (args[3].equals("search")) {
            outputSearch.setType("search");
            try {
                Criterias criterias = objectMapper.readValue(Paths.get(args[4]).toFile(), Criterias.class);
                System.out.println(criterias);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //вызов 4-х методов по поиску и записи этого всего в List<Result>
        } else if (args[3].equals("stat")) {
            outputSearch.setType("stat");
            //вызов одного жесткого запроса и запись в нужный вид
        }

        System.out.println(Repo.findAllBySurname("Иванов"));
        System.out.println(Repo.findAllByExactProductAndCount("хлеб", 3));
        System.out.println(Repo.findAllByTotalSum(1900, 27000));
        System.out.println(Repo.findBadCustomers(3));
    }

    private static List<Result> search(String criteriaName, Criterias criterias) {
        List<SearchCriteria> criteriaList = criterias.getCriterias();
        if (criteriaList != null && !criteriaList.isEmpty()) {

            if (criteriaName.equals(lastName)) {
                Optional<SearchCriteria> searchCriteria = criteriaList.stream()
                        .filter(criteria -> criteria.getLastName() != null && !criteria.getLastName().isEmpty())
                        .findFirst();
                searchCriteria.ifPresent(criteria -> Repo.findAllBySurname(criteria.getLastName()));
            } else if (criteriaName.equals(productName)) {
                Optional<SearchCriteria> productNameCriterias = criteriaList.stream()
                        .filter(criteria -> criteria.getProductName() != null && !criteria.getProductName().isEmpty() &&
                                criteria.getMinTimes() != 0)
                        .findFirst();
                productNameCriterias.ifPresent(searchCriteria ->
                        Repo.findAllByExactProductAndCount(searchCriteria.getProductName(), searchCriteria.getMinTimes()));
            } else if (criteriaName.equals(minExpenses)) {
                Optional<SearchCriteria> minExpensesCriterias = criteriaList.stream()
                        .filter(criteria -> criteria.getMinExpenses() != 0 && criteria.getMaxExpenses() != 0)
                        .findFirst();
                minExpensesCriterias.ifPresent(searchCriteria ->
                        Repo.findAllByTotalSum(searchCriteria.getMinExpenses(), searchCriteria.getMaxExpenses()));
            } else if (criteriaName.equals(badCustomers)) {
                Optional<SearchCriteria> badCustomersCriterias = criteriaList.stream()
                        .filter(criteria -> criteria.getBadCustomers() != 0)
                        .findFirst();
                badCustomersCriterias.ifPresent(searchCriteria -> Repo.findBadCustomers(searchCriteria.getBadCustomers()));
            }
        }
        return null;
    }

}
