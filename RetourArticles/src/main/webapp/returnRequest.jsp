<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.retourarticles.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Return Request - Return Items Application</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <nav class="navbar">
            <a href="ReturnServlet?action=orders" class="back-link">← Back to Orders</a>
            <h1>Return Request</h1>
        </nav>

        <div class="auth-card">
            <form action="ReturnServlet" method="post" class="auth-form">
                <input type="hidden" name="action" value="initiateReturn">
                <input type="hidden" name="orderId" value="<%= request.getParameter("orderId") %>">
                <input type="hidden" name="productId" value="<%= request.getParameter("productId") %>">
                
                
                <div class="form-group">
                    <label for="reason">Return Reason:</label>
                    <select id="reason" name="reason" required class="form-input">
                        <option value="">Select a reason</option>
                        <option value="defective">Defective Item</option>
                        <option value="wrong_size">Wrong Size</option>
                        <option value="not_as_described">Not as Described</option>
                        <option value="changed_mind">Changed Mind</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="quantity">Quantity to Return:</label>
                    <input type="number" id="quantity" name="quantity" min="1" required class="form-input">
                </div>

                <div class="form-group">
                    <label for="comments">Additional Comments:</label>
                    <textarea id="comments" name="comments" rows="4" class="form-textarea"></textarea>
                </div>

                <button type="submit" class="btn btn-primary">Submit Return Request</button>
            </form>
        </div>
    </div>
</body>
</html>
