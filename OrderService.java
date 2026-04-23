package com.ss14.service;

import com.ss14.model.Order;
import com.ss14.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private SessionFactory sessionFactory;

    public void cancelOrder(Long orderId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Order order = session.get(Order.class, orderId);
            if (order == null) throw new RuntimeException("Đơn hàng không tồn tại!");

            // Bước 1: Hủy đơn
            order.setStatus("CANCELLED");
            session.update(order);

            // Bước 2: Hoàn kho
            Product product = session.get(Product.class, order.getProductId());
            if (product == null) throw new RuntimeException("Sản phẩm không tồn tại!");

            product.setStock(product.getStock() + order.getQuantity());
            session.update(product);

            // Chốt đơn thành công
            transaction.commit();
            System.out.println("Thành công!");

        } catch (Exception e) {
            if (transaction != null) {
                // Quay xe nếu lỗi
                transaction.rollback();
                System.out.println("Đã Rollback dữ liệu an toàn!");
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }
}