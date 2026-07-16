package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.respository.hibernate.CartEntity;
import com.assignment.cartCrud.respository.hibernate.CartProductEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Profile("sqlite")
public class CartHibernateRepositoryImpl implements CartRepository {
    private final SessionFactory sessionFactory;
    private final Object transactionLock = new Object();

    public CartHibernateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addCart(Cart cart) {
        inTransaction(session -> {
            CartEntity entity = new CartEntity(
                    cart.getId(), cart.getCreationTime(), cart.getLastAccessedTime());
            cart.getProducts().forEach(product -> entity.addProduct(new CartProductEntity(product)));
            session.persist(entity);
            return null;
        });
    }

    @Override
    public Optional<Cart> getCart(String id) {
        return inTransaction(session -> Optional.ofNullable(session.find(CartEntity.class, id))
                .map(this::toModel));
    }

    @Override
    public Optional<Cart> getAndTouchCart(String id) {
        return inTransaction(session -> {
            CartEntity entity = session.find(CartEntity.class, id);
            if (entity == null) {
                return Optional.empty();
            }
            entity.touch();
            return Optional.of(toModel(entity));
        });
    }

    @Override
    public boolean deleteCart(String id) {
        return inTransaction(session -> {
            CartEntity entity = session.find(CartEntity.class, id);
            if (entity == null) {
                return false;
            }
            session.remove(entity);
            return true;
        });
    }

    @Override
    public boolean addProductToCart(String cartId, Product product) {
        return inTransaction(session -> {
            CartEntity entity = session.find(CartEntity.class, cartId);
            if (entity == null) {
                return false;
            }
            entity.addProduct(new CartProductEntity(product));
            entity.touch();
            return true;
        });
    }

    @Override
    public int deleteExpiredCarts(LocalDateTime expirationThreshold) {
        return inTransaction(session -> {
            List<CartEntity> expiredCarts = session.createQuery(
                            "from CartEntity where lastAccessedTime <= :threshold", CartEntity.class)
                    .setParameter("threshold", expirationThreshold)
                    .getResultList();
            expiredCarts.forEach(session::remove);
            return expiredCarts.size();
        });
    }

    @Override
    public Iterator<Cart> getAllCarts() {
        return inTransaction(session -> session.createQuery("from CartEntity", CartEntity.class)
                .getResultList()
                .stream()
                .map(this::toModel)
                .iterator());
    }

    private Cart toModel(CartEntity entity) {
        Cart cart = new Cart();
        cart.setId(entity.getId());
        cart.setCreationTime(entity.getCreationTime());
        cart.setLastAccessedTime(entity.getLastAccessedTime());
        cart.setProducts(entity.getProducts().stream()
                .map(CartProductEntity::toModel)
                .toList());
        return cart;
    }

    private <T> T inTransaction(Function<Session, T> operation) {
        synchronized (transactionLock) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    T result = operation.apply(session);
                    transaction.commit();
                    return result;
                } catch (RuntimeException exception) {
                    transaction.rollback();
                    throw exception;
                }
            }
        }
    }
}
