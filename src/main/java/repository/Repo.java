package repository;

import exceptions.*;
import input_data.SearchCriteria;
import output_search.Customer;
import output_search.Result;

import java.sql.*;
import java.util.Optional;

public class Repo {

    private static final String lastNameSearch = "select * from postgres.public.customers c where c.last_name=?";

    private static final String productSearch =
            "select c.last_name, c.first_name, p2.product_name, count(*) as total_count\n" +
                    "from postgres.public.purchase p\n" +
                    "         join postgres.public.customers c on c.id = p.customer_id\n" +
                    "         join postgres.public.product p2 on p2.id = p.product_id\n" +
                    "where p2.product_name = ?\n" +
                    "group by c.last_name, c.first_name, p2.product_name\n" +
                    "having count(*) > ?";

    private static final String totalSumSearch =
            "select res.last_name, res.first_name, total\n" +
                    "from (\n" +
                    "      select c.last_name, c.first_name, sum(p2.price) as total\n" +
                    "      from postgres.public.purchase p\n" +
                    "          join postgres.public.customers c on c.id = p.customer_id\n" +
                    "          join postgres.public.product p2 on p2.id = p.product_id\n" +
                    "      group by c.last_name, c.first_name) as res\n" +
                    "where total > ? and total < ?\n" +
                    "group by res.last_name, res.first_name, total";

    private static final String findBadCustomersSearch =
            "select c.last_name, c.first_name, count(*)\n" +
                    "from postgres.public.purchase p\n" +
                    "         join postgres.public.customers c on c.id = p.customer_id\n" +
                    "         join postgres.public.product p2 on p2.id = p.product_id\n" +
                    "group by c.last_name, c.first_name\n" +
                    "order by count(*)\n" +
                    "limit ?";

    private static Connection connection;
//    public static SearchCriteria searchCriteria;

    private Repo() {

    }

    //это нормально что все методы статик?
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
            } catch (SQLException e) {
                throw new InvalidConnection("not right properties for connection");
            }
        }
        return connection;
    }

    public static Result findAllBySurname(String surname) {
        String lastName;
        String firstName;
        Result resultBySurnameCustomers = new Result();

        try (PreparedStatement statement = getConnection().prepareStatement(lastNameSearch)) {
            statement.setString(1, surname);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (!resultSet.isLast()) {
                resultSet.next();
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                resultBySurnameCustomers.getResults().add(new Customer(lastName, firstName));
            }
        } catch (SQLException e) {
            throw new InvalidSurname("no any customer with that surname");
        }
        return resultBySurnameCustomers;
    }

    public static Result findAllByExactProductAndCount(String productName, int minTimes) {
        String lastName;
        String firstName;
        Result resultByProductAndCount = new Result();

        try (PreparedStatement statement = getConnection().prepareStatement(productSearch)) {
            statement.setString(1, productName);
            statement.setInt(2, minTimes);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (!resultSet.isLast()) {
                resultSet.next();
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                resultByProductAndCount.getResults().add(new Customer(lastName, firstName));
            }
        } catch (SQLException e) {
            throw new InvalidProductOrTimes("check product name or times not right");
        }
        return resultByProductAndCount;
    }

    public static Result findAllByTotalSum(int minExpenses, int maxExpenses) {
        String lastName;
        String firstName;
        Result resultByTotalSum = new Result();

        try (PreparedStatement statement = getConnection().prepareStatement(totalSumSearch)) {
            statement.setInt(1, minExpenses);
            statement.setInt(2, maxExpenses);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (!resultSet.isLast()) {
                resultSet.next();
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                resultByTotalSum.getResults().add(new Customer(lastName, firstName));
            }
        } catch (SQLException e) {
            throw new InvalidExpenses("check expenses range");
        }
        return resultByTotalSum;
    }

    public static Result findBadCustomers(int badCustomers) {
        String lastName;
        String firstName;
        Result resultByBadCustomers = new Result();

        try (PreparedStatement statement = getConnection().prepareStatement(findBadCustomersSearch)) {
            statement.setInt(1, badCustomers);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (!resultSet.isLast()) {
                resultSet.next();
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");


                resultByBadCustomers.getResults().add(new Customer(lastName, firstName));
            }
        } catch (SQLException e) {
            throw new InvalidQuantityOfCustomers("choose the right quantity of bad customers");
        }
        return resultByBadCustomers;
    }

    //тут надо все полученные результаты объединить и потом запихать в один выходной json

}
