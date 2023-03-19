-- create tables

create table customers(
                          id bigserial primary key,
                          first_name varchar(100),
                          last_name varchar(100)
);

create table product (
                         id bigserial primary key,
                         product_name varchar(100),
                         price numeric
);

create table purchase (
                          id bigserial primary key,
                          customer_id bigint,
                          product_id bigint,
                          date DATE,
                          constraint fk_customer foreign key (customer_id) references customers (id),
                          constraint fk_product foreign key (product_id) references product (id)
);

-- insert data in tables

insert into customers(first_name, last_name) values ('Петр', 'Иванов'),
                                                    ('Иван', 'Петров'),
                                                    ('Василий', 'Пупкин'),
                                                    ('Семен', 'Барашкин'),
                                                    ('Владимир', 'Пупкин'),
                                                    ('Сергей', 'Сказочный'),
                                                    ('Сергей', 'Сергеев');


insert into product(product_name, price) values ('хлеб', 20),
                                                ('молоко', 80),
                                                ('колбаса', 300),
                                                ('мясо', 500),
                                                ('сметана', 90),
                                                ('сыр', 700),
                                                ('шоколад', 40),
                                                ('вино', 2000),
                                                ('коньяк', 5000),
                                                ('телефон', 20000);



insert into purchase(customer_id, product_id, "date") values (1, 1, '2020-01-04'),
                                                             (1, 1, '2020-01-04'),
                                                             (1, 2, '2020-01-05'),
                                                             (1, 2, '2020-02-04'),
                                                             (1, 2, '2020-04-14'),
                                                             (1, 2, '2020-04-15'),
                                                             (1, 6, '2020-04-15'),
                                                             (1, 7, '2020-06-23'),
                                                             (1, 8, '2020-06-23'),
                                                             (1, 10, '2020-08-04'),
                                                             (2, 3, '2020-02-17'),
                                                             (2, 4, '2020-02-17'),
                                                             (2, 5, '2020-02-17'),
                                                             (3, 1, '2021-11-04'),
                                                             (3, 1, '2021-12-07'),
                                                             (3, 1, '2021-12-08'),
                                                             (3, 1, '2021-12-08'),
                                                             (3, 1, '2021-12-08'),
                                                             (4, 7, '2020-07-04'),
                                                             (4, 7, '2020-07-04'),
                                                             (4, 7, '2020-07-04'),
                                                             (4, 8, '2020-10-14'),
                                                             (4, 1, '2020-10-24'),
                                                             (4, 3, '2020-10-26'),
                                                             (5, 4, '2020-06-12'),
                                                             (5, 5, '2020-06-12'),
                                                             (5, 6, '2020-06-12'),
                                                             (5, 1, '2020-06-12'),
                                                             (6, 10, '2021-10-26'),
                                                             (6, 9, '2021-10-26'),
                                                             (7, 8, '2022-09-26');

--sql for searching by surname

select * from postgres.public.customers c where c.last_name=?;

--sql for searching product and its count

select c.last_name, c.first_name, p2.product_name, count(*) as total_count
from postgres.public.purchase p
         join postgres.public.customers c on c.id = p.customer_id
         join postgres.public.product p2 on p2.id = p.product_id
where p2.product_name = ?
group by c.last_name, c.first_name, p2.product_name
having count(*) > ?;

--sql for total sum for the customer
select res.last_name, res.first_name, total
from (
         select c.last_name, c.first_name, sum(p2.price) as total
         from postgres.public.purchase p
                  join postgres.public.customers c on c.id = p.customer_id
                  join postgres.public.product p2 on p2.id = p.product_id
         group by c.last_name, c.first_name) as res
where total > ? and total < ?
group by res.last_name, res.first_name, total;

--sql for total sum for the customer


-- select p.customer_id, sum(p2.price) over () as total
-- from my_shop.my_data.purchase p
--        join my_shop.my_data.customers c on c.id = p.customer_id
--        join my_shop.my_data.product p2 on p2.id = p.product_id;

-- select sum(x.total_sum), avg(x.total_sum) as average from (
--                                  select c.last_name, c.first_name,
--                                         sum(p2.price) over (partition by p.customer_id) as total_sum,
--
--
--                                         p.date, p2.product_name
--                                  from my_shop.my_data.purchase p
--                                           join my_shop.my_data.customers c on c.id = p.customer_id
--                                           join my_shop.my_data.product p2 on p2.id = p.product_id
--                                  group by c.last_name, c.first_name, p.date, p2.product_name, p2.price,
--                                           p.product_id, p.customer_id
--                              ) as x
-- group by x.total_sum;

--sql for bad customers

select c.last_name, c.first_name, count(*)
from postgres.public.purchase p
         join postgres.public.customers c on c.id = p.customer_id
         join postgres.public.product p2 on p2.id = p.product_id
group by c.last_name, c.first_name
order by count(*)
    limit ?;

--STAT

--sql for counting days
select count(the_day) from
    (select generate_series('2020-01-14'::date, '2020-02-02'::date, '1 day') as the_day) days
where extract(dow from the_day) not in (0, 6);

--sql for statistic
select x.last_name, x.first_name, x.product_name, x.date,
       x.total_each_pr, x.total_sum_by_person, x.total_sum,
       (x.total_sum / (select count(*) from customers)) as avg_sum from (
                                                                            select c.last_name, c.first_name, p2.product_name, p.date, p2.price,
                                                                                   sum(p2.price) over (partition by p.customer_id, p2.product_name) as total_each_pr,
                                                                                    sum(p2.price) over (partition by p.customer_id) as total_sum_by_person,
                                                                                    sum(p2.price) over () as total_sum
                                                                            from postgres.public.purchase p
                                                                                     join postgres.public.customers c on c.id = p.customer_id
                                                                                     join postgres.public.product p2 on p2.id = p.product_id
                                                                            where p.date >= '2019-12-08' and p.date < '2022-10-26') as x
group by x.last_name, x.first_name, x.product_name, x.date, x.total_each_pr, x.total_sum_by_person, x.total_sum
order by x.last_name, x.first_name, x.total_each_pr DESC;
