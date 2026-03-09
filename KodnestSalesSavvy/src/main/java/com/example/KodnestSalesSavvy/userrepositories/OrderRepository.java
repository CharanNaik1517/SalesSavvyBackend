package com.example.KodnestSalesSavvy.userrepositories;

import com.example.KodnestSalesSavvy.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Repository
//public interface OrderRepository extends JpaRepository<Order, String> {
//
//    @Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
//    List<Order> findSuccessfulOrdersByMonthAndYear(int month, int year);
//
//    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createdAt) = :date AND o.status = 'SUCCESS'")
//    List<Order> findSuccessfulOrdersByDate(LocalDate date);
//
//
//    @Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
//    List<Order> findSuccessfulOrdersByYear(int year);
//
//    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'SUCCESS'")
//    BigDecimal calculateOverallBusiness();
//
//    @Query("SELECT o FROM Order o WHERE o.status = :status")
//    List<Order> findAllByStatusForOverallBusiness(String status);
//}



//@Repository
//public interface OrderRepository extends JpaRepository<Order, Integer> {
//
//    // Monthly
//    @Query("""
//        SELECT o FROM Order o
//        WHERE MONTH(o.createdAt) = :month
//        AND YEAR(o.createdAt) = :year
//        AND o.status = 'SUCCESS'
//    """)
//    List<Order> findSuccessfulOrdersByMonthAndYear(
//            @Param("month") int month,
//            @Param("year") int year
//    );
//
//    // Yearly
//    @Query("""
//        SELECT o FROM Order o
//        WHERE YEAR(o.createdAt) = :year
//        AND o.status = 'SUCCESS'
//    """)
//    List<Order> findSuccessfulOrdersByYear(
//            @Param("year") int year
//    );
//
//    // OVERALL (BEST METHOD — No @Query Needed)
//    List<Order> findByStatus(String status);
//}




import com.example.KodnestSalesSavvy.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByMonthAndYear(int month, int year);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createdAt) = :date AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByDate(LocalDate date);

    @Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year AND o.status = 'SUCCESS'")
    List<Order> findSuccessfulOrdersByYear(int year);


    @Query("SELECT o FROM Order o WHERE o.status = com.example.KodnestSalesSavvy.entities.OrderStatus.SUCCESS")
    List<Order> findAllSuccessfulOrders();
}