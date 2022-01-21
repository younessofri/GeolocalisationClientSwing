package dao;

import java.util.List;

import javax.ejb.Remote;

import entities.User;


@Remote
public interface IDaoUserRemote {
	boolean create(User u);
	boolean delete (User u);
	boolean update (User u);
	User getById(int id);
	List<User> getByImei(String imei);
	List<User> getAll();

}
