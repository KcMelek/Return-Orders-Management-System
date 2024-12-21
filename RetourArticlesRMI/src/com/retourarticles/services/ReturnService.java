package com.retourarticles.services;

import com.retourarticles.model.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReturnService extends Remote {
    User registerUser(User user) throws RemoteException;
    User loginUser(String email, String password) throws RemoteException;
    List<Order> getUserOrders(int userId) throws RemoteException;
    Return initiateReturn(int orderId, int productId, int quantity, String reason) throws RemoteException;
    List<Return> getUserReturns(int userId) throws RemoteException;
	void processRefund(int returnId) throws RemoteException;
	void processExchange(int returnId) throws RemoteException;
}
