package com.retourArticles.servlet;

import com.retourarticles.services.ReturnService;
import com.retourarticles.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class ReturnServiceServlet extends HttpServlet {

    private ReturnService returnService;

    @Override
    public void init() throws ServletException {
        try {
            // Use a context parameter to fetch RMI server URL
            String rmiUrl = getServletContext().getInitParameter("rmiServerUrl");
            if (rmiUrl == null || rmiUrl.isEmpty()) {
                throw new ServletException("RMI server URL is not configured");
            }
            returnService = (ReturnService) Naming.lookup(rmiUrl);
        } catch (Exception e) {
            throw new ServletException("Failed to connect to RMI service", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        try {
            if ("orders".equals(action)) {
                if (user == null) {
                    response.sendRedirect("ReturnServlet?action=login");
                    return;
                }
                List<Order> orders = returnService.getUserOrders(user.getUserId());
                List<Return> returns = returnService.getUserReturns(user.getUserId());
                request.setAttribute("orders", orders);
                request.setAttribute("returns", returns);
                request.getRequestDispatcher("/orders.jsp").forward(request, response);
            } else if ("returnStatus".equals(action)) {
                if (user == null) {
                    response.sendRedirect("ReturnServlet?action=login");
                    return;
                }
                List<Return> returns = returnService.getUserReturns(user.getUserId());
                System.out.println("Fetched returns: " + returns); // Debugging line to check the data
                request.setAttribute("returns", returns);
                request.getRequestDispatcher("/returnStatus.jsp").forward(request, response);
            } else if ("login".equals(action)) {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else if ("register".equals(action)) {
                // Handle the registration page request
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            } else {
                response.sendRedirect("ReturnServlet?action=login");
            }
        } catch (RemoteException e) {
            throw new ServletException("Error processing RMI request", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            int returnId;
            switch (action) {
                case "login":
                    handleLogin(request, response);
                    break;
                case "logout":
                    handleLogout(request, response);
                    break;
                case "register":
                    handleRegister(request, response);
                    break;
                case "initiateReturn":
                    handleInitiateReturn(request, response);
                    break;
                case "processRefund":
                    returnId = Integer.parseInt(request.getParameter("returnId"));
                    returnService.processRefund(returnId);
                    request.getSession().setAttribute("message", "Money refunded successfully.");
                    response.sendRedirect("ReturnServlet?action=returnStatus");
                    break;
                case "processExchange":
                    returnId = Integer.parseInt(request.getParameter("returnId"));
                    returnService.processExchange(returnId);
                    request.getSession().setAttribute("message", "Product exchanged successfully.");
                    response.sendRedirect("ReturnServlet?action=returnStatus");
                    break;
                default:
                    response.sendRedirect("login.jsp");
            }
        } catch (RemoteException e) {
            throw new ServletException("Error processing RMI request", e);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = returnService.loginUser(email, password);
            if (user != null) {
                // Store the user object in session
                request.getSession(true).setAttribute("user", user);
                response.sendRedirect("ReturnServlet?action=orders");
            } else {
                // Invalid credentials case (if loginUser returns null)
                request.setAttribute("error", "Invalid credentials.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (RemoteException e) {
            // Handle any RemoteException and set the error message
            request.setAttribute("error", "Invalid credentials.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }


    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            User registeredUser = returnService.registerUser(user);
            if (registeredUser != null) {
                response.sendRedirect("ReturnServlet?action=login");
            } else {
                request.setAttribute("error", "User already exists.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (RemoteException e) {
            request.setAttribute("error", "User already exists.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }


    private void handleInitiateReturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        String productIdParam = request.getParameter("productId");
        
        String quantityParam = request.getParameter("quantity");
        String reason = request.getParameter("reason");
        String comments = request.getParameter("comments");


        try {
            int orderId = Integer.parseInt(orderIdParam);
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam); // may need a null check before parsing

            if (returnService == null) {
                throw new ServletException("ReturnService is not available");
            }

            Return returnRequest = returnService.initiateReturn(orderId, productId, quantity, reason);
            returnRequest.setComments(comments);

            response.sendRedirect("ReturnServlet?action=returnStatus");
        } catch (RemoteException e) {
            throw new ServletException("Error initiating return", e);
        }
    }
    @Override
    public void destroy() {
        returnService = null;
    }
}
