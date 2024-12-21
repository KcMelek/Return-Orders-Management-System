<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.retourarticles.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Return Status - Return Items Application</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* Ajoutez ou ajustez ces styles pour l'alignement */
        .process-return-header {
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .order-header {
            position: relative;
            padding: 10px;
        }

        .order-card {
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 15px;
            margin: 15px 0;
            position: relative;
            background-color: #fff;
        }

        .status-badge {
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <nav class="navbar">
            <h1>My Returns</h1>
            <div class="nav-links">
                <a href="ReturnServlet?action=orders" class="nav-link btn btn-primary">See Orders</a>
                <form action="ReturnServlet?action=logout" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-outline">Logout</button>
                </form>
            </div>
        </nav>
        <%
            String message = (String) session.getAttribute("message");
            if (message != null) {
        %>
            <div class="alert alert-success" role="alert" style="text-align: center; margin: 20px auto; padding: 15px; max-width: 600px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 5px;">
                <%= message %>
            </div>
        <%
                // Remove the message after displaying it
                session.removeAttribute("message");
            }
        %>

        <div class="orders-container">
            <% 
                // Retrieve the returns from the request attributes
                List<Return> returns = (List<Return>) request.getAttribute("returns");
                
                // Check if returns are available
                if (returns != null && !returns.isEmpty()) {
                    for (Return returnObj : returns) {
            %>
                <div class="order-card">
                    <div class="order-header">
                        <h3>Return Request #<%= returnObj.getReturnId() %></h3>
                        <% 
                            // Afficher le bouton Process Return dans le coin si le statut est "Accepted"
                            if ("Accepted".equals(returnObj.getReturnStatus())) {
                        %>
                            <div class="process-return-header">
                                 <form action="processRefund.jsp" method="get" class="return-form">
                                    <input type="hidden" name="returnId" value="<%= returnObj.getReturnId() %>">
                                    <button type="submit" class="btn btn-secondary">Process Return</button>
                                </form>
                            </div>
                        <% 
                            } else {
                        %>
                            <span class="order-date">
                                <%= returnObj.getReturnStatus() %> - <%= returnObj.getProcessedDate() != null ? returnObj.getProcessedDate() : "Pending" %>
                            </span>
                        <% 
                            }
                        %>
                    </div>
                    <div class="order-status">
                        Status: <span class="status-badge"><%= returnObj.getReturnStatus() %></span>
                    </div>
                    <div class="order-items">
                        <p>Order Number: #<%= returnObj.getOrderId() %></p> <!-- Displaying the Order ID -->
                        <p>Product Name: <%= returnObj.getProduct().getProductName() %></p> <!-- Displaying the Product Name -->
                        <p>Quantity: <%= returnObj.getQuantity() %></p>
                        <p>Reason: <%= returnObj.getReturnReason() %></p>
                    </div>
                </div>
            <% 
                    }
                } else {
            %>
                <div class="empty-state">
                    <p>No return requests found.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
