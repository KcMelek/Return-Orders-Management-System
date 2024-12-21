<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.retourarticles.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Orders</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <nav class="navbar">
            <h1>My Orders</h1>
            <div class="nav-links">
                <a href="ReturnServlet?action=returnStatus" class="nav-link btn btn-primary">Track Returns</a> <!-- Track Returns Button -->
                <form action="ReturnServlet?action=logout" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-outline">Logout</button>
                </form>
            </div>
        </nav>

        <div class="orders-container">
            <% 
                // Retrieve the orders from the request attributes
                List<Order> orders = (List<Order>) request.getAttribute("orders");

                if (orders != null && !orders.isEmpty()) {
                    for (Order order : orders) {
            %>
                <div class="order-card">
                    <div class="order-header">
                        <h3>Order #<%= order.getOrderId() %></h3>
                        <span class="order-date"><%= order.getOrderDate() %></span>
                    </div>
                    <div class="order-status">
                        Status: <span class="status-badge"><%= order.getOrderStatus() %></span>
                    </div>
                    <div class="order-items">
                        <% 
                            for (Product product : order.getProducts()) { 
                        %>
                        <div class="order-item">
                            <span class="item-name"><%= product.getProductName() %></span>
                            <span class="item-price">$<%= String.format("%.2f", product.getPrice()) %></span>
                            <span class="item-quantity">Qty: <%= product.getQuantity() %></span>
                            <form action="returnRequest.jsp" method="get" class="return-form">
                                <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                                <button type="submit" class="btn btn-secondary">Return Item</button>
                            </form>
                        </div>
                        <% } %>
                    </div>
                </div>
            <% 
                    }
                } else {
            %>
                <div class="empty-state">
                    <p>No orders found.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
