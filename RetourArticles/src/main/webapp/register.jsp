<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up - Return Items Application</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="auth-card">
            <h2>Create Account</h2>
            <% if (request.getAttribute("error") != null) { %>
                <div class="error"><%= request.getAttribute("error") %></div>
            <% } %>
            <!-- Form action set to ReturnServlet with a hidden action parameter for registration -->
            <form action="ReturnServlet" method="post" class="auth-form">
                <input type="hidden" name="action" value="register"> <!-- Add action parameter for registration -->
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required class="form-input">
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required class="form-input">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required class="form-input">
                </div>
                <button type="submit" class="btn btn-primary">Create Account</button>
            </form>
            <p class="auth-link">Already have an account? <a href="ReturnServlet?action=login">Login here</a></p>
        </div>
    </div>
</body>
</html>
