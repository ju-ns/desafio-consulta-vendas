package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Seller;
import com.devsuperior.dsmeta.projections.SaleMinProjection;
import com.devsuperior.dsmeta.projections.SellerMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(nativeQuery = true, value = """
            SELECT a.id as id,
                   a.date as date,
                   b.name as sellerName,
                   a.amount as amount
                   FROM tb_sales a
                   INNER JOIN tb_seller b
                        ON a.seller_id = b.id
                   WHERE a.date BETWEEN :ini AND :end
                   AND LOWER(b.name) LIKE LOWER (CONCAT('%', :name, '%'))""",
            countQuery = """
           SELECT COUNT(*)
           FROM tb_sales a
           LEFT JOIN tb_seller b
                ON a.seller_id = b.id
                AND a.date BETWEEN :ini AND :end
           WHERE LOWER(b.name) LIKE LOWER (CONCAT('%', :name, '%'))""")
    Page<SaleMinProjection> searchByDateAndName(LocalDate ini, LocalDate end, String name, Pageable pageable);


    @Query(nativeQuery = true, value = """
            SELECT b.name as name,
                    SUM(a.amount) as total,
            FROM tb_sales a
            INNER JOIN tb_seller b
            ON a.seller_id = b.id
            WHERE a.date BETWEEN :minDate AND :maxDate
            GROUP BY b.name
            """)
    List<SellerMinProjection> searchTotal(LocalDate minDate, LocalDate maxDate);


}
