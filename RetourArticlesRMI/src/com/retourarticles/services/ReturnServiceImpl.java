package com.retourarticles.services;

import com.retourarticles.model.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReturnServiceImpl extends UnicastRemoteObject implements ReturnService {

    private static final long serialVersionUID = 1L;

    private List<User> users;
    private List<Order> orders;
    private List<Return> returns;
    private List<Product> products;

    public ReturnServiceImpl() throws RemoteException {
        super();
        this.users = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.returns = new ArrayList<>();
        this.products = new ArrayList<>();
        
        // Generate some mock products to be used in orders
        generateMockProducts();
    }

    private void generateMockProducts() {
        // Create some products
        products.add(new Product(1, "Smartphone", 599.99, 50));
        products.add(new Product(2, "Laptop", 899.99, 30));
        products.add(new Product(3, "Headphones", 199.99, 100));
        products.add(new Product(4, "Smartwatch", 249.99, 70));
        products.add(new Product(5, "TV", 1099.99, 20));
    }


    // Method to register a new user with mock orders and products
    @Override
    public User registerUser(User user) throws RemoteException {
        // Check if the user already exists
        for (User existingUser : users) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new RemoteException("User with this email already exists");
            }
        }

        user.setUserId(users.size() + 1); // Assigning a new user ID
        users.add(user);
        
        generateMockOrders(user);

        return user;
    }

    // Method to generate fake orders for the user
    private void generateMockOrders(User user) {
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            Order order = new Order();
            order.setOrderId(orders.size() + 1);
            order.setUserId(user.getUserId());
            order.setOrderDate(new java.util.Date());
            order.setOrderStatus("Completed");

            List<Product> orderProducts = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Product product = products.get(rand.nextInt(products.size())); 
                Product orderedProduct = new Product(product.getProductId(), product.getProductName(), product.getPrice(), rand.nextInt(5) + 1); // Random quantity
                orderProducts.add(orderedProduct);
            }

            order.setProducts(orderProducts);
            orders.add(order);
            
            Return returnRequest = new Return();
            returnRequest.setReturnId(returns.size() + 1);
            returnRequest.setOrderId(order.getOrderId());
            returnRequest.setProductId(order.getProducts().get(0).getProductId());  // Use the first product's ID for return
            returnRequest.setQuantity(1); // Force return with quantity 1
            returnRequest.setReturnReason("Product not as expected");  // Example reason
            returnRequest.setReturnStatus("Accepted"); // Set status to Accepted

            // Add the return to the list
            returns.add(returnRequest);
        }
    }

    // Method to login the user
    @Override
    public User loginUser(String email, String password) throws RemoteException {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user; 
            }
        }
        throw new RemoteException("Invalid credentials");
    }

    @Override
    public List<Order> getUserOrders(int userId) throws RemoteException {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId() == userId) {
                userOrders.add(order); 
            }
        }
        return userOrders;
    }

    // Method to initiate a return for an order
    @Override
    public Return initiateReturn(int orderId, int productId, int quantity, String reason) throws RemoteException {
        // Find the order
        Order order = null;
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                order = o;
                break;
            }
        }
        
        if (order == null) {
            throw new RemoteException("Order not found");
        }

        // Check if the product exists in the order
        Product product = null;
        for (Product p : order.getProducts()) {
            if (p.getProductId() == productId && p.getQuantity() >= quantity) {
                product = p;
                break;
            }
        }

        if (product == null) {
            throw new RemoteException("Product not found or insufficient quantity");
        }

        Return returnRequest = new Return();
        returnRequest.setReturnId(returns.size() + 1);
        returnRequest.setOrderId(orderId);
        returnRequest.setProductId(productId);
        returnRequest.setQuantity(quantity);
        returnRequest.setReturnReason(reason);
        returnRequest.setReturnStatus("Pending");

        returns.add(returnRequest);
        return returnRequest;
    }

    // Method to get returns for a user
    @Override
    public List<Return> getUserReturns(int userId) throws RemoteException {
        List<Return> userReturns = new ArrayList<>();
        
        for (Return returnRequest : returns) {
            Order order = null;
            for (Order o : orders) {
                if (o.getOrderId() == returnRequest.getOrderId() && o.getUserId() == userId) {
                    order = o;
                    break;
                }
            }
            
            if (order != null) {
                Product product = null;
                for (Product p : order.getProducts()) {
                    if (p.getProductId() == returnRequest.getProductId()) {
                        product = p;
                        break;
                    }
                }
                
                if (product != null) {
                    returnRequest.setOrder(order);
                    returnRequest.setProduct(product);
                    userReturns.add(returnRequest);
                }
            }
        }
        
        return userReturns;
    }



    // Method to process a refund for a return
    @Override
    public void processRefund(int returnId) throws RemoteException {
        // Find the return object
        Return returnRequest = null;
        for (Return r : returns) {
            if (r.getReturnId() == returnId) {
                returnRequest = r;
                break;
            }
        }

        if (returnRequest == null) {
            throw new RemoteException("Return not found");
        }

        if (!returnRequest.getReturnStatus().equals("Accepted")) {
            throw new RemoteException("Return must be accepted before refunding");
        }

        returnRequest.setReturnStatus("Refunded");
        returnRequest.setProcessedDate(new Date()); // Set the date of processing

    }
    
    // Method to process a refund for a return
    @Override
    public void processExchange(int returnId) throws RemoteException {
        // Find the return object
        Return returnRequest = null;
        for (Return r : returns) {
            if (r.getReturnId() == returnId) {
                returnRequest = r;
                break;
            }
        }

        if (returnRequest == null) {
            throw new RemoteException("Return not found");
        }

        if (!returnRequest.getReturnStatus().equals("Accepted")) {
            throw new RemoteException("Return must be accepted before refunding");
        }

        returnRequest.setReturnStatus("Exchanged");
        returnRequest.setProcessedDate(new Date());

    }

}

