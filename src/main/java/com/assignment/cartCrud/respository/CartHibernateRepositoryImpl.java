package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.respository.hibernate.CartEntity;
import com.assignment.cartCrud.respository.hibernate.CartJpaRepository;
import com.assignment.cartCrud.respository.hibernate.CartProductEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;

@Repository
@Profile("sqlite")
public class CartHibernateRepositoryImpl implements CartRepository {
    private final CartJpaRepository repository;

    public CartHibernateRepositoryImpl(CartJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void addCart(Cart cart) {
        CartEntity entity = new CartEntity(
                cart.getId(), cart.getCreationTime(), cart.getLastAccessedTime());
        cart.getProducts().forEach(product -> entity.addProduct(new CartProductEntity(product)));
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cart> getCart(String id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Optional<Cart> getAndTouchCart(String id) {
        Optional<CartEntity> entity = repository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        entity.get().touch();
        return entity.map(this::toModel);
    }

    @Override
    @Transactional
    public boolean deleteCart(String id) {
        Optional<CartEntity> entity = repository.findById(id);
        if (entity.isEmpty()) {
            return false;
        }
        repository.delete(entity.get());
        return true;
    }

    @Override
    @Transactional
    public boolean addProductToCart(String cartId, Product product) {
        Optional<CartEntity> entity = repository.findById(cartId);
        if (entity.isEmpty()) {
            return false;
        }
        entity.get().addProduct(new CartProductEntity(product));
        entity.get().touch();
        return true;
    }

    @Override
    @Transactional
    public int deleteExpiredCarts(LocalDateTime expirationThreshold) {
        return Math.toIntExact(
                repository.deleteByLastAccessedTimeLessThanEqual(expirationThreshold));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterator<Cart> getAllCarts() {
        return repository.findAll()
                .stream()
                .map(this::toModel)
                .iterator();
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

}
