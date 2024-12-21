<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.retourarticles.model.Product" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Refund or Exchange - Return Items Application</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        h1 {
            text-align: center;
            font-size: 24px;
            color: #333;
        }

        .toggle-buttons {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin: 20px 0;
        }
        
		.btn-secondary.active{
		    background-color: #c3bfbf;
		}
        
        .form-container {
            margin-top: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        .form-select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .hidden {
            display: none;
        }
    </style>
    <script>
        function toggleOption(option) {
            const refundForm = document.getElementById("refundForm");
            const exchangeForm = document.getElementById("exchangeForm");

            if (option === "refund") {
                refundForm.classList.remove("hidden");
                exchangeForm.classList.add("hidden");
            } else if (option === "exchange") {
                refundForm.classList.add("hidden");
                exchangeForm.classList.remove("hidden");
            }

            document.getElementById("refundButton").classList.toggle("active", option === "refund");
            document.getElementById("exchangeButton").classList.toggle("active", option === "exchange");
        }
    </script>
</head>
<body>
    <div class="container">
        <nav class="navbar">
            <a href="ReturnServlet?action=returnStatus" class="back-link">← Back to Return Tracking</a>
            <h1>Choose Refund or Exchange</h1>
        </nav>

        <div class="toggle-buttons">
            <button id="refundButton" class="btn btn-secondary active" onclick="toggleOption('refund')">Refund</button>
            <button id="exchangeButton" class="btn btn-secondary" onclick="toggleOption('exchange')">Exchange</button>
        </div>

        <div class="form-container">
            <!-- Refund Form -->
            <form  id="refundForm" action="ReturnServlet" method="post" class="auth-form">
                <input type="hidden" name="action" value="processRefund">
                <input type="hidden" name="returnId" value="<%= request.getParameter("returnId") %>">
                <p>Get your money back to your original payment method.</p>
                <button type="submit" class="btn btn-secondary">Confirm Refund</button>
            </form>

            <!-- Exchange Form -->
            <form id="exchangeForm" class="hidden" action="ReturnServlet" method="post">
                <input type="hidden" name="action" value="processExchange">
                <input type="hidden" name="returnId" value="<%= request.getParameter("returnId") %>">
                <p>Exchange your item for a different product.</p>
                <div class="form-group">
                    <label for="product">Choose New Product:</label>
                    <select id="product" name="productId" required class="form-select">
                        <option value="">Select Product</option>
                        <%-- Dynamically populate available products here --%>
                        <option value="101">Product 1 - Red Shoes</option>
                        <option value="102">Product 2 - Blue Jacket</option>
                        <option value="103">Product 3 - Black Hat</option>
                        <option value="104">Product 4 - Green T-shirt</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary">Confirm Exchange</button>
            </form>
        </div>
    </div>
</body>
</html>
